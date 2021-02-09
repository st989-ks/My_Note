package com.pipe.my_note.fragment;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.pipe.my_note.MainActivity;
import com.pipe.my_note.R;
import com.pipe.my_note.data.NoteData;
import com.pipe.my_note.observe.Publisher;
import com.pipe.my_note.ui.Constant;
import com.pipe.my_note.Navigation;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChangeFragment extends Fragment {

    private boolean newFragment;
    private EditText etName;
    private TextView tvCreated;
    private Publisher publisher;
    private EditText etTags;
    private TextView tvKey;
    private EditText etLinkCard;
    private EditText etText;
    private NoteData noteData;
    private NoteData noteDataChang;
    private CheckBox like;
    private long nowDay;
    private boolean isLandscape;

    //фрагмент для редактирования данных
    public static ChangeFragment newInstance(NoteData note) {
        ChangeFragment f = new ChangeFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constant.ARG_CHANGE_NOTE, note);
        f.setArguments(args);
        return f;
    }

    //фрагмент для добавления новых данных
    public static ChangeFragment newInstance() {
        ChangeFragment f = new ChangeFragment();
        return f;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        publisher = activity.getPublisher();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            noteData = getArguments().getParcelable(Constant.ARG_CHANGE_NOTE);
        }
        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change, container, false);
        initView(view);
        if (noteData != null) {
            newFragment = true;
            changeCardView();
        } else {
            newFragment = false;
            createNewCardView();
        }
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onDetach() {
        publisher = null;
        super.onDetach();
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

    private void changeCardView() {
        etName.setText(noteData.getTitle());
        tvCreated.setText(noteData.getFormatDate());
        etTags.setText(noteData.getTag());
        tvKey.setText(Integer.toString(noteData.getKey()));
        etLinkCard.setText(Integer.toString(noteData.getLinkCard()));
        etText.setText(noteData.getText());
    }

    private void createNewCardView() {
        tvCreated.setText(getDate());

        //тут будет ключ авто инкремент
        tvKey.setText(Integer.toString((int) (Math.random() * 1000)));
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
            noteDataChang = collectNote();
            publisher.notifySingle(noteDataChang);
            popBackStackIfNotLand();
        });
        Button buttonCancel = view.findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(v -> popBackStackIfNotLand());
    }


    private void popBackStackIfNotLand() {
        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)
            Navigation.popBackStack(requireActivity());
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
