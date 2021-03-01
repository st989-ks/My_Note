package com.pipe.my_note.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pipe.my_note.MainActivity;
import com.pipe.my_note.NavigationFragment;
import com.pipe.my_note.R;
import com.pipe.my_note.data.NoteData;
import com.pipe.my_note.data.NoteSource;
import com.pipe.my_note.data.NotesSourceFirebaseImpl;
import com.pipe.my_note.data.StringData;
import com.pipe.my_note.dialog.MyBottomSheetDialogFragment;
import com.pipe.my_note.dialog.OnDialogListener;
import com.pipe.my_note.observe.Publisher;
import com.pipe.my_note.ui.RecyclerViewAdapter;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.pipe.my_note.MainActivity.getIdFromOrientation;

public class FirstFragmentListOfNotes extends Fragment {

    private static final int MY_DEFAULT_DURATION = 1000;
    public boolean isLandscape;
    public boolean deleteNotes;
    NavigationFragment navigationFragment;
    private RecyclerViewAdapter recyclerViewAdapter;
    private NoteSource notesSource;
    private Publisher publisher;
    private RecyclerView recyclerView;
    private boolean bulledPositionForReplace;
    private int positionNote;

    public static FirstFragmentListOfNotes newInstance() {
        return new FirstFragmentListOfNotes();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        publisher = activity.getPublisher();
        navigationFragment = activity.getNavigationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerViewAdapter = new RecyclerViewAdapter(this);
        notesSource = new NotesSourceFirebaseImpl().
                init(notesSource -> recyclerViewAdapter.notifyDataSetChanged());
        recyclerViewAdapter.setNoteSource(notesSource);
        Log.wtf(StringData.TAG, String.valueOf(notesSource.size()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        initView(view);
        setHasOptionsMenu(true);
        Log.wtf(StringData.TAG, String.valueOf(notesSource.size()));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            positionNote = savedInstanceState.getInt(StringData.ARG_SIX_FIRST_FRAGMENT_POSITION);
            addFragmentOrientating();
        } else {
            positionNote = 0;
        }
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        initRecyclerView();
    }

    public void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Установим адаптер
        recyclerView.setAdapter(recyclerViewAdapter);
        // Установим анимацию. А чтобы было хорошо заметно, сделаем анимацию долгой
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(MY_DEFAULT_DURATION);
        animator.setRemoveDuration(MY_DEFAULT_DURATION);
        recyclerView.setItemAnimator(animator);

        recyclerViewAdapter.setOnItemClickListener((v, position) -> {
            positionNote = getNotePosition(notesSource.getNoteData(position));
            Fragment secondFragment;
            secondFragment = SecondFragment.newInstance(getNote(positionNote), positionNote);
            navigationFragment.replaceFragment(getIdFromOrientation(requireActivity()),
                    secondFragment, false);
            publisher.subscribe(note -> {
                notesSource.updateNoteData(positionNote, note);
                recyclerViewAdapter.notifyItemChanged(positionNote);
            });
        });
    }

    @Override
    public void onDetach() {
        navigationFragment = null;
        publisher = null;
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem search = menu.findItem(R.id.menu_search);
        MenuItem addNote = menu.findItem(R.id.menu_add_a_note);
        MenuItem delete = menu.findItem(R.id.menu_delete_a_note);

        SearchView searchText = (SearchView) search.getActionView();
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getContext(), query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        addNote.setOnMenuItemClickListener(item -> {
            navigationFragment.replaceFragment(getIdFromOrientation(requireActivity())
                    , SecondFragmentShift.newInstance(), true);
            publisher.subscribe(noteData -> {
                notesSource.addNoteData(noteData);
                recyclerViewAdapter.notifyItemInserted(notesSource.size() - 1);
            });
            Toast.makeText(getContext(), R.string.menu_add_a_note, Toast.LENGTH_SHORT).show();
            return true;
        });

        delete.setOnMenuItemClickListener(item -> {
            MyBottomSheetDialogFragment bottomSheetDialogFragment = new MyBottomSheetDialogFragment();

            bottomSheetDialogFragment.setOnDialogListener(() -> {
                deleteNoteData();
                recyclerViewAdapter.notifyDataSetChanged();
            });
            bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), StringData.DIALOG);
            return true;
        });
    }

    public void deleteNoteData() {
        ArrayList<Integer> arrDelete = new ArrayList<>();
        for (int i = 0; i < notesSource.size(); i++) {
            boolean like = notesSource.getNoteData(i).getLike();
            if (like) {
                arrDelete.add(0, i);
            }
        }
        for (int i = 0; i < arrDelete.size(); i++) {
            notesSource.deleteNoteData(arrDelete.get(i));
        }
    }

    private int getNotePosition(NoteData findNote) {
        return notesSource.getNoteDataPosition(findNote);
    }

    private void writeCurrentNote() {
        // Специальный класс для хранения
        SharedPreferences sharedPref = requireActivity().getSharedPreferences(StringData.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        // Сохраняем посредством специального класса editor
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(StringData.ARG_FIFE_COUNT, positionNote);
        // Сохраняем значения
        editor.apply();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(StringData.ARG_SIX_FIRST_FRAGMENT_POSITION, positionNote);
        outState.putParcelable(StringData.ARG_SIX_FIRST_FRAGMENT_NOTE, (notesSource.getNoteData(positionNote)));
        super.onSaveInstanceState(outState);
    }


    private NoteData getNote(int position) {
        return notesSource.getNoteData(position);
    }

    private void addFragmentOrientating() {
        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        if (isLandscape) {
            showLandTheNote();
        } else {
            showPartTheNote();
        }
    }

    private void showLandTheNote() {
        // Создаём новый фрагмент с текущей позицией
        Fragment secondFragment;
        secondFragment = SecondFragment.newInstance(getNote(positionNote));
        navigationFragment.replaceFragment(R.id.second_note, secondFragment, false);
    }

    private void showPartTheNote() {
        // Создаём новый фрагмент с текущей позицией
        Fragment fragment;
        if (bulledPositionForReplace) {
            fragment = SecondFragment.newInstance(getNote(0));
        } else {
            fragment = new FirstFragmentListOfNotes();
        }
        bulledPositionForReplace = false;
        navigationFragment.replaceFragment(R.id.first_note, fragment, true);
    }


}