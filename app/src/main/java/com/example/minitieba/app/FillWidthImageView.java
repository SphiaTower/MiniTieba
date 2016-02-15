package com.example.minitieba.app;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Voyager on 8/22/2015.
 */
public class FillWidthImageView extends ImageView {

    public FillWidthImageView(Context context) {
        super(context);
    }

    public FillWidthImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FillWidthImageView(Context context, AttributeSet attrs,
                              int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int diw = drawable.getIntrinsicWidth();
            if (diw > 0) {
                int height = width * drawable.getIntrinsicHeight() / diw;
                setMeasuredDimension(width, height);
            } else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}