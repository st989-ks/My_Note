package com.pipe.my_note;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.pipe.my_note.fragment.AboutFragment;
import com.pipe.my_note.fragment.ChangeFragment;
import com.pipe.my_note.fragment.FirstFragment;
import com.pipe.my_note.fragment.SettingsFragment;
import com.pipe.my_note.observe.Publisher;
import com.pipe.my_note.ui.Navigation;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final static String TAG = " Мое сообщение ";
    private final Publisher publisher = new Publisher();
    private Navigation navigation;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment fragment = getVisibleFragment(fragmentManager);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        navigation = new Navigation(getSupportFragmentManager());
        getNavigation().replaceFragment(MainActivity.this, new FirstFragment(),
                R.id.root_of_note, false, true);
        Log.i(TAG, this.getClass().getSimpleName() + " -onCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem search = menu.findItem(R.id.menu_search);
        SearchView searchText = (SearchView) search.getActionView();
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                Log.i(TAG, this.getClass().getSimpleName() + " -onQueryTextSubmit");
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG, this.getClass().getSimpleName() + " -onQueryTextChange");
                return true;
            }
        });
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.i(TAG, this.getClass().getSimpleName() + " -onCreateOptionsMenu");
        return true;
    }

    public Navigation getNavigation() {
        return navigation;
    }

    private void initView() {
        Toolbar toolbar = initToolBar();
        initDrawer(toolbar);
        Log.i(TAG, this.getClass().getSimpleName() + " -initView");
    }

    private Toolbar initToolBar() {
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        Log.i(TAG, this.getClass().getSimpleName() + " -initToolBar");
        return toolbar;
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
                    Navigation.replaceFragment(MainActivity.this,
                            new SettingsFragment(),
                            Navigation.getIdFromOrientation(MainActivity.this),
                            true, false);
                    drawer.closeDrawer(GravityCompat.START);
                    Log.i(TAG, this.getClass().getSimpleName() + " -initDrawer" + " -menu_settings");
                    return true;
                case R.id.menu_about:
                    Navigation.replaceFragment(MainActivity.this,
                            new AboutFragment(),
                            Navigation.getIdFromOrientation(MainActivity.this),
                            true, false);
                    drawer.closeDrawer(GravityCompat.START);
                    Log.i(TAG, this.getClass().getSimpleName() + " -initDrawer" + " -menu_about");
                    return true;
                case R.id.menu_add_a_note:
                    Navigation.replaceFragment(MainActivity.this,
                            new ChangeFragment(),
                            Navigation.getIdFromOrientation(MainActivity.this),
                            true, false);
                    drawer.closeDrawer(GravityCompat.START);
                    Log.i(TAG, this.getClass().getSimpleName() + " -initDrawer" + " -menu_add_a_note");
                    return true;
                case R.id.menu_delete_a_note:
                    drawer.closeDrawer(GravityCompat.START);
                    Log.i(TAG, this.getClass().getSimpleName() + " -initDrawer" + " -menu_delete_a_note");
                    return true;
            }
            Log.i(TAG, this.getClass().getSimpleName() + " -initDrawer" + " -false");
            return false;
        });
    }

    public Publisher getPublisher() {
        Log.i(TAG, this.getClass().getSimpleName() + " -getPublisher");
        return publisher;
    }

    private Fragment getVisibleFragment(FragmentManager fragmentManager) {
        List<Fragment> fragments = fragmentManager.getFragments();
        int countFragments = fragments.size();
        for (int i = countFragments - 1; i >= 0; i--) {
            Fragment fragment = fragments.get(i);
            if (fragment.isVisible())
                return fragment;
        }
        return null;
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}