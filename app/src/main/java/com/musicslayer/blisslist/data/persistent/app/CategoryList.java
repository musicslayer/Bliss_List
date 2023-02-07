package com.musicslayer.blisslist.data.persistent.app;

import android.content.SharedPreferences;

import com.musicslayer.blisslist.item.Category;
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

        ArrayList<String> categories = Category.categories;

        int size = categories.size();
        editor.putInt("category_size", size);

        for(int i = 0; i < size; i++) {
            String category = categories.get(i);
            editor.putString("category_name_" + i, category);
        }

        editor.apply();
    }

    public void loadAllData() {
        Category.reset();

        SharedPreferences sharedPreferences = SharedPreferencesUtil.getSharedPreferences(getSharedPreferencesKey());

        int size = sharedPreferences.getInt("category_size", 0);

        for(int i = 0; i < size; i++) {
            String category = sharedPreferences.getString("category_name_" + i, "?");
            Category.addCategoryNoSave(category);
        }
    }
}
