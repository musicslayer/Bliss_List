package com.musicslayer.blisslist.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.google.android.flexbox.FlexboxLayout;
import com.musicslayer.blisslist.R;
import com.musicslayer.blisslist.dialog.AddItemDialog;
import com.musicslayer.blisslist.dialog.BaseDialogFragment;
import com.musicslayer.blisslist.item.Item;
import com.musicslayer.blisslist.util.HelpUtil;

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
                    String itemName = ((AddItemDialog)dialog).itemName;
                    Item.addItem(itemName, false);
                    updateLayout();
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

        updateLayout();
    }

    public void updateLayout() {
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

        for(String item : Item.items) {
            AppCompatButton B_ITEM = new AppCompatButton(this);
            B_ITEM.setText(item);
            B_ITEM.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isRemove) {
                        isRemove = false;
                        Item.removeItem(item);
                    }
                    else {
                        boolean isFull = Item.isFull(item);
                        Item.updateItem(item, !isFull);
                    }

                    updateLayout();
                }
            });

            if(Item.isFull(item)) {
                flexboxLayoutFull.addView(B_ITEM);
            }
            else {
                flexboxLayoutNeed.addView(B_ITEM);
            }
        }
    }
}