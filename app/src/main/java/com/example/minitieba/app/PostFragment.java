package com.example.minitieba.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import tower.sphia.auto_pager_recycler.lib.AutoPagerAdapter;
import tower.sphia.auto_pager_recycler.lib.AutoPagerLoader;
import tower.sphia.auto_pager_recycler.lib.AutoPagerRefreshableFragment;
import tower.sphia.auto_pager_recycler.lib.DataNotLoadedException;

import java.io.IOException;
import java.util.TreeMap;

/**
 * Created by Rye on 7/23/2015.
 */
public class PostFragment extends AutoPagerRefreshableFragment<FloorGroup, FloorElement> {

    public static PostFragment newInstance(String url) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Loader<TreeMap<Integer, FloorGroup>> onCreateLoader(int id, Bundle args) {
        return new AutoPagerLoader<FloorGroup>(getActivity()) {
            @Override
            @NonNull
            protected FloorGroup newPage(int index) throws DataNotLoadedException {
                try {
                    return new FloorGroup(MiniTieba.getInstance().get(getArguments().getString("url") + "&pn=" + --index * 10));
                } catch (IOException e) {
                    throw new DataNotLoadedException();
                }
            }
        };
    }

    @Override
    protected AutoPagerAdapter<FloorGroup, FloorElement> onCreateAdapter() {
        return new PostAdapter();
    }


    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        ReplyView content;

        public ItemViewHolder(View itemView) {
            super(itemView);
            content = (ReplyView) itemView.findViewById(R.id.text);
        }
    }

    private class PostAdapter extends AutoPagerAdapter<FloorGroup, FloorElement> {

        @Override
        protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_not_clickable, viewGroup, false);

            ItemViewHolder holder = new ItemViewHolder(view);
            if (viewType > 0) {
                holder.content.addTextView();
            } else {
                holder.content.initView(getItem(-viewType));

            }
            return holder;
        }

        @Override
        public int getItemViewType(int position) {
            int itemViewType = super.getItemViewType(position);
            if (itemViewType != AutoPagerAdapter.ITEM) {
                return itemViewType;
            } else {
                FloorElement item = getItem(position);
                int size = item.getPieces().size();
                if (size == 1) {
                    return AutoPagerAdapter.ITEM;
                } else {
                    return -position;
                }
            }
        }

        @Override
        protected void onBindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            ((ItemViewHolder) viewHolder).content.setData(getItem(position));
        }
    }
}
