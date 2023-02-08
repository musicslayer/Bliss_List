package com.musicslayer.blisslist.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.musicslayer.blisslist.R;
import com.musicslayer.blisslist.item.Category;
import com.musicslayer.blisslist.util.ToastUtil;
import com.musicslayer.blisslist.view.red.PlainTextEditText;

public class RenameCategoryDialog extends BaseDialog {
    final String oldName;

    public String user_NEWNAME;

    public RenameCategoryDialog(Activity activity, String oldName) {
        super(activity);
        this.oldName = oldName;
    }

    public int getBaseViewID() {
        return R.id.rename_category_dialog;
    }

    public void createLayout(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_rename_category);

        AppCompatTextView T = findViewById(R.id.rename_category_dialog_currentTextView);
        T.setText("Current Category Name: " + oldName);

        final PlainTextEditText E = findViewById(R.id.rename_category_dialog_editText);

        if(savedInstanceState == null) {
            E.setTextString(oldName);
        }

        AppCompatButton B_RENAME = findViewById(R.id.rename_category_dialog_renameButton);
        B_RENAME.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean isValid = E.test();

                if(!isValid) {
                    ToastUtil.showToast("must_fill_inputs");
                }
                else {
                    user_NEWNAME = E.getTextString();

                    if(oldName.equals(user_NEWNAME)) {
                        ToastUtil.showToast("category_name_cannot_be_same");
                    }
                    else if(Category.isCategorySaved(user_NEWNAME)) {
                        ToastUtil.showToast("category_name_used");
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
