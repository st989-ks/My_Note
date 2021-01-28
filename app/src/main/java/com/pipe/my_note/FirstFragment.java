package com.pipe.my_note;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FirstFragment extends Fragment {

    private static final String ARG_INDEX = "CompletionNote";
    private boolean isLandscape;
    private Content completionNote;

    public FirstFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(ARG_INDEX, completionNote);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        if (savedInstanceState != null) {
            completionNote = savedInstanceState.getParcelable(ARG_INDEX);
        } else {
            completionNote = newNote(0);
        }

        if (isLandscape) {
            showLandTheCard(completionNote);
        }
    }

    private Content newNote(int index) {
        Resources res = getResources();
        return new Content(res.getStringArray(R.array.tags)[index],
                res.getStringArray(R.array.date)[index], res.getStringArray(R.array.tags)[index],
                res.getIntArray(R.array.key)[index], res.getStringArray(R.array.related_cards)[index],
                res.getStringArray(R.array.text)[index]);
    }

    private void initList(View view) {
        LinearLayout layoutView = (LinearLayout) view;
        String[] tags = getResources().getStringArray(R.array.tags);
        Context context = getContext();
        for (int i = 0; i < tags.length; i++) {
            if (context != null) {
                String tag = tags[i];
                TextView textView = new TextView(context);
                textView.setText(tag);
                textView.setTextSize(30);
                layoutView.addView(textView);
                final int fi = i;
                textView.setOnClickListener(v -> {
                    completionNote = newNote(fi);
                    showTheCard(completionNote);
                });
            }
        }
    }

    private void showTheCard(Content completionNote) {
        if (isLandscape) {
            showLandTheCard(completionNote);
        } else {
            showPortTheCard(completionNote);
        }
    }

    private void showLandTheCard(Content completionNote) {
        // Создаём новый фрагмент с текущей позицией
        SecondFragment secondFragment = SecondFragment.newInstance(completionNote);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.zettelkasten, secondFragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();

    }

    private void showPortTheCard(Content completionNote) {
        // Откроем вторую activity
        Context context = getContext();
        if (context != null) {
            Intent intent = new Intent(context, SecondActivity.class);
            // и передадим туда параметры
            intent.putExtra(SecondFragment.ARG_SECOND_NOTE, completionNote);
            startActivity(intent);
        }
    }
}