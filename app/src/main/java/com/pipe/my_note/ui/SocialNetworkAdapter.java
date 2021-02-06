package com.pipe.my_note.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.pipe.my_note.R;
import com.pipe.my_note.data.NoteData;
import com.pipe.my_note.data.NoteSource;
import com.pipe.my_note.data.NoteSourceImpl;


public class SocialNetworkAdapter
        extends RecyclerView.Adapter<SocialNetworkAdapter.ViewHolder> {

    private final static String TAG = "SocialNetworkAdapter";
    private NoteSource dataSource;
    private OnItemClickListener itemClickListener;  // Слушатель будет устанавливаться извне
    private final OnRegisterMenu fragment;
    private int menuPosition;

    public int getMenuPosition() {
        return menuPosition;
    }

    // Передаем в конструктор источник данных
    // В нашем случае это массив, но может быть и запросом к БД
    public SocialNetworkAdapter(NoteSource dataSource, OnRegisterMenu fragment) {
        this.dataSource = dataSource;
        this.fragment = fragment;
    }

    // Создать новый элемент пользовательского интерфейса
    // Запускается менеджером
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Создаем новый элемент пользовательского интерфейса
        // Через Inflater
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.form_of_records, viewGroup, false);
        Log.d(TAG, "onCreateViewHolder");
        // Здесь можно установить всякие параметры
        return new ViewHolder(v);
    }

    // Заменить данные в пользовательском интерфейсе
    // Вызывается менеджером
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        // Получить элемент из источника данных (БД, интернет...)
        // Вынести на экран используя ViewHolder
        viewHolder.setData(dataSource.getNoteData(i));
        Log.d(TAG, "onBindViewHolder");
    }

    // Вернуть размер данных, вызывается менеджером
    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    // Сеттер слушателя нажатий
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    // Интерфейс для обработки нажатий как в ListView
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    // Этот класс хранит связь между данными и элементами View
    // Сложные данные могут потребовать несколько View на
    // один пункт списка
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;
        private TextView textViewTag;
        private TextView textViewDate;
        private CheckBox checkBox;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.name_title);
            textViewTag = itemView.findViewById(R.id.tag_title);
            textViewDate = itemView.findViewById(R.id.date_title);
            checkBox = itemView.findViewById(R.id.check_box);

            registerContextMenu(itemView);

            // Обработчик нажатий на картинке
            itemView.setOnClickListener((View.OnClickListener) v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, getAdapterPosition());
                }
            });

            // Обработчик нажатий на картинке
            itemView.setOnLongClickListener(v -> {
                menuPosition = getLayoutPosition();
                itemView.showContextMenu(10, 10);
                return true;
            });

        }

        private void registerContextMenu(@NonNull View itemView) {
            if (fragment != null) {
                itemView.setOnLongClickListener(v -> {
                    menuPosition = getLayoutPosition();
                    return true;
                });
                fragment.onRegister(itemView);
            }
        }

        public void setData(NoteData NoteData) {
            textViewTitle.setText(NoteData.getTitle());
            textViewTag.setText(NoteData.getTag());
            textViewDate.setText(NoteData.getFormatDate());
            checkBox.setChecked(NoteData.getLike());
        }
    }
}
