package com.epiagregator.model.userprofile;

import com.epiagregator.impls.webapi.error.RetrofitException;
import com.epiagregator.impls.webapi.model.SignInRequest;
import com.epiagregator.model.persistance.Persistance;
import com.epiagregator.model.providers.Providers;
import com.epiagregator.screens.signin.SignInMvpView;

import rx.Subscription;

/**
 * Created by etien on 23/01/2017.
 */

public class UserProfileService {

    private static void setActiveUserProfile(UserProfile activeUserProfile) {
        Persistance.getUserProfilePersistance().setActiveUserProfile(activeUserProfile);
    }

    public static Subscription loginUser(SignInRequest signInRequest, SignInMvpView signInMvpView) {
        signInMvpView.showLoading();
        return Providers.getAccountProvider().loginUser(signInRequest)
                .subscribe(userProfile -> {
                    setActiveUserProfile(userProfile);
                    signInMvpView.onResponse(userProfile);
                }, throwable -> {
                    signInMvpView.onError(RetrofitException.converToRetrofitException(throwable));
                });
    }

    public static Subscription registerUser(SignInRequest signInRequest, SignInMvpView signInMvpView) {
        signInMvpView.showLoading();
        return Providers.getAccountProvider().registerUser(signInRequest)
            .subscribe(userProfile -> {
                setActiveUserProfile(userProfile);
                signInMvpView.onResponse(userProfile);
            }, throwable -> {
                signInMvpView.onError(RetrofitException.converToRetrofitException(throwable));
            });
    }

    public static boolean iSignedIn() {
        return Persistance.getUserProfilePersistance().isSignedIn();
    }

    public static void signOut() {
        Persistance.getUserProfilePersistance().setActiveUserProfile(null);
    }
}
