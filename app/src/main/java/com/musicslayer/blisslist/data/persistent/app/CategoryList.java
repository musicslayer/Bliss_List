package com.musicslayer.blisslist.data.persistent.app;

import android.content.SharedPreferences;

import com.musicslayer.blisslist.data.bridge.DataBridge;
import com.musicslayer.blisslist.item.Category;
import com.musicslayer.blisslist.util.HashMapUtil;
import com.musicslayer.blisslist.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class CategoryList {
    public String getSharedPreferencesKey() {
        return "category_data";
    }

    public void saveAllData() {
        SharedPreferences sharedPreferences = SharedPreferencesUtil.getSharedPreferences(getSharedPreferencesKey());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();

        ArrayList<Category> categories = new ArrayList<>(Category.map_categories.values());

        int size = categories.size();
        editor.putInt("category_size", size);

        for(int i = 0; i < size; i++) {
            Category category = categories.get(i);
            editor.putString("category_" + i, DataBridge.serialize(category, Category.class));
        }

        editor.putString("category_favorite_name", Category.favoriteCategory.categoryName);

        editor.apply();
    }

    public void loadAllData() {
        Category.map_categories = new HashMap<>();

        SharedPreferences sharedPreferences = SharedPreferencesUtil.getSharedPreferences(getSharedPreferencesKey());

        int size = sharedPreferences.getInt("category_size", 0);

        for(int i = 0; i < size; i++) {
            Category category = DataBridge.deserialize(sharedPreferences.getString("category_" + i, "?"), Category.class);
            HashMapUtil.putValueInMap(Category.map_categories, category.categoryName, category);
        }

        String favoriteCategoryName = sharedPreferences.getString("category_favorite_name", "?");
        Category.favoriteCategory = HashMapUtil.getValueFromMap(Category.map_categories, favoriteCategoryName);
    }
}
