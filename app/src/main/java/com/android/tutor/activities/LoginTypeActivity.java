package com.android.tutor.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.android.tutor.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.tutor.utilities.Constants.LOGIN_TYPE;
import static com.android.tutor.utilities.Constants.STUDENT_LOGIN;
import static com.android.tutor.utilities.Constants.TUTOR_LOGIN;

public class LoginTypeActivity extends AppCompatActivity {

    @BindView(R.id.studentLoginBTN)
     Button studentLoginBTN;

    @BindView(R.id.tutorLoginBTN)
     Button tutorLoginBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_type);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.studentLoginBTN)
    public void onStudentLoginBtnClick() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(LOGIN_TYPE, STUDENT_LOGIN);
        startActivity(intent);
    }

    @OnClick(R.id.tutorLoginBTN)
    public void onTutorLoginBtnClick() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(LOGIN_TYPE, TUTOR_LOGIN);
        startActivity(intent);
    }
    @OnClick(R.id.backIV)
    public void onBackClick() {
        onBackPressed();
    }
}
