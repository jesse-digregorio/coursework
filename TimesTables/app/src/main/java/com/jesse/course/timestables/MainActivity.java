package com.jesse.course.timestables;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    protected SeekBar timesTableSeekBar;
    protected ListView timesTableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        SeekBar timesTableSeekBar = (SeekBar) findViewById(R.id.timesTableSeekBar);
//        ListView timesTableListView = (ListView) findViewById(R.id.timesTableListView);
        timesTableSeekBar = (SeekBar) findViewById(R.id.timesTableSeekBar);
        timesTableListView = (ListView) findViewById(R.id.timesTableListView);

        timesTableSeekBar.setMax(19);
        timesTableSeekBar.setProgress(9);

        timesTableSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                                         @Override
                                                         public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                                             generateTimesTable(progress + 1);
                                                         }

                                                         @Override
                                                         public void onStartTrackingTouch(SeekBar seekBar) {

                                                         }

                                                         @Override
                                                         public void onStopTrackingTouch(SeekBar seekBar) {

                                                         }
                                                     }
        );

        generateTimesTable(timesTableSeekBar.getProgress() + 1);







        ArrayList<Integer> tableBase = new ArrayList<Integer>();
        for (int i = 1; i <= 12; i++) {
            tableBase.add(i);
        }


    }

    public void generateTimesTable(int timesTable) {

        ArrayList<String> timesTableContent = new ArrayList<String>();
        for (int i = 1; i <= 10; i++) {
            timesTableContent.add(Integer.toString(i * timesTable));
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, timesTableContent);
        timesTableListView.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
