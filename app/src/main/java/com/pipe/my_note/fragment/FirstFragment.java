package com.pipe.my_note.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.pipe.my_note.ui.Constant;
import com.pipe.my_note.ui.FragmentHandler;
import com.pipe.my_note.ui.OnRegisterMenu;
import com.pipe.my_note.ui.RecyclerViewAdapter;

public class FirstFragment extends Fragment implements OnRegisterMenu {

    public boolean isLandscape;
    private int completionNote = 0;

    private NoteSource notesSource;
    private RecyclerViewAdapter adapter;
    private Publisher publisher;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(Constant.ARG_INDEX, completionNote);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView(view);
    }

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
            completionNote = savedInstanceState.getInt(Constant.ARG_INDEX);
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

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new RecyclerViewAdapter(notesSource, this);

//        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
//        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator, null));
//        recyclerView.addItemDecoration(itemDecoration);

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
        Fragment fragment;
        if (completionNote == null) {
            fragment = ChangeFragment.newInstance();
        } else {
            fragment = SecondFragment.newInstance(completionNote);
        }
        FragmentHandler.replaceFragment(requireActivity(), fragment,
                R.id.second_zettelkasten, false, false);
    }

    private void showPortTheCard(NoteData completionNote) {
        // Откроем вторую activity
        Context context = getContext();
        Fragment fragment;
        if (context != null) {
            if (completionNote == null) {
                fragment = ChangeFragment.newInstance();
            } else {
                fragment = SecondFragment.newInstance(completionNote);
            }
            FragmentHandler.replaceFragment(requireActivity(), fragment,
                    R.id.root_of_note, true, false);
        }
    }

    @Override
    public void onRegister(View view) {
        registerForContextMenu(view);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = requireActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_about:
//                FragmentHandler.replaceFragment(this,
//                        new AboutFragment(),
//                        FragmentHandler.getIdFromOrientation(this),
//                        true, false);
                Log.i(MainActivity.TAG, this.getClass().getSimpleName() +
                        " -onOptionsItemSelected" + " -menu_about");
                return true;
            case R.id.menu_settings:
//                FragmentHandler.replaceFragment(MainActivity.this,
//                        new SettingsFragment(),
//                        FragmentHandler.getIdFromOrientation(MainActivity.this),
//                        true, false);
                Log.i(MainActivity.TAG, this.getClass().getSimpleName() +
                        " -onOptionsItemSelected" + " -menu_settings");
                return true;
            case R.id.menu_add_a_note:
//                FragmentHandler.replaceFragment(MainActivity.this,
//                        new ChangeFragment(),
//                        FragmentHandler.getIdFromOrientation(MainActivity.this),
//                        true, false);
                showTheCard(null);
                publisher.subscribe(note -> {
                    notesSource.addNoteData(note);
                    completionNote = notesSource.size() - 1;
                    adapter.notifyItemInserted(completionNote);
                });
                Log.i(MainActivity.TAG, this.getClass().getSimpleName() +
                        " -onOptionsItemSelected" + " -menu_add_a_note");
                return true;
            case R.id.menu_delete_a_note:
                Log.i(MainActivity.TAG, this.getClass().getSimpleName() +
                        " -onOptionsItemSelected" + " -menu_delete_a_note");
                return true;
        }
        Log.i(MainActivity.TAG, this.getClass().getSimpleName() + " -onOptionsItemSelected");
        return super.onOptionsItemSelected(item);
    }

}