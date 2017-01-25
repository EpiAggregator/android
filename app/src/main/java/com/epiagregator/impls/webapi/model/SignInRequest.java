package com.epiagregator.impls.webapi.model;

import com.google.gson.annotations.SerializedName;

public class SignInRequest {
    @SerializedName("username")
    private final String mEmail;

    @SerializedName("password")
    private final String mPassword;

    public SignInRequest(
            String email,
            String password) {

        mEmail = email;
        mPassword = password;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPassword() {
        return mPassword;
    }
}