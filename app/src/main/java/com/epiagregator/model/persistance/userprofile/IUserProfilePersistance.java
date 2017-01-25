package com.epiagregator.model.persistance.userprofile;

import com.epiagregator.model.userprofile.UserProfile;

/**
 * Created by etien on 23/01/2017.
 */
public interface IUserProfilePersistance {
    boolean isSignedIn();

    UserProfile getActiveUserProfile();

    void setActiveUserProfile(UserProfile userProfile);
}