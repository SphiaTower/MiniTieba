package com.example.minitieba.app;

import android.support.annotation.NonNull;

import java.net.URLEncoder;

/**
 * Created by Voyager on 2/3/2016.
 */
public class Bar {
    public static final Bar JAY = Bar.ofName("jay");
    public static final Bar BILLBOARD = Bar.ofName("billboard");
    public static final Bar ANDROID = Bar.ofName("android");
    public static final Bar[] barSet = new Bar[]{
            ANDROID, JAY, BILLBOARD
    };
    private static final String PREFIX = "http://tieba.baidu.com/mo/q---24B0552272F2BE444B55ED0ECA6199B3%3AFG%3D1--1-3-0--2--wapp_1437754104714_813/m?kw=";
    private static final String SUFFIX = "&lm=&lp=5003";
    private String barName;
    private String url;

    private Bar(@NonNull String barName) {
        this.barName = barName;
        this.url = PREFIX + Bar.toKeyword(this.barName) + SUFFIX;
    }

    public static String toKeyword(@NonNull String barName) {
        boolean isAlphabetic = barName.matches("\\w+");
        if (isAlphabetic) {
            return barName;
        } else {
            return URLEncoder.encode(barName);
        }
    }

    public static Bar ofName(@NonNull String name) {
        return new Bar(name);
    }

    public String getBarName() {
        return barName;
    }

    public String getUrl() {
        return url;
    }
}
