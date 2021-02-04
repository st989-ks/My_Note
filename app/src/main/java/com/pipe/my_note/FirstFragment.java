package com.pipe.my_note;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FirstFragment extends Fragment {

    private static final String ARG_INDEX = "CompletionNote";
    private ArrayList<Note> notesArray = new ArrayList<>();
    private boolean isLandscape;
    private int completionNote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(ARG_INDEX, completionNote);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setNotesArray();
        initList(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        if (savedInstanceState != null) {
            completionNote = savedInstanceState.getInt(ARG_INDEX);
        } else {
            completionNote = 0;
        }
        if (isLandscape) {
            showLandTheCard(getNote(completionNote));
        }
    }

    private void setNotesArray() {
        if (notesArray.size() > 0) {
            notesArray.clear();
        }
        String[] notes = getResources().getStringArray(R.array.tags);
        for (int i = 0; i < notes.length; i++) {
            notesArray.add(createNote(i));
        }
    }

    private Note createNote(int index) {
        Resources res = getResources();
        Note note = new Note(res.getStringArray(R.array.title)[index],
                res.getStringArray(R.array.tags)[index],
                res.getIntArray(R.array.key)[index],
                Long.parseLong(res.getStringArray(R.array.date)[index]),
                res.getIntArray(R.array.related_cards)[index],
                res.getStringArray(R.array.text)[index]);
        return note;
    }

    private Note getNote(int position) {
        return notesArray.get(position);
    }

    private void initList(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(notesArray);
        recyclerViewAdapter.setOnItemClickListener(position -> showTheCard(getNote(position)));
        recyclerView.setAdapter(recyclerViewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void showTheCard(Note completionNote) {
        if (isLandscape) {
            showLandTheCard(completionNote);
        } else {
            showPortTheCard(completionNote);
        }
    }

    private void showLandTheCard(Note completionNote) {
        // Создаём новый фрагмент с текущей позицией
        SecondFragment secondFragment = SecondFragment.newInstance(completionNote);
        FragmentHandler.replaceFragment(requireActivity(), secondFragment, R.id.second_zettelkasten, false);
    }

    private void showPortTheCard(Note completionNote) {
        // Откроем вторую activity
        Context context = getContext();
        if (context != null) {
            SecondFragment detail = SecondFragment.newInstance(completionNote);
            FragmentHandler.replaceFragment(requireActivity(), detail, R.id.root_of_note, true);
        }
    }
}