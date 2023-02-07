package com.musicslayer.blisslist.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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

        Button B_ADD = findViewById(R.id.choose_category_dialog_addButton);
        B_ADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCategoryDialogFragment.show(activity, "create");
            }
        });

        updateLayout();
    }

    public void updateLayout() {
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
            AppCompatImageButton B_DELETE = new AppCompatImageButton(activity);
            B_DELETE.setImageResource(R.drawable.baseline_delete_24);
            B_DELETE.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
            });

            AppCompatImageButton B_RENAME = new AppCompatImageButton(activity);
            B_RENAME.setImageResource(R.drawable.baseline_edit_24);
            B_RENAME.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentRenameCategoryName = categoryName;
                    renameCategoryDialogFragment.updateArguments(RenameCategoryDialog.class, categoryName);
                    renameCategoryDialogFragment.show(activity, "rename");
                }
            });

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
                        // Register new favorite.
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
                    user_CATEGORY = categoryName;
                    isComplete = true;
                    dismiss();
                }
            });

            TableRow.LayoutParams TRP = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
            TRP.setMargins(80,0,0,0);

            TableRow TR = new TableRow(activity);
            TR.addView(B_DELETE);
            TR.addView(B_RENAME);
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
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {
        if(bundle != null) {
            currentDeleteCategoryName = bundle.getString("currentDeleteCategoryName");
            currentRenameCategoryName = bundle.getString("currentRenameCategoryName");
        }
        super.onRestoreInstanceState(bundle);
    }
}
