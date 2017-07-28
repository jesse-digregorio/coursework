package com.jesse.course.newsreader.domain;

import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * BaseService - Use this abstract class to setup api services. Typically, there would be one service
 *      setup per base API URL. (Example being NewsReaderApi, as it only uses the single firebase url)
 * @param <InterfaceType> RetroFit Service interface
 */
public abstract class BaseService<InterfaceType> {

    private static final int HTTP_TIMEOUT = 10;
    private static final TimeUnit HTTP_TIMEOUT_UNITS = TimeUnit.SECONDS;

    private final Class<InterfaceType> mInterfaceClass;
    private final String mBaseUrl;
    private OkHttpClient mOkClient;
    private InterfaceType mService = null;

    BaseService(final Class<InterfaceType> interfaceClass, String baseUrl) {
        mInterfaceClass = interfaceClass;
        mOkClient = getOkHttpClient();
        mBaseUrl = baseUrl;

        initService();
    }

    /**
     * InitService - We need to setup an Http client for retrofit to be
     *      able to use with our service. Note: the Retrofit.Builder
     *      build() call is slightly expensive. Be sure to keep these
     *      calls minimized (i.e. this service should be a singleton
     *      instance).
     */
    private void initService() {
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

    /**
     * Building an OkHttpClient for http calls
     * You can configure the http settings here, as observed below with defaults.
     * @return a pre-configured httpclient
     */
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