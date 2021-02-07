package com.pipe.my_note.fragment;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pipe.my_note.R;
import com.pipe.my_note.data.NoteData;
import com.pipe.my_note.data.NoteSource;
import com.pipe.my_note.data.NoteSourceImpl;
import com.pipe.my_note.ui.OnRegisterMenu;
import com.pipe.my_note.ui.SocialNetworkAdapter;

import java.util.Date;

public class SocialNetworkFragment extends Fragment implements OnRegisterMenu {

    private NoteSource data;
    private SocialNetworkAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        // Получим источник данных для списка
        initView(view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_a_note:
                data.addNoteData(new NoteData("Заголовок " + data.size(),
                        "Описание " + data.size(),
                        data.size()+1,
                        new Date().getTime(),
                        data.size(),
                        "Текст"+data.size(),
                        false));
                adapter.notifyItemInserted(data.size() - 1);
                recyclerView.scrollToPosition(data.size() - 1);
                return true;
            case R.id.menu_delete_a_note:
                data.clearNoteData();
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        // Получим источник данных для списка
        data = new NoteSourceImpl(getResources()).init();
        initRecyclerView();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = adapter.getMenuPosition();

        switch (item.getItemId()) {
            case R.id.menu_add_a_note:
                data.updateNoteData(position,
                        new NoteData( data.getNoteData(position).getTitle(),
                                data.getNoteData(position).getTag(),
                                data.getNoteData(position).getKey(),
                                data.getNoteData(position).getCreated(),
                                data.getNoteData(position).getLinkCard(),
                                data.getNoteData(position).getText(),
                                data.getNoteData(position).getLike()));
                adapter.notifyItemChanged(position);
                //adapter.notifyDataSetChanged();
                return true;
            case R.id.menu_delete_a_note:
                data.deleteNoteData(position);
                adapter.notifyItemRemoved(position);
                //adapter.notifyDataSetChanged();

                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void initRecyclerView() {

        // Эта установка служит для повышения производительности системы
        recyclerView.setHasFixedSize(true);

        // Будем работать со встроенным менеджером
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Установим адаптер
        adapter = new SocialNetworkAdapter(data, this);
        recyclerView.setAdapter(adapter);

        // Добавим разделитель карточек
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator, null));
        recyclerView.addItemDecoration(itemDecoration);

        // Установим слушателя
        adapter.setOnItemClickListener(new SocialNetworkAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(), String.format("Позиция - %d", position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRegister(View view) {
        registerForContextMenu(view);
    }

    public static SocialNetworkFragment newInstance() {
        return new SocialNetworkFragment();
    }
}
