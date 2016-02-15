package com.example.minitieba.app;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

/**
 * Created by Voyager on 8/19/2015.
 */
public class ReplyView extends LinearLayout {

    public ReplyView(Context context) {
        super(context);
    }

    public ReplyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReplyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void addTextView() {
        TextView textView = new AppCompatTextView(getContext());
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setLinksClickable(true);
        textView.setTextAppearance(getContext(), android.R.style.TextAppearance_Medium);
        textView.setLineSpacing(0, 1.4f);
        addView(textView);
    }

    public void initView(FloorElement data) {
        for (FloorElement.ReplyPiece dataPiece : data.getPieces()) {
            switch (dataPiece.mType) {
                case IMAGE:
                    ImageView imageView = new FillWidthImageView(getContext());
                    imageView.setPadding(0, 0, 0, 10);
                    addView(imageView);
                    break;
                case TEXT:
                    addTextView();
                    break;
                case OTHER:
                    break;
            }
        }
    }

    public void setData(FloorElement data) {
        int i = 0;
        for (FloorElement.ReplyPiece dataPiece : data.getPieces()) {
            switch (dataPiece.mType) {
                case IMAGE:
                    final ImageView imageView = (ImageView) getChildAt(i);
                    final String url = dataPiece.mAttachment.url;
                    Picasso.with(getContext())
                            .load(url)
                            .placeholder(R.drawable.ic_placeholder)
                            .error(R.drawable.ic_launcher)
                            .into(imageView);
                    imageView.setOnClickListener(v -> ImageActivity.start(getContext(),url));
                    break;
                case TEXT:
                    TextView textView = (TextView) getChildAt(i);
                    textView.setText(dataPiece.mText);
                    break;
                case OTHER:
                    break;
            }
            i++;
        }
    }
}
