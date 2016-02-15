package com.example.minitieba.app;

import android.app.Activity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Created by Voyager on 2/3/2016.
 */
public class MenuCreator {
    private MenuCreator() {

    }
    public static void create(Activity activity, Menu menu) {
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) search.getActionView();
        MiniTieba.requireNonNull(searchView, "searchView is null");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                search.collapseActionView();
                BarActivity.start(activity, query); // todo invalid barname
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                search.collapseActionView(); // this don't work
                searchView.setIconified(true);
            }
        });
    }
}
