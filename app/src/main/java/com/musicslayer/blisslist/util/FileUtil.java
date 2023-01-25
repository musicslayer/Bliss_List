package com.musicslayer.blisslist.util;

import com.musicslayer.blisslist.app.App;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

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

    public static ArrayList<String> readFileIntoLines(BufferedReader file) {
        ArrayList<String> stringArrayList = new ArrayList<>();

        try
        {
            String string;
            while((string = file.readLine()) != null) {
                stringArrayList.add(string);
            }

            StreamUtil.safeFlushAndClose(file);
        }
        catch(IOException e) {
            ThrowableUtil.processThrowable(e);
            StreamUtil.safeFlushAndClose(file);
            throw new IllegalStateException(e);
        }

        return stringArrayList;
    }
}
