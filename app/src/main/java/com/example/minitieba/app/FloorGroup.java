package com.example.minitieba.app;

import tower.sphia.auto_pager_recycler.lib.Page;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rye on 7/23/2015.
 */
public class FloorGroup implements Page<FloorElement> {
    private List<FloorElement> mFloors;
    private int mPageCurrentCount;
    private int mPageAllCount;
    private String mHtml;

    public FloorGroup(String html) {
        mHtml = html;
        parsePages(html);
        parseElements(html);
    }

    @Override
    public String toString() {
        return mHtml;
    }

    public void parseElements(String html) {
        mFloors = new ArrayList<>();
        Pattern pattern = Pattern.compile("<div class=\"i\">(.*?)</div>", Pattern.MULTILINE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            String group = matcher.group(1).replaceAll("<span class=\"g\">(.*?)</span>", "");
            mFloors.add(FloorElement.createFloor(group));
        }
    }

    private void parsePages(String html) {
        Pattern pattern = Pattern.compile("第([0-9]*?)/([0-9]*?)页<input");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            mPageCurrentCount = Integer.valueOf(matcher.group(1));
            mPageAllCount = Integer.valueOf(matcher.group(2));
        } else {
            mPageCurrentCount = 1;
            mPageAllCount = 1;
        }
    }


    @Override
    public int index() {
        return mPageCurrentCount;
    }

    @Override
    public int last() {
        return mPageAllCount;
    }

    @Override
    public Iterator<FloorElement> iterator() {
        return mFloors.iterator();
    }

}
