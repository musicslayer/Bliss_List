package com.musicslayer.blisslist.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import com.google.android.flexbox.FlexboxLayout;
import com.musicslayer.blisslist.R;
import com.musicslayer.blisslist.dialog.AddItemDialog;
import com.musicslayer.blisslist.dialog.BaseDialogFragment;
import com.musicslayer.blisslist.dialog.ChooseCategoryDialog;
import com.musicslayer.blisslist.item.Category;
import com.musicslayer.blisslist.util.ToastUtil;

public class MainActivity extends BaseActivity {
    public boolean isRemove;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void createLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        BaseDialogFragment addItemDialogFragment = BaseDialogFragment.newInstance(AddItemDialog.class);
        addItemDialogFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(((AddItemDialog)dialog).isComplete) {
                    String itemName = ((AddItemDialog)dialog).user_ITEMNAME;

                    if(Category.getItem(Category.currentCategoryName).isSaved(itemName)) {
                        ToastUtil.showToast("item_name_used");
                    }
                    else {
                        Category.getItem(Category.currentCategoryName).addItem(itemName, false);
                        updateLayout();
                    }
                }
            }
        });
        addItemDialogFragment.restoreListeners(this, "add");

        AppCompatImageButton addButton = findViewById(R.id.main_addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRemove = false;

                addItemDialogFragment.show(MainActivity.this, "add");

                updateLayout();
            }
        });

        AppCompatImageButton removeButton = findViewById(R.id.main_removeButton);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRemove = !isRemove;

                updateLayout();
            }
        });

        BaseDialogFragment chooseCategoryDialogFragment = BaseDialogFragment.newInstance(ChooseCategoryDialog.class);
        chooseCategoryDialogFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(((ChooseCategoryDialog)dialog).isComplete) {
                    Category.currentCategoryName = ((ChooseCategoryDialog)dialog).user_CATEGORY;
                    updateLayout();
                }
            }
        });
        chooseCategoryDialogFragment.restoreListeners(this, "category");

        AppCompatImageButton categoryButton = findViewById(R.id.main_categoryButton);
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRemove = false;

                chooseCategoryDialogFragment.show(MainActivity.this, "category");

                updateLayout();
            }
        });

        updateLayout();
    }

    public void updateLayout() {
        Toolbar toolbar = findViewById(R.id.main_toolbar);

        String subtitle = Category.currentCategoryName;
        if(Category.currentCategoryName.equals(Category.favoriteCategoryName)) {
            subtitle = "♥ " + subtitle;
        }
        toolbar.setSubtitle(subtitle);

        TextView needText = findViewById(R.id.main_needText);
        TextView haveText = findViewById(R.id.main_haveText);

        needText.setText("Need (" + Category.getItem(Category.currentCategoryName).numNeed() + ")");
        haveText.setText("Have (" + Category.getItem(Category.currentCategoryName).numHave() + ")");

        AppCompatImageButton removeButton = findViewById(R.id.main_removeButton);
        if(isRemove) {
            removeButton.setColorFilter(Color.RED);
        }
        else {
            removeButton.clearColorFilter();
        }

        FlexboxLayout flexboxLayoutNeed = findViewById(R.id.main_needFlexboxLayout);
        FlexboxLayout flexboxLayoutHave = findViewById(R.id.main_haveFlexboxLayout);

        flexboxLayoutNeed.removeAllViews();
        flexboxLayoutHave.removeAllViews();

        for(String itemName : Category.getItem(Category.currentCategoryName).itemNames) {
            AppCompatButton B_ITEM = new AppCompatButton(this);
            B_ITEM.setText(itemName);
            B_ITEM.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isRemove) {
                        isRemove = false;
                        Category.getItem(Category.currentCategoryName).removeItem(itemName);
                    }
                    else {
                        boolean isHave = Category.getItem(Category.currentCategoryName).isHave(itemName);
                        Category.getItem(Category.currentCategoryName).updateItem(itemName, !isHave);
                    }

                    updateLayout();
                }
            });

            if(Category.getItem(Category.currentCategoryName).isHave(itemName)) {
                flexboxLayoutHave.addView(B_ITEM);
            }
            else {
                flexboxLayoutNeed.addView(B_ITEM);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("isRemove", isRemove);
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {
        if(bundle != null) {
            isRemove = bundle.getBoolean("isRemove");
            updateLayout();
        }
        super.onRestoreInstanceState(bundle);
    }
}