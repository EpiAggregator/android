package com.epiagregator.impls.webapi.providers.fake;

import com.epiagregator.impls.webapi.model.Entry;
import com.epiagregator.impls.webapi.model.Feed;
import com.epiagregator.impls.webapi.model.SignInRequest;
import com.epiagregator.impls.webapi.model.UpdateEntryRequest;
import com.epiagregator.impls.webapi.model.UserModel;
import com.epiagregator.model.providers.IAccountProvider;
import com.epiagregator.model.userprofile.AuthToken;
import com.epiagregator.model.userprofile.UserProfile;

import org.joda.time.DateTime;

import java.util.List;

import rx.Observable;

/**
 * Created by etien on 24/01/2017.
 */

public class FakeWebApiAccountProvider implements IAccountProvider {

    private FakeWebApiUtils mFakeWebApiUtils;

    public FakeWebApiAccountProvider() {
        this.mFakeWebApiUtils = FakeWebApiUtils.getInstance();
    }

    @Override
    public Observable<UserProfile> loginUser(SignInRequest signInRequest) {
        return createFakeUser(signInRequest);
    }

    @Override
    public Observable<UserProfile> registerUser(SignInRequest signInRequest) {
        return createFakeUser(signInRequest);
    }

    @Override
    public Observable<UserModel> me() {
        return null;
    }

    @Override
    public Observable<List<Entry>> getEntriesByFeed(String feedId) {
        return Observable.just(mFakeWebApiUtils.getEntriesByFeed(feedId));
    }

    @Override
    public Observable<List<Feed>> getFeeds() {
        return Observable.just(mFakeWebApiUtils.getFeeds());
    }

    @Override
    public Observable<List<Entry>> getEntries() {
        return Observable.just(mFakeWebApiUtils.getEntries());
    }

    @Override
    public Observable<Feed> getFeed(String feedId) {
        return Observable.just(mFakeWebApiUtils.getFeed(feedId));
    }

    @Override
    public Observable<Entry> updateEntry(String feedId, String entryId, UpdateEntryRequest updateEntryRequest) {
        return Observable.just(mFakeWebApiUtils.updateEntry(feedId, entryId, updateEntryRequest));
    }

    @Override
    public Observable<Void> addFeed(String feedUri) {
        return Observable.just(null);
    }

    private static Observable<UserProfile> createFakeUser(SignInRequest signInRequest) {
        UserProfile userProfile = new UserProfile(null, signInRequest.getEmail(), signInRequest.getPassword());
        userProfile.setAuthToken(new AuthToken("Bearer", "fake-token", DateTime.now().plusDays(30).getMillis()));
        return Observable.just(userProfile);
    }
}
