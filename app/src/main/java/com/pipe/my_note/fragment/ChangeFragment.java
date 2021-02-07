package com.pipe.my_note.fragment;


import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.pipe.my_note.ui.FragmentHandler;
import com.pipe.my_note.MainActivity;
import com.pipe.my_note.R;
import com.pipe.my_note.observe.Publisher;
import com.pipe.my_note.data.NoteData;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChangeFragment extends Fragment {

    static final String ARG_SECOND_NOTE = "content";
    private EditText etName;
    private TextView tvCreated;
    private Publisher publisher;
    private EditText etTags;
    private TextView tvKey;
    private EditText etLinkCard;
    private EditText etText;
    private NoteData note;
    private CheckBox like;
    private long nowDay;

    public static ChangeFragment newInstance(NoteData note) {
        ChangeFragment f = new ChangeFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SECOND_NOTE, note);
        f.setArguments(args);
        return f;
    }
    public static ChangeFragment newInstance() {
        ChangeFragment fragment = new ChangeFragment();
        return fragment;
    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        MainActivity activity = (MainActivity) context;
//        publisher = activity.getPublisher();
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(ARG_SECOND_NOTE);
        }
    }

    @Override
    public void onDetach() {
        publisher = null;
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        publisher.unsubscribeAll();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change, container, false);
        setHasOptionsMenu(true);
        initView(view);
        if (note != null) {
            changeCardView();
        } else {
            createNewCardView();
        }
        return view;
    }

    private void changeCardView() {
        etName.setText(note.getTitle());
        tvCreated.setText(note.getFormatDate());
        etTags.setText(note.getTag());
        tvKey.setText(Integer.toString(note.getKey()));
        etLinkCard.setText(Integer.toString(note.getLinkCard()));
        etText.setText(note.getText());
    }

    private void createNewCardView() {
//        edName.setText(note.getTitle());
        tvCreated.setText(getDate());
//        edTags.setText(note.getTag());
        tvKey.setText(Integer.toString((int) (Math.random() * 1000)));//должен быть первичный ключ авто инкремент
//        edLinkCard.setText(Integer.toString(note.getLinkCard()));
//        edText.setText(note.getText());
    }

    private void initView(View view) {

        etName = view.findViewById(R.id.zettelkasten_name_enter);
        tvCreated = view.findViewById(R.id.zettelkasten_created_enter);
        etTags = view.findViewById(R.id.zettelkasten_tags_enter);
        tvKey = view.findViewById(R.id.zettelkasten_key_enter);
        etLinkCard = view.findViewById(R.id.zettelkasten_link_card_enter);
        etText = view.findViewById(R.id.zettelkasten_text_enter);

        Button buttonSave = view.findViewById(R.id.button_save);
        buttonSave.setOnClickListener(v -> {
            note = collectNote();
            publisher.notifySingle(note);
            FragmentHandler.popBackStack(requireActivity());
        });
        Button buttonCancel = view.findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(v -> FragmentHandler.popBackStack(requireActivity()));
    }

    private NoteData collectNote() {
        String title = this.etName.getText().toString();
        String tag = this.etTags.getText().toString();
        int key = Integer.parseInt(this.tvKey.getText().toString());
        long created = nowDay;
        int linkCard = linkCardNote();
        String text = this.etText.getText().toString();
        return new NoteData(title, tag, key, created, linkCard, text, false);
    }

    private void setDateTextView(long dateUT) {
        tvCreated.setText(DateUtils.formatDateTime(null, dateUT, DateUtils.FORMAT_SHOW_DATE));
    }

    public String getDate() {
        Date newDay = new Date();
        nowDay = newDay.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(newDay);
    }

    private int linkCardNote() {
        int linkCard;
        if (this.etLinkCard.getText().toString().equals("")) {
            linkCard = 0;
        } else {
            linkCard = Integer.parseInt(this.etLinkCard.getText().toString());
        }
        return linkCard;
    }
}
