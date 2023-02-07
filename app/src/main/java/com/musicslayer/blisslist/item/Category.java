package com.musicslayer.blisslist.item;

import com.musicslayer.blisslist.data.persistent.app.CategoryList;

import java.util.ArrayList;

public class Category {
    public static ArrayList<String> categories = new ArrayList<>();

    public static boolean isSaved(String category) {
        return categories.contains(category);
    }

    public static void addCategoryNoSave(String category) {
        // Assume the category doesn't already exist.
        categories.add(category);
    }

    public static void addCategory(String category) {
        // Assume the category doesn't already exist.
        categories.add(category);

        new CategoryList().saveAllData();
    }

    public static void removeCategory(String category) {
        // Assume the item already exists.
        categories.remove(category);

        new CategoryList().saveAllData();
    }

    public static void renameCategory(String oldCategory, String newCategory) {
        // Assume the item already exists.
        categories.remove(oldCategory);
        categories.add(newCategory);

        new CategoryList().saveAllData();
    }

    public static void reset() {
        categories = new ArrayList<>();
    }
}
