package com.pipe.my_note;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.navigation.NavigationView;
import com.pipe.my_note.fragments.FirstFragmentListOfNotes;
import com.pipe.my_note.fragments.ForthFragmentAbout;
import com.pipe.my_note.fragments.ThirdFragmentSettings;
import com.pipe.my_note.observe.Publisher;

public class MainActivity extends AppCompatActivity {
    public static boolean bulledPositionForReplace;
    private final Publisher publisher = new Publisher();
    NavigationFragment navigationFragment;
    private boolean exit = false;

    public static int getIdFromOrientation(FragmentActivity activity) {
        Boolean isLandscape = activity.getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (isLandscape) {
            return R.id.second_note;
        } else {
            return R.id.first_note;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationFragment = new NavigationFragment(getSupportFragmentManager());
        initView();
        getNewFragment();
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public NavigationFragment getNavigationFragment() {
        return navigationFragment;
    }

    private void initView() {
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initDrawer(toolbar);
    }

    private void initDrawer(Toolbar toolbar) {
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open_nav_drawer, R.string.close_nav_drawer
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.navigation_bar);
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            switch (itemId) {
                case R.id.menu_settings:
                    Fragment thirdFragmentSettings = new ThirdFragmentSettings();
                    getNavigationFragment().replaceFragment(getIdFromOrientation(this),
                            thirdFragmentSettings, false);
                    Toast.makeText(this, R.string.menu_settings, Toast.LENGTH_SHORT).show();
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                case R.id.menu_about:
                    Fragment forthFragmentAbout = new ForthFragmentAbout();
                    getNavigationFragment().replaceFragment(getIdFromOrientation(this),
                            forthFragmentAbout, false);
                    Toast.makeText(this, R.string.menu_about, Toast.LENGTH_SHORT).show();
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
            }
            return false;
        });
    }

    private void getNewFragment() {
        getNavigationFragment().replaceFragment(R.id.first_note, new FirstFragmentListOfNotes(), true);
    }

    @Override
    public void onBackPressed() {
        getNewFragment();
        if (exit) {
            MainActivity.this.finish();
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 1000);
        }
    }
}