package com.android.tutor.activities;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.tutor.R;
import com.android.tutor.utilities.PreferenceConnector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.tutor.utilities.Constants.LOGIN_STATUS;
import static com.android.tutor.utilities.Constants.LOGIN_TYPE;
import static com.android.tutor.utilities.Constants.PASSWORD;
import static com.android.tutor.utilities.Constants.USER_NAME;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.navigationView)
    NavigationView navigationView;

    @BindView(R.id.menuIV)
    protected ImageView menuIV;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    protected String loginType = "";
    protected String userName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        loginType = PreferenceConnector.readString(this, LOGIN_TYPE, "");
        userName = PreferenceConnector.readString(this, USER_NAME, "");
    }

    public void setActivityLayout(@LayoutRes int layoutResID) {
        LinearLayout activityContainer = (LinearLayout) findViewById(R.id.bodyContainerLL);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        setUpListeners();
        if (this instanceof DashboardActivity) {
            menuIV.setImageResource(R.drawable.menu);
        } else if (this instanceof ProfileActivity) {
            menuIV.setImageResource(R.drawable.ic_arrow_back_black_24dp);

        }
    }

    private void setUpListeners() {
        ButterKnife.bind(this);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Check to see which item was being clicked and perform appropriate action
        drawerLayout.closeDrawer(GravityCompat.START);
        item.setChecked(true);
        switch (item.getItemId()) {
            //Replacing the main content with ContentFragment Which is our Inbox View;
            case R.id.nav_home:
                if (!(this instanceof DashboardActivity)) {
                    Intent intent = new Intent(BaseActivity.this, DashboardActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.nav_profile:
                if (!(this instanceof ProfileActivity)) {
                    Intent intent = new Intent(BaseActivity.this, ProfileActivity.class);
                    intent.putExtra(LOGIN_TYPE, loginType);
                    startActivity(intent);
                }
                break;
            case R.id.nav_settings:
                break;
            case R.id.logout:
                performLogout();
                break;

        }

        //Checking if the item is in checked state or not, if not make it in checked state
        if (item.isChecked()) {
            item.setChecked(false);
        } else {
            item.setChecked(true);
        }

        return true;
    }

    private void performLogout() {
        PreferenceConnector.writeBoolean(this, LOGIN_STATUS, false);
        PreferenceConnector.writeString(this, USER_NAME, "");
        PreferenceConnector.writeString(this, PASSWORD, "");
        Intent intent = new Intent(BaseActivity.this, LoginTypeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this instanceof DashboardActivity) {
            navigationView.getMenu().getItem(R.id.nav_home).setChecked(true);
        } else if (this instanceof ProfileActivity) {
            navigationView.getMenu().getItem(R.id.nav_profile).setChecked(true);
        }
    }

    @OnClick(R.id.menuIV)
    public void onMenuClick() {
        if (this instanceof DashboardActivity) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        } else if (this instanceof ProfileActivity) {
            onBackPressed();
        }
    }

}
