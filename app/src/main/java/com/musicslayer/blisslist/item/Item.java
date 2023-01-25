package com.musicslayer.blisslist.item;

import com.musicslayer.blisslist.data.persistent.app.ItemList;
import com.musicslayer.blisslist.util.HashMapUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class Item {
    public static ArrayList<String> items = new ArrayList<>();
    private static HashMap<String, Boolean> map_isFull = new HashMap<>();

    public static void addItemNoSave(String item, boolean isFull) {
        // Assume the item doesn't already exist.
        items.add(item);
        HashMapUtil.putValueInMap(map_isFull, item, isFull);
    }

    public static void addItem(String item, boolean isFull) {
        // Assume the item doesn't already exist.
        items.add(item);
        HashMapUtil.putValueInMap(map_isFull, item, isFull);

        new ItemList().saveAllData();
    }

    public static void updateItem(String item, boolean isFull) {
        // Assume the item already exists.
        HashMapUtil.putValueInMap(map_isFull, item, isFull);

        new ItemList().saveAllData();
    }

    public static void removeItem(String item) {
        // Assume the item already exists.
        items.remove(item);
        HashMapUtil.removeValueFromMap(map_isFull, item);

        new ItemList().saveAllData();
    }

    public static void reset() {
        items = new ArrayList<>();
        map_isFull = new HashMap<>();
    }

    public static boolean isFull(String item) {
        return HashMapUtil.getValueFromMap(map_isFull, item);
    }
}
