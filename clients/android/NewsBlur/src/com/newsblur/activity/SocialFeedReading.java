package com.newsblur.activity;

import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

import com.newsblur.database.MixedFeedsReadingAdapter;
import com.newsblur.domain.SocialFeed;
import com.newsblur.util.FeedUtils;
import com.newsblur.util.UIUtils;

public class SocialFeedReading extends Reading {

    public static final String EXTRA_IGNORE_FILTERS = "ignore_filters";
    private boolean ignoreFilters;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        ignoreFilters = getIntent().hasExtra(EXTRA_IGNORE_FILTERS);
        SocialFeed socialFeed = FeedUtils.dbHelper.getSocialFeed(fs.getSingleSocialFeed().getKey());
        if (socialFeed == null) finish(); // don't open fatally stale intents
        UIUtils.setCustomActionBar(this, socialFeed.photoUrl, socialFeed.feedTitle);
        readingAdapter = new MixedFeedsReadingAdapter(getFragmentManager(), defaultFeedView, socialFeed.userId);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        // If we have navigated from the profile we want to ignore the StateFilter and ReadFilter settings
        // for the feed to ensure we can find the story.
        if (ignoreFilters) {
            return FeedUtils.dbHelper.getStoriesLoaderIgnoreFilters(fs);
        } else {
            return super.onCreateLoader(loaderId, bundle);
        }
    }
}
