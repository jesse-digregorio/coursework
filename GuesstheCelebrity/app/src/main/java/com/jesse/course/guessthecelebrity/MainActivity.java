package com.jesse.course.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    private ImageView celebrityImage = null;
    private ArrayList<Button> buttons = null;
    private ArrayList<Celebrity> celebrities = null;

    private ArrayList<Celebrity> possibleAnswers = null;
    private int correctAnswer;
    private Bitmap correctBitmap;


    public class CelebrityDownloader extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            return null;
        }
    }


    private class DataDownloader extends  AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection connection = null;

            Log.i("DataDownloader", urls[0]);
            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();

                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("DataDownloader", e.getMessage());
            }

            return null;
        }
    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {

            try {

                URL url = new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();

                InputStream inputStream = connection.getInputStream();

                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);

                return myBitmap;


            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }

            return null;
        }
        /*
        @Override
        protected Bitmap doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();

                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                return bitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
        */
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        celebrityImage = (ImageView) findViewById(R.id.celebrityImageView);

        buttons = new ArrayList<>();
        Button currentButton = (Button) findViewById(R.id.guessButton0);
        buttons.add(currentButton);
        currentButton = (Button) findViewById(R.id.guessButton1);
        buttons.add(currentButton);
        currentButton = (Button) findViewById(R.id.guessButton2);
        buttons.add(currentButton);
        currentButton = (Button) findViewById(R.id.guessButton3);
        buttons.add(currentButton);

        DataDownloader task = new DataDownloader();
        String celebData = null;
        try {
            celebData = task.execute("http://www.posh24.se/kandisar").get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (celebData != null) {
            //Log.i("Data", celebData);
            Log.i("---WTF---", "CELEB DATA WAS NOT NULL");
            celebrities = loadCelebrityData(celebData);
            Log.i("---WTF---", "Size " + Integer.toString(celebrities.size()));
            generateQuestion();
            drawOptions();
        }
        else {
            Log.i("Error", "celebData is null. Dammit.");
        }



    }

    public ArrayList<Celebrity> loadCelebrityData(String rawData) {
        Log.i("---WTF---", "INSIDE LOAD CELEBRITY DATA");

        ArrayList<Celebrity> result = new ArrayList<>();

        String[] splitResult = rawData.split("<div class=\"image\">");

        Pattern srcPattern = Pattern.compile("src=\"(.*?)\"");
        Matcher srcMatcher = null;

        Pattern altPattern = Pattern.compile("alt=\"(.*?)\"");
        Matcher altMatcher = null;

        String celebName = "";
        String celebUrl  = "";

        for (int r = 0; r < splitResult.length; r++) {
            celebName = "";
            srcMatcher = srcPattern.matcher(splitResult[r]);
            altMatcher = altPattern.matcher(splitResult[r]);

            while (srcMatcher.find() && altMatcher.find()) {
                celebUrl = srcMatcher.group(1);
                celebName = altMatcher.group(1);
                Log.i("---IMG SRC", celebName);
                Log.i("---IMG ALT", celebUrl);
                Log.i("---IMG RANK", Integer.toString(r));
            }

            if (!celebName.equals("")) {
                result.add(new Celebrity(celebName, celebUrl, r+1));
            }

        }

        return result;

    }

    public void generateQuestion() {
        Random rand = new Random();
        int celeb = 0;

        possibleAnswers = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            celeb = rand.nextInt(celebrities.size());

            possibleAnswers.add(celebrities.get(celeb));
        }

        correctAnswer = rand.nextInt(possibleAnswers.size());
        Log.i("correctAnswerUrl", possibleAnswers.get(correctAnswer).getUrl());

        for (int i = 0; i < possibleAnswers.size(); i++) {
            Log.i("possibleAnswers", possibleAnswers.get(i).getName());
        }
        Log.i("correctAnswer", Integer.toString(correctAnswer));
        Log.i("correctAnswer", possibleAnswers.get(correctAnswer).getName());
        Log.i("correctAnswer", possibleAnswers.get(correctAnswer).getUrl());


        ImageDownloader imageTask = new ImageDownloader();
        try {
            correctBitmap = imageTask.execute(possibleAnswers.get(correctAnswer).getUrl()).get();

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void drawOptions() {
        celebrityImage.setImageBitmap(correctBitmap);

        for (int i = 0; i < 4; i++) {
            buttons.get(i).setText(possibleAnswers.get(i).getName());
        }
    }

    public void guessButtonClick(View view) {
        Log.i("guessButtonClick", view.getTag().toString());

        if (view.getTag().toString().equals(Integer.toString(correctAnswer))) {
            Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(),
                    "Wrong! - It is " + possibleAnswers.get(correctAnswer).getName(),
                    Toast.LENGTH_LONG).show();
        }

        generateQuestion();
        drawOptions();

    }


}
