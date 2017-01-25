package com.epiagregator.impls.webapi;

import com.epiagregator.model.persistance.userprofile.IUserProfilePersistance;
import com.epiagregator.model.userprofile.AuthToken;
import com.epiagregator.model.userprofile.UserProfile;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by etien on 25/01/2017.
 */

public class OAuthInterceptor implements Interceptor {

    private final IUserProfilePersistance mIUserProfilePersistance;

    public OAuthInterceptor(IUserProfilePersistance userProfilePersistance) {
        this.mIUserProfilePersistance = userProfilePersistance;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        if (mIUserProfilePersistance.isSignedIn()) {
            UserProfile userProfile = mIUserProfilePersistance.getActiveUserProfile();
            AuthToken authToken = userProfile.getAuthToken();
            builder.header("Authorization", authToken.getType() + " " + authToken.getToken());
        }

        return chain.proceed(builder.build());
    }
}