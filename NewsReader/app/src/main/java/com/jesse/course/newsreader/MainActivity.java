package com.jesse.course.newsreader;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jesse.course.newsreader.domain.NewsReaderApi;
import com.jesse.course.newsreader.model.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Publisher;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.internal.operators.flowable.FlowableConcatArray;
import io.reactivex.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.listView)
    ListView mListView;
    Unbinder mUnbinder;

    NewsReaderApi mApiService;

    List<Article> articles = new ArrayList<>(20);
    List<String> titles = new ArrayList<>(20);
    List<String> content = new ArrayList<>(20);

    SQLiteDatabase articlesDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);

        initDb();
        initApi();
        initListView();

        fetchArticles();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    private void initDb() {
        articlesDb = this.openOrCreateDatabase("Articles", MODE_PRIVATE, null);
        articlesDb.execSQL("CREATE TABLE IF NOT EXISTS articles (id INTEGER PRIMARY KEY, articleId INTEGER, articleTitle VARCHAR, articleContent VARCHAR)");
    }

    private void initApi() {
        mApiService = new NewsReaderApi("https://hacker-news.firebaseio.com/");
    }

    private void initListView() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);
        mListView.setAdapter(arrayAdapter);
    }

    private void updateListView() {
        Cursor c = articlesDb.rawQuery("SELECT * FROM articles", null);

        int titleIndex = c.getColumnIndex("articleTitle");
        int contentIndex = c.getColumnIndex("articleContent");

        if (c.moveToFirst()) {
            titles.clear();
            content.clear();

            do {
                titles.add(c.getString(titleIndex));
            } while (c.moveToNext());

            ((ArrayAdapter) mListView.getAdapter()).notifyDataSetChanged();
        }
        c.close();
    }

    private void fetchArticles() {
        mApiService.API()
                .getRecentArticlesList()
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable(articleId -> articleId)
                .take(20)
                .flatMap(articleId -> mApiService.API()
                        .getArticle(articleId)
                        .doOnNext(this::saveArticle)
                ).subscribe();
    }

    private void saveArticle(Article article) {
        Log.i("Article - Author", article.author);
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(strings[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                Log.i("URL CONTENT", result);

                JSONArray jsonArray = new JSONArray(result);

                int numberOfItems = jsonArray.length() > 1 ? 1 : jsonArray.length();

                for (int i = 0; i < numberOfItems; i++) {

                    String articleId = jsonArray.getString(i);
                    Log.i("JsonItem", articleId);

                    url = new URL("https://hacker-news.firebaseio.com/v0/item/" + articleId + ".json?print=pretty");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    in = urlConnection.getInputStream();
                    reader = new InputStreamReader(in);

                    String articleInfo = "";
                    data = reader.read();
                    while (data != -1) {
                        char current = (char) data;
                        articleInfo += current;
                        data = reader.read();
                    }

                    //Log.i("ArticleInfo", articleInfo);
                    JSONObject jsonObject = new JSONObject(articleInfo);

                    //articlesDb.execSQL("DELETE FROM articles");

                    if (!jsonObject.isNull("title") && !jsonObject.isNull("url")) {
                        String articleTitle = jsonObject.getString("title");
                        String articleUrl = jsonObject.getString("url");

                        Log.i("ArticleTitle", articleTitle);
                        Log.i("ArticleUrl", articleUrl);

                        url = new URL(articleUrl);
                        urlConnection = (HttpURLConnection) url.openConnection();
                        in = urlConnection.getInputStream();
                        reader = new InputStreamReader(in);

                        String articleContent = "";
                        data = reader.read();
                        while (data != -1) {
                            char current = (char) data;
                            articleContent += current;
                            data = reader.read();
                        }
                        //Log.i("ArticleContent", articleContent);

                        String sql = "INSERT INTO articles (articleId, articleTitle, articleContent) VALUES (?, ?, ?)";

                        SQLiteStatement statement = articlesDb.compileStatement(sql);
                        statement.bindString(1, articleId);
                        statement.bindString(2, articleTitle);
                        statement.bindString(3, articleContent);

                        statement.execute();
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            updateListView();
        }
    }
}
