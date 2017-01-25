package com.epiagregator.impls.webapi.providers;

import com.epiagregator.impls.webapi.EpiAgregatorService;
import com.epiagregator.impls.webapi.OAuthInterceptor;
import com.epiagregator.impls.webapi.model.SignInRequest;
import com.epiagregator.impls.webapi.model.UserModel;
import com.epiagregator.model.providers.IAccountProvider;
import com.epiagregator.model.userprofile.AuthToken;
import com.epiagregator.model.userprofile.UserProfile;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by etien on 24/01/2017.
 */

public class WebApiAccountProvider implements IAccountProvider {

    private final EpiAgregatorService mEpiAgregatorService;

    public WebApiAccountProvider(OAuthInterceptor oAuthInterceptor) {
        this.mEpiAgregatorService = EpiAgregatorService.Creator.newRxService(oAuthInterceptor);
    }

    @Override
    public Observable<UserProfile> loginUser(SignInRequest signInRequest) {
        return mEpiAgregatorService.getAccessToken(signInRequest)
                .map(authToken -> {
                    UserProfile userProfile = new UserProfile(null, signInRequest.getEmail(), signInRequest.getPassword());
                    userProfile.setAuthToken(authToken);
                    return userProfile;
                });
    }

    @Override
    public Observable<UserProfile> registerUser(SignInRequest signInRequest) {
        return mEpiAgregatorService.registerUser(signInRequest)
                .flatMap(new Func1<Void, Observable<AuthToken>>() {
                    @Override
                    public Observable<AuthToken> call(Void aVoid) {
                        return mEpiAgregatorService.getAccessToken(signInRequest);
                    }
                })
                .map(authToken -> {
                    UserProfile userProfile = new UserProfile(null, signInRequest.getEmail(), signInRequest.getPassword());
                    userProfile.setAuthToken(authToken);
                    return userProfile;
                });
    }

    @Override
    public Observable<UserModel> me() {
        return null;
    }
}
