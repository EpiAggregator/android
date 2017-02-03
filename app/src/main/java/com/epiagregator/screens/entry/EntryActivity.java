package com.epiagregator.screens.entry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.epiagregator.R;
import com.epiagregator.impls.webapi.WebApiCallListener;
import com.epiagregator.impls.webapi.error.RetrofitException;
import com.epiagregator.impls.webapi.model.Entry;
import com.epiagregator.model.userprofile.UserProfileService;
import com.epiagregator.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EntryActivity extends AppCompatActivity {

    public static final String ENTRY_EXTRA_KEY = "entryExtraKey";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.activity_entry_description)
    TextView entryDescription;

    public static Intent newIntent(Context mContext, Entry entry) {
        Intent intent = new Intent(mContext, EntryActivity.class);
        intent.putExtra(ENTRY_EXTRA_KEY, entry);
        return intent;
    }

    private Entry mEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        ButterKnife.bind(this);

        mEntry = getIntent().getParcelableExtra(ENTRY_EXTRA_KEY);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mEntry.getTitle());
            getSupportActionBar().setSubtitle(Utils.getEntryHeaderInfo(this, mEntry));
        }

        entryDescription.setMovementMethod(LinkMovementMethod.getInstance());
        entryDescription.setText(Utils.getHtml(mEntry.getDescription(), false));

        if (!mEntry.getRead()) {
            mEntry.setRead(true);
            UserProfileService.patchEntry(mEntry, new UpdateEntryListener());
        }
    }

    private class UpdateEntryListener implements WebApiCallListener<Entry> {

        private String mSuccessMessage;

        public UpdateEntryListener() {
            this(null);
        }

        public UpdateEntryListener(String successMessage) {
            this.mSuccessMessage = successMessage;
        }

        @Override
        public void onResponse(Entry result) {
            if (mSuccessMessage != null) {
                Toast.makeText(EntryActivity.this, mSuccessMessage, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onError(RetrofitException exception) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.entry_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open_in_browser:
                Utils.openWebPage(this, mEntry.getLink());
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
