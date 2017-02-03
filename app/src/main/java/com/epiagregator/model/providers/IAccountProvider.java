package com.epiagregator.model.providers;

import com.epiagregator.impls.webapi.model.Entry;
import com.epiagregator.impls.webapi.model.Feed;
import com.epiagregator.impls.webapi.model.SignInRequest;
import com.epiagregator.impls.webapi.model.UpdateEntryRequest;
import com.epiagregator.impls.webapi.model.UserModel;
import com.epiagregator.model.userprofile.UserProfile;

import java.util.List;

import rx.Observable;

/**
 * Created by etien on 24/01/2017.
 */

public interface IAccountProvider {
    Observable<UserProfile> loginUser(SignInRequest signInRequest);

    Observable<UserProfile> registerUser(SignInRequest signInRequest);

    Observable<UserModel> me();

    Observable<List<Entry>> getEntriesByFeed(String feedId);

    Observable<List<Feed>> getFeeds();

    Observable<List<Entry>> getEntries();

    Observable<Feed> getFeed(String feedId);

    Observable<Entry> updateEntry(String feedId, String entryId, UpdateEntryRequest updateEntryRequest);

    Observable<Void> addFeed(String feedUri);
}
