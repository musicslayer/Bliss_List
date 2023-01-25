package com.musicslayer.blisslist.util;

import android.content.Context;

import com.musicslayer.blisslist.dialog.BaseDialogFragment;
import com.musicslayer.blisslist.dialog.HelpDialog;

public class HelpUtil {
    public static void showHelp(Context context, int id) {
        String helpText = FileUtil.readFile(id);

        BaseDialogFragment helpDialogFragment = BaseDialogFragment.newInstance(HelpDialog.class, helpText);
        helpDialogFragment.show(context, "help");
    }
}
