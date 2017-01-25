package com.epiagregator;

import android.app.Application;

import com.epiagregator.impls.UserProfilePersistance;
import com.epiagregator.impls.webapi.OAuthInterceptor;
import com.epiagregator.impls.webapi.providers.FakeWebApiAccountProvider;
import com.epiagregator.model.persistance.Persistance;
import com.epiagregator.model.persistance.userprofile.IUserProfilePersistance;
import com.epiagregator.model.providers.Providers;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by etien on 23/01/2017.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        JodaTimeAndroid.init(this);

        IUserProfilePersistance userProfilePersistance = new UserProfilePersistance(this);
        OAuthInterceptor oAuthInterceptor = new OAuthInterceptor(userProfilePersistance);

        Persistance.setUserProfilePersistance(userProfilePersistance);

        Providers.setAccountProvider(new FakeWebApiAccountProvider());
    }
}
