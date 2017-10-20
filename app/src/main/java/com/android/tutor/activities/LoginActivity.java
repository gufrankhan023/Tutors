package com.android.tutor.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tutor.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.tutor.utilities.Constants.LOGIN_TYPE;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.backIV)
     ImageView backIV;
    @BindView(R.id.titleTV)
     TextView titleTV;
    @BindView(R.id.userNameET)
     EditText userNameET;
    @BindView(R.id.passwordET)
     EditText passwordET;
    @BindView(R.id.loginBTN)
     Button loginBTN;
    @BindView(R.id.signUpTV)
     TextView signUpTV;

    private String loginType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (getIntent().hasExtra(LOGIN_TYPE)) {
            loginType = getIntent().getStringExtra(LOGIN_TYPE);
        }
    }

    @OnClick(R.id.signUpTV)
    public void onSignUpClick() {
        Intent intent =new Intent(LoginActivity.this,RegistrationActivity.class);
        intent.putExtra(LOGIN_TYPE,loginType);
        startActivity(intent);
    }

    @OnClick(R.id.loginBTN)
    public void onLoginClick() {

    }

    @OnClick(R.id.backIV)
    public void onBackClick() {
        onBackPressed();
    }


}
