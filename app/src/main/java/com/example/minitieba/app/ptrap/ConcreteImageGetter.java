package com.example.minitieba.app.ptrap;

import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import com.example.minitieba.app.MiniTieba;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by Rye on 4/5/2015.
 */
public class ConcreteImageGetter implements Html.ImageGetter {

    public ConcreteImageGetter() {
        super();
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = bitmap.getWidth() / 2;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    public static Drawable zoomDrawable(Drawable drawable, int w) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap rawBmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float scale = ((float) w / width);

        matrix.postScale(scale, scale);
        Bitmap zoomedBmp = Bitmap.createBitmap(rawBmp, 0, 0, width, height,
                matrix, true);
//        zoomedBmp = getRoundedCornerBitmap(zoomedBmp);
        return new BitmapDrawable(null, zoomedBmp);
    }

    @Override
    @Nullable
    public Drawable getDrawable(@NonNull String source) {
        try {
            return new BitmapDrawable(Picasso.with(MiniTieba.getInstance()).load(source).get());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public enum ImageType {
        // todo 适配屏幕大小
        PHOTO(1000), FACE(200), EMOTION_GIF(100), EMOTION_JPG(60);
        private int zoomedSideLength;

        ImageType(int zoomedSideLength) {
            this.zoomedSideLength = zoomedSideLength;
        }

        public int getZoomedSideLength() {
            return zoomedSideLength;
        }
    }
}
