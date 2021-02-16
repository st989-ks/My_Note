package com.pipe.my_note.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pipe.my_note.MainActivity;
import com.pipe.my_note.NavigationFragment;
import com.pipe.my_note.R;
import com.pipe.my_note.data.NoteData;
import com.pipe.my_note.data.NoteSource;
import com.pipe.my_note.data.NoteSourceImpl;
import com.pipe.my_note.data.StringData;
import com.pipe.my_note.observe.Observer;
import com.pipe.my_note.observe.Publisher;
import com.pipe.my_note.ui.RecyclerViewAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class SecondFragmentShift extends Fragment {

    EditText tvName;
    TextView tvCreated;
    TextView tvKey;
    EditText tvTags;
    EditText tvLinkCard;
    EditText tvText;
    MainActivity activity;
    private String newDay;

    private NoteData noteData;

    NavigationFragment navigationFragment;
    private Publisher publisher;

    public static SecondFragmentShift newInstance(NoteData content) {
        SecondFragmentShift secondFragmentShift = new SecondFragmentShift();
        Bundle args = new Bundle();
        args.putParcelable(StringData.ARG_SECOND_CREATE, content);
        secondFragmentShift.setArguments(args);
        return secondFragmentShift;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        readNewCurrentNote();
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
            noteData = getArguments().getParcelable(StringData.ARG_SECOND_CREATE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_shift, container, false);
        initTextCardChange(view);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        writePositionNote();
        outState.putParcelable(StringData.ARG_SECOND_CREATE_LAND, noteData);
    }

    private void initTextCardChange(View view) {
        tvName = view.findViewById(R.id.zettelkasten_name_enter);
        tvCreated = view.findViewById(R.id.zettelkasten_created_enter);
        tvTags = view.findViewById(R.id.zettelkasten_tags_enter);
        tvKey = view.findViewById(R.id.zettelkasten_key_enter);
        tvLinkCard = view.findViewById(R.id.zettelkasten_link_card_enter);
        tvText = view.findViewById(R.id.zettelkasten_text_enter);
        if (noteData != null) {
            tvName.setText(noteData.getTitle());
            tvCreated.setText(noteData.getFormatDate());
            tvTags.setText(noteData.getTag());
            tvKey.setText(noteData.getKey());
            tvLinkCard.setText(noteData.getLinkCard());
            tvText.setText(noteData.getText());
        }
    }

    private void readNewCurrentNote(){
        SharedPreferences sharedPref = requireActivity().getSharedPreferences(StringData.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        int newCurrentNote = sharedPref.getInt(StringData.ARG_FIFE_COUNT, Integer.parseInt("0"));
    }

    private void writePositionNote(){
        // Специальный класс для хранения
        SharedPreferences sharedPref = requireActivity().getSharedPreferences(StringData.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        // Сохраняем посредством специального класса editor
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(StringData.ARG_SIX_POSITION, Integer.parseInt(noteData.getKey()));
        editor.putBoolean(StringData.ARG_FIRST_BULLED_POSITION, true);
        // Сохраняем значения
        editor.apply();
    }


}