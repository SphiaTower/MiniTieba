package com.example.minitieba.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import tower.sphia.auto_pager_recycler.lib.AutoPagerAdapter;
import tower.sphia.auto_pager_recycler.lib.AutoPagerLoader;
import tower.sphia.auto_pager_recycler.lib.AutoPagerRefreshableFragment;
import tower.sphia.auto_pager_recycler.lib.DataNotLoadedException;

import java.io.IOException;
import java.util.TreeMap;

/**
 * Created by Voyager on 8/11/2015.
 */
public class BarFragment extends AutoPagerRefreshableFragment<PostGroup, PostElement> {


    private static final String TAG = "BarFragment";
    public static final String ARG_URL = "url";

    public static BarFragment newInstance(Bar bar) {
        final BarFragment fragment = new BarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, bar.getUrl());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Loader<TreeMap<Integer, PostGroup>> onCreateLoader(int id, Bundle args) {
        return new AutoPagerLoader<PostGroup>(getActivity()) {
            @Override
            @NonNull
            protected PostGroup newPage(int index) throws DataNotLoadedException {
                if (MiniTieba.DEBUG) Log.d(TAG, "newPage() called with " + "index = [" + index + "]");
                String baseUrl = getArguments().getString("url");
                MiniTieba.requireNonNull(baseUrl, "no url specified for the BarFragment");
                String url = baseUrl + "&pn=" + (index - 1) * 10;
                try {
                    String content = MiniTieba.getInstance().get(url);
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("content", content);
                        jsonObject.put("page", index);
                        return new PostGroup(jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                throw new DataNotLoadedException();
            }
        };
    }

    @Override
    protected AutoPagerAdapter<PostGroup, PostElement> onCreateAdapter() {
        return new BoardAdapter();
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ItemViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.text);
        }
    }

    private class BoardAdapter extends AutoPagerAdapter<PostGroup, PostElement> {


        @Override
        protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_item, viewGroup, false);
            view.setOnClickListener(v -> {
                PostElement postElement = getAutoPagerManager().findItemByView(v);
                String url = postElement.getUrl();
                Intent intent = new Intent();
                intent.setClass(getActivity(), PostActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(PostActivity.ARG_URL, url);
                bundle.putString(PostActivity.ARG_TITLE, postElement.getTitle().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            });
            return new ItemViewHolder(view);
        }

        @Override
        protected void onBindItemViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            ((ItemViewHolder) viewHolder).title.setText(getItem(i).getTitle());
        }
    }
}
