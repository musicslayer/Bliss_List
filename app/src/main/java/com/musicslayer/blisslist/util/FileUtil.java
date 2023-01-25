package com.musicslayer.blisslist.util;

import com.musicslayer.blisslist.app.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtil {
    public static String readFile(int id) {
        return readFile(new BufferedReader(new InputStreamReader(App.applicationContext.getResources().openRawResource(id))));
    }

    public static String readFile(BufferedReader file) {
        StringBuilder stringBuilder = new StringBuilder();

        try
        {
            String string;
            while((string = file.readLine()) != null) {
                stringBuilder.append(string).append("\n");
            }

            StreamUtil.safeFlushAndClose(file);
        }
        catch(IOException e) {
            ThrowableUtil.processThrowable(e);
            StreamUtil.safeFlushAndClose(file);
            throw new IllegalStateException(e);
        }

        return stringBuilder.toString();
    }
}
