package com.musicslayer.blisslist.item;

import com.musicslayer.blisslist.data.persistent.app.CategoryList;
import com.musicslayer.blisslist.util.HashMapUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class Category {
    public static ArrayList<String> categoryNames = new ArrayList<>();
    public static HashMap<String, Item> map_items = new HashMap<>();

    public static Item getItem(String categoryName) {
        return HashMapUtil.getValueFromMap(map_items, categoryName);
    }

    public static boolean isSaved(String categoryName) {
        return categoryNames.contains(categoryName);
    }

    public static void addCategoryNoSave(String categoryName) {
        // Assume the category name doesn't already exist.
        categoryNames.add(categoryName);
        HashMapUtil.putValueInMap(map_items, categoryName, new Item());
    }

    public static void addCategory(String categoryName) {
        // Assume the category name doesn't already exist.
        categoryNames.add(categoryName);
        HashMapUtil.putValueInMap(map_items, categoryName, new Item());

        new CategoryList().saveAllData();
    }

    public static void removeCategory(String categoryName) {
        // Assume the category name already exists.
        categoryNames.remove(categoryName);
        HashMapUtil.removeValueFromMap(map_items, categoryName);

        new CategoryList().saveAllData();
    }

    public static void renameCategory(String oldCategoryName, String newCategoryName) {
        // Assume the category name already exists.
        Item item = getItem(oldCategoryName);

        categoryNames.remove(oldCategoryName);
        categoryNames.add(newCategoryName);

        HashMapUtil.removeValueFromMap(map_items, oldCategoryName);
        HashMapUtil.putValueInMap(map_items, newCategoryName, item);

        new CategoryList().saveAllData();
    }

    public static void reset() {
        categoryNames = new ArrayList<>();
        map_items = new HashMap<>();
    }
}
