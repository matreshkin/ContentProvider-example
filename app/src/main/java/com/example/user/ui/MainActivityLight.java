package com.example.user.ui;

import android.graphics.Color;
import android.os.Bundle;

public class MainActivityLight extends MainActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.BLACK);
    }
}
