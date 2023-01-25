package com.musicslayer.blisslist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.musicslayer.blisslist.R;

public class MainActivity extends BaseActivity {
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void createLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        Button B_BUTTON = findViewById(R.id.main_button);
        B_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(MainActivity.this, TransactionExplorerActivity.class));
                //finish();

                Toast.makeText(MainActivity.this, "Bliss List Ready!", Toast.LENGTH_LONG).show();
            }
        });
    }
}