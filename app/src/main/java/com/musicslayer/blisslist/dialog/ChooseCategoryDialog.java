package com.musicslayer.blisslist.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.appcompat.widget.AppCompatButton;

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
                        //PersistentUserDataStore.getInstance(ChartPortfolio.class).addPortfolio(new ChartPortfolioObj(name));
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

        BaseDialogFragment confirmDeleteCategoryDialogFragment = BaseDialogFragment.newInstance(ConfirmDeleteCategoryDialog.class);
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
                        updateLayout();
                    }
                }
            }
        });
        renameCategoryDialogFragment.restoreListeners(activity, "rename");

        ArrayList<String> categoryNames = new ArrayList<>(Category.categories);
        Collections.sort(categoryNames, Comparator.comparing(String::toLowerCase));

        for(String categoryName : categoryNames) {
            AppCompatButton B = new AppCompatButton(activity);
            B.setText(categoryName);
            B.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_category_24, 0, 0, 0);
            B.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user_CATEGORY = categoryName;
                    isComplete = true;
                    dismiss();
                }
            });

            AppCompatButton B_DELETE = new AppCompatButton(activity);
            B_DELETE.setText("Delete");
            B_DELETE.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_delete_24, 0, 0, 0);
            B_DELETE.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentDeleteCategoryName = categoryName;
                    confirmDeleteCategoryDialogFragment.show(activity, "delete");
                }
            });

            AppCompatButton B_RENAME = new AppCompatButton(activity);
            B_RENAME.setText("Rename");
            B_RENAME.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24, 0, 0, 0);
            B_RENAME.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentRenameCategoryName = categoryName;
                    renameCategoryDialogFragment.updateArguments(RenameCategoryDialog.class, categoryName);
                    renameCategoryDialogFragment.show(activity, "rename");
                }
            });

            TableRow.LayoutParams TRP = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            TRP.setMargins(80,0,0,0);

            TableRow TR = new TableRow(activity);
            TR.addView(B);
            TR.addView(B_DELETE, TRP);
            TR.addView(B_RENAME);
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
