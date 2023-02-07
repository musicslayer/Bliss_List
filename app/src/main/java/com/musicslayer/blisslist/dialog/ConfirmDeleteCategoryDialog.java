package com.musicslayer.blisslist.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.musicslayer.blisslist.R;
import com.musicslayer.blisslist.view.ConfirmationView;

public class ConfirmDeleteCategoryDialog extends BaseDialog {
    public ConfirmDeleteCategoryDialog(Activity activity) {
        super(activity);
    }

    public int getBaseViewID() {
        return R.id.confirm_delete_category_dialog;
    }

    public void createLayout(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_confirm_delete_category);

        ConfirmationView C = findViewById(R.id.confirm_delete_category_dialog_confirmationView);
        C.setOnConfirmationListener(new ConfirmationView.ConfirmationListener() {
            @Override
            public void onConfirmation(ConfirmationView confirmationView) {
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
