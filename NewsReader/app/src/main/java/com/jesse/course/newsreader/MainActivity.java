package com.jesse.course.newsreader;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jesse.course.newsreader.domain.NewsReaderApi;
import com.jesse.course.newsreader.model.Article;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.listView)
    ListView mListView;
    Unbinder mUnbinder;

    NewsReaderApi mApiService;

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
        updateListView();

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

    private void fetchArticles() {
        mApiService.API()
                .getRecentArticlesList()
                .concatMapIterable(articleId -> articleId)
                .take(20)
                .flatMap(
                        articleId -> mApiService.API()
                                .getArticle(articleId)
                                .doOnNext(this::saveArticle)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                ).doOnComplete(this::updateListView)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void saveArticle(Article article) {
        Log.i("Article - Author", article.author);

        String sql = "INSERT INTO articles (articleId, articleTitle, articleContent) VALUES (?, ?, ?)";

        SQLiteStatement statement = articlesDb.compileStatement(sql);
        statement.bindLong(1, article.id);
        statement.bindString(2, article.title);
        statement.bindString(3, article.author);

        statement.execute();
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
                content.add(c.getString(contentIndex));
            } while (c.moveToNext());
        }
        c.close();

        ((ArrayAdapter) mListView.getAdapter()).notifyDataSetChanged();
    }
}
