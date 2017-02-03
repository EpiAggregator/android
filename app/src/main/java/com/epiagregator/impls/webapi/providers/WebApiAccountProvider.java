package com.epiagregator.impls.webapi.providers;

import android.content.Context;

import com.epiagregator.impls.webapi.EpiAgregatorService;
import com.epiagregator.impls.webapi.OAuthInterceptor;
import com.epiagregator.impls.webapi.model.Entry;
import com.epiagregator.impls.webapi.model.Feed;
import com.epiagregator.impls.webapi.model.SignInRequest;
import com.epiagregator.impls.webapi.model.UpdateEntryRequest;
import com.epiagregator.impls.webapi.model.UserModel;
import com.epiagregator.model.providers.IAccountProvider;
import com.epiagregator.model.userprofile.AuthToken;
import com.epiagregator.model.userprofile.UserProfile;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by etien on 24/01/2017.
 */

public class WebApiAccountProvider implements IAccountProvider {

    private final EpiAgregatorService mEpiAgregatorService;

    public WebApiAccountProvider(Context context, OAuthInterceptor oAuthInterceptor) {
        this.mEpiAgregatorService = EpiAgregatorService.Creator.newRxService(context, oAuthInterceptor);
    }

    @Override
    public Observable<UserProfile> loginUser(SignInRequest signInRequest) {
        return mEpiAgregatorService.getAccessToken(signInRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(authToken -> {
                    UserProfile userProfile = new UserProfile(null, signInRequest.getEmail(), signInRequest.getPassword());
                    userProfile.setAuthToken(authToken);
                    return userProfile;
                });
    }

    @Override
    public Observable<UserProfile> registerUser(SignInRequest signInRequest) {
        return mEpiAgregatorService.registerUser(signInRequest)
                .flatMap(new Func1<Void, Observable<AuthToken>>() {
                    @Override
                    public Observable<AuthToken> call(Void aVoid) {
                        return mEpiAgregatorService.getAccessToken(signInRequest);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(authToken -> {
                    UserProfile userProfile = new UserProfile(null, signInRequest.getEmail(), signInRequest.getPassword());
                    userProfile.setAuthToken(authToken);
                    return userProfile;
                });
    }

    @Override
    public Observable<UserModel> me() {
        return null;
    }

    @Override
    public Observable<List<Entry>> getEntriesByFeed(String feedId) {
        return mEpiAgregatorService.getEntriesByFeed(feedId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<Feed>> getFeeds() {
        return mEpiAgregatorService.getFeeds()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<Entry>> getEntries() {
        return mEpiAgregatorService.getEntries()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Feed> getFeed(String feedId) {
        return mEpiAgregatorService.getFeed(feedId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Entry> updateEntry(String feedId, String entryId, UpdateEntryRequest updateEntryRequest) {
        return mEpiAgregatorService.updateEntry(feedId, entryId, updateEntryRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Void> addFeed(String feedUri) {
        return mEpiAgregatorService.addFeed(Collections.singletonList(feedUri))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
