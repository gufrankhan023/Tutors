package com.android.tutor.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.tutor.R;
import com.android.tutor.utilities.PreferenceConnector;

import static com.android.tutor.utilities.Constants.LOGIN_STATUS;
import static com.android.tutor.utilities.Constants.SPLASH_DELAY_TIME;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        navigate();
    }

    private void navigate() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PreferenceConnector.readBoolean(SplashActivity.this, LOGIN_STATUS, false)) {
                    Intent loginIntent = new Intent(SplashActivity.this, DashboardActivity.class);
                    startActivity(loginIntent);
                } else {
                    Intent loginIntent = new Intent(SplashActivity.this, LoginTypeActivity.class);
                    startActivity(loginIntent);
                }
                finish();
            }
        }, SPLASH_DELAY_TIME);


    }
}
