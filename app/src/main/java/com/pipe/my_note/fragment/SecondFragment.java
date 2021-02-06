package com.pipe.my_note.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.pipe.my_note.R;
import com.pipe.my_note.data.NoteData;

public class SecondFragment extends Fragment {

    static final String ARG_SECOND_NOTE = "content";
    private NoteData note;

    public static SecondFragment newInstance(NoteData content) {

        SecondFragment f = new SecondFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SECOND_NOTE, content);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(ARG_SECOND_NOTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        TextView tvName = view.findViewById(R.id.zettelkasten_name);
        tvName.setText(note.getTitle());
        TextView tvCreated = view.findViewById(R.id.zettelkasten_created);
        tvCreated.setText(note.getFormatDate());
        TextView tvTags = view.findViewById(R.id.zettelkasten_tags);
        tvTags.setText(note.getTag());
        TextView tvKey = view.findViewById(R.id.zettelkasten_key);
        tvKey.setText(Integer.toString((note.getKey())));
        TextView tvLinkCard = view.findViewById(R.id.zettelkasten_link_card);
        tvLinkCard.setText(Integer.toString((note.getLinkCard())));
        TextView tvText = view.findViewById(R.id.zettelkasten_text);
        tvText.setText(note.getText());
        Button buttonCancel = view.findViewById(R.id.button_change);
//        buttonCancel.setOnClickListener(v -> {
//            FragmentHandler.replaceFragment(,
//                    new ChangeFragment(),
//                    FragmentHandler.getIdFromOrientation(),
//                    true);
//            FragmentHandler.popBackStack(requireActivity());
//        });
        return view;
    }
}
