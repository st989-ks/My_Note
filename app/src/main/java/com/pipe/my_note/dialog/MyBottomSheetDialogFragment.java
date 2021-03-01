package com.pipe.my_note.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pipe.my_note.R;

public class MyBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private OnDialogListener dialogListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_custom, container, false);
        setCancelable(false);

        view.findViewById(R.id.btnOk).setOnClickListener(view1 -> {
            if (dialogListener != null) dialogListener.onDialogOk();
            dismiss();
        });

        view.findViewById(R.id.btnCancel).setOnClickListener(view1 -> {
            dismiss();
        });

        return view;
    }

    // Установим слушатель диалога
    public void setOnDialogListener(OnDialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

}
