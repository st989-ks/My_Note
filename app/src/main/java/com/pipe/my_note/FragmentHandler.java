package com.pipe.my_note;

import android.content.res.Configuration;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

abstract class FragmentHandler {
    static void replaceFragment(FragmentActivity activity, Fragment fragment, int fragmentIdToReplace, boolean addToBackStack) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(fragmentIdToReplace, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (addToBackStack){
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    static int getIdFromOrientation(FragmentActivity activity) {
        Boolean isLandscape = activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (isLandscape) {
            return R.id.second_zettelkasten;
        } else {
            return R.id.root_of_note;
        }
    }
}

