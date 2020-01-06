package com.awashwinter.tifuapp.experimental;

import android.content.Intent;
import android.os.Bundle;

import com.awashwinter.tifuapp.R;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.awashwinter.tifuapp.base.Utils;
import com.awashwinter.tifuapp.ui.login.LoginActivity;
import com.awashwinter.tifuapp.usecases.SortType;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private HomeMainViewModel homeViewModel;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    ImageView mImageViewHeader;

    TextView mTextViewDisplayname;

    TextView mTextViewEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        homeViewModel = ViewModelProviders.of(this).get(HomeMainViewModel.class);

        setSupportActionBar(mToolbar);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(mDrawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(mNavigationView, navController);
        initHeaderViews();
        initViewModel();
    }

    private void initHeaderViews() {
        mImageViewHeader = mNavigationView.getHeaderView(0).findViewById(R.id.imageAvatarHeader);
        mTextViewDisplayname = mNavigationView.getHeaderView(0).findViewById(R.id.textDisplaynameHeader);
        mTextViewEmail = mNavigationView.getHeaderView(0).findViewById(R.id.textEmailHeader);
    }


    private void initViewModel(){
        homeViewModel.getIsUserLoggedIn().observe(this, isLoggedIn -> {
            if(!isLoggedIn){
                finish();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        homeViewModel.checkUserIsLoggedIn();
        homeViewModel.setCurrentUser();
        homeViewModel.getLoggedInUser().observe(this, loggedInUser -> {
            Utils.setImage(mImageViewHeader, loggedInUser.getAvatar());
            mTextViewDisplayname.setText(loggedInUser.getDisplayName());
            mTextViewEmail.setText(loggedInUser.getUserId());
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.sort_date_desc){
            homeViewModel.sendSortType(SortType.DATE_DESCENDING);
            item.setChecked(true);
        }
        if(item.getItemId() == R.id.sort_date_asc){
            homeViewModel.sendSortType(SortType.DATE_ASCENDING);
            item.setChecked(true);
        }
        if(item.getItemId() == R.id.sort_like_desc){
            homeViewModel.sendSortType(SortType.LIKE_DESCENDING);
            item.setChecked(true);
        }
        if(item.getItemId() == R.id.sort_like_asc){
            homeViewModel.sendSortType(SortType.LIKE_ASCENDING);
            item.setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
