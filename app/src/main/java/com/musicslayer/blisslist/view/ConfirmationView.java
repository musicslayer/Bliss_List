package com.musicslayer.blisslist.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatButton;

import com.musicslayer.blisslist.R;

public class ConfirmationView extends LinearLayout {
    private ConfirmationView.ConfirmationListener confirmationListener;

    public ConfirmationView(Context context) {
        this(context, null);
    }

    public ConfirmationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        makeLayout();
    }

    public void makeLayout() {
        this.makeLayoutDialog();
    }

    public void makeLayoutDialog() {
        Context context = getContext();

        AppCompatButton B = new AppCompatButton(context);
        B.setText("Confirm");
        B.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_24, 0, 0, 0);
        B.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(confirmationListener != null) {
                    confirmationListener.onConfirmation(ConfirmationView.this);
                }
            }
        });

        this.addView(B);
    }

    public void setOnConfirmationListener(ConfirmationView.ConfirmationListener confirmationListener) {
        this.confirmationListener = confirmationListener;
    }

    abstract public static class ConfirmationListener {
        abstract public void onConfirmation(ConfirmationView confirmationView);
    }
}
