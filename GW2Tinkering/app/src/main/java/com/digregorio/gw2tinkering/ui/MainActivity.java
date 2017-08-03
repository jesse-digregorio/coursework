package com.digregorio.gw2tinkering.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.digregorio.gw2tinkering.R;
import com.digregorio.gw2tinkering.domain.GWTinkerApi;
import com.digregorio.gw2tinkering.model.World;
import com.digregorio.gw2tinkering.ui.adapter.WorldAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    GWTinkerApi mApiService;
    private CompositeDisposable mComposite = new CompositeDisposable();

    private List<World> mWorldList = new ArrayList<World>();

    @BindView(R.id.wolrdListView)
    ListView mWorldListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        ArrayAdapter<World> arrayAdapter = new WorldAdapter(this, R.layout.world_item_row, mWorldList);
        mWorldListView.setAdapter(arrayAdapter);

        mApiService = new GWTinkerApi();
        //mComposite.add(fetchWorldDisposable());
        mComposite.add(fetchWorldListDisposable());

        Log.i("Total Worlds", Integer.toString(mWorldList.size()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mComposite.clear();
    }

    private Disposable fetchWorldListDisposable() {
        return mApiService.API().getWorlds()
                //.doOnNext(worlds -> mWorldList = worlds)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                //.subscribe(new WorldListObserver());
                .subscribe(this::updateWorldList, this::updateListViewError, this::updateListView);
    }

    private void updateWorldList(List<World> worldList) {
        mWorldList.addAll(worldList);
    }

    private void updateListView() {
        Log.i("Total Worlds Observer", Integer.toString(mWorldList.size()));
        ((ArrayAdapter) mWorldListView.getAdapter()).notifyDataSetChanged();
    }

    private void updateListViewError(Throwable throwable) {
        throwable.printStackTrace();
    }

    private Disposable fetchWorldDisposable() {
        return mApiService.API().getWorlds()
                .flatMapIterable(world -> world)
                .doOnNext(world -> Log.i("API Output", world.name))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
