package com.example.simplygreensfuapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class Activity_BlueBin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_bin);
    }
    public void openGarbage(View v) {
        SharedPreferences filterFile = getSharedPreferences("filterPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = filterFile.edit();

        //clear current filterFile
        editor.clear();
        //add only one filter to the file
        editor.putBoolean("garbage", true);
        editor.commit();

        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }
}