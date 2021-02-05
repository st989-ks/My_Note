package com.pipe.my_note;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.pipe.my_note.source.Note;
import com.pipe.my_note.source.NoteSource;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final Fragment fragment;
    private OnItemClickListener clickListener;
    private NoteSource dataSource;
    private int menuPosition;

    public RecyclerViewAdapter(NoteSource dataSource, Fragment fragment) {
        this.dataSource = dataSource;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.form_of_records,
                parent,
                false);
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
        holder.onBind(dataSource.getNote(position));
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewTag;
        private TextView textViewDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.name_title);
            textViewTag = itemView.findViewById(R.id.tag_title);
            textViewDate = itemView.findViewById(R.id.date_title);
            itemView.setOnClickListener(v -> {
                clickListener.onItemClick(getAdapterPosition());
            });
        }

        public void onBind(Note note) {
            textViewName.setText(note.getTitle());
            textViewTag.setText(note.getTag());
            textViewDate.setText(note.getFormatDate());
        }
        private void registerContextMenu(View view) {
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
