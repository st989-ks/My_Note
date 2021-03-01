package com.pipe.my_note.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pipe.my_note.MainActivity;
import com.pipe.my_note.NavigationFragment;
import com.pipe.my_note.R;
import com.pipe.my_note.data.NoteData;
import com.pipe.my_note.data.NoteSource;
import com.pipe.my_note.data.StringData;
import com.pipe.my_note.observe.Publisher;
import com.pipe.my_note.ui.RecyclerViewAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class SecondFragmentShift extends Fragment {

    NavigationFragment navigationFragment;
    boolean changeNote;
    private EditText tvTitle;
    private TextView dateCreated;
    private EditText tvTags;
    private EditText tvLinkCard;
    private EditText tvText;
    private RecyclerViewAdapter recyclerViewAdapter;
    private NoteData noteData;
    private Publisher publisher;
    private NoteSource notesSource;

    public static SecondFragmentShift newInstance(NoteData noteData) {
        SecondFragmentShift fragment = new SecondFragmentShift();
        Bundle args = new Bundle();
        args.putParcelable(StringData.ARG_SECOND_CREATE, noteData);
        fragment.setArguments(args);
        return fragment;
    }

    public static SecondFragmentShift newInstance() {
        SecondFragmentShift fragment = new SecondFragmentShift();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
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
            noteData = getArguments().getParcelable(StringData.ARG_SECOND_CREATE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_shift, container, false);
        initView(view);
        if (noteData != null) {
            initTextNoteChange();
        } else {
            initTextNewNote();
        }
        initButton(view);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
//        writePositionNote();
        outState.putParcelable(StringData.ARG_SECOND_CREATE_LAND, noteData);
    }

    private void initView(View view) {
        tvTitle = view.findViewById(R.id.zettelkasten_name_enter);
        dateCreated = view.findViewById(R.id.zettelkasten_created_enter);
        tvTags = view.findViewById(R.id.zettelkasten_tags_enter);
        tvLinkCard = view.findViewById(R.id.zettelkasten_link_card_enter);
        tvText = view.findViewById(R.id.zettelkasten_text_enter);
    }

    private void initTextNoteChange() {
        changeNote = true;
        tvTitle.setText(noteData.getTitle());
        dateCreated.setText(new SimpleDateFormat("dd/MM/yy").format(noteData.getDate()));
        tvTags.setText(noteData.getTag());
        tvLinkCard.setText(noteData.getLinkCard());
        tvText.setText(noteData.getText());
    }

    private void initTextNewNote() {
        tvTitle.setText("");
        dateCreated.setText(new SimpleDateFormat("dd/MM/yy").format(new Date()));
        tvTags.setText("");
        tvLinkCard.setText("");
        tvText.setText("");
    }

    private NoteData collectNoteData() {
        String title = this.tvTitle.getText().toString();
        String tag = this.tvTags.getText().toString();
        String linkCard = this.tvLinkCard.getText().toString();
        String text = this.tvText.getText().toString();
        if (noteData != null) {
            NoteData answer;
            answer = new NoteData(title, tag, this.noteData.getDate(), linkCard, text, false);
            answer.setId(noteData.getId());
            return answer;
        } else {
            Date date = new Date();
            return new NoteData(title, tag, date, linkCard, text, false);
        }
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
        editor.putBoolean(StringData.ARG_FIRST_BULLED_POSITION, true);
        // Сохраняем значения
        editor.apply();
    }

    private void initButton(View view) {
        Button buttonSave = view.findViewById(R.id.zettelkasten_button_save);
        buttonSave.setOnClickListener(v -> {
            noteData = collectNoteData();
            publisher.notifySingle(noteData);
            if (changeNote) {
                Fragment fragmentFirst = FirstFragmentListOfNotes.newInstance();
                navigationFragment.replaceFragment(R.id.first_note, fragmentFirst, false);
            } else {
                navigationFragment.popBackStack(requireActivity());
            }

        });

        Button buttonCancel = view.findViewById(R.id.zettelkasten_button_cancel);
        buttonCancel.setOnClickListener(v -> {
            Toast.makeText(getContext(), R.string.button_cancel, Toast.LENGTH_SHORT).show();
            navigationFragment.popBackStack(requireActivity());
        });
    }


}