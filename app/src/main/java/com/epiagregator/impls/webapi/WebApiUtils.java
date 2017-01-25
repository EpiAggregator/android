package com.epiagregator.impls.webapi;

import android.content.Context;
import android.util.Log;

import com.epiagregator.R;
import com.epiagregator.impls.webapi.error.RetrofitException;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by etien on 29/10/2016.
 */

public class WebApiUtils {
    private static final String TAG = WebApiUtils.class.getSimpleName();

    public static final String EMAIL_ALREADY_EXIST = "Email already exist";

    private Map<String, Integer> mErrorMap;

    private static WebApiUtils mInstance = null;

    public WebApiUtils() {
        mErrorMap = new HashMap<>();

        mErrorMap.put(EMAIL_ALREADY_EXIST, R.string.sign_in_email_already_exist);
    }

    public static WebApiUtils getInstance() {
        if (mInstance == null) {
            mInstance = new WebApiUtils();
        }

        return mInstance;
    }

    public String convertErrorToMessage(RetrofitException exception, Context context) {
        JsonObject jsonObject = (JsonObject) exception.getResponse().body();

        if (jsonObject.has("message")) {
            String message = jsonObject.getAsJsonPrimitive("message").getAsString();
            Integer messageId = mErrorMap.get(message);

            if (messageId != null) {
                return context.getString(messageId);
            } else {
                Log.d(TAG, "Can't find associated error message for: " + message);
                return message;
            }
        }

        Log.d(TAG, "No field \"message\" found in the response");
        return exception.getMessage();
    }
}
