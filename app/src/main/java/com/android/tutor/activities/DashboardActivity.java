package com.android.tutor.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.tutor.R;
import com.android.tutor.customviews.RoundedImageView;

import butterknife.BindView;

public class DashboardActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_dashboard);
    }
}
