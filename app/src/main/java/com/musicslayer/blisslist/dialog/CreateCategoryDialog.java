package com.musicslayer.blisslist.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.musicslayer.blisslist.R;
import com.musicslayer.blisslist.item.Category;
import com.musicslayer.blisslist.util.ToastUtil;
import com.musicslayer.blisslist.view.red.PlainTextEditText;

public class CreateCategoryDialog extends BaseDialog {
    int LAST_CHECK = 0;

    public String user_NAME;
    public String user_STYLE;

    public CreateCategoryDialog(Activity activity) {
        super(activity);
    }

    public int getBaseViewID() {
        return R.id.create_category_dialog;
    }

    public void createLayout(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_create_category);

        RadioGroup radioGroup = findViewById(R.id.create_category_dialog_radioGroup);
        AppCompatRadioButton[] rb = new AppCompatRadioButton[3];

        rb[0] = findViewById(R.id.create_category_dialog_hardcodedRadioButton);
        rb[0].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LAST_CHECK = 0;
            }
        });

        rb[1] = findViewById(R.id.create_category_dialog_foundRadioButton);
        rb[1].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LAST_CHECK = 1;
            }
        });

        radioGroup.check(rb[LAST_CHECK].getId());
        rb[LAST_CHECK].callOnClick();

        final PlainTextEditText E = findViewById(R.id.create_category_dialog_editText);

        AppCompatButton B_CREATE = findViewById(R.id.create_category_dialog_createButton);
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
                        if(LAST_CHECK == 0) {
                            user_STYLE = "list";
                        }
                        else if(LAST_CHECK == 1) {
                            user_STYLE = "todo";
                        }

                        isComplete = true;
                        dismiss();
                    }
                }
            }
        });
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle bundle = super.onSaveInstanceState();
        bundle.putInt("LAST_CHECK", LAST_CHECK);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {
        if(bundle != null) {
            LAST_CHECK = bundle.getInt("LAST_CHECK");
        }
        super.onRestoreInstanceState(bundle);
    }
}
