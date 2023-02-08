package com.musicslayer.blisslist.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.musicslayer.blisslist.R;
import com.musicslayer.blisslist.item.Category;
import com.musicslayer.blisslist.util.ToastUtil;
import com.musicslayer.blisslist.view.red.PlainTextEditText;

public class CreateCategoryDialog extends BaseDialog {
    public String user_NAME;

    public CreateCategoryDialog(Activity activity) {
        super(activity);
    }

    public int getBaseViewID() {
        return R.id.create_category_dialog;
    }

    public void createLayout(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_create_category);

        final PlainTextEditText E = findViewById(R.id.create_category_dialog_editText);

        Button B_CREATE = findViewById(R.id.create_category_dialog_createButton);
        B_CREATE.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean isValid = E.test();

                if(!isValid) {
                    ToastUtil.showToast("must_fill_inputs");
                }
                else {
                    user_NAME = E.getTextString();

                    if(Category.isCategorySaved(user_NAME)) {
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
