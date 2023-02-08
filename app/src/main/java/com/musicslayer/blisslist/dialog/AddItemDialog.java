package com.musicslayer.blisslist.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import com.musicslayer.blisslist.R;
import com.musicslayer.blisslist.item.Category;
import com.musicslayer.blisslist.util.ToastUtil;
import com.musicslayer.blisslist.view.red.PlainTextEditText;

public class AddItemDialog extends BaseDialog {
    public String user_ITEMNAME;

    public AddItemDialog(Activity activity) {
        super(activity);
    }

    public int getBaseViewID() {
        return R.id.add_item_dialog;
    }

    public void createLayout(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_add_item);

        final PlainTextEditText E = findViewById(R.id.add_item_dialog_editText);

        AppCompatButton B_CREATE = findViewById(R.id.add_item_dialog_createButton);
        B_CREATE.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean isValid = E.test();

                if(!isValid) {
                    ToastUtil.showToast("must_fill_inputs");
                }
                else {
                    user_ITEMNAME = E.getTextString();

                    if(Category.currentCategory.isItemSaved(user_ITEMNAME)) {
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
