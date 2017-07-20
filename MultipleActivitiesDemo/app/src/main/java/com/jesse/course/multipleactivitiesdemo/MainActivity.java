package com.jesse.course.multipleactivitiesdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] names = {"Eva", "Mom", "Dominick", "Margherita"};
        ArrayAdapter<String> namesAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);

        ListView listView = (ListView) findViewById(R.id.namesListView);
        listView.setAdapter(namesAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i("ListView", "in onItemClick for the ListView");
                Log.i("ListViewItem", parent.getItemAtPosition(position).toString());

                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                String name = parent.getItemAtPosition(position).toString();
                intent.putExtra("clickedName", name);
                startActivity(intent);
            }
        });

    }

    public void switchToSecond(View view) {

        Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
        startActivity(intent);

    }

}
