package com.epiagregator.impls.webapi.error;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by etien on 27/10/2016.
 */

public class RetrofitException extends RuntimeException {
    private static RetrofitException httpError(String url, Response response) {
        String message = response.code() + " " + response.message();
        return new RetrofitException(message, url, response, Kind.HTTP, null);
    }

    private static RetrofitException networkError(IOException exception) {
        return new RetrofitException(exception.getMessage(), null, null, Kind.NETWORK, exception);
    }

    private static RetrofitException unexpectedError(Throwable exception) {
        return new RetrofitException(exception.getMessage(), null, null, Kind.UNEXPECTED, exception);
    }

    public static RetrofitException converToRetrofitException(Throwable throwable) {
        RetrofitException retrofitException;

        if (throwable instanceof RetrofitException) {
            retrofitException = (RetrofitException) throwable;
        } else {
            if (throwable instanceof HttpException) {
                // We had non-200 http error
                HttpException httpException = (HttpException) throwable;
                Response response = httpException.response();
                retrofitException = httpError(response.raw().request().url().toString(), response);
            } else if (throwable instanceof IOException) {
                // A network error happened
                retrofitException = networkError((IOException) throwable);
            } else {
                // We don't know what happened. We need to simply convert to an unknown error

                retrofitException = unexpectedError(throwable);
            }
        }

        return retrofitException;
    }

    /** Identifies the event kind which triggered a {@link RetrofitException}. */
    public enum Kind {
        /** An {@link IOException} occurred while communicating to the server. */
        NETWORK,
        /** A non-200 HTTP status code was received from the server. */
        HTTP,
        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }

    private final String url;
    private final Response response;
    private final Kind kind;

    RetrofitException(String message, String url, Response response, Kind kind, Throwable exception) {
        super(message, exception);
        this.url = url;
        this.response = response;
        this.kind = kind;
    }

    /** The request URL which produced the error. */
    public String getUrl() {
        return url;
    }

    /** Response object containing status code, headers, body, etc. */
    public Response getResponse() {
        return response;
    }

    /** The event kind which triggered this error. */
    public Kind getKind() {
        return kind;
    }
}