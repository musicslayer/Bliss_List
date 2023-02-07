package com.musicslayer.blisslist.activity;

import android.content.Intent;
import android.os.Bundle;

import com.musicslayer.blisslist.app.App;
import com.musicslayer.blisslist.data.persistent.app.CategoryList;
import com.musicslayer.blisslist.util.ToastUtil;

// This Activity class only exists for initialization code, not to be seen by the user.
public class InitialActivity extends BaseActivity {
    @Override
    public void createLayout(Bundle savedInstanceState) {
        // Don't actually show anything. This activity exists because it is the only one allowed to perform initialization.
        startActivity(new Intent(this, com.musicslayer.blisslist.activity.MainActivity.class));
        finish();
    }

    public void initialize() {
        new CategoryList().loadAllData();

        ToastUtil.initialize();

        App.isAppInitialized = true;
    }
}