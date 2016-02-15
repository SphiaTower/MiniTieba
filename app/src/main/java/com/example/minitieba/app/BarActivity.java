package com.example.minitieba.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

/**
 * Created by Voyager on 2/3/2016.
 */
public class BarActivity extends AppCompatActivity {

    public static final String KEY_BAR_NAME = "barName";
    private static final String TAG = "BarActivity";

    public static void start(Context context, @NonNull String barName) {
        Intent starter = new Intent(context, BarActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BAR_NAME, barName);
        starter.putExtras(bundle);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MiniTieba.DEBUG) Log.d(TAG, "onCreate() called with " + "savedInstanceState = [" + savedInstanceState + "]");
        String barName;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            MiniTieba.requireNonNull(extras, "no bundle available");
            barName = extras.getString(KEY_BAR_NAME);
            MiniTieba.requireNonNull(barName, "null barName in bundle");
        } else {
            barName = savedInstanceState.getString(KEY_BAR_NAME);
            MiniTieba.requireNonNull(barName, "null barName in savedInstanceState");
        }
        setContentView(R.layout.activity_bar);
        setSupportActionBar(((Toolbar) findViewById(R.id.mainToolbar)));
        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentById(R.id.bar_linear_layout) == null) {
            Bar bar = Bar.ofName(barName);
            BarFragment fragment = BarFragment.newInstance(bar);
            manager.beginTransaction().add(R.id.bar_linear_layout, fragment).commit();
        } else {
            Toast.makeText(this, "fragment already exists", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuCreator.create(this, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
