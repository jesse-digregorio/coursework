package com.digregorio.gw2tinkering;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.digregorio.gw2tinkering.domain.GWTinkerApi;
import com.digregorio.gw2tinkering.model.World;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    GWTinkerApi mApiService;
    private CompositeDisposable mComposit = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mApiService = new GWTinkerApi();
        mComposit.add(fetchWorldDisposable());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mComposit.clear();
    }

    private Disposable fetchWorldDisposable() {

        return mApiService.API().getWorlds()
                .flatMapIterable(world -> world)
                .doOnNext(world -> Log.i("API Output", world.name))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }


    public class WorldListObserver extends DisposableObserver<List<World>> {

        public List<World> worldList;

        @Override
        public void onNext(List<World> t) {
            worldList = t;
        }
        @Override
        public void onComplete() {
            Log.i("Observer", "WorldListObserver Done");
        }
        @Override
        public void onError(Throwable t) {
            Log.e("Observer", t.getMessage());
        }
    }

}
