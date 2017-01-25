package com.epiagregator.model.userprofile;

/**
 * Created by etien on 23/01/2017.
 */

public class UserProfile {

    private final String mUserId;
    private final String mUserEmail;
    private final String mUserPassword;
    private AuthToken mAuthToken;

    public UserProfile(String userId, String userEmail, String userPassword) {
        this.mUserId = userId;
        this.mUserEmail = userEmail;
        this.mUserPassword = userPassword;
    }

    public void setAuthToken(AuthToken authToken) {
        this.mAuthToken = authToken;
    }

    public AuthToken getAuthToken() {
        return mAuthToken;
    }

    public String getUserEmail() {
        return mUserEmail;
    }

    public String getUserPassword() {
        return mUserPassword;
    }
}
