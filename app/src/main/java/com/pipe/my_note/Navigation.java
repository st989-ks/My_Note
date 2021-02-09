package com.pipe.my_note;

import android.content.res.Configuration;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.pipe.my_note.fragment.SecondFragment;

public class Navigation {

    private final FragmentManager fragmentManager;

    public Navigation(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public static void replaceFragment(FragmentActivity activity, Fragment fragment,
                                       int fragmentIdToReplace, boolean addToBackStack,
                                       boolean popUpBeforeReplace, boolean add) {
        //Получить менеджер фрагментов
        FragmentManager fragmentManager = activity.getSupportFragmentManager();

        // Открыть транзакцию
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Boolean isLandscape = false;
        if (popUpBeforeReplace) {
            Fragment oldFragment = fragmentManager.findFragmentById(fragmentIdToReplace);
            if (activity.getResources().getConfiguration().orientation == Configuration
                    .ORIENTATION_LANDSCAPE && oldFragment instanceof SecondFragment) {
                isLandscape = true;
            }
        }
        if (isLandscape) {
            fragmentManager.popBackStack();
        } else {
            fragmentTransaction.replace(fragmentIdToReplace, fragment);

            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commitAllowingStateLoss();
        }

        fragmentTransaction.replace(fragmentIdToReplace, fragment);
//        fragmentManager.popBackStack();
    }

    public static int getIdFromOrientation(FragmentActivity activity) {
        Boolean isLandscape = activity.getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (isLandscape) {
            return R.id.second_zettelkasten;
        } else {
            return R.id.root_of_note;
        }
    }

    public static void popBackStack(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.popBackStack();
    }
}

