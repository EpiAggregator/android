package com.epiagregator.screens.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.epiagregator.R;
import com.epiagregator.impls.webapi.WebApiCallListener;
import com.epiagregator.impls.webapi.error.RetrofitException;
import com.epiagregator.impls.webapi.model.Entry;
import com.epiagregator.impls.webapi.model.Feed;
import com.epiagregator.model.userprofile.UserProfile;
import com.epiagregator.model.userprofile.UserProfileService;
import com.epiagregator.screens.signin.LoginActivity;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;
import java.util.regex.Pattern;

import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    private Menu mNavigationMenu;

    private Pattern httpUrlPattern = Pattern.compile("^(http://|https://)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?$");

    private EntryAdapter mEntryAdapter;

    private MenuItem mCurrentItemSelected;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        UserProfile userProfile = UserProfileService.getActiveUserProfile();

        String text = userProfile.getUserEmail().substring(0, 1);
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .textColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .useFont(Typeface.DEFAULT)
                .toUpperCase()
                .endConfig()
                .buildRound(text, Color.WHITE);

        ImageView mUserImageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.activity_main_drawer_img_profile);
        TextView mUserEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.activity_main_drawer_user_email);

        mUserImageView.setImageDrawable(drawable);
        mUserEmail.setText(userProfile.getUserEmail());

        mNavigationMenu = navigationView.getMenu();
        navigationView.setCheckedItem(R.id.nav_home);

        getSupportActionBar().setTitle(R.string.main_drawer_menu_home);

        RecyclerView entriesRecyclerView = (RecyclerView) findViewById(R.id.activity_main_rc_entry);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        mEntryAdapter = new EntryAdapter(this);
        entriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        entriesRecyclerView.setAdapter(mEntryAdapter);

        mCompositeSubscription.add(UserProfileService.getFeeds(new FeedCallBack()));
        mCompositeSubscription.add(UserProfileService.getEntries(new EntriesCallBack()));
    }

    private void refreshContent() {
        mCompositeSubscription.add(UserProfileService.getFeeds(new FeedCallBack()));

        if (mCurrentItemSelected == null) {
            mCompositeSubscription.add(UserProfileService.getEntries(new EntriesCallBack()));
        } else {
            mCompositeSubscription.add(UserProfileService.getEntriesByFeedId(mCurrentItemSelected.getIntent().getAction(), new EntriesCallBack()));
        }
    }

    @Override
    protected void onDestroy() {
        mCompositeSubscription.unsubscribe();

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sign_out) {
            UserProfileService.signOut();
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return true;
        }

        if (id == R.id.action_add_rss_feed) {
            View addRssDialogView = LayoutInflater.from(this).inflate(R.layout.add_rss_feed_dialog, null);
            EditText txtUrl = (EditText) addRssDialogView.findViewById(R.id.userInputDialog);
            AlertDialog addRssDialog = new AlertDialog.Builder(this)
                    .setView(addRssDialogView)
                    .setPositiveButton(R.string.add, (dialogInterface, i) -> {
                        String feedUri = txtUrl.getText().toString();
                        mCompositeSubscription.add(UserProfileService.addFeed(feedUri, new FeedCallBack()));
                        dialogInterface.dismiss();
                    })
                    .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    })
                    .show();

            final Button positiveButton = addRssDialog.getButton(AlertDialog.BUTTON_POSITIVE);

            RxTextView.textChangeEvents(txtUrl)
                    .map(textViewTextChangeEvent -> {
                        CharSequence uri = textViewTextChangeEvent.text();
                        return uri.length() > 0 && Patterns.WEB_URL.matcher(uri).matches();
                    })
                    .subscribe(positiveButton::setEnabled);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (mCurrentItemSelected == null || !mCurrentItemSelected.getTitle().equals(item.getTitle())) {
            setTitle(item.getTitle());

            if (id == R.id.nav_home) {
                mCompositeSubscription.add(UserProfileService.getEntries(new EntriesCallBack()));
            } else {
                String feedId = item.getIntent().getAction();
                mCompositeSubscription.add(UserProfileService.getEntriesByFeedId(feedId, new EntriesCallBack()));
            }
            mCurrentItemSelected = item;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class FeedCallBack implements WebApiCallListener<List<Feed>> {

        @Override
        public void onResponse(List<Feed> result) {
            //Toast.makeText(MainActivity.this, "Get feed count size : " + result.size(), Toast.LENGTH_LONG).show();

            SubMenu subMenuItemFeeds = mNavigationMenu.findItem(R.id.nav_feeds).getSubMenu();
            subMenuItemFeeds.clear();

            for (Feed feed: result) {
                MenuItem itemFeed = subMenuItemFeeds.add(R.id.main_drawer_menu_feed_group, Menu.FLAG_APPEND_TO_GROUP, 0, feed.getTitle()).setCheckable(true).setChecked(false).setIntent(new Intent(feed.getId()));
                if (feed.getImage() != null && feed.getImage().length() > 0) {
                    Picasso.with(MainActivity.this)
                            .load(feed.getImage())
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    itemFeed.setIcon(new BitmapDrawable(getResources(), bitmap));
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            });
                }
            }
        }

        @Override
        public void onError(RetrofitException exception) {

        }
    }

    class EntriesCallBack implements WebApiCallListener<List<Entry>> {

        @Override
        public void onResponse(List<Entry> result) {
            //Toast.makeText(MainActivity.this, "Get entries count size : " + result.size(), Toast.LENGTH_LONG).show();
            mSwipeRefreshLayout.setRefreshing(false);
            mEntryAdapter.setItems(result);
        }

        @Override
        public void onError(RetrofitException exception) {

        }
    }
}
