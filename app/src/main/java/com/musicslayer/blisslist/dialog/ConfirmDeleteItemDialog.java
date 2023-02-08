package com.musicslayer.blisslist.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.musicslayer.blisslist.R;

public class ConfirmDeleteItemDialog extends BaseDialog {
    final String name;

    public ConfirmDeleteItemDialog(Activity activity, String name) {
        super(activity);
        this.name = name;
    }

    public int getBaseViewID() {
        return R.id.confirm_delete_item_dialog;
    }

    public void createLayout(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_confirm_delete_item);

        AppCompatTextView T = findViewById(R.id.confirm_delete_item_dialog_textView);
        T.setText("Item Name: " + name);

        AppCompatButton B_CONFIRM = findViewById(R.id.confirm_delete_item_dialog_confirmButton);
        B_CONFIRM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isComplete = true;
                dismiss();
            }
        });

        AppCompatButton B_CANCEL = findViewById(R.id.confirm_delete_item_dialog_cancelButton);
        B_CANCEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
