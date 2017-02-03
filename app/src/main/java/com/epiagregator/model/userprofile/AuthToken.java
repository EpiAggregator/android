package com.epiagregator.model.userprofile;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Created by etien on 24/01/2017.
 */

public class AuthToken {

    private final String token;
    private final long expiresIn;
    private final String type;

    public AuthToken(String type, String token, long expiresIn) {
        this.type = type;
        this.token = token;
        this.expiresIn = expiresIn;
    }

    public String getToken() {
        return token;
    }

    public DateTime getExpiresIn() {
        return new DateTime(expiresIn, DateTimeZone.UTC);
    }

    public String getType() {
        return type;
    }

    public boolean isExpired() {
        DateTime date = DateTime.now(DateTimeZone.UTC);
        return date.isAfter(getExpiresIn());
    }
}
