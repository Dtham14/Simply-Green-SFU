package com.example.simplygreensfuapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

public class SearchActivity<context> extends AppCompatActivity {
    // linking
    SearchView mySearchView;
    ListView myList;

    ArrayList<String> list;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mySearchView = (SearchView) findViewById(R.id.searchView);
        myList = (ListView) findViewById(R.id.myList);

        list = new ArrayList<>();
        //Blue bin
        list.add("Milk/Juice Cartons");
        list.add("Plastic Container");
        list.add("Aluminium Container");
        list.add("Glass Container");

        //Yellow bin
        list.add("Printed Paper");
        list.add("Cardboard Boxes");
        list.add("Paper Towels");
        list.add("Magazines");

        //Green bin
        list.add("Fruits");
        list.add("Pizza Box");
        list.add("Paper Cups");

        //Black bin
        list.add("Black Plastic Bags");
        list.add("Styrofoam");
        list.add("Chip Bags");
        list.add("Candy Wrappers");


        //initialize adapter with text color and font
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(getResources().getColor(R.color.body, getTheme()));
                text.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat));
                return view;
            }
        };
        myList.setAdapter(adapter);

        //click calls startActivity
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(view);
            }
        });

        //filter list while typing
        mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });


    }

    /**
     * Start the bins Activity based on text in TextView
     *
     * @param view View
     */
    private void startActivity(View view) {
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        String text = textView.getText().toString();

        int position = list.indexOf(text);
        if (position >= 0 && position < 4) {
            Intent intent = new Intent(view.getContext(), Activity_BlueBin.class);
            startActivity(intent);
        }

        if (position >= 4 && position < 8) {
            Intent intent2 = new Intent(view.getContext(), Activity_YellowBin.class);
            startActivity(intent2);
        }

        if (position >= 8 && position < 11) {
            Intent intent3 = new Intent(view.getContext(), Activity_GreenBin.class);
            startActivity(intent3);
        }

        if (position >= 11 && position < 15) {
            Intent intent4 = new Intent(view.getContext(), Activity_BlackBin.class);
            startActivity(intent4);
        }
    }
}