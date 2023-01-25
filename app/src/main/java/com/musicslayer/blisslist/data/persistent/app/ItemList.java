package com.musicslayer.blisslist.data.persistent.app;

import android.content.SharedPreferences;

import com.musicslayer.blisslist.item.Item;
import com.musicslayer.blisslist.util.SharedPreferencesUtil;

import java.util.ArrayList;

public class ItemList {
    public String getSharedPreferencesKey() {
        return "item_data";
    }

    public void saveAllData() {
        SharedPreferences sharedPreferences = SharedPreferencesUtil.getSharedPreferences(getSharedPreferencesKey());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();

        ArrayList<String> items = Item.items;

        int size = items.size();
        editor.putInt("item_size", size);

        for(int i = 0; i < size; i++) {
            String item = items.get(i);
            editor.putString("item_name_" + i, item);
            editor.putBoolean("item_full_" + i, Item.isFull(item));
        }

        editor.apply();
    }

    public void loadAllData() {
        Item.reset();

        SharedPreferences sharedPreferences = SharedPreferencesUtil.getSharedPreferences(getSharedPreferencesKey());

        int size = sharedPreferences.getInt("item_size", 0);

        for(int i = 0; i < size; i++) {
            String item = sharedPreferences.getString("item_name_" + i, "?");
            boolean isFull = sharedPreferences.getBoolean("item_full_" + i, false);
            Item.addItemNoSave(item, isFull);
        }
    }
}
