package com.musicslayer.blisslist.item;

import com.musicslayer.blisslist.data.bridge.DataBridge;

import java.io.IOException;
import java.util.ArrayList;

public class Item implements DataBridge.SerializableToJSON {
    public String itemName;
    public boolean isHave;

    @Override
    public void serializeToJSON(DataBridge.Writer o) throws IOException {
        o.beginObject()
                .serialize("!V!", "1", String.class)
                .serialize("itemName", itemName, String.class)
                .serialize("isHave", isHave, Boolean.class)
                .endObject();
    }

    public static Item deserializeFromJSON(DataBridge.Reader o) throws IOException {
        o.beginObject();

        String version = o.deserialize("!V!", String.class);
        Item item = new Item();

        if("1".equals(version)) {
            String itemName = o.deserialize("itemName", String.class);
            boolean isHave = o.deserialize("isHave", Boolean.class);
            o.endObject();

            item.itemName = itemName;
            item.isHave = isHave;
        }
        else {
            throw new IllegalStateException("version = " + version);
        }

        return item;
    }

    public static int numNeed(ArrayList<Item> items) {
        int count = 0;
        for(Item item : items) {
            if(!item.isHave) {
                count++;
            }
        }
        return count;
    }

    public static int numHave(ArrayList<Item> items) {
        int count = 0;
        for(Item item : items) {
            if(item.isHave) {
                count++;
            }
        }
        return count;
    }
}
