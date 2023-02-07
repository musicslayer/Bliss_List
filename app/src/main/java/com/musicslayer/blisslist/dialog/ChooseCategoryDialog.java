package com.musicslayer.blisslist.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import com.musicslayer.blisslist.R;
import com.musicslayer.blisslist.item.Category;
import com.musicslayer.blisslist.util.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ChooseCategoryDialog extends BaseDialog {
    String currentDeleteCategoryName;
    String currentRenameCategoryName;
    boolean isEdit;
    boolean isRemove;

    public String user_CATEGORY;

    public ChooseCategoryDialog(Activity activity) {
        super(activity);
    }

    public int getBaseViewID() {
        return R.id.choose_category_dialog;
    }

    public void createLayout(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_choose_category);

        BaseDialogFragment createCategoryDialogFragment = BaseDialogFragment.newInstance(CreateCategoryDialog.class);
        createCategoryDialogFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(((CreateCategoryDialog)dialog).isComplete) {
                    String name = ((CreateCategoryDialog)dialog).user_NAME;

                    if(Category.isSaved(name)) {
                        ToastUtil.showToast("category_name_used");
                    }
                    else {
                        Category.addCategory(name);
                        updateLayout();
                    }
                }
            }
        });
        createCategoryDialogFragment.restoreListeners(activity, "create");

        AppCompatImageButton B_ADD = findViewById(R.id.choose_category_dialog_addButton);
        B_ADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdit = false;
                isRemove = false;

                updateLayout();

                createCategoryDialogFragment.show(activity, "create");
            }
        });

        AppCompatImageButton B_REMOVE = findViewById(R.id.choose_category_dialog_removeButton);
        B_REMOVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdit = false;
                isRemove = !isRemove;

                updateLayout();
            }
        });

        AppCompatImageButton B_EDIT = findViewById(R.id.choose_category_dialog_editButton);
        B_EDIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdit = !isEdit;
                isRemove = false;

                updateLayout();
            }
        });

        updateLayout();
    }

    public void updateLayout() {
        AppCompatImageButton B_REMOVE = findViewById(R.id.choose_category_dialog_removeButton);
        if(isRemove) {
            B_REMOVE.setColorFilter(Color.RED);
        }
        else {
            B_REMOVE.clearColorFilter();
        }

        AppCompatImageButton B_EDIT = findViewById(R.id.choose_category_dialog_editButton);
        if(isEdit) {
            B_EDIT.setColorFilter(Color.RED);
        }
        else {
            B_EDIT.clearColorFilter();
        }

        TableLayout table = findViewById(R.id.choose_category_dialog_tableLayout);
        table.removeAllViews();

        BaseDialogFragment confirmDeleteCategoryDialogFragment = BaseDialogFragment.newInstance(ConfirmDeleteCategoryDialog.class, "");
        confirmDeleteCategoryDialogFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(((ConfirmDeleteCategoryDialog)dialog).isComplete) {
                    Category.removeCategory(currentDeleteCategoryName);
                    updateLayout();
                }
            }
        });
        confirmDeleteCategoryDialogFragment.restoreListeners(activity, "delete");

        BaseDialogFragment renameCategoryDialogFragment = BaseDialogFragment.newInstance(RenameCategoryDialog.class, "");
        renameCategoryDialogFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(((RenameCategoryDialog)dialog).isComplete) {
                    String newName = ((RenameCategoryDialog)dialog).user_NEWNAME;

                    if(newName.equals(currentRenameCategoryName)) {
                        ToastUtil.showToast("category_name_cannot_be_same");
                    }
                    else if(Category.isSaved(newName)) {
                        ToastUtil.showToast("category_name_used");
                    }
                    else {
                        Category.renameCategory(currentRenameCategoryName, newName);

                        if(currentRenameCategoryName.equals(Category.currentCategoryName)) {
                            Category.currentCategoryName = newName;
                        }

                        if(currentRenameCategoryName.equals(Category.favoriteCategoryName)) {
                            Category.favoriteCategory(newName);
                        }

                        updateLayout();
                        activity.updateLayout();
                    }
                }
            }
        });
        renameCategoryDialogFragment.restoreListeners(activity, "rename");

        ArrayList<String> categoryNames = new ArrayList<>(Category.categoryNames);
        Collections.sort(categoryNames, Comparator.comparing(String::toLowerCase));

        for(String categoryName : categoryNames) {
            AppCompatImageButton B_FAVORITE = new AppCompatImageButton(activity);
            if(categoryName.equals(Category.favoriteCategoryName)) {
                B_FAVORITE.setImageResource(R.drawable.baseline_favorite_24);
            }
            else {
                B_FAVORITE.setImageResource(R.drawable.baseline_favorite_border_24);
            }
            B_FAVORITE.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!categoryName.equals(Category.favoriteCategoryName)) {
                        isEdit = false;
                        isRemove = false;

                        Category.favoriteCategory(categoryName);

                        updateLayout();
                        activity.updateLayout();
                    }
                }
            });

            AppCompatButton B = new AppCompatButton(activity);
            B.setText(categoryName);
            B.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            B.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_category_24, 0, 0, 0);
            B.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isEdit) {
                        currentRenameCategoryName = categoryName;
                        renameCategoryDialogFragment.updateArguments(RenameCategoryDialog.class, categoryName);
                        renameCategoryDialogFragment.show(activity, "rename");
                    }
                    else if(isRemove) {
                        if(Category.numCategories() == 1) {
                            ToastUtil.showToast("only_category_cannot_be_deleted");
                        }
                        else if(categoryName.equals(Category.currentCategoryName)) {
                            ToastUtil.showToast("current_category_cannot_be_deleted");
                        }
                        else if(categoryName.equals(Category.favoriteCategoryName)) {
                            ToastUtil.showToast("favorite_category_cannot_be_deleted");
                        }
                        else {
                            currentDeleteCategoryName = categoryName;
                            confirmDeleteCategoryDialogFragment.updateArguments(ConfirmDeleteCategoryDialog.class, categoryName);
                            confirmDeleteCategoryDialogFragment.show(activity, "delete");
                        }
                    }
                    else {
                        user_CATEGORY = categoryName;
                        isComplete = true;
                        dismiss();
                    }

                    isEdit = false;
                    isRemove = false;

                    updateLayout();
                }
            });

            TableRow.LayoutParams TRP = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);

            TableRow TR = new TableRow(activity);
            TR.addView(B_FAVORITE);
            TR.addView(B, TRP);
            table.addView(TR);
        }
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle bundle = super.onSaveInstanceState();
        bundle.putString("currentDeleteCategoryName", currentDeleteCategoryName);
        bundle.putString("currentRenameCategoryName", currentRenameCategoryName);
        bundle.putBoolean("isEdit", isEdit);
        bundle.putBoolean("isRemove", isRemove);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {
        if(bundle != null) {
            currentDeleteCategoryName = bundle.getString("currentDeleteCategoryName");
            currentRenameCategoryName = bundle.getString("currentRenameCategoryName");
            isEdit = bundle.getBoolean("isEdit");
            isRemove = bundle.getBoolean("isRemove");
        }
        super.onRestoreInstanceState(bundle);
    }
}
