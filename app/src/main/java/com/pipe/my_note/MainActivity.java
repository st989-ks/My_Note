package com.pipe.my_note;

import android.content.SharedPreferences;
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
import com.pipe.my_note.data.NoteData;
import com.pipe.my_note.data.NoteSource;
import com.pipe.my_note.data.NoteSourceImpl;
import com.pipe.my_note.data.StringData;
import com.pipe.my_note.fragments.FirstFragmentListOfNotes;
import com.pipe.my_note.fragments.ForthFragmentAbout;
import com.pipe.my_note.fragments.SecondFragment;
import com.pipe.my_note.fragments.ThirdFragmentSettings;
import com.pipe.my_note.observe.Publisher;

public class MainActivity extends AppCompatActivity {
    public static boolean bulledPositionForReplace;
    private final Publisher publisher = new Publisher();
    public boolean isLandscape;
    private NoteSource notesSource;
    private boolean exit = false;
    NavigationFragment navigationFragment;
    private int numberPosition = 1;

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
    }

    public Publisher getPublisher() {
        return publisher;
    }
    public com.pipe.my_note.NavigationFragment getNavigationFragment() {
        return navigationFragment;
    }

    private void initView() {
        initToolbar();
        addFragmentOrientating();
    }

    private void addFragmentOrientating() {
        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        if (isLandscape) {
            showLandTheCard();
        } else {
            showPartTheCard();
        }
    }

    private void showLandTheCard() {
        // Создаём новый фрагмент с текущей позицией
        Fragment fragmentFirst = FirstFragmentListOfNotes.newInstance();
        getNavigationFragment().replaceFragment( R.id.first_note, fragmentFirst, true);

        Fragment secondFragment;
//        readNumberNote();
        secondFragment = SecondFragment.newInstance(notesSource.getNoteData(numberPosition));

        getNavigationFragment().replaceFragment( R.id.second_note, secondFragment, false);
    }

    private void showPartTheCard() {
        // Создаём новый фрагмент с текущей позицией
        Fragment fragment;
//        readNumberNote();
        if (bulledPositionForReplace) {
            fragment = SecondFragment.newInstance(notesSource.getNoteData(numberPosition));

        } else {
            fragment = new FirstFragmentListOfNotes();
        }
        bulledPositionForReplace = false;
        getNavigationFragment().replaceFragment(R.id.first_note, fragment, true);
    }

    private NoteData getNote(int position) {
        return notesSource.getNoteData(position);
    }

    private void readNumberNote() {
        SharedPreferences sharedPref = getSharedPreferences(StringData.SHARED_PREFERENCE_NAME, MODE_PRIVATE);

        int position = sharedPref.getInt(StringData.ARG_SIX_POSITION, 0);
        if (position <= notesSource.size()) numberPosition = position;


//        bulledPositionForReplace = sharedPref.getBoolean(StringData.ARG_FIRST_BULLED_POSITION, false);
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
                    getNavigationFragment().replaceFragment( getIdFromOrientation(this),
                            thirdFragmentSettings, false);
                    Toast.makeText(this, R.string.menu_settings, Toast.LENGTH_SHORT).show();
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                case R.id.menu_about:
                    Fragment forthFragmentAbout = new ForthFragmentAbout();
                    getNavigationFragment().replaceFragment( getIdFromOrientation(this),
                            forthFragmentAbout, false);
                    Toast.makeText(this, R.string.menu_about, Toast.LENGTH_SHORT).show();
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
            }
            return false;
        });
    }

    @Override
    public void onBackPressed() {
        addFragmentOrientating();
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