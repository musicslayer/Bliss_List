package com.musicslayer.blisslist.item;

import com.musicslayer.blisslist.data.bridge.DataBridge;
import com.musicslayer.blisslist.data.persistent.app.CategoryList;
import com.musicslayer.blisslist.util.HashMapUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Category implements DataBridge.SerializableToJSON {
    public static HashMap<String, Category> map_categories = new HashMap<>();
    public static Category currentCategory;
    public static Category favoriteCategory;

    public String categoryName;
    public HashMap<String, Item> map_items = new HashMap<>();

    @Override
    public void serializeToJSON(DataBridge.Writer o) throws IOException {
        o.beginObject()
                .serialize("!V!", "1", String.class)
                .serialize("categoryName", categoryName, String.class)
                .serializeHashMap("map_items", map_items, String.class, Item.class)
                .endObject();
    }

    public static Category deserializeFromJSON(DataBridge.Reader o) throws IOException {
        o.beginObject();

        String version = o.deserialize("!V!", String.class);
        Category category = new Category();

        if("1".equals(version)) {
            String categoryName = o.deserialize("categoryName", String.class);
            HashMap<String, Item> map_items = o.deserializeHashMap("map_items", String.class, Item.class);
            o.endObject();

            category.categoryName = categoryName;
            category.map_items = map_items;
        }
        else {
            throw new IllegalStateException("version = " + version);
        }

        return category;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof Category) && categoryName.equals(((Category) other).categoryName);
    }

    public static void createDefaultIfNeeded() {
        if(map_categories.isEmpty()) {
            addCategoryNoSave("Default");
            makeFavoriteCategory("Default");
        }
    }

    public int numNeed() {
        return Item.numNeed(new ArrayList<>(map_items.values()));
    }

    public int numHave() {
        return Item.numHave(new ArrayList<>(map_items.values()));
    }

    public static boolean isCategorySaved(String categoryName) {
        return getCategory(categoryName) != null;
    }

    public static Category getCategory(String categoryName) {
        return HashMapUtil.getValueFromMap(map_categories, categoryName);
    }

    public static int numCategories() {
        return map_categories.size();
    }

    public static void addCategoryNoSave(String categoryName) {
        Category category = new Category();
        category.categoryName = categoryName;
        HashMapUtil.putValueInMap(map_categories, categoryName, category);
    }

    public static void addCategory(String categoryName) {
        addCategoryNoSave(categoryName);

        new CategoryList().saveAllData();
    }

    public static void removeCategory(String categoryName) {
        HashMapUtil.removeValueFromMap(map_categories, categoryName);

        new CategoryList().saveAllData();
    }

    public static void renameCategory(String oldCategoryName, String newCategoryName) {
        Category category = getCategory(oldCategoryName);
        category.categoryName = newCategoryName;

        HashMapUtil.removeValueFromMap(map_categories, oldCategoryName);
        HashMapUtil.putValueInMap(map_categories, newCategoryName, category);

        new CategoryList().saveAllData();
    }

    public static void makeCurrentCategory(String categoryName) {
        currentCategory = getCategory(categoryName);

        // We do not save which category is current.
    }

    public static void makeFavoriteCategory(String categoryName) {
        favoriteCategory = getCategory(categoryName);

        new CategoryList().saveAllData();
    }

    public boolean isItemSaved(String itemName) {
        return getItem(itemName) != null;
    }

    public Item getItem(String itemName) {
        return HashMapUtil.getValueFromMap(map_items, itemName);
    }

    public void addItemNoSave(String itemName, boolean isHave) {
        Item item = new Item();
        item.itemName = itemName;
        item.isHave = isHave;
        HashMapUtil.putValueInMap(map_items, itemName, item);
    }

    public void addItem(String itemName, boolean isHave) {
        addItemNoSave(itemName, isHave);

        new CategoryList().saveAllData();
    }

    public void removeItem(String itemName) {
        HashMapUtil.removeValueFromMap(map_items, itemName);

        new CategoryList().saveAllData();
    }

    public void toggleItem(String itemName) {
        Item item = getItem(itemName);
        item.isHave = !item.isHave;

        new CategoryList().saveAllData();
    }
}
