package com.pipe.my_note.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pipe.my_note.MainActivity;
import com.pipe.my_note.NavigationFragment;
import com.pipe.my_note.R;
import com.pipe.my_note.data.NoteData;
import com.pipe.my_note.data.NoteSource;
import com.pipe.my_note.data.NoteSourceImpl;
import com.pipe.my_note.data.NoteSourceResponse;
import com.pipe.my_note.data.StringData;
import com.pipe.my_note.observe.Observer;
import com.pipe.my_note.observe.Publisher;
import com.pipe.my_note.ui.RecyclerViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class FirstFragmentListOfNotes extends Fragment {

    private static final int MY_DEFAULT_DURATION = 1000;

    private int completionNote;
    private RecyclerViewAdapter recyclerViewAdapter;
    private NoteSource notesSource;

    NavigationFragment navigationFragment;
    private Publisher publisher;

    public boolean isLandscape;
    private RecyclerView recyclerView;
    private String newDay;

    public static FirstFragmentListOfNotes newInstance() {
        return new FirstFragmentListOfNotes();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity)context;
        publisher = activity.getPublisher();
        navigationFragment = activity.getNavigationFragment();
    }

    @Override
    public void onDetach() {
        navigationFragment = null;
        publisher = null;
        super.onDetach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        initView(view);
        recyclerViewAdapter.setNoteSource(notesSource);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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
            getDate();

            notesSource.addNoteData(new NoteData(
                    getString(R.string.current_note)+" "+(notesSource.size()),
                    getString(R.string.new_note),
                    Integer.toString(notesSource.size()), newDay,
                    "", "", true));


            recyclerViewAdapter.notifyItemInserted(notesSource.size()-1);
            recyclerViewAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(notesSource.size()-1);
//            Fragment secondFragmentShift = new SecondFragmentShift();
//            NavigationFragment.replaceFragment((FragmentActivity) activity, getIdFromOrientation((FragmentActivity) activity),
//                    secondFragmentShift, false);

            publisher.subscribe(new Observer() {
                @Override
                public void updateNotes(NoteData noteData) {
                    notesSource.addNoteData(noteData);
                    recyclerViewAdapter.notifyItemInserted(notesSource.size() - 1);
                    // это сигнал, чтобы вызванный метод onCreateView
                    // перепрыгнул на конец списка
//                    moveToFirstPosition = true;
                }
            });
            Toast.makeText(getContext(), R.string.menu_add_a_note, Toast.LENGTH_SHORT).show();
            return true;
        });

        delete.setOnMenuItemClickListener(item -> {
            deleteNoteData ();
            recyclerViewAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(), R.string.menu_delete_a_note, Toast.LENGTH_SHORT).show();
            return true;
        });
    }
    private NoteData getNote(int position) {
        return notesSource.getNoteData(position);
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        initRecyclerView();
        setHasOptionsMenu(true);
        notesSource = new NoteSourceImpl(getResources()).init(new NoteSourceResponse() {
            @Override
            public void initialized(NoteSource cardsData) {
                recyclerViewAdapter.notifyDataSetChanged();
            }
        });

    }

    public void initRecyclerView() {

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Установим адаптер
        recyclerViewAdapter = new RecyclerViewAdapter(notesSource, this);
        recyclerView.setAdapter(recyclerViewAdapter);

        // Установим анимацию. А чтобы было хорошо заметно, сделаем анимацию долгой
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(MY_DEFAULT_DURATION);
        animator.setRemoveDuration(MY_DEFAULT_DURATION);
        recyclerView.setItemAnimator(animator);

        recyclerViewAdapter.setOnItemClickListener((v, position) -> {
            getIdFromOrientation(getActivity());
            completionNote = position;
            Fragment secondFragment;
            secondFragment = SecondFragment.newInstance(getNote(position));
            navigationFragment.replaceFragment( getIdFromOrientation(getActivity()),
                    secondFragment, false);
//                publisher.subscribe(note -> {
//                    notesSource.updateNoteData(position, note);
//                    recyclerViewAdapter.notifyItemChanged(position);
//                });
        });
//        writeCurrentNote();
    }

    private void writeCurrentNote(){
        // Специальный класс для хранения
        SharedPreferences sharedPref = requireActivity().getSharedPreferences(StringData.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        // Сохраняем посредством специального класса editor
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(StringData.ARG_FIFE_COUNT, notesSource.size());
        // Сохраняем значения
        editor.apply();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public static int getIdFromOrientation(FragmentActivity activity) {
        Boolean isLandscape = activity.getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (isLandscape) {
            return R.id.second_note;
        } else {
            return R.id.first_note;
        }
    }
    public String getDate() {
        Date newDay = new Date();
        long longNowDay = newDay.getTime();
        this.newDay = Long.toString(longNowDay);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(longNowDay);
    }

    public void deleteNoteData (){
        ArrayList<Integer> arrDelete = new ArrayList<>();
        for(int i = 0; i < notesSource.size(); i++) {
            boolean like = notesSource.getNoteData(i).getLike();
            if(like) {
                arrDelete.add(0,i);
            }
        }
        for(int i = 0; i < arrDelete.size(); i++) {
            notesSource.deleteNoteData(arrDelete.get(i));
        }
    }
}