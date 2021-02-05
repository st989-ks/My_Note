package com.pipe.my_note.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pipe.my_note.FragmentHandler;
import com.pipe.my_note.MainActivity;
import com.pipe.my_note.R;
import com.pipe.my_note.RecyclerViewAdapter;
import com.pipe.my_note.observe.Publisher;
import com.pipe.my_note.source.Note;
import com.pipe.my_note.source.NoteSource;

public class FirstFragment extends Fragment {

    private static final String ARG_INDEX = "CompletionNote";
    private boolean isLandscape;
    private int completionNote;

    private NoteSource notesSource;
    private RecyclerViewAdapter recyclerViewAdapter;
    private Publisher publisher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        publisher = activity.getPublisher();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        publisher = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(ARG_INDEX, completionNote);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notesSource = new NoteSource(getResources()).init();
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

    private Note getNote(int position) {
        return notesSource.getNote(position);
    }

    private void initList(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerViewAdapter = new RecyclerViewAdapter(notesSource, this);
        recyclerViewAdapter.setOnItemClickListener(position -> {
            showTheCard(getNote(position));
            completionNote = position;
            publisher.subscribe(note -> {
                notesSource.updateNote(position, note);
                completionNote = position;
                recyclerViewAdapter.notifyItemChanged(position);
            });
        });
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
        FragmentHandler.replaceFragment(requireActivity(), secondFragment, R.id.second_zettelkasten, false,false);
    }

    private void showPortTheCard(Note completionNote) {
        // Откроем вторую activity
        Context context = getContext();
        if (context != null) {
            SecondFragment detail = SecondFragment.newInstance(completionNote);
            FragmentHandler.replaceFragment(requireActivity(), detail, R.id.root_of_note, true, false);
        }
    }
}