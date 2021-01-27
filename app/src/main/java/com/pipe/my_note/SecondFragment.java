package com.pipe.my_note;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SecondFragment extends Fragment {

    static final String ARG_INDEX = "index";
    private int index;

    public static SecondFragment newInstance(int index) {
        SecondFragment f = new SecondFragment();    // создание
        // Передача параметра
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView = getView().findViewById(R.id.zettelkasten);
        TypedArray text = getResources().obtainTypedArray(R.array.text);
        textView.setText(text.getResourceId(index, -1));

//        TextView textView = view.findViewsWithText(R.layout.activity_zettelkasten);
//        AppCompatImageView imageCoatOfArms = view.findViewById(R.id.zettelkasten);
//         Получить из ресурсов массив указателей на изображения гербов
//        TypedArray images = getResources().obtainTypedArray(R.array.text);
//         Выбрать по индексу подходящий
//        imageCoatOfArms.setImageResource(images.getResourceId(index, -1));
    }
}
