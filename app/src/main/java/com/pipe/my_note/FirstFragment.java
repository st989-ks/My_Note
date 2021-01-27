package com.pipe.my_note;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FirstFragment extends Fragment {

    private boolean isLandscape;

    public FirstFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Определение, можно ли будет расположить рядом во фрагменте
        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        // Если можно рядом, то сделаем это
//        if (isLandscape) {
//            showLandTheCard(0);
//        }
    }

    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        /*args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        */
        fragment.setArguments(args);
        return fragment;
    }

    private void initList(View view) {
        LinearLayout layoutView = (LinearLayout) view;
        String[] tags = getResources().getStringArray(R.array.tags);
        // В этом цикле создаём элемент TextView,
        // заполняем его значениями,
        // и добавляем на экран.
        // Кроме того, создаём обработку касания на элемент
        Context context = getContext();
        for (int i = 0; i < tags.length; i++) {
            if (context != null) {
                String tag = tags[i];
                TextView textView = new TextView(context);
                textView.setText(tag);
                textView.setTextSize(30);
                layoutView.addView(textView);
                final int fi = i;
                textView.setOnClickListener(v -> showTheCard(fi));
            }
        }
    }

    private void showTheCard(int index) {
        if (isLandscape) {
//            showLandTheCard(index);
        } else {
            showPortTheCard(index);
        }
    }

//    private void showLandTheCard(int index) {
//        // Создаём новый фрагмент с текущей позицией
//        SecondFragment detail = SecondFragment.newInstance(index);
//        // Выполняем транзакцию по замене фрагмента
//        FragmentActivity context = getActivity();
//        if (context != null) {
//            FragmentManager fragmentManager = context.getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.zettelkasten, detail);  // замена фрагмента
//            //fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//            fragmentTransaction.commit();
//        }
//    }

    private void showPortTheCard(int index) {
        // Откроем вторую activity
        Context context = getContext();
        if (context != null) {
            Intent intent = new Intent(context, SecondActivity.class);
            // и передадим туда параметры
            intent.putExtra(SecondFragment.ARG_INDEX, index);
            startActivity(intent);
        }
    }
}