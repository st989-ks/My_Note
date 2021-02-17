package com.pipe.my_note;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class NavigationFragment {

    private final FragmentManager fragmentManager;

    public NavigationFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void addFragment(int containerViewId, Fragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction = fragmentTransaction.add(containerViewId, fragment);
        if (addToBackStack) fragmentTransaction = fragmentTransaction
                .addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void replaceFragment(int fragmentIdToReplace, Fragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction = fragmentTransaction.replace(fragmentIdToReplace, fragment);
        if (addToBackStack) fragmentTransaction = fragmentTransaction
                .addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void popBackStack(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.popBackStack();
    }
}