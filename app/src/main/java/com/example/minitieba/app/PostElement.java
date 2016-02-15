package com.example.minitieba.app;

import android.text.Html;
import android.text.Spanned;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rye on 7/23/2015.
 */
public class PostElement {
    private final Spanned mTitle;
    private final String mUrl;
    private JSONObject jsonObject;

    private PostElement(String json) {
        try {
            jsonObject = new JSONObject(json);
            String title = jsonObject.getString("title");
            String url = jsonObject.getString("url");
            this.mTitle = Html.fromHtml(title);
            this.mUrl = url;
        } catch (JSONException e) {
            throw new RuntimeException("json");
        }
    }

    public static PostElement getInstance(String json) {
        return new PostElement(json);
    }

    @Override
    public String toString() {
        return jsonObject.toString();
    }

    public Spanned getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }
}
