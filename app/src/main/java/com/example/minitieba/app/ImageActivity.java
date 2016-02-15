package com.example.minitieba.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import com.example.minitieba.app.ptrap.ConcreteImageGetter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by Voyager on 9/11/2015.
 */
public class ImageActivity extends AppCompatActivity {

    private static final String KEY_BITMAP = "bitmap";

    public static void start(Context from, String url) {
        Intent intent = new Intent(from, ImageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BITMAP, url);
        intent.putExtras(bundle);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        String parcelable = getIntent().getExtras().getString(KEY_BITMAP);
        final ImageView imageView = (ImageView) findViewById(R.id.image);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        //We get width and height in pixels here
        final int width = metrics.widthPixels;
        Picasso.with(this).load(parcelable).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                Drawable drawable = imageView.getDrawable();
                drawable = ConcreteImageGetter.zoomDrawable(drawable, width);
                imageView.setImageDrawable(drawable);
                //  new PhotoViewAttacher(imageView);
            }

            @Override
            public void onError() {

            }
        });
    }
}
