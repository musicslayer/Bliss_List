package com.musicslayer.blisslist.data.persistent.app;

import android.content.SharedPreferences;

import com.musicslayer.blisslist.data.bridge.DataBridge;
import com.musicslayer.blisslist.item.Category;
import com.musicslayer.blisslist.item.Item;
import com.musicslayer.blisslist.util.HashMapUtil;
import com.musicslayer.blisslist.util.SharedPreferencesUtil;

import java.util.ArrayList;

public class CategoryList {
    public String getSharedPreferencesKey() {
        return "category_data";
    }

    public void saveAllData() {
        SharedPreferences sharedPreferences = SharedPreferencesUtil.getSharedPreferences(getSharedPreferencesKey());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();

        ArrayList<String> categoryNames = Category.categoryNames;

        int size = categoryNames.size();
        editor.putInt("category_size", size);

        for(int i = 0; i < size; i++) {
            String categoryName = categoryNames.get(i);
            editor.putString("category_name_" + i, categoryName);

            Item item = Category.getItem(categoryName);
            editor.putString("category_item_" + i, DataBridge.serialize(item, Item.class));
        }

        editor.apply();
    }

    public void loadAllData() {
        Category.reset();

        SharedPreferences sharedPreferences = SharedPreferencesUtil.getSharedPreferences(getSharedPreferencesKey());

        int size = sharedPreferences.getInt("category_size", 0);

        for(int i = 0; i < size; i++) {
            String categoryName = sharedPreferences.getString("category_name_" + i, "?");
            Category.addCategoryNoSave(categoryName);

            Item item = DataBridge.deserialize(sharedPreferences.getString("category_item_" + i, "?"), Item.class);

            HashMapUtil.putValueInMap(Category.map_items, categoryName, item);
        }
    }
}
