package com.epiagregator.impls.webapi.providers.fake;

import com.epiagregator.impls.webapi.model.Entry;
import com.epiagregator.impls.webapi.model.Feed;
import com.epiagregator.impls.webapi.model.UpdateEntryRequest;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Created by etien on 26/01/2017.
 */

public class FakeWebApiUtils {

    private static FakeWebApiUtils mInstance;

    private List<Feed> mFeeds = new ArrayList<>();
    private Map<String, List<Entry>> mEntries = new HashMap<>();

    private static final int NUM_ENTRIES = 100;

    private static final String REDDIT_FEED_TITLE[] = {
            "politics", "worldnews", "funny", "news", "pics", "nba", "gaming", "movies"
    };

    private static final String ENTRY_LINK = "https://www.reddit.com/r/Jokes/comments/5q6xuu/i_pissed_off_two_men_today_because_i_referred_to/";
    private static final String FEED_REDDIT_BASE_LINK = "https://www.reddit.com/r/";
    private List<Entry> entries;

    public static FakeWebApiUtils getInstance() {
        if (mInstance == null) {
            mInstance = new FakeWebApiUtils();
        }
        return mInstance;
    }

    private FakeWebApiUtils() {
        Lorem lorem = LoremIpsum.getInstance();

        Feed tmpFeed;
        Entry tmpEntry;
        Random random = new Random();

        for (int i = 0; i < REDDIT_FEED_TITLE.length; i++) {
            String feedTitle = REDDIT_FEED_TITLE[i];
            tmpFeed = new Feed(lorem.getWords(10, 30), lorem.getTitle(2, 5), FEED_REDDIT_BASE_LINK + feedTitle, "", "");
            String feedId = UUID.randomUUID().toString();
            tmpFeed.setId(feedId);

            int numEntries = Math.max(10, random.nextInt(20));

            List<Entry> tmpEntries = new ArrayList<>();
            for (int j = 0; j < numEntries; j++) {
                String entryId = UUID.randomUUID().toString();
                tmpEntry = new Entry(entryId, feedId, lorem.getName(), ENTRY_LINK, lorem.getTitle(5, 10), DateTime.now().minus(random.nextInt()).toDate(), lorem.getHtmlParagraphs(1, 4), false, false);
                tmpEntries.add(tmpEntry);
            }

            mFeeds.add(tmpFeed);
            mEntries.put(feedId, tmpEntries);
        }
    }

    List<Entry> getEntriesByFeed(String feedId) {
        return mEntries.get(feedId);
    }

    List<Feed> getFeeds() {
        return mFeeds;
    }

    List<Entry> getEntries() {
        List<Entry> entries = new ArrayList<>();
        for (Map.Entry<String, List<Entry>> entry: mEntries.entrySet()) {
            entries.addAll(entry.getValue());
        }
        return entries;
    }

    Feed getFeed(String feedId) {
        for (Feed feed: mFeeds) {
            if (feed.getId().equals(feedId)) {
                return feed;
            }
        }
        return null;
    }

    Entry updateEntry(String feedId, String entryId, UpdateEntryRequest updateEntryRequest) {
        List<Entry> entries = getEntriesByFeed(feedId);

        if (entries != null) {
            for (Entry entry: entries) {
                if (entry.getId().equals(entryId)) {
                    entry.setRead(updateEntryRequest.getRead());
                    entry.setFavorite(updateEntryRequest.getFavorite());
                    return entry;
                }
            }
        }
        return null;
    }
}
