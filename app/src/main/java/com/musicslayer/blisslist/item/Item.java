package com.musicslayer.blisslist.item;

import com.musicslayer.blisslist.data.bridge.DataBridge;
import com.musicslayer.blisslist.data.persistent.app.CategoryList;
import com.musicslayer.blisslist.util.HashMapUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Item implements DataBridge.SerializableToJSON {
    public ArrayList<String> itemNames = new ArrayList<>();
    public HashMap<String, Boolean> map_isHave = new HashMap<>();

    @Override
    public void serializeToJSON(DataBridge.Writer o) throws IOException {
        o.beginObject()
                .serialize("!V!", "1", String.class)
                .serializeArrayList("itemNames", itemNames, String.class)
                .serializeHashMap("map_isHave", map_isHave, String.class, Boolean.class)
                .endObject();
    }

    public static Item deserializeFromJSON(DataBridge.Reader o) throws IOException {
        o.beginObject();

        String version = o.deserialize("!V!", String.class);
        Item item = new Item();

        if("1".equals(version)) {
            ArrayList<String> itemNames = o.deserializeArrayList("itemNames", String.class);
            HashMap<String, Boolean> map_isHave = o.deserializeHashMap("map_isHave", String.class, Boolean.class);
            o.endObject();

            item.itemNames = itemNames;
            item.map_isHave = map_isHave;
        }
        else {
            throw new IllegalStateException("version = " + version);
        }

        return item;
    }

    public int numNeed() {
        int count = 0;
        for(String itemName : itemNames) {
            if(!isHave(itemName)) {
                count++;
            }
        }
        return count;
    }

    public int numHave() {
        int count = 0;
        for(String itemName : itemNames) {
            if(isHave(itemName)) {
                count++;
            }
        }
        return count;
    }

    public boolean isSaved(String item) {
        return itemNames.contains(item);
    }

    public void addItemNoSave(String itemName, boolean isHave) {
        // Assume the item name doesn't already exist.
        itemNames.add(itemName);
        HashMapUtil.putValueInMap(map_isHave, itemName, isHave);
    }

    public void addItem(String itemName, boolean isHave) {
        // Assume the item name doesn't already exist.
        itemNames.add(itemName);
        HashMapUtil.putValueInMap(map_isHave, itemName, isHave);

        new CategoryList().saveAllData();
    }

    public void updateItem(String itemName, boolean isHave) {
        // Assume the item name already exists.
        HashMapUtil.putValueInMap(map_isHave, itemName, isHave);

        new CategoryList().saveAllData();
    }

    public void removeItem(String itemName) {
        // Assume the item name already exists.
        itemNames.remove(itemName);
        HashMapUtil.removeValueFromMap(map_isHave, itemName);

        new CategoryList().saveAllData();
    }

    public void reset() {
        itemNames = new ArrayList<>();
        map_isHave = new HashMap<>();
    }

    public boolean isHave(String item) {
        return HashMapUtil.getValueFromMap(map_isHave, item);
    }
}
