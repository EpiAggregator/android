package com.epiagregator.model.userprofile;

import com.epiagregator.impls.webapi.WebApiCallListener;
import com.epiagregator.impls.webapi.error.RetrofitException;
import com.epiagregator.impls.webapi.model.Entry;
import com.epiagregator.impls.webapi.model.Feed;
import com.epiagregator.impls.webapi.model.SignInRequest;
import com.epiagregator.impls.webapi.model.UpdateEntryRequest;
import com.epiagregator.model.persistance.Persistance;
import com.epiagregator.model.providers.Providers;
import com.epiagregator.screens.signin.SignInMvpView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by etien on 23/01/2017.
 */

public class UserProfileService {

    private static void setActiveUserProfile(UserProfile activeUserProfile) {
        Persistance.getUserProfilePersistance().setActiveUserProfile(activeUserProfile);
    }

    public static Subscription loginUser(SignInRequest signInRequest, SignInMvpView signInMvpView) {
        signInMvpView.showLoading();
        return Providers.getAccountProvider().loginUser(signInRequest)
                .subscribe(userProfile -> {
                    setActiveUserProfile(userProfile);
                    signInMvpView.onResponse(userProfile);
                }, throwable -> {
                    signInMvpView.onError(RetrofitException.converToRetrofitException(throwable));
                });
    }

    public static Subscription registerUser(SignInRequest signInRequest, SignInMvpView signInMvpView) {
        signInMvpView.showLoading();
        return Providers.getAccountProvider().registerUser(signInRequest)
            .subscribe(userProfile -> {
                setActiveUserProfile(userProfile);
                signInMvpView.onResponse(userProfile);
            }, throwable -> {
                signInMvpView.onError(RetrofitException.converToRetrofitException(throwable));
            });
    }

    public static boolean iSignedIn() {
        return Persistance.getUserProfilePersistance().isSignedIn();
    }

    public static void signOut() {
        Persistance.getUserProfilePersistance().setActiveUserProfile(null);
    }

    public static UserProfile getActiveUserProfile() {
        return Persistance.getUserProfilePersistance().getActiveUserProfile();
    }

    public static Subscription getFeeds(WebApiCallListener<List<Feed>> feedCallBack) {
        return Providers.getAccountProvider().getFeeds()
                .subscribe(feedCallBack::onResponse, throwable -> {
                    feedCallBack.onError(RetrofitException.converToRetrofitException(throwable));
                });
    }

    public static Subscription addFeed(String feedUri, WebApiCallListener<List<Feed>> feedCallBack) {
        return Providers.getAccountProvider().addFeed(feedUri)
                .delay(3, TimeUnit.SECONDS)
                .flatMap(new Func1<Void, Observable<List<Feed>>>() {
                    @Override
                    public Observable<List<Feed>> call(Void aVoid) {
                        return Providers.getAccountProvider().getFeeds();
                    }
                }).subscribe(feedCallBack::onResponse, throwable -> {
                    feedCallBack.onError(RetrofitException.converToRetrofitException(throwable));
                });
    }

    public static Subscription getEntries(WebApiCallListener<List<Entry>> entriesCallBack) {
        return Providers.getAccountProvider().getEntries()
                .subscribe(entriesCallBack::onResponse, throwable -> {
                    entriesCallBack.onError(RetrofitException.converToRetrofitException(throwable));
                });
    }

    public static Subscription patchEntry(Entry mEntry, WebApiCallListener<Entry> favoriteListener) {
        return Providers.getAccountProvider().updateEntry(mEntry.getFeedId(), mEntry.getId(), new UpdateEntryRequest(mEntry.getRead(), mEntry.getFavorite()))
                .subscribe(favoriteListener::onResponse, throwable -> {
                    favoriteListener.onError(RetrofitException.converToRetrofitException(throwable));
                });
    }

    public static Subscription getEntriesByFeedId(String feedId, WebApiCallListener<List<Entry>> entriesCallBack) {
        return Providers.getAccountProvider().getEntriesByFeed(feedId)
                .subscribe(entriesCallBack::onResponse, throwable -> {
                    entriesCallBack.onError(RetrofitException.converToRetrofitException(throwable));
                });
    }
}
