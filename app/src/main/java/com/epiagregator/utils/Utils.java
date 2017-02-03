package com.epiagregator.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Browser;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateUtils;

import com.epiagregator.R;
import com.epiagregator.impls.webapi.model.Entry;

/**
 * Created by etien on 01/02/2017.
 */

public class Utils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if ((activeNetworkInfo != null) && (activeNetworkInfo.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    public static void openWebPage(Context context, String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        i.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
        context.startActivity(i);
    }

    public static CharSequence getEntryHeaderInfo(Context context, Entry entry) {
        CharSequence ago =
                DateUtils.getRelativeTimeSpanString(entry.getPubDate().getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
        if (entry.getAuthor() != null && entry.getAuthor().length() != 0) {
            return context.getString(R.string.entry_header_info_author_date, entry.getAuthor(), ago);
        } else {
            return ago;
        }
    }

    @SuppressWarnings("deprecation")
    public static Spanned getHtml(String html, boolean compact) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, compact ? Html.FROM_HTML_MODE_COMPACT : Html.FROM_HTML_SEPARATOR_LINE_BREAK_DIV);
        } else {
            return  Html.fromHtml(html);
        }
    }
}
