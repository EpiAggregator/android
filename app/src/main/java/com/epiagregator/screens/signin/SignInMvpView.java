package com.epiagregator.screens.signin;

import com.epiagregator.impls.webapi.WebApiCallListener;
import com.epiagregator.model.userprofile.UserProfile;

/**
 * Created by etien on 24/01/2017.
 */

public interface SignInMvpView extends WebApiCallListener<UserProfile> {
    void showLoading();
}
