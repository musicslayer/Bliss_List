package com.musicslayer.blisslist.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.google.android.flexbox.FlexboxLayout;
import com.musicslayer.blisslist.R;
import com.musicslayer.blisslist.dialog.AddItemDialog;
import com.musicslayer.blisslist.dialog.BaseDialogFragment;
import com.musicslayer.blisslist.dialog.ChooseCategoryDialog;
import com.musicslayer.blisslist.item.Category;
import com.musicslayer.blisslist.util.HelpUtil;
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

        ImageButton helpButton = findViewById(R.id.main_helpButton);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelpUtil.showHelp(MainActivity.this, R.raw.help_main);
            }
        });

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

        ImageButton addButton = findViewById(R.id.main_addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemDialogFragment.show(MainActivity.this, "add");
            }
        });

        ImageButton removeButton = findViewById(R.id.main_removeButton);
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

        ImageButton categoryButton = findViewById(R.id.main_categoryButton);
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseCategoryDialogFragment.show(MainActivity.this, "category");
            }
        });

        updateLayout();
    }

    public void updateLayout() {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.setSubtitle(Category.currentCategoryName);

        ImageButton removeButton = findViewById(R.id.main_removeButton);
        if(isRemove) {
            removeButton.setColorFilter(Color.RED);
        }
        else {
            removeButton.clearColorFilter();
        }

        FlexboxLayout flexboxLayoutNeed = findViewById(R.id.main_needFlexboxLayout);
        FlexboxLayout flexboxLayoutFull = findViewById(R.id.main_fullFlexboxLayout);

        flexboxLayoutNeed.removeAllViews();
        flexboxLayoutFull.removeAllViews();

        if(Category.getItem(Category.currentCategoryName) != null) {
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
                            boolean isFull = Category.getItem(Category.currentCategoryName).isFull(itemName);
                            Category.getItem(Category.currentCategoryName).updateItem(itemName, !isFull);
                        }

                        updateLayout();
                    }
                });

                if(Category.getItem(Category.currentCategoryName).isFull(itemName)) {
                    flexboxLayoutFull.addView(B_ITEM);
                }
                else {
                    flexboxLayoutNeed.addView(B_ITEM);
                }
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