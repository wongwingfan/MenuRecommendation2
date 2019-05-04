package com.example.menurecommendation;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipeFragment.DataPassListener, HomeFragment.generateRecipePageListener {

    public final int REQUEST_CODE = 1;
    private String welcomeReply;
    HomeFragment homeFragment;
    MapFragment mapFragment;
    RecipeFragment recipeFragment;
    ProfileFragment profileFragment;
    public static MyDBHandler dbHandler;
    public static Toolbar appBar;
    public static TextView appTitle;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (homeFragment == null)
                        homeFragment = new HomeFragment();
                    selectedFragment = homeFragment;
                    break;
                case R.id.navigation_map:
                    if (mapFragment == null){
                        mapFragment = new MapFragment();
                    }
                    selectedFragment = mapFragment;
                    break;
                case R.id.navigation_recipe:
                    if (recipeFragment == null)
                        recipeFragment = new RecipeFragment();
                    selectedFragment = recipeFragment;
                    break;
                case R.id.navigation_profile:
                    if (profileFragment == null)
                        profileFragment = new ProfileFragment();
                    selectedFragment = profileFragment;
                    break;
            }
            while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                onBackPressed();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.Pcolor2));
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);
        if (isFirstRun) {
            //show start activity
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                    .putBoolean("isFirstRun", false).apply();
            Log.d("New user", "new User");
            openDataBase();
            startActivityForResult(new Intent(MainActivity.this, FirstLaunchActivity.class), REQUEST_CODE);
        } else {
            openDataBase();
            setHomePage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                welcomeReply = data.getStringExtra(FirstLaunchActivity.WELCOME_Reply);
                setContentView(R.layout.welcome_screen);
                TextView welcomeText = findViewById(R.id.welcome);
                welcomeText.setText(welcomeReply);
                fadingEffect(welcomeText);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setHomePage();
                    }
                }, 4000);
            }
        }
    }

    private void fadingEffect(TextView txtView){
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f ) ;
        fadeIn.setDuration(1500);
        txtView.startAnimation(fadeIn);
    }

    @Override
    public void passData(String data) {
        RecipeSearchFragment recipeSearchFragment = new RecipeSearchFragment();
        Bundle args = new Bundle();
        args.putString(RecipeSearchFragment.DATA_RECEIVE, data);
        recipeSearchFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, recipeSearchFragment)
                .addToBackStack(null)
                .commit();
    }

    public void passID(int data) {
        RecipeSearchFragment recipeSearchFragment = new RecipeSearchFragment();
        Bundle args = new Bundle();
        args.putInt(RecipeSearchFragment.DATA_RECEIVE, data);
        recipeSearchFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, recipeSearchFragment)
                .addToBackStack(null)
                .commit();
    }
    @Override
    public void generateRecipePage() {
        MenuGenerationFragment menuGenerationFragment = new MenuGenerationFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, menuGenerationFragment)
                .addToBackStack(null)
                .commit();
    }


    public void openDataBase(){
        dbHandler = new MyDBHandler(this);
        try {
            dbHandler.createDataBase();
        }
        catch (IOException e) {
            throw new Error("Unable to create database");
        }
    }

    public void setHomePage(){
        setContentView(R.layout.activity_main_page);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        appBar = findViewById(R.id.my_toolbar);
        appTitle = findViewById(R.id.toolbar_title);
        appBar.setNavigationIcon(null);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (homeFragment == null)
            homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                .addToBackStack(null)
                .commit();
        appTitle.setText(R.string.home);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}
