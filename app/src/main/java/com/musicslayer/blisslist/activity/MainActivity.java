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
import com.musicslayer.blisslist.dialog.ConfirmDeleteItemDialog;
import com.musicslayer.blisslist.dialog.RenameItemDialog;
import com.musicslayer.blisslist.item.Category;
import com.musicslayer.blisslist.item.Item;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    String currentDeleteItemName;
    String currentRenameItemName;
    public boolean isEditMode;
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

        BaseDialogFragment addItemDialogFragment = BaseDialogFragment.newInstance(AddItemDialog.class);
        addItemDialogFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(((AddItemDialog)dialog).isComplete) {
                    String itemName = ((AddItemDialog)dialog).user_ITEMNAME;
                    Category.currentCategory.addItem(itemName, false);

                    updateLayout();
                }
            }
        });
        addItemDialogFragment.restoreListeners(this, "add");

        AppCompatImageButton addButton = findViewById(R.id.main_addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEditMode = false;
                isRemoveMode = false;

                addItemDialogFragment.show(MainActivity.this, "add");

                updateLayout();
            }
        });

        AppCompatImageButton removeButton = findViewById(R.id.main_removeButton);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEditMode = false;
                isRemoveMode = !isRemoveMode;

                updateLayout();
            }
        });

        AppCompatImageButton editButton = findViewById(R.id.main_editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEditMode = !isEditMode;
                isRemoveMode = false;

                updateLayout();
            }
        });

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
                isEditMode = false;
                isRemoveMode = false;

                chooseCategoryDialogFragment.show(MainActivity.this, "category");

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

        AppCompatImageButton editButton = findViewById(R.id.main_editButton);
        if(isEditMode) {
            editButton.setColorFilter(Color.RED);
        }
        else {
            editButton.clearColorFilter();
        }

        AppCompatImageButton removeButton = findViewById(R.id.main_removeButton);
        if(isRemoveMode) {
            removeButton.setColorFilter(Color.RED);
        }
        else {
            removeButton.clearColorFilter();
        }

        BaseDialogFragment confirmDeleteItemDialogFragment = BaseDialogFragment.newInstance(ConfirmDeleteItemDialog.class, "");
        confirmDeleteItemDialogFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(((ConfirmDeleteItemDialog)dialog).isComplete) {
                    Category.currentCategory.removeItem(currentDeleteItemName);
                    updateLayout();
                }
            }
        });
        confirmDeleteItemDialogFragment.restoreListeners(this, "delete");

        BaseDialogFragment renameItemDialogFragment = BaseDialogFragment.newInstance(RenameItemDialog.class, "");
        renameItemDialogFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(((RenameItemDialog)dialog).isComplete) {
                    String newName = ((RenameItemDialog)dialog).user_NEWNAME;
                    Category.currentCategory.renameItem(currentRenameItemName, newName);

                    updateLayout();
                }
            }
        });
        renameItemDialogFragment.restoreListeners(this, "rename");

        FlexboxLayout flexboxLayoutNeed = findViewById(R.id.main_needFlexboxLayout);
        FlexboxLayout flexboxLayoutHave = findViewById(R.id.main_haveFlexboxLayout);

        flexboxLayoutNeed.removeAllViews();
        flexboxLayoutHave.removeAllViews();

        ArrayList<Item> items = new ArrayList<>(Category.currentCategory.map_items.values());
        for(Item item : items) {
            AppCompatButton B_ITEM = new AppCompatButton(this);
            B_ITEM.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_forward_24, 0, 0, 0);
            B_ITEM.setText(item.itemName);
            B_ITEM.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isEditMode) {
                        currentRenameItemName = item.itemName;
                        renameItemDialogFragment.updateArguments(RenameItemDialog.class, item.itemName);
                        renameItemDialogFragment.show(MainActivity.this, "rename");
                    }
                    else if(isRemoveMode) {
                        currentDeleteItemName = item.itemName;
                        confirmDeleteItemDialogFragment.updateArguments(ConfirmDeleteItemDialog.class, item.itemName);
                        confirmDeleteItemDialogFragment.show(MainActivity.this, "delete");
                    }
                    else {
                        Category.currentCategory.toggleItem(item.itemName);
                    }

                    isEditMode = false;
                    isRemoveMode = false;

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
        bundle.putString("currentDeleteCategoryName", currentDeleteItemName);
        bundle.putString("currentRenameCategoryName", currentRenameItemName);
        bundle.putBoolean("isEditMode", isEditMode);
        bundle.putBoolean("isRemoveMode", isRemoveMode);
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {
        if(bundle != null) {
            currentDeleteItemName = bundle.getString("currentDeleteCategoryName");
            currentRenameItemName = bundle.getString("currentRenameCategoryName");
            isEditMode = bundle.getBoolean("isEditMode");
            isRemoveMode = bundle.getBoolean("isRemoveMode");
            updateLayout();
        }
        super.onRestoreInstanceState(bundle);
    }
}