package com.example.simplygreensfuapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class FilterActivity extends AppCompatActivity {
    private SharedPreferences filterPreference;
    private int[] switchIds;
    private String[] waypointTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_filter);

        //initiate SharedPreference
        filterPreference = getApplicationContext().getSharedPreferences("filterPreferences", Context.MODE_PRIVATE);

        //get ids from switches
        waypointTypes = getResources().getStringArray(R.array.waypoint_types);
        switchIds = new int[waypointTypes.length];
        for (int i = 0; i < switchIds.length; i++) {
            try {
                switchIds[i] = R.id.class.getField(waypointTypes[i]).getInt(null);
            } catch (Exception e) {
                Log.e("Resource id", "Failed to get id.", e);
            }
        }

        //set up switches with click listener
        for (int i = 0; i < waypointTypes.length; i++) {
            SwitchCompat sw = findViewById(switchIds[i]);
            sw.setChecked(filterPreference.getBoolean(waypointTypes[i], false));
            sw.setTag(waypointTypes[i]);
            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SharedPreferences.Editor editor = filterPreference.edit();
                    editor.putBoolean((String) buttonView.getTag(), isChecked);
                    editor.apply();
                }
            });
        }
    }

    /**
     * A function to turn on all items in filter UI
     *
     * @param view View
     */
    public void toggleAll(View view) {
        SharedPreferences.Editor editor = filterPreference.edit();
        for (int i = 0; i < waypointTypes.length; i++) {
            SwitchCompat sw = findViewById(switchIds[i]);
            sw.setChecked(true);
            editor.putBoolean(waypointTypes[i], true);
        }
        editor.apply();
    }
}


