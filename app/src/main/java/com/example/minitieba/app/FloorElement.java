package com.example.minitieba.app;

import android.text.Html;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rye on 7/23/2015.
 */
public class FloorElement {
    public static final String PREFIX = "http://tieba.baidu.com/mo/q---C3C08E6B6EAAB401D5486AB75EDF8C8F%3AFG%3D1--1-3-0--2--wapp_1437649607698_391/";
    private final String mRawContent;
    private List<ReplyPiece> mPieces = new ArrayList<>();

    private FloorElement(String content) {
        mRawContent = content;
        Pattern pattern = Pattern.compile("<a href=\"(.*?)\">图</a>");
        Matcher matcher = pattern.matcher(content);
        if (!matcher.find()) {
            mPieces.add(ReplyPiece.newText(content));
            return;
        }
        int lastEnd = 0;
        do {
            Attachment attachment = new Attachment(matcher.group(1).replaceAll("amp;", ""), matcher.start(), matcher.end());
            // if text exists between 2 atts, add a new piece of text
            if (attachment.start - lastEnd > 0) {
                String substring = content.substring(lastEnd, attachment.start);
                mPieces.add(ReplyPiece.newText(substring));
            }
            // update the end att index
            lastEnd = attachment.end;
            // add this att into pieces
            mPieces.add(ReplyPiece.newAttachment(attachment));
        } while (matcher.find());
        // for the end piece of text
        if (content.length() > lastEnd) {
            String substring = content.substring(lastEnd, content.length());
            mPieces.add(ReplyPiece.newText(substring));
        }
    }

    public static FloorElement createFloor(String content) {
        return new FloorElement(content);
    }

    @Override
    public String toString() {
        return mRawContent;
    }

    public List<ReplyPiece> getPieces() {
        return mPieces;
    }

    public enum PieceType {
        IMAGE, TEXT, OTHER
    }

    public static class ReplyPiece {
        public final CharSequence mText;
        public final Attachment mAttachment;
        public final PieceType mType;

        private ReplyPiece(String text) {
            // handle link in text which omits the url prefix
            text = text.replace("<a href=\"", "<a href=\"" + PREFIX);
            // delete emotions and convert some net characters
            mText = Html.fromHtml(text.replaceAll("<img src(.*?)/>", ""));
            mAttachment = null;
            mType = PieceType.TEXT;
        }

        private ReplyPiece(Attachment attachment) {
            mAttachment = attachment;
            mText = null;
            mType = PieceType.IMAGE;
        }

        public static ReplyPiece newText(String text) {
            return new ReplyPiece(text);
        }

        public static ReplyPiece newAttachment(Attachment attachment) {
            return new ReplyPiece(attachment);
        }
    }

    public class Attachment {
        public final String url;
        public final int start;
        public final int end;

        public Attachment(String url, int start, int end) {
            this.url = url;
            this.start = start;
            this.end = end;
        }
    }
}
