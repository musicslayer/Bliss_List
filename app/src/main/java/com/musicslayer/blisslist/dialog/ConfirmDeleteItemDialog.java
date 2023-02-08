package com.musicslayer.blisslist.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

        TextView T = findViewById(R.id.confirm_delete_item_dialog_textView);
        T.setText("Item Name: " + name);

        Button B_CONFIRM = findViewById(R.id.confirm_delete_item_dialog_confirmButton);
        B_CONFIRM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isComplete = true;
                dismiss();
            }
        });

        Button B_CANCEL = findViewById(R.id.confirm_delete_item_dialog_cancelButton);
        B_CANCEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
