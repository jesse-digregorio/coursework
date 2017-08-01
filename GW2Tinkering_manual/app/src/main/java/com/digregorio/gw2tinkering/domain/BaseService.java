package com.digregorio.gw2tinkering.domain;

import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BaseService<InterfaceType> {

    private static final int HTTP_TIMEOUT = 10;
    private static final TimeUnit HTTP_TIMEOUT_UNITS = TimeUnit.SECONDS;

    private final Class<InterfaceType> mInterfaceClass;
    private final String mBaseUrl;
    private OkHttpClient mOkClient;
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
        if(mOkClient == null) {
            OkHttpClient.Builder okClientBuilder = new OkHttpClient.Builder();
            okClientBuilder.connectTimeout(HTTP_TIMEOUT, HTTP_TIMEOUT_UNITS);
            okClientBuilder.readTimeout(HTTP_TIMEOUT, HTTP_TIMEOUT_UNITS);
            okClientBuilder.writeTimeout(HTTP_TIMEOUT, HTTP_TIMEOUT_UNITS);
            mOkClient = okClientBuilder.build();
        }
        return mOkClient;
    }
}
