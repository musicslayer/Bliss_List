package com.musicslayer.blisslist.util;

import java.io.Closeable;
import java.io.Flushable;

public class StreamUtil {
    public static void safeFlushAndClose(Object obj) {
        try {
            if(obj != null) {
                if(obj instanceof Flushable) {
                    ((Flushable)obj).flush();
                }

                if(obj instanceof Closeable) {
                    ((Closeable)obj).close();
                }
            }
        }
        catch(Exception ignored) {
        }
    }
}