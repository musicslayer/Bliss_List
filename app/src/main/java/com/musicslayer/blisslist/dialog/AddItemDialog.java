package com.musicslayer.blisslist.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;

import com.musicslayer.blisslist.R;
import com.musicslayer.blisslist.item.Item;

public class AddItemDialog extends BaseDialog {
    public String itemName;

    public AddItemDialog(Activity activity) {
        super(activity);
    }

    public int getBaseViewID() {
        return R.id.add_item_dialog;
    }

    public void createLayout(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_add_item);

        final AppCompatEditText E = findViewById(R.id.add_item_dialog_editText);

        Button B_CREATE = findViewById(R.id.add_item_dialog_createButton);
        B_CREATE.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(E.getText().toString().isEmpty()) {
                    Toast.makeText(activity, "Item name cannot be empty.", Toast.LENGTH_SHORT).show();
                }
                else {
                    itemName = E.getText().toString();

                    if(Item.items.contains(itemName)) {
                        Toast.makeText(activity, "Item name already exists.", Toast.LENGTH_SHORT).show();
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
