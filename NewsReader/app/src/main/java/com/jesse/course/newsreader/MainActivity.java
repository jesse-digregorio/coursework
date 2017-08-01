package com.jesse.course.newsreader;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.jesse.course.newsreader.databinding.MainActivityBinding;
import com.jesse.course.newsreader.domain.NewsReaderApi;
import com.jesse.course.newsreader.model.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private MainActivityBinding mViewBinding;

    NewsReaderApi mApiService;
    SQLiteDatabase articlesDb;

    List<String> titles = new ArrayList<>(20);
    List<String> content = new ArrayList<>(20);

    CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);

        initDb();
        initApi();
        initListView();
        updateListView();

        fetchArticles();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();

        if (mViewBinding != null) {
            mViewBinding.unbind();
            mViewBinding = null;
        }
    }

    private void initDb() {
        articlesDb = this.openOrCreateDatabase("Articles", MODE_PRIVATE, null);
        articlesDb.execSQL("CREATE TABLE IF NOT EXISTS articles (id INTEGER PRIMARY KEY, articleId INTEGER, articleTitle VARCHAR, articleContent VARCHAR)");
    }

    private void initApi() {
        mApiService = new NewsReaderApi();
    }

    private void initListView() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);
        mViewBinding.listView.setAdapter(arrayAdapter);

        mCompositeDisposable.add(
                RxTextView.textChangeEvents(mViewBinding.articleSearchEditText)
                        .debounce(800, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onSearchTextChanged)
        );
    }

    private void onSearchTextChanged(TextViewTextChangeEvent event) {
        ArrayAdapter adapter = ((ArrayAdapter) mViewBinding.listView.getAdapter());
        adapter.getFilter().filter(event.text().toString());
    }

    /**
     * FetchArticles - triggers a retrofit http call that chains
     * several other calls together to fetch articles from
     * the newsfeed API service. The articles are then stored
     * in the DB.
     */
    private Disposable fetchArticles() {
        return mApiService.API()
                .getRecentArticlesList() // API service http request using retrofit interface (see: NewsReaderService)
                .concatMapIterable(articleId -> articleId) // Iterate through each id that the api request returns
                .take(20) // Use only 20 of the fetched article ids that come from the concatMap
                .flatMap(
                        articleId -> mApiService.API() // for each article id from the concatMap,
                                .getArticle(articleId) // create another retrofit http request from our api service for the article information
                                .doOnNext(this::saveArticle) // save each article response into our db
                                .subscribeOn(Schedulers.io()) // perform http request work on io thread
                                .observeOn(AndroidSchedulers.mainThread()) // observe the responses from the ui thread
                )
                .doOnComplete(this::updateListView)
                .subscribeOn(Schedulers.io()) // perform http request work on io thread
                .observeOn(AndroidSchedulers.mainThread()) // observe the responses from the ui thread
                .subscribe(); // Trigger a subscribe call which causes the observable to begin publishing events
    }

    /**
     * SaveArticle - Places an article in the DB
     *
     * @param article - article model to be saved into the db
     */
    private void saveArticle(Article article) {
        String sql = "INSERT INTO articles (articleId, articleTitle, articleContent) VALUES (?, ?, ?)";

        SQLiteStatement statement = articlesDb.compileStatement(sql);
        statement.bindLong(1, article.id);
        statement.bindString(2, article.title);
        statement.bindString(3, article.author);

        statement.execute();
    }

    /**
     * UpdateListView - fetches all DB-stored articles and puts them in a list
     * for our adapter. The ListView's adapter is then notified that it's
     * data set has possibly changed.
     */
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

        // Always close your DB cursors after work is complete
        c.close();

        // Notify the ListView's Adapter to display articles
        ((ArrayAdapter) mViewBinding.listView.getAdapter()).notifyDataSetChanged();
    }
}
