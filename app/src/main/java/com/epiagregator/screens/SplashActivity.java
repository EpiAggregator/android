package com.epiagregator.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.epiagregator.model.userprofile.UserProfileService;
import com.epiagregator.screens.main.MainActivity;
import com.epiagregator.screens.signin.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = null;
        if (UserProfileService.iSignedIn()) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
    }
}
