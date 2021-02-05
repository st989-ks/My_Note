package com.pipe.my_note.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.pipe.my_note.R;

public class AboutFragment extends Fragment {

//    static final String ARG_ABOUT_FRAGMENT = "ARG_ABOUT_FRAGMENT";
//    private Content content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            content = getArguments().getParcelable(ARG_ABOUT_FRAGMENT);
//        }
    }

//    public static AboutFragment newInstance(Content content) {
//        AboutFragment f = new AboutFragment();
//        Bundle args = new Bundle();
//        args.putParcelable(ARG_ABOUT_FRAGMENT, content);
//        f.setArguments(args);
//        return f;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }
}