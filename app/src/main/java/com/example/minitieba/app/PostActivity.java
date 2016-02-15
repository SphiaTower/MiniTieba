package com.example.minitieba.app;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import com.example.minitieba.app.ptrap.LiteRefreshableAutoPagerActivity;

/**
 * Created by Rye on 7/23/2015.
 */
public class PostActivity extends LiteRefreshableAutoPagerActivity {
    public static final String ARG_URL = "url";
    public static final String ARG_TITLE = "title";
    public String mUrl;
    public String mTitle;

    @Override
    protected String getCustomTitle() {
        return mTitle;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(ARG_URL, mUrl);
        outState.putString(ARG_TITLE, mTitle);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Uri data = getIntent().getData();
            if (data != null) {
                mUrl = data.toString();
                mTitle = "展开全文";
            } else {
                mUrl = getIntent().getExtras().getString(ARG_URL);
                mTitle = getIntent().getExtras().getString(ARG_TITLE);
            }
        } else {
            mUrl = savedInstanceState.getString(ARG_URL);
            mTitle = savedInstanceState.getString(ARG_TITLE);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment initFragment() {
        return PostFragment.newInstance(mUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
