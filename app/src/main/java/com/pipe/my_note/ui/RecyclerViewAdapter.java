package com.pipe.my_note.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.pipe.my_note.R;
import com.pipe.my_note.data.NoteData;
import com.pipe.my_note.data.NoteSource;

import java.text.SimpleDateFormat;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final Fragment fragment;
    private NoteSource dataSource;
    private OnItemClickListener clickListener;
    private int menuPosition;

    public RecyclerViewAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setNoteSource(NoteSource noteSource) {
        this.dataSource = noteSource;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.form_of_records,
                parent, false);
        return new ViewHolder(v);
    }

    public int getMenuPosition() {
        return menuPosition;
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(dataSource.getNoteData(position));
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewName;
        private final TextView textViewTag;
        private final TextView textViewDate;
        private final CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.name_title);
            textViewTag = itemView.findViewById(R.id.tag_title);
            textViewDate = itemView.findViewById(R.id.date_title);
            checkBox = itemView.findViewById(R.id.check_box);
            registerContextMenu(itemView);
            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onItemClick(v, getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(v -> {
                menuPosition = getLayoutPosition();
                itemView.showContextMenu(10, 10);
                return true;
            });
            checkBox.setOnClickListener(v -> {
                dataSource.getNoteData(getAdapterPosition()).setLike(checkBox.isChecked());
            });
        }

        public void onBind(NoteData noteData) {
            textViewName.setText(noteData.getTitle());
            textViewTag.setText(noteData.getTag());
            checkBox.setChecked(noteData.getLike());
            textViewDate.setText(new SimpleDateFormat("dd/MM/yy").format(noteData.getDate()));
        }

        private void registerContextMenu(@NonNull View view) {
            if (fragment != null) {
                view.setOnLongClickListener(v -> {
                    menuPosition = getLayoutPosition();
                    return false;
                });
                fragment.registerForContextMenu(view);
            }
        }
    }
}
