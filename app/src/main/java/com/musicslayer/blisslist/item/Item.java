package com.musicslayer.blisslist.item;

import com.musicslayer.blisslist.data.bridge.DataBridge;
import com.musicslayer.blisslist.data.persistent.app.CategoryList;
import com.musicslayer.blisslist.util.HashMapUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Item implements DataBridge.SerializableToJSON {
    public ArrayList<String> itemNames = new ArrayList<>();
    public HashMap<String, Boolean> map_isFull = new HashMap<>();

    @Override
    public void serializeToJSON(DataBridge.Writer o) throws IOException {
        o.beginObject()
                .serialize("!V!", "1", String.class)
                .serializeArrayList("itemNames", itemNames, String.class)
                .serializeHashMap("map_isFull", map_isFull, String.class, Boolean.class)
                .endObject();
    }

    public static Item deserializeFromJSON(DataBridge.Reader o) throws IOException {
        o.beginObject();

        String version = o.deserialize("!V!", String.class);
        Item item = new Item();

        if("1".equals(version)) {
            ArrayList<String> itemNames = o.deserializeArrayList("itemNames", String.class);
            HashMap<String, Boolean> map_isFull = o.deserializeHashMap("map_isFull", String.class, Boolean.class);
            o.endObject();

            item.itemNames = itemNames;
            item.map_isFull = map_isFull;
        }
        else {
            throw new IllegalStateException("version = " + version);
        }

        return item;
    }

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

        new CategoryList().saveAllData();
    }

    public void updateItem(String itemName, boolean isFull) {
        // Assume the item name already exists.
        HashMapUtil.putValueInMap(map_isFull, itemName, isFull);

        new CategoryList().saveAllData();
    }

    public void removeItem(String itemName) {
        // Assume the item name already exists.
        itemNames.remove(itemName);
        HashMapUtil.removeValueFromMap(map_isFull, itemName);

        new CategoryList().saveAllData();
    }

    public void reset() {
        itemNames = new ArrayList<>();
        map_isFull = new HashMap<>();
    }

    public boolean isFull(String item) {
        return HashMapUtil.getValueFromMap(map_isFull, item);
    }
}
