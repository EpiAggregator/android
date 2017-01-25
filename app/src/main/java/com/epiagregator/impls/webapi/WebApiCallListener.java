package com.epiagregator.impls.webapi;

import com.epiagregator.impls.webapi.error.RetrofitException;

/**
 * Created by etien on 29/10/2016.
 */

public interface WebApiCallListener<T> {
    void onResponse(T result);
    void onError(RetrofitException exception);
}
