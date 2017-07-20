package com.jesse.course.higherorlower;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Random rand = new Random();
    private static final int max = 20;
    private static final int min = 1;

    private int numberToGuess = 0;

    protected void setNewNumberToGuess() {
        numberToGuess = rand.nextInt(max) + min;
    }

    public void clickGuess(View view) {


        int guess = Integer.parseInt(((EditText) findViewById(R.id.editTextGuess)).getText().toString());
        String toastMessage = "";

        if (guess > numberToGuess) {
            toastMessage = "Too high!";
        }
        else if (guess < numberToGuess) {
            toastMessage = "Too low!";
        }
        else {
            toastMessage = "Correct!!!";
            setNewNumberToGuess();
        }
        Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setNewNumberToGuess();
    }
}
