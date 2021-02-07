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

import com.pipe.my_note.MainActivity;
import com.pipe.my_note.R;
import com.pipe.my_note.data.NoteData;
import com.pipe.my_note.data.NoteSource;
import com.pipe.my_note.data.NoteSourceImpl;
import com.pipe.my_note.observe.Publisher;
import com.pipe.my_note.ui.FragmentHandler;
import com.pipe.my_note.ui.OnRegisterMenu;
import com.pipe.my_note.ui.RecyclerViewAdapter;

public class FirstFragment extends Fragment implements OnRegisterMenu {

    private static final String ARG_INDEX = "CompletionNote";
    private boolean isLandscape;
    private int completionNote;

    private NoteSource notesSource;
    private RecyclerViewAdapter adapter;
    private Publisher publisher;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        initView(view);
        setHasOptionsMenu(true);
        return view;
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

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        // Получим источник данных для списка
        notesSource = new NoteSourceImpl(getResources()).init();
        initRecyclerView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(ARG_INDEX, completionNote);
        super.onSaveInstanceState(outState);
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        initList(view);
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notesSource = new NoteSourceImpl(getResources()).init();
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

    private NoteData getNote(int position) {
        return notesSource.getNoteData(position);
    }

    private void initRecyclerView() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new RecyclerViewAdapter(notesSource, this);

        adapter.setOnItemClickListener((v, position) -> {
            FirstFragment.this.showTheCard(FirstFragment.this.getNote(position));
            completionNote = position;
            publisher.subscribe(note -> {
                notesSource.updateNoteData(position, note);
                completionNote = position;
                adapter.notifyItemChanged(position);
            });
        });
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void showTheCard(NoteData completionNote) {
        if (isLandscape) {
            showLandTheCard(completionNote);
        } else {
            showPortTheCard(completionNote);
        }
    }

    private void showLandTheCard(NoteData completionNote) {
        // Создаём новый фрагмент с текущей позицией
        SecondFragment secondFragment = SecondFragment.newInstance(completionNote);
        FragmentHandler.replaceFragment(requireActivity(), secondFragment, R.id.second_zettelkasten, false, false);
    }

    private void showPortTheCard(NoteData completionNote) {
        // Откроем вторую activity
        Context context = getContext();
        if (context != null) {
            SecondFragment secondFragment = SecondFragment.newInstance(completionNote);
            FragmentHandler.replaceFragment(requireActivity(), secondFragment, R.id.root_of_note, true, false);
        }
    }

    @Override
    public void onRegister(View view) {
        registerForContextMenu(view);
    }
}