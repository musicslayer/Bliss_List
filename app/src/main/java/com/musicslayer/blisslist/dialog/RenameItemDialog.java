package com.musicslayer.blisslist.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.musicslayer.blisslist.R;
import com.musicslayer.blisslist.item.Category;
import com.musicslayer.blisslist.util.ToastUtil;
import com.musicslayer.blisslist.view.red.PlainTextEditText;

public class RenameItemDialog extends BaseDialog {
    final String oldName;

    public String user_NEWNAME;

    public RenameItemDialog(Activity activity, String oldName) {
        super(activity);
        this.oldName = oldName;
    }

    public int getBaseViewID() {
        return R.id.rename_item_dialog;
    }

    public void createLayout(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_rename_item);

        TextView T = findViewById(R.id.rename_item_dialog_currentTextView);
        T.setText("Current Item Name: " + oldName);

        final PlainTextEditText E = findViewById(R.id.rename_item_dialog_editText);

        if(savedInstanceState == null) {
            E.setTextString(oldName);
        }

        Button B_RENAME = findViewById(R.id.rename_item_dialog_renameButton);
        B_RENAME.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean isValid = E.test();

                if(!isValid) {
                    ToastUtil.showToast("must_fill_inputs");
                }
                else {
                    user_NEWNAME = E.getTextString();

                    if(oldName.equals(user_NEWNAME)) {
                        ToastUtil.showToast("item_name_cannot_be_same");
                    }
                    else if(Category.currentCategory.isItemSaved(user_NEWNAME)) {
                        ToastUtil.showToast("item_name_used");
                    }
                    else {
                        isComplete = true;
                        dismiss();
                    }
                }
            }
        });
    }
}
