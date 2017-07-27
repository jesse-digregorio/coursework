package com.jesse.course.newsreader.domain;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BaseService<InterfaceType> {

    private final Class<InterfaceType> mInterfaceClass;
    private final String mBaseUrl;
    private final OkHttpClient mOkClient;
    private InterfaceType mService = null;

    public BaseService(final Class<InterfaceType> interfaceClass, String baseUrl) {
        mInterfaceClass = interfaceClass;
        mOkClient = getOkHttpClient();
        mBaseUrl = baseUrl;

        initService();
    }

    public void initService() {
        try {
            OkHttpClient.Builder clientBuilder = mOkClient.newBuilder();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(mBaseUrl)
                    .client(clientBuilder.build())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            mService = retrofit.create(mInterfaceClass);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public InterfaceType API() {
        return mService;
    }

    private OkHttpClient getOkHttpClient() {
        return null;
    }
}