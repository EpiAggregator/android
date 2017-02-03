package com.epiagregator.impls.webapi.model;

/**
 * Created by etien on 25/01/2017.
 */

public class UpdateEntryRequest {
    private Boolean read;
    private Boolean favorite;

    public UpdateEntryRequest() {
    }

    public UpdateEntryRequest(Boolean read, Boolean favorite) {
        this.read = read;
        this.favorite = favorite;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }
}
