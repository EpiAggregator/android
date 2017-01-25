package com.epiagregator.model.providers;

import com.epiagregator.impls.webapi.model.SignInRequest;
import com.epiagregator.impls.webapi.model.UserModel;
import com.epiagregator.model.userprofile.UserProfile;

import rx.Observable;

/**
 * Created by etien on 24/01/2017.
 */

public interface IAccountProvider {
    Observable<UserProfile> loginUser(SignInRequest signInRequest);

    Observable<UserProfile> registerUser(SignInRequest signInRequest);

    Observable<UserModel> me();
}
