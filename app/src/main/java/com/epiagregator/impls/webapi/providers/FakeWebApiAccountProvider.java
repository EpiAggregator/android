package com.epiagregator.impls.webapi.providers;

import com.epiagregator.impls.webapi.model.SignInRequest;
import com.epiagregator.impls.webapi.model.UserModel;
import com.epiagregator.model.providers.IAccountProvider;
import com.epiagregator.model.userprofile.AuthToken;
import com.epiagregator.model.userprofile.UserProfile;

import org.joda.time.DateTime;

import rx.Observable;

/**
 * Created by etien on 24/01/2017.
 */

public class FakeWebApiAccountProvider implements IAccountProvider {

    @Override
    public Observable<UserProfile> loginUser(SignInRequest signInRequest) {
        return createFakeUser(signInRequest);
    }

    @Override
    public Observable<UserProfile> registerUser(SignInRequest signInRequest) {
        return createFakeUser(signInRequest);
    }

    @Override
    public Observable<UserModel> me() {
        return null;
    }

    private static Observable<UserProfile> createFakeUser(SignInRequest signInRequest) {
        UserProfile userProfile = new UserProfile(null, signInRequest.getEmail(), signInRequest.getPassword());
        userProfile.setAuthToken(new AuthToken("Bearer", "fake-token", DateTime.now().plusDays(30).getMillis()));
        return Observable.just(userProfile);
    }
}
