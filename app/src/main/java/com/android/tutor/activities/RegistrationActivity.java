package com.android.tutor.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import static com.android.tutor.utilities.Constants.REGISTER;
import static com.android.tutor.utilities.Constants.STUDENT_DB;
import static com.android.tutor.utilities.Constants.STUDENT_LOGIN;
import static com.android.tutor.utilities.Constants.TUTOR_DB;
import static com.android.tutor.utilities.Constants.TUTOR_LOGIN;
import static com.android.tutor.utilities.Constants.Tutor_Database;
import static com.android.tutor.utilities.Constants.USER_NAME;

public class RegistrationActivity extends AppCompatActivity implements CustomDialogs.ICustomAlertDialogListener {

    @BindView(R.id.backIV)
    ImageView backIV;
    @BindView(R.id.firstNameET)
    EditText firstNameET;
    @BindView(R.id.lastNameET)
    EditText lastNameET;
    @BindView(R.id.emailET)
    EditText emailET;
    @BindView(R.id.phoneET)
    EditText phoneET;
    @BindView(R.id.passwordET)
    EditText passwordET;
    @BindView(R.id.confPasswordET)
    EditText confPasswordET;
    @BindView(R.id.registerBTN)
    Button registerBTN;
    @BindView(R.id.titleTV)
    TextView titleTV;
    private String loginType = "";
    private DatabaseReference studentDB, tutorDB;
    private static final String TAG = RegistrationActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private boolean isUserRegistered=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        if (getIntent().hasExtra(LOGIN_TYPE)) {
            loginType = getIntent().getStringExtra(LOGIN_TYPE);
        }
        if(loginType.equals(STUDENT_LOGIN))
        titleTV.setText(getString(R.string.student_reg));
        else
            titleTV.setText(getString(R.string.tutor_reg));
        setUpDataBase();

    }

    private void setUpDataBase() {
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
        studentDB = mFirebaseInstance.getReference(STUDENT_DB);
        tutorDB = mFirebaseInstance.getReference(TUTOR_DB);
    }

    @OnClick(R.id.loginTV)
    public void onLoginClick() {
        onBackPressed();
    }


    @OnClick(R.id.registerBTN)
    public void onRegisterClick() {
        Student student = new Student();
        student.setFirstName(firstNameET.getText().toString().trim());
        student.setLastName(lastNameET.getText().toString().trim());
        student.setEmail(emailET.getText().toString().trim());
        student.setPhone(phoneET.getText().toString().trim());
        student.setConfPassword(passwordET.getText().toString().trim());
        student.setPassword(confPasswordET.getText().toString().trim());

        if (TextUtils.isEmpty(student.getFirstName())) {
            CustomDialogs.getAlertDialog(this, null, getString(R.string.please_enter_first_name), "");
            CommonMethods.requestFieldFocus(firstNameET);
        } else if (TextUtils.isEmpty(student.getLastName())) {
            CustomDialogs.getAlertDialog(this, null, getString(R.string.please_enter_last_name_), "");
            CommonMethods.requestFieldFocus(lastNameET);

        } else if (TextUtils.isEmpty(student.getEmail())) {
            CustomDialogs.getAlertDialog(this, null, getString(R.string.please_enter_email), "");
            CommonMethods.requestFieldFocus(emailET);

        } else if (!CommonMethods.validateEmail(student.getEmail())) {
            CustomDialogs.getAlertDialog(this, null, getString(R.string.please_enter_a_valid_email), "");
            CommonMethods.requestFieldFocus(emailET);
        } else if (TextUtils.isEmpty(student.getPhone())) {
            CustomDialogs.getAlertDialog(this, null, getString(R.string.please_enter_phone), "");
            CommonMethods.requestFieldFocus(phoneET);

        } else if (student.getPhone().length() != 10) {
            CustomDialogs.getAlertDialog(this, null, getString(R.string.please_enter_10_digit_phone_number), "");
            CommonMethods.requestFieldFocus(phoneET);

        } else if (TextUtils.isEmpty(student.getPassword())) {
            CustomDialogs.getAlertDialog(this, null, getString(R.string.please_enter_password), "");
            CommonMethods.requestFieldFocus(passwordET);

        } else if (TextUtils.isEmpty(student.getConfPassword())) {
            CustomDialogs.getAlertDialog(this, null, getString(R.string.please_enter_confirm_password), "");
            CommonMethods.requestFieldFocus(confPasswordET);
        } else if (!student.getPassword().equals(student.getConfPassword())) {
            CustomDialogs.getAlertDialog(this, null, getString(R.string.password_and_confirm_password_doesnt_match), "");
            CommonMethods.requestFieldFocus(confPasswordET);

        } else {
            progressDialog = CustomDialogs.showProgressDialog(this);
            checkAvailability(student);
        }


    }

    private void checkAvailability(final Student student) {
        ValueEventListener listener=  new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Student st = dataSnapshot1.getValue(Student.class);
                    if (st.getPhone().equals(student.getPhone())) {
                        isUserRegistered = true;
                        break;
                    }
                }
                if (isUserRegistered) {
                    CustomDialogs.getAlertDialog(RegistrationActivity.this, null, getString(R.string.already_registered_with_the_current_phone_number), "");
                } else {
                    saveNewUser(student);
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

    private void saveNewUser(final Student student) {
        String key = student.getPhone();
        if (loginType.equals(STUDENT_LOGIN)) {
            studentDB.child(key).setValue(student);
            addStudentListener();

        } else if (loginType.equals(TUTOR_LOGIN)) {
            tutorDB.child(key).setValue(student);
            addTutorListener();
        }
    }

    private void addTutorListener() {
        tutorDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                CustomDialogs.getAlertDialog(RegistrationActivity.this, RegistrationActivity.this, "Registered successfully.", REGISTER);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    private void addStudentListener() {
        studentDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                CustomDialogs.getAlertDialog(RegistrationActivity.this, RegistrationActivity.this, "Registered successfully.", REGISTER);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    @OnClick(R.id.backIV)
    public void onBackClick() {
        onBackPressed();
    }

    @Override
    public void onAlertDialogYesClick(String tag) {
        if (tag.equals(REGISTER)) {
            onBackPressed();
        }
    }

    @Override
    public void onAlertDialogNoClick(String tag) {

    }
}
