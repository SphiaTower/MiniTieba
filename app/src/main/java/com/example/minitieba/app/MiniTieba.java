package com.example.minitieba.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import tower.sphia.auto_pager_recycler.lib.AutoPagerAdapter;
import tower.sphia.auto_pager_recycler.lib.AutoPagerManager;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Rye on 7/23/2015.
 */
public class MiniTieba extends Application {
    public static final int READ_TIMEOUT_MILLIS = 10000;
    public static final int CONNECT_TIMEOUT_MILLIS = 15000;
    private static final String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0)";
    private static final String TAG = "MyApp";
    public static boolean DEBUG = false;
    private static MiniTieba singleton;

    public static MiniTieba getInstance() {
        return singleton;
    }

    public static <T> T requireNonNull(T o, String message) {
        if (o == null) {
            throw new NullPointerException(message);
        }
        return o;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
//        AutoPagerManager.enableDebug();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        enactPreferences(sharedPref);
    }

    public void enactPreferences(SharedPreferences sharedPref) {
        boolean debugPager = sharedPref.getBoolean(getString(R.string.pref_key_auto_pager), false);
        boolean debugAdapter = sharedPref.getBoolean(getString(R.string.pref_key_auto_adapter), false);
        boolean debugSeine = sharedPref.getBoolean(getString(R.string.pref_key_minitieba), false);
        int pagerZone = Integer.parseInt(sharedPref.getString(getString(R.string.pref_key_pager_zone), "3"));
        AutoPagerAdapter.DEBUG = debugAdapter;
        AutoPagerManager.enalbleDebug(debugPager);
        AutoPagerManager.setAutoPagerZoneSize(pagerZone);
        DEBUG = debugSeine;
    }

    public String get(String url) throws IOException {
        if (DEBUG) Log.d(TAG, "get() called with " + "url = [" + url + "]");

        URL urlObj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
        try {
            conn.setReadTimeout(READ_TIMEOUT_MILLIS /* milliseconds */);
            conn.setConnectTimeout(CONNECT_TIMEOUT_MILLIS /* milliseconds */);
            conn.setDoInput(true);
            // Enable cache
            conn.setUseCaches(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "*/*");
            // add reuqest header
            conn.setRequestMethod("GET");
//        int responseCode = conn.getResponseCode();
            InputStream inputStream = new BufferedInputStream(conn.getInputStream());
            // todo potential bug of NPE
            return readStream(inputStream);
        } finally {
            conn.disconnect();
        }
    }

    private String readStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

}
