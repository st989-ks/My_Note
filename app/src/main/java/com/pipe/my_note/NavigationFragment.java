package com.pipe.my_note;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public class NavigationFragment {

    private final FragmentManager fragmentManager;

    public NavigationFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void addFragment(int containerViewId, Fragment fragment, String addToBackStack,
                                   boolean popUpBeforeReplace) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction = fragmentTransaction.add(containerViewId, fragment);
        if (addToBackStack != null) fragmentTransaction = fragmentTransaction
                .addToBackStack(addToBackStack);
        fragmentTransaction.commit();
    }

    public void replaceFragment(int fragmentIdToReplace, Fragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction = fragmentTransaction.replace(fragmentIdToReplace, fragment);
        if (addToBackStack) fragmentTransaction = fragmentTransaction
                .addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static void popBackStack(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    public static Fragment getVisibleFragment(FragmentManager fragmentManager) {
        List<Fragment> fragments = fragmentManager.getFragments();
        int countFragments = fragments.size();

        Log.wtf("getVisibleFragment", "=============================  "
                + countFragments);

        for (int i = countFragments - 1; i >= 0; i--) {
            Fragment fragment = fragments.get(i);
            if (fragment.isVisible())
                return fragment;
        }
        return null;
    }
}