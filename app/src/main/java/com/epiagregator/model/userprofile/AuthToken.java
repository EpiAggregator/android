package com.epiagregator.model.userprofile;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Created by etien on 24/01/2017.
 */

public class AuthToken {

    private final String mToken;
    private final long mExpiresIn;
    private final String mType;

    public AuthToken(String type, String token, long expiresIn) {
        this.mType = type;
        this.mToken = token;
        this.mExpiresIn = expiresIn;
    }

    public String getToken() {
        return mToken;
    }

    public DateTime getExpiresIn() {
        return new DateTime(mExpiresIn, DateTimeZone.UTC);
    }

    public String getType() {
        return mType;
    }

    public boolean isExpired() {
        DateTime date = DateTime.now(DateTimeZone.UTC);
        return date.isAfter(getExpiresIn());
    }
}
