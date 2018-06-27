package com.de.k3ron3i.journalapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditMode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mode);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
