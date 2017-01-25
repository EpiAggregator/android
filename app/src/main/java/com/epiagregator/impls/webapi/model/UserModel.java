package com.epiagregator.impls.webapi.model;

/**
 * Created by etien on 25/01/2017.
 */

public class UserModel {

    private String mId;
    private String mEmail;
    private String mPassword;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }
}
