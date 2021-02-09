package com.pipe.my_note.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.pipe.my_note.MainActivity;
import com.pipe.my_note.Navigation;
import com.pipe.my_note.R;
import com.pipe.my_note.data.NoteData;
import com.pipe.my_note.data.NoteSource;
import com.pipe.my_note.observe.Observer;
import com.pipe.my_note.observe.Publisher;
import com.pipe.my_note.ui.Constant;
import com.pipe.my_note.ui.RecyclerViewAdapter;

public class SecondFragment extends Fragment {

    TextView tvName;
    TextView tvCreated;
    TextView tvTags;
    TextView tvKey;
    TextView tvLinkCard;
    TextView tvText;
    private NoteData note;
    private boolean isLandscape;
    private Publisher publisher;
    private NoteSource notesSource;
    private int completionNote;
    private RecyclerViewAdapter adapter;
    private Navigation navigation;

    public static SecondFragment newInstance(NoteData content) {

        SecondFragment f = new SecondFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constant.ARG_NOTE, content);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        publisher = null;
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(Constant.ARG_NOTE);
        }
        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        initTextCard(view);
        initButton(view);
    }

    private void initTextCard(View view) {
        tvName = view.findViewById(R.id.zettelkasten_name);
        tvCreated = view.findViewById(R.id.zettelkasten_created);
        tvTags = view.findViewById(R.id.zettelkasten_tags);
        tvKey = view.findViewById(R.id.zettelkasten_key);
        tvLinkCard = view.findViewById(R.id.zettelkasten_link_card);
        tvText = view.findViewById(R.id.zettelkasten_text);
        tvName.setText(note.getTitle());
        tvCreated.setText(note.getFormatDate());
        tvTags.setText(note.getTag());
        tvKey.setText(Integer.toString((note.getKey())));
        tvLinkCard.setText(Integer.toString((note.getLinkCard())));
        tvText.setText(note.getText());
    }

    private void initButton(View view) {
        Button buttonCancel = view.findViewById(R.id.button_change);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecondFragment.this.showTheCard(note);
                publisher.subscribe(new Observer() {
                    @Override
                    public void updateNotes(NoteData note) {
                        notesSource.addNoteData(note);
                        completionNote = notesSource.size() - 1;
                        adapter.notifyItemInserted(completionNote);
                    }
                });
                Log.i(MainActivity.TAG, SecondFragment.this.getClass().getSimpleName() +
                        " -initView" + " -buttonCancel.setOnClickListener");
            }
        });
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
        fragment = ChangeFragment.newInstance(completionNote);
        Navigation.replaceFragment(requireActivity(), fragment,
                R.id.second_zettelkasten, true, true, false);
    }

    private void showPortTheCard(NoteData completionNote) {
        // Откроем вторую activity
        Context context = getContext();
        Fragment fragment;
        if (context != null) {
            fragment = ChangeFragment.newInstance(completionNote);
            Navigation.replaceFragment(requireActivity(), fragment,
                    R.id.root_of_note, true, true, false);
        }
    }
}
