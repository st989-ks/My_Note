package com.pipe.my_note;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class SecondFragment extends Fragment {

    static final String ARG_SECOND_NOTE = "content";

    private Content content;

    public static SecondFragment newInstance(Content content) {
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
            content = getArguments().getParcelable(ARG_SECOND_NOTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        TextView tvName = view.findViewById(R.id.zettelkasten_name);
        tvName.setText(content.getNameCard());
        TextView tvCreated = view.findViewById(R.id.zettelkasten_created);
        tvCreated.setText(content.getCreated());
        TextView tvTags = view.findViewById(R.id.zettelkasten_tags);
        tvTags.setText(content.getTags());
        TextView tvKey = view.findViewById(R.id.zettelkasten_key);
        tvKey.setText(Integer.toString((content.getKey())));
        TextView tvLinkCard = view.findViewById(R.id.zettelkasten_link_card);
        tvLinkCard.setText(content.getLinkCard());
        TextView tvText = view.findViewById(R.id.zettelkasten_text);
        tvText.setText(content.getTextNote());
        return view;
    }
}
