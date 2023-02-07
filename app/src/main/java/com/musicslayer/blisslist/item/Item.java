package com.musicslayer.blisslist.item;

import com.musicslayer.blisslist.data.persistent.app.ItemList;
import com.musicslayer.blisslist.util.HashMapUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class Item {
    public ArrayList<String> itemNames = new ArrayList<>();
    private HashMap<String, Boolean> map_isFull = new HashMap<>();

    public boolean isSaved(String item) {
        return itemNames.contains(item);
    }

    public void addItemNoSave(String itemName, boolean isFull) {
        // Assume the item name doesn't already exist.
        itemNames.add(itemName);
        HashMapUtil.putValueInMap(map_isFull, itemName, isFull);
    }

    public void addItem(String itemName, boolean isFull) {
        // Assume the item name doesn't already exist.
        itemNames.add(itemName);
        HashMapUtil.putValueInMap(map_isFull, itemName, isFull);

        new ItemList().saveAllData();
    }

    public void updateItem(String itemName, boolean isFull) {
        // Assume the item name already exists.
        HashMapUtil.putValueInMap(map_isFull, itemName, isFull);

        new ItemList().saveAllData();
    }

    public void removeItem(String itemName) {
        // Assume the item name already exists.
        itemNames.remove(itemName);
        HashMapUtil.removeValueFromMap(map_isFull, itemName);

        new ItemList().saveAllData();
    }

    public void reset() {
        itemNames = new ArrayList<>();
        map_isFull = new HashMap<>();
    }

    public boolean isFull(String item) {
        return HashMapUtil.getValueFromMap(map_isFull, item);
    }
}
