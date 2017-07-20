package com.jesse.course.downloadingimages;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    ImageView mainImageView = null;
    Button mainButton = null;

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputSteam = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputSteam);

                return myBitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainImageView = (ImageView) findViewById(R.id.mainImageView);
        mainButton = (Button) findViewById(R.id.mainButton);

    }

    public void downloadImageClick(View view) {

        String urlString = "http://vignette3.wikia.nocookie.net/p__/images/2/20/Bob-Belcher-bobs-burgers-38510373-860-1440.jpg/revision/latest?cb=20160310203230&path-prefix=protagonist";
        Bitmap myImage = null;

        ImageDownloader task = new ImageDownloader();
        try {

            myImage = task.execute(urlString).get();
            mainImageView.setImageBitmap(myImage);

        } catch (Exception e) {

           e.printStackTrace();

        }
        Log.i("Interaction", "downloadImageClick" + ((Button)view).getText().toString());

    }

}
