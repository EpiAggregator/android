package com.epiagregator.impls.webapi;

import android.content.Context;

import com.epiagregator.BuildConfig;
import com.epiagregator.impls.webapi.error.RxErrorHandlingCallAdapterFactory;
import com.epiagregator.impls.webapi.model.Entry;
import com.epiagregator.impls.webapi.model.Feed;
import com.epiagregator.impls.webapi.model.SignInRequest;
import com.epiagregator.impls.webapi.model.UpdateEntryRequest;
import com.epiagregator.impls.webapi.model.UserModel;
import com.epiagregator.model.userprofile.AuthToken;
import com.epiagregator.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by etien on 25/01/2017.
 */

public interface EpiAgregatorService {

    @POST("token")
    Observable<AuthToken> getAccessToken(@Body SignInRequest signInRequest);

    @POST("users")
    Observable<Void> registerUser(@Body SignInRequest signInRequest);

    @GET("users")
    Observable<UserModel> getUser(@Field("email") String email);

    @GET("feeds/{id}/entries")
    Observable<List<Entry>> getEntriesByFeed(@Path("id") String feedId);

    @GET("feeds")
    Observable<List<Feed>> getFeeds();

    @GET("feeds/entries")
    Observable<List<Entry>> getEntries();

    @GET("feeds/{id}")
    Observable<Feed> getFeed(@Path("id") String feedId);

    @POST("feeds")
    Observable<Void> addFeed(@Body List<String> feedUris);

    @PATCH("feeds/{id}/entries/{entryId}")
    Observable<Entry> updateEntry(@Path("id") String feedId, @Path("entryId") String entryId, @Body UpdateEntryRequest updateEntryRequest);

    class Creator {

        private static class CacheControlInterceptor implements Interceptor {

            private Context mContext;

            public CacheControlInterceptor(Context context) {
                this.mContext = context;
            }

            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                if (Utils.isNetworkAvailable(mContext)) {
                    int maxAge = 60; // read from cache for 1 minute
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .build();
                } else {
                    int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
            }
        }

        public static EpiAgregatorService newRxService(Context context, Interceptor... interceptors) {
            Gson gson = new GsonBuilder()
                    //.registerTypeAdapter(DateTime.class, new DateTimeTypeConverter())
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(BuildConfig.HOST)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create());

            Retrofit retrofit = builder
                    .client(getCacheHttpClient(context, interceptors)).build();
            return retrofit.create(EpiAgregatorService.class);
        }

        private static OkHttpClient getCacheHttpClient(Context context, Interceptor ... interceptors) {
            //setup cache
            File httpCacheDirectory = new File(context.getCacheDir(), "responses");
            int cacheSize = 10 * 1024 * 1024; // 10 MiB
            Cache cache = new Cache(httpCacheDirectory, cacheSize);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            for (Interceptor interceptor : interceptors) {
                httpClient.addInterceptor(interceptor);
            }

            httpClient.addNetworkInterceptor(new CacheControlInterceptor(context));

            return httpClient.cache(cache).build();
        }
    }
}
