package com.example.simplygreensfuapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class QuestionnaireActivity extends AppCompatActivity {

    private SharedPreferences filterFile;
    private String[] labels;
    private int[][] ids;
    private CardViewAdapter cardViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire_test);
        try {
            labels = getResources().getStringArray(R.array.questionnaire_labels);
            ids = new int[labels.length][2];
            initCards();

            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.span)));
            cardViewAdapter = new CardViewAdapter(ids);
            recyclerView.setAdapter(cardViewAdapter);
            cardViewAdapter.setOnItemClickListener((view, position) -> openCard(position));
        } catch (Exception e) {
            return;
        }
    }


    public void initCards() {
        for (int i = 0; i < labels.length; i++) {
            try {
                ids[i][0] = R.string.class.getField("questionnaire_label_" + labels[i]).getInt(null);
                ids[i][1] = R.drawable.class.getField(labels[i]).getInt(null);
            } catch (Exception e) {
                Log.e("Resource id", "Failed to get id.", e);
                return;
            }
        }
    }

    public void openCard(int position) {
        Intent intent;

        intent = new Intent(this, ItemInfoActivity.class);
        intent.putExtra("", labels[position]);
        startActivity(intent);
    }

    //Function to open MapActivity
    public void openMap(View v) {
        openMap();
    }

    public void openMap() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    //Function to open SearchActivity
    public void openSearch(View v) {
        openSearch();
    }

    public void openSearch() {
        Intent intent = new Intent(this, SearchActivity.class);
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


    public void openExpressStation(View v){
        filterFile  = getSharedPreferences("filterPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = filterFile.edit();

        //clear current filterFile
        editor.clear();
        //add only one filter to the file
        editor.putBoolean("expressStation", true);
        editor.commit();

        openMap();
    }

    public void openAll(View v){
        //open shared preference file
        filterFile  = getSharedPreferences("filterPreferences", Context.MODE_PRIVATE);
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