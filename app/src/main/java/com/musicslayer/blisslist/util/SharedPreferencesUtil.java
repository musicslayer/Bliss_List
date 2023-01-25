package com.musicslayer.blisslist.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.musicslayer.blisslist.app.App;

public class SharedPreferencesUtil {
    public static SharedPreferences getSharedPreferences(String name) {
        return App.applicationContext.getSharedPreferences(name, Context.MODE_PRIVATE);
    }
}
