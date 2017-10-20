package com.android.tutor.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tutor.R;
import com.android.tutor.models.Student;
import com.android.tutor.utilities.CustomDialogs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.tutor.utilities.Constants.LOGIN_TYPE;
import static com.android.tutor.utilities.Constants.STUDENT_LOGIN;
import static com.android.tutor.utilities.Constants.TUTOR_LOGIN;

public class RegistrationActivity extends AppCompatActivity {

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
    @BindView(R.id.userNameET)
    EditText userNameET;
    @BindView(R.id.passwordET)
    EditText passwordET;
    @BindView(R.id.confPasswordET)
    EditText confPasswordET;
    @BindView(R.id.registerBTN)
    Button registerBTN;
    @BindView(R.id.loginTV)
    TextView loginTV;
    private String loginType = "";
    private DatabaseReference studentDB, tutorDB;
    private static final String TAG = RegistrationActivity.class.getSimpleName();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        if (getIntent().hasExtra(LOGIN_TYPE)) {
            loginType = getIntent().getStringExtra(LOGIN_TYPE);
        }
        setUpDataBase();

    }

    private void setUpDataBase() {
        studentDB = FirebaseDatabase.getInstance().getReference("students");
        tutorDB = FirebaseDatabase.getInstance().getReference("tutors");
        FirebaseDatabase.getInstance().getReference("Database_Name").setValue("Tutor Database");
        FirebaseDatabase.getInstance().getReference("Database_Name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "App title updated");
                String appTitle = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });
    }

    @OnClick(R.id.signUpTV)
    public void onLoginClick() {
        onBackPressed();
    }


    @OnClick(R.id.registerBTN)
    public void onRegisterClick() {
        progressDialog = CustomDialogs.showProgressDialog(this);
        Student student = new Student();
        student.setFirstName(firstNameET.getText().toString().trim());
        student.setLastName(lastNameET.getText().toString().trim());
        student.setEmail(emailET.getText().toString().trim());
        student.setPhone(phoneET.getText().toString().trim());
        student.setConfPassword(passwordET.getText().toString().trim());
        student.setPassword(confPasswordET.getText().toString().trim());

        saveNewUse(student);

    }

    private void saveNewUse(final Student student) {
        String key = emailET.getText().toString();
        if (loginType.equals(STUDENT_LOGIN)) {
            studentDB.orderByKey().equalTo(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    progressDialog.hide();
                    if (dataSnapshot == null || dataSnapshot.getChildren() == null) {
                        studentDB.child(student.getEmail()).setValue(student);
                    } else {
                        CustomDialogs.getAlertDialog(RegistrationActivity.this, "Already has an account with this email id.", "");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if (loginType.equals(TUTOR_LOGIN)) {

        }
    }

    @OnClick(R.id.backIV)
    public void onBackClick() {
        onBackPressed();
    }
}
