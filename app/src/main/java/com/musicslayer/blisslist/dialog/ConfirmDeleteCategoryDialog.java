package com.musicslayer.blisslist.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.musicslayer.blisslist.R;

public class ConfirmDeleteCategoryDialog extends BaseDialog {
    String name;

    public ConfirmDeleteCategoryDialog(Activity activity, String name) {
        super(activity);
        this.name = name;
    }

    public int getBaseViewID() {
        return R.id.confirm_delete_category_dialog;
    }

    public void createLayout(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_confirm_delete_category);

        TextView T = findViewById(R.id.confirm_delete_category_dialog_textView);
        T.setText("Category Name: " + name);

        Button B_CONFIRM = findViewById(R.id.confirm_delete_category_dialog_confirmButton);
        B_CONFIRM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isComplete = true;
                dismiss();
            }
        });

        Button B_CANCEL = findViewById(R.id.confirm_delete_category_dialog_cancelButton);
        B_CANCEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
