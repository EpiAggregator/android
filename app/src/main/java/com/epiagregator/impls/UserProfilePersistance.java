package com.epiagregator.impls;

import android.content.Context;
import android.content.SharedPreferences;

import com.epiagregator.R;
import com.epiagregator.model.persistance.userprofile.IUserProfilePersistance;
import com.epiagregator.model.userprofile.UserProfile;
import com.google.gson.Gson;

/**
 * Created by etien on 23/01/2017.
 */

public class UserProfilePersistance implements IUserProfilePersistance {

    private final SharedPreferences mSharedPref;
    private final Context mContext;
    private final Gson mGson;

    public UserProfilePersistance(Context context) {
        mGson = new Gson();
        this.mContext = context;
        this.mSharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    @Override
    public boolean isSignedIn() {
        return mSharedPref.getString(mContext.getString(R.string.shared_prefs_user_profile), null) != null;
    }

    @Override
    public UserProfile getActiveUserProfile() {
        String userProfile = mSharedPref.getString(mContext.getString(R.string.shared_prefs_user_profile), null);
        return userProfile == null ? null : mGson.fromJson(userProfile, UserProfile.class);
    }

    @Override
    public void setActiveUserProfile(UserProfile userProfile) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        if (userProfile == null) {
            editor.clear().apply();
        } else {
            editor.putString(mContext.getString(R.string.shared_prefs_user_profile), mGson.toJson(userProfile));
            editor.apply();
        }
    }
}
