package com.epiagregator.impls.webapi;

import android.util.Log;

import com.epiagregator.impls.webapi.error.RetrofitException;

import rx.Subscriber;

/**
 * Created by etien on 28/10/2016.
 */

public class WebApiCallSubscriber<T> extends Subscriber<T> {
    private static final String TAG = WebApiCallSubscriber.class.getSimpleName();

    private WebApiCallListener<T> mWebApiCallListener;

    public WebApiCallSubscriber(WebApiCallListener webApiCallListener) {
        this.mWebApiCallListener = webApiCallListener;
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable throwable) {
        RetrofitException retrofitException = RetrofitException.converToRetrofitException(throwable);

        Log.e(TAG, String.format("Network error occurred (%s %s) for url: %s", retrofitException.getKind(), retrofitException.getMessage(), retrofitException.getUrl()));

        if (mWebApiCallListener != null) {
            mWebApiCallListener.onError(retrofitException);
        }
    }

    @Override
    public void onNext(T t) {
        if (mWebApiCallListener != null) {
            mWebApiCallListener.onResponse(t);
        }
    }
}
