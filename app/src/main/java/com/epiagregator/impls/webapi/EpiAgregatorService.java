package com.epiagregator.impls.webapi;

import com.epiagregator.BuildConfig;
import com.epiagregator.impls.webapi.error.RxErrorHandlingCallAdapterFactory;
import com.epiagregator.impls.webapi.model.Entry;
import com.epiagregator.impls.webapi.model.Feed;
import com.epiagregator.impls.webapi.model.SignInRequest;
import com.epiagregator.impls.webapi.model.UpdateEntryRequest;
import com.epiagregator.impls.webapi.model.UserModel;
import com.epiagregator.model.userprofile.AuthToken;

import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
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

    String ENDPOINT = "https://api.epiagregator.com/";

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

    @GET("feeds/{id}")
    Observable<Feed> getFeed(@Path("id") String feedId);

    @PATCH("feeds/{id}/entries/{entryId}")
    Observable<Entry> updateEntry(@Path("id") String feedId, @Path("entryId") String entryId, @Body UpdateEntryRequest updateEntryRequest);

    class Creator {

        public static EpiAgregatorService newRxService(Interceptor... interceptors) {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(BuildConfig.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create());

            Retrofit retrofit = builder
                    .client(getHttpClient(interceptors)).build();
            return retrofit.create(EpiAgregatorService.class);
        }

        private static OkHttpClient getHttpClient(Interceptor ... interceptors) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            for (Interceptor interceptor : interceptors) {
                httpClient.addInterceptor(interceptor);
            }

            OkHttpClient client = httpClient.build();
            httpClient.addInterceptor(logging);

            return client;
        }
    }
}
