package com.pipe.my_note.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.pipe.my_note.MainActivity;
import com.pipe.my_note.NavigationFragment;
import com.pipe.my_note.R;
import com.pipe.my_note.data.NoteData;
import com.pipe.my_note.data.NoteSource;
import com.pipe.my_note.data.StringData;
import com.pipe.my_note.observe.Observer;
import com.pipe.my_note.observe.Publisher;
import com.pipe.my_note.ui.RecyclerViewAdapter;

import java.text.SimpleDateFormat;

import static android.content.Context.MODE_PRIVATE;

public class SecondFragment extends Fragment {

    TextView tvName;
    TextView tvCreated;
    TextView tvTags;
    TextView tvKey;
    TextView tvLinkCard;
    TextView tvText;

    private int positionNote;
    private NoteData noteData;
    private Publisher publisher;
    NavigationFragment navigationFragment;
    private NoteSource notesSource;
    private RecyclerViewAdapter recyclerViewAdapter;

    public static SecondFragment newInstance(NoteData content) {
        SecondFragment secondFragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putParcelable(StringData.ARG_SECOND, content);
        secondFragment.setArguments(args);
        return secondFragment;
    }
    public static SecondFragment newInstance(NoteData content, int position) {
        SecondFragment secondFragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putParcelable(StringData.ARG_SECOND, content);
        args.putInt(StringData.ARG_FIFE_COUNT,position);
        secondFragment.setArguments(args);
        return secondFragment;
    }

    public static int getIdFromOrientation(FragmentActivity activity) {
        boolean isLandscape = activity.getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (isLandscape) {
            return R.id.second_note;
        } else {
            return R.id.first_note;
        }
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            noteData = getArguments().getParcelable(StringData.ARG_SECOND);
            positionNote = getArguments().getInt(StringData.ARG_FIFE_COUNT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        initTextCard(view);
        initButton(view);
        return view;
    }

    @SuppressLint("SimpleDateFormat")
    private void initTextCard(View view) {
        tvName = view.findViewById(R.id.zettelkasten_name);
        tvCreated = view.findViewById(R.id.zettelkasten_created);
        tvTags = view.findViewById(R.id.zettelkasten_tags);
        tvLinkCard = view.findViewById(R.id.zettelkasten_link_card);
        tvText = view.findViewById(R.id.zettelkasten_text);
        if (noteData != null) {
            tvName.setText(noteData.getTitle());
            tvCreated.setText(new SimpleDateFormat("dd/MM/yy").format(noteData.getDate()));
            tvTags.setText(noteData.getTag());
            tvLinkCard.setText(noteData.getLinkCard());
            tvText.setText(noteData.getText());
        }
    }

    private void initButton(View view) {
        Button buttonChange = view.findViewById(R.id.zettelkasten_button_change);
        recyclerViewAdapter = new RecyclerViewAdapter(this);

        buttonChange.setOnClickListener(v -> {
            Fragment secondFragmentShift;
            secondFragmentShift = SecondFragmentShift.newInstance(noteData);
            navigationFragment.replaceFragment( getIdFromOrientation(requireActivity()),
                    secondFragmentShift, true);
        });
    }

    private void readNewCurrentNote() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences(StringData.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        int newCurrentNote = sharedPref.getInt(StringData.ARG_FIFE_COUNT, Integer.parseInt("0"));
    }

    private void writePositionNote() {
        // Специальный класс для хранения
        SharedPreferences sharedPref = requireActivity().getSharedPreferences(StringData.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        // Сохраняем посредством специального класса editor
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(StringData.ARG_SIX_POSITION, Integer.parseInt(noteData.getId()));
        // Сохраняем значения
        editor.apply();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(StringData.ARG_SECOND_CREATE_LAND, noteData);
        MainActivity.bulledPositionForReplace = true;
        super.onSaveInstanceState(outState);
    }
}