package com.example.simplygreensfuapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * An Activity started by QuestionnaireActivity with Intent to specify a layout
 */
public class ItemInfoActivity extends AppCompatActivity {
    private Intent intent;
    private SharedPreferences filterFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        //Log.d("TEST", intent.getStringExtra(""));
        try {
            setContentView(R.layout.class.getField("questionnaire_" + intent.getStringExtra("")).getInt(null));
        } catch (Exception e) {
            Log.e("Resource id", "Failed to get id.", e);
            finish();
        }
    }

    //Function to open MapActivity
    public void openMap(View v) {
        openMap();
    }

    public void openMap() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    //Function to open MapActivity
    public void openGarbage(View v) {
        filterFile = getSharedPreferences("filterPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = filterFile.edit();

        //clear current filterFile
        editor.clear();
        //add only one filter to the file
        editor.putBoolean("garbage", true);
        editor.commit();

        openMap();
    }


    public void openExpressStation(View v) {
        filterFile = getSharedPreferences("filterPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = filterFile.edit();

        //clear current filterFile
        editor.clear();
        //add only one filter to the file
        editor.putBoolean("expressStation", true);
        editor.commit();

        openMap();
    }

    public void openAll(View v) {
        //open shared preference file
        filterFile = getSharedPreferences("filterPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = filterFile.edit();

        editor.clear();

        editor.putBoolean("charger", true);
        editor.putBoolean("expressStation", true);
        editor.putBoolean("tumbler", true);
        editor.putBoolean("garbage", true);
        editor.putBoolean("water", true);
        editor.putBoolean("print", true);
        editor.putBoolean("bus", true);
        editor.putBoolean("print4", true);
        editor.putBoolean("soap", true);
        editor.putBoolean("cycleStand", true);

        editor.commit();
        openMap();
    }
}