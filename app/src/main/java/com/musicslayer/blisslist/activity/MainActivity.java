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
import com.musicslayer.blisslist.item.Item;
import com.musicslayer.blisslist.util.ToastUtil;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    public boolean isRemoveMode;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void createLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        BaseDialogFragment chooseCategoryDialogFragment = BaseDialogFragment.newInstance(ChooseCategoryDialog.class);
        chooseCategoryDialogFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(((ChooseCategoryDialog)dialog).isComplete) {
                    String currentCategoryName = ((ChooseCategoryDialog)dialog).user_CATEGORY;
                    Category.makeCurrentCategory(currentCategoryName);

                    updateLayout();
                }
            }
        });
        chooseCategoryDialogFragment.restoreListeners(this, "category");

        AppCompatImageButton categoryButton = findViewById(R.id.main_categoryButton);
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRemoveMode = false;

                chooseCategoryDialogFragment.show(MainActivity.this, "category");

                updateLayout();
            }
        });

        BaseDialogFragment addItemDialogFragment = BaseDialogFragment.newInstance(AddItemDialog.class);
        addItemDialogFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(((AddItemDialog)dialog).isComplete) {
                    String itemName = ((AddItemDialog)dialog).user_ITEMNAME;

                    if(Category.currentCategory.isItemSaved(itemName)) {
                        ToastUtil.showToast("item_name_used");
                    }
                    else {
                        Category.currentCategory.addItem(itemName, false);
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
                isRemoveMode = false;

                addItemDialogFragment.show(MainActivity.this, "add");

                updateLayout();
            }
        });

        AppCompatImageButton removeButton = findViewById(R.id.main_removeButton);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRemoveMode = !isRemoveMode;

                updateLayout();
            }
        });

        updateLayout();
    }

    public void updateLayout() {
        Toolbar toolbar = findViewById(R.id.main_toolbar);

        String subtitle = Category.currentCategory.categoryName;
        if(Category.currentCategory.equals(Category.favoriteCategory)) {
            subtitle = "♥ " + subtitle;
        }
        toolbar.setSubtitle(subtitle);

        TextView needText = findViewById(R.id.main_needText);
        TextView haveText = findViewById(R.id.main_haveText);

        needText.setText("Need (" + Category.currentCategory.numNeed() + ")");
        haveText.setText("Have (" + Category.currentCategory.numHave() + ")");

        AppCompatImageButton removeButton = findViewById(R.id.main_removeButton);
        if(isRemoveMode) {
            removeButton.setColorFilter(Color.RED);
        }
        else {
            removeButton.clearColorFilter();
        }

        FlexboxLayout flexboxLayoutNeed = findViewById(R.id.main_needFlexboxLayout);
        FlexboxLayout flexboxLayoutHave = findViewById(R.id.main_haveFlexboxLayout);

        flexboxLayoutNeed.removeAllViews();
        flexboxLayoutHave.removeAllViews();

        ArrayList<Item> items = new ArrayList<>(Category.currentCategory.map_items.values());
        for(Item item : items) {
            AppCompatButton B_ITEM = new AppCompatButton(this);
            B_ITEM.setText(item.itemName);
            B_ITEM.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isRemoveMode) {
                        isRemoveMode = false;
                        Category.currentCategory.removeItem(item.itemName);
                    }
                    else {
                        Category.currentCategory.toggleItem(item.itemName);
                    }

                    updateLayout();
                }
            });

            if(item.isHave) {
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
        bundle.putBoolean("isRemoveMode", isRemoveMode);
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {
        if(bundle != null) {
            isRemoveMode = bundle.getBoolean("isRemoveMode");
            updateLayout();
        }
        super.onRestoreInstanceState(bundle);
    }
}