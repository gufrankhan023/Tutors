package com.android.tutor.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.android.tutor.R;
import com.android.tutor.models.Student;
import com.android.tutor.utilities.CommonMethods;
import com.android.tutor.utilities.CustomDialogs;
import com.android.tutor.utilities.PreferenceConnector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static com.android.tutor.utilities.Constants.LOGIN_STATUS;
import static com.android.tutor.utilities.Constants.LOGIN_TYPE;
import static com.android.tutor.utilities.Constants.PASSWORD;
import static com.android.tutor.utilities.Constants.STUDENT_DB;
import static com.android.tutor.utilities.Constants.STUDENT_LOGIN;
import static com.android.tutor.utilities.Constants.TUTOR_DB;
import static com.android.tutor.utilities.Constants.TUTOR_LOGIN;
import static com.android.tutor.utilities.Constants.USER_NAME;

public class LoginActivity extends AppCompatActivity {

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

    private String loginType = "", userName = "", password = "";
    private DatabaseReference studentDB, tutorDB;
    private ProgressDialog progressDialog;
    private boolean loginSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (getIntent().hasExtra(LOGIN_TYPE)) {
            loginType = getIntent().getStringExtra(LOGIN_TYPE);
        }
        if (loginType.equals(STUDENT_LOGIN))
            titleTV.setText(getString(R.string.student_login));
        else
            titleTV.setText(getString(R.string.tutor_login));

        setUpDataBase();
    }

    private void setUpDataBase() {
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
        studentDB = mFirebaseInstance.getReference(STUDENT_DB);
        tutorDB = mFirebaseInstance.getReference(TUTOR_DB);
    }

    @OnClick(R.id.signUpTV)
    public void onSignUpClick() {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        intent.putExtra(LOGIN_TYPE, loginType);
        startActivity(intent);
    }

    @OnClick(R.id.loginBTN)
    public void onLoginClick() {
        userName = userNameET.getText().toString().trim();
        password = passwordET.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            CustomDialogs.getAlertDialog(LoginActivity.this, null, getString(R.string.please_enter_phone), "");
            CommonMethods.requestFieldFocus(userNameET);
        } else if (TextUtils.isEmpty(password)) {
            CustomDialogs.getAlertDialog(LoginActivity.this, null, getString(R.string.please_enter_password), "");
            CommonMethods.requestFieldFocus(passwordET);

        } else {
            if (CommonMethods.isNetworkAvailable(LoginActivity.this))
                performLogin();
            else
                CustomDialogs.getAlertDialog(LoginActivity.this, null, getString(R.string.internet_not_available), "");
        }
    }

    private void performLogin() {
        progressDialog = CustomDialogs.showProgressDialog(this);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Student student = dataSnapshot1.getValue(Student.class);
                    if (student.getPhone().equals(userName) && student.getPassword().equals(password)) {
                        loginSuccess = true;
                        break;
                    }
                }
                if (loginSuccess) {
                    PreferenceConnector.writeString(LoginActivity.this, USER_NAME, userName);
                    PreferenceConnector.writeString(LoginActivity.this, PASSWORD, password);
                    PreferenceConnector.writeString(LoginActivity.this, LOGIN_TYPE, loginType);
                    PreferenceConnector.writeBoolean(LoginActivity.this, LOGIN_STATUS, true);

                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    CustomDialogs.getAlertDialog(LoginActivity.this, null, getString(R.string.invalid_username_or_password), "");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        };
        switch (loginType) {
            case STUDENT_LOGIN:
                studentDB.addValueEventListener(listener);
                break;
            case TUTOR_LOGIN:
                tutorDB.addValueEventListener(listener);
                break;
        }
    }

    @OnClick(R.id.backIV)
    public void onBackClick() {
        onBackPressed();
    }

}
