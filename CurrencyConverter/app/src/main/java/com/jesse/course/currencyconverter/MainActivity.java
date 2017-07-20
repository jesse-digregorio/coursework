package com.jesse.course.currencyconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public void clickConvertButton(View view) {

        EditText usDollarET = (EditText) findViewById(R.id.usDollarEditText);

        double usDollarAmount = Double.parseDouble(usDollarET.getText().toString());

        double euroAmount = usDollarAmount * 0.88;

        Log.i("USD Amount", Double.toString(usDollarAmount));
        Log.i("Converted Amount", Double.toString(euroAmount));
        Toast.makeText(getApplicationContext(), "€" + Double.toString(euroAmount), Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
