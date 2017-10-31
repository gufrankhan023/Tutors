package com.android.tutor.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.tutor.R;
import com.android.tutor.customviews.RoundedImageView;
import com.android.tutor.models.Student;
import com.android.tutor.utilities.CommonMethods;
import com.android.tutor.utilities.CustomDialogs;
import com.android.tutor.utilities.PreferenceConnector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;

import static com.android.tutor.utilities.Constants.CAMERA_IMAGE;
import static com.android.tutor.utilities.Constants.LOGIN_STATUS;
import static com.android.tutor.utilities.Constants.LOGIN_TYPE;
import static com.android.tutor.utilities.Constants.PASSWORD;
import static com.android.tutor.utilities.Constants.PICK_IMAGE;
import static com.android.tutor.utilities.Constants.REGISTER;
import static com.android.tutor.utilities.Constants.STUDENT_DB;
import static com.android.tutor.utilities.Constants.STUDENT_LOGIN;
import static com.android.tutor.utilities.Constants.TUTOR_DB;
import static com.android.tutor.utilities.Constants.TUTOR_LOGIN;
import static com.android.tutor.utilities.Constants.USER_NAME;

public class ProfileActivity extends BaseActivity implements CustomDialogs.IOnBottomSheetClickListener, CustomDialogs.ICustomAlertDialogListener {
    @BindView(R.id.profileIMG)
    RoundedImageView profileIMG;
    @BindView(R.id.firstNameET)
    EditText firstNameET;
    @BindView(R.id.lastNameET)
    EditText lastNameET;
    @BindView(R.id.phoneET)
    EditText phoneET;
    @BindView(R.id.emailET)
    EditText emailET;

    private DatabaseReference studentDB, tutorDB;
    private ProgressDialog progressDialog;
    private Student profile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_profile);
        profileIMG.setImageResource(R.drawable.logo);
        setUpDataBase();
        getProfileFromServer();
    }

    private void setUpDataBase() {
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
        studentDB = mFirebaseInstance.getReference(STUDENT_DB);
        tutorDB = mFirebaseInstance.getReference(TUTOR_DB);
    }

    @Optional
    @OnClick({R.id.profileIMG, R.id.updateProfileBTN})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profileIMG:
                CustomDialogs.getBottomSheetDialog(this);
                break;
            case R.id.updateProfileBTN:
                upddateProfile();
                break;

        }

    }

    private void upddateProfile() {
        progressDialog = CustomDialogs.showProgressDialog(this);
        profile.setFirstName(firstNameET.getText().toString().trim());
        profile.setLastName(lastNameET.getText().toString().trim());
        profile.setPhone(phoneET.getText().toString().trim());
        profile.setEmail(emailET.getText().toString().trim());
        switch (loginType) {
            case STUDENT_LOGIN:
                studentDB.child(userName).setValue(profile);
                addStudentProfileUpdateListener();
                break;
            case TUTOR_LOGIN:
                tutorDB.child(userName).setValue(profile);
                addTutorProfileUpdateListener();
                break;
        }

    }

    @Override
    public void onOptionClick(View view) {
        switch (view.getId()) {
            case R.id.galleryIV:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                break;
            case R.id.cameraIV:
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_IMAGE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK ) {
            if (requestCode == PICK_IMAGE && data.getData() != null) {
                Uri uri = data.getData();

                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    profileIMG.setImageBitmap(imageBitmap);
                    if (profile != null) {
                        profile.setProfilImage(CommonMethods.convertBitmapToString(imageBitmap));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == CAMERA_IMAGE) {
                try {
                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                    profileIMG.setImageBitmap(imageBitmap);
                    if (profile != null) {
                        profile.setProfilImage(CommonMethods.convertBitmapToString(imageBitmap));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void addStudentProfileUpdateListener() {
        studentDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                CustomDialogs.getAlertDialog(ProfileActivity.this, ProfileActivity.this, "Profile updated successfully.", REGISTER);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                CustomDialogs.getAlertDialog(ProfileActivity.this, ProfileActivity.this, "Error occurred while updating profile.", "");
            }
        });
    }
    private void addTutorProfileUpdateListener() {
        tutorDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                CustomDialogs.getAlertDialog(ProfileActivity.this, ProfileActivity.this, "Profile updated successfully.", REGISTER);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                CustomDialogs.getAlertDialog(ProfileActivity.this, ProfileActivity.this, "Error occurred while updating profile.", "");
            }
        });
    }

    @Override
    public void onAlertDialogYesClick(String tag) {

    }

    @Override
    public void onAlertDialogNoClick(String tag) {

    }


    public void getProfileFromServer() {
        progressDialog = CustomDialogs.showProgressDialog(this);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Student student = dataSnapshot1.getValue(Student.class);
                    if (student.getPhone().equals(userName)) {
                        profile = student;
                        break;
                    }
                }
                if (profile != null) {
                    populateProfile();
                } else {
                    CustomDialogs.getAlertDialog(ProfileActivity.this, ProfileActivity.this, "Error occurred while fetching profile.", "");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                CustomDialogs.getAlertDialog(ProfileActivity.this, ProfileActivity.this, "Error occurred while fetching profile.", "");
            }
        };
        switch (loginType) {
            case STUDENT_LOGIN:
                studentDB.child(STUDENT_DB).child(userName);
                studentDB.addValueEventListener(listener);
                break;
            case TUTOR_LOGIN:
                tutorDB.child(STUDENT_DB).child(userName);
                tutorDB.addValueEventListener(listener);
                break;
        }

    }

    private void populateProfile() {
        firstNameET.setText(profile.getFirstName());
        lastNameET.setText(profile.getLastName());
        phoneET.setText(profile.getPhone());
        emailET.setText(profile.getEmail());
        if (!TextUtils.isEmpty(profile.getProfilImage()))
            profileIMG.setImageBitmap(CommonMethods.convertStringTOBitmap(profile.getProfilImage()));
    }
}
