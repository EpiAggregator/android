package com.epiagregator.screens.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.epiagregator.R;
import com.epiagregator.impls.webapi.WebApiCallListener;
import com.epiagregator.impls.webapi.error.RetrofitException;
import com.epiagregator.impls.webapi.model.Entry;
import com.epiagregator.model.userprofile.UserProfileService;
import com.epiagregator.screens.entry.EntryActivity;
import com.epiagregator.utils.Utils;
import com.jakewharton.rxbinding.view.RxView;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by etien on 29/01/2017.
 */

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.EntryViewHolder> {

    private int lastPosition = -1;

    private Context mContext;
    private List<Entry> mEntries = new ArrayList<>();

    public EntryAdapter(Context context) {
        this.mContext = context;
    }

    public void setItems(List<Entry> entries) {
        mEntries = entries;
        notifyDataSetChanged();
    }

    @Override
    public EntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EntryViewHolder(LayoutInflater.from(mContext).inflate(R.layout.entry_item, null, false));
    }

    @Override
    public int getItemCount() {
        return mEntries.size();
    }

    @Override
    public void onViewDetachedFromWindow(EntryViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(EntryViewHolder holder, int position) {

        final Entry entry = mEntries.get(position);

        holder.headerInfo.setText(Utils.getEntryHeaderInfo(mContext, entry));

        holder.title.setText(entry.getTitle());
        holder.description.setText(Utils.getHtml(entry.getDescription(), true));
        holder.like.setLiked(entry.getFavorite());

        holder.like.setOnLikeListener(new FavoriteListener(entry));

        if (entry.getRead()) {
            holder.title.setAlpha(0.5f);
        } else {
            holder.title.setAlpha(1.0f);
        }

        RxView.clicks(holder.itemView)
                .subscribe(aVoid -> {
                    mContext.startActivity(EntryActivity.newIntent(mContext, entry));

                    if (!entry.getRead()) {
                        // update ui
                        entry.setRead(true);
                        notifyItemChanged(position);
                    }
                });

        // the animation
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.up_from_bottom);
            holder.itemView.startAnimation(animation);
        }

        lastPosition = holder.getAdapterPosition();
    }

    private class FavoriteListener implements OnLikeListener, WebApiCallListener<Entry> {

        private Entry mEntry;

        FavoriteListener(Entry entry) {
            this.mEntry = entry;
        }

        @Override
        public void onResponse(Entry result) {
            if (result.getFavorite()) {
                Toast.makeText(mContext, mContext.getString(R.string.entry_added_to_favorites), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mContext, mContext.getString(R.string.entry_deleted_from_favorites), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onError(RetrofitException exception) {

        }

        @Override
        public void liked(LikeButton likeButton) {
            mEntry.setFavorite(true);
            UserProfileService.patchEntry(mEntry, this);
        }

        @Override
        public void unLiked(LikeButton likeButton) {
            mEntry.setFavorite(false);
            UserProfileService.patchEntry(mEntry, this);
        }
    }

    class EntryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.entry_header_info)
        TextView headerInfo;

        @BindView(R.id.entry_title)
        TextView title;

        @BindView(R.id.entry_description)
        TextView description;

        @BindView(R.id.entry_like_button)
        LikeButton like;

        EntryViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
