package com.example.minitieba.app;

import org.json.JSONException;
import org.json.JSONObject;
import tower.sphia.auto_pager_recycler.lib.Page;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rye on 7/23/2015.
 */
public class PostGroup implements Page<PostElement> {

    private int mPage;
    private List<PostElement> mPosts;
    private JSONObject jsonObject;

    public PostGroup(String json) throws JSONException {
        jsonObject = new JSONObject(json);
        String content = jsonObject.getString("content");
        mPage = jsonObject.getInt("page");
        parse(content);
    }

    /* todo contract */
    @Override
    public String toString() {
        return jsonObject.toString();
    }

    public void parse(String html) {
        mPosts = new ArrayList<>();
        Pattern pattern = Pattern.compile("<div class=\"i(.*?)\"><a href=\"(.*?)\">(\\d)\\.&#160;(.*?)</a>");
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            String title = matcher.group(4);
            String url = "http://tieba.baidu.com/mo/q---C3C08E6B6EAAB401D5486AB75EDF8C8F%3AFG%3D1--1-3-0--2--wapp_1437649607698_391/" + matcher.group(2);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("title", title).put("url", url);
            } catch (JSONException e) {
                throw new RuntimeException("json");
            }
            PostElement postElement = PostElement.getInstance(jsonObject.toString());
            mPosts.add(postElement);
        }
    }


    @Override
    public int index() {
        return mPage;
    }

    @Override
    public int last() {
        return 1000;
    }

    @Override
    public Iterator<PostElement> iterator() {
        return mPosts.iterator();
    }

}
