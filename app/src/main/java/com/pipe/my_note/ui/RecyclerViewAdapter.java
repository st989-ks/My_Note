package com.pipe.my_note.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.pipe.my_note.MainActivity;
import com.pipe.my_note.R;
import com.pipe.my_note.data.NoteData;
import com.pipe.my_note.data.NoteSource;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final Fragment fragment;
    private final NoteSource dataSource;
    private OnItemClickListener clickListener;
    private int menuPosition;


    public RecyclerViewAdapter(NoteSource dataSource, Fragment fragment) {
        this.dataSource = dataSource;
        this.fragment = fragment;
        Log.i(MainActivity.TAG, this.getClass().getSimpleName() + " -RecyclerViewAdapter");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.form_of_records,
                parent, false);
        Log.i(MainActivity.TAG, this.getClass().getSimpleName() + " -onCreateViewHolder");
        return new ViewHolder(v);
    }

    public int getMenuPosition() {
        return menuPosition;
    }


    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
        Log.i(MainActivity.TAG, this.getClass().getSimpleName() + " -setOnItemClickListener");
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(dataSource.getNoteData(position));
        Log.i(MainActivity.TAG, this.getClass().getSimpleName() + " -onBindViewHolder");
    }

    @Override
    public int getItemCount() {
        Log.i(MainActivity.TAG, this.getClass().getSimpleName() + " -getItemCount");
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
            Log.i(MainActivity.TAG, this.getClass().getSimpleName() + " -ViewHolder");
        }

        public void onBind(NoteData noteData) {
            getTextViewName().setText(noteData.getTitle());
            getTextViewTag().setText(noteData.getTag());
            getTextViewDate().setText(noteData.getFormatDate());
            getCheckBox().setChecked(noteData.getLike());
            Log.i(MainActivity.TAG, this.getClass().getSimpleName() + " -onBind");
        }

        private void registerContextMenu(@NonNull View view) {
            if (fragment != null) {
                view.setOnLongClickListener(v -> {
                    menuPosition = getLayoutPosition();
                    return false;
                });
                fragment.registerForContextMenu(view);
            }
            Log.i(MainActivity.TAG, this.getClass().getSimpleName() + " -registerContextMenu");
        }
        public TextView getTextViewName() {
            return textViewName;
        }

        public TextView getTextViewTag() {
            return textViewTag;
        }

        public TextView getTextViewDate() {
            return textViewDate;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }
    }
}
