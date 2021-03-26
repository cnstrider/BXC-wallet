package com.landis.eoswallet.util;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.widget.TextView;

import com.yatoooon.screenadaptation.ActualScreen;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.lang.reflect.Field;

public class ViewUtils {

    private static int statusBarHeight;
    private static boolean isStatusBarHeightParsed;
    private static float density;

    public static int getStatusBarHeight(Context context) {
        if (isStatusBarHeightParsed) {
            return statusBarHeight;
        }
        statusBarHeight = (int) (25 * context.getResources().getDisplayMetrics().density);
        try {
            Class dimenClass = Class.forName("com.android.internal.R$dimen");
            Field field = dimenClass.getField("status_bar_height");
            field.setAccessible(true);
            Object object = dimenClass.newInstance();
            statusBarHeight = context.getResources().getDimensionPixelSize(field.getInt(object));
            isStatusBarHeightParsed = true;
        } catch (Exception e) {
            e.printStackTrace();
            isStatusBarHeightParsed = false;
        }
        return statusBarHeight;
    }

    public static float caculateTextWidth(Context context, String text, Paint paint, int paddingDp) {
        //多字，宽度=文字宽度+padding
        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);
        return ScreenAdapterTools.getInstance().loadCustomAttrValue(textRect.width() + (int)((paddingDp + ActualScreen.screenInfo(context)[2]) * 2));
    }

    public static float caculateTextWidth(Context context, String text, int size, int paddingDp) {
        //多字，宽度=文字宽度+padding
        Rect textRect = new Rect();
        TextPaint paint = new TextPaint();
        paint.setTextSize(size);
        paint.getTextBounds(text, 0, text.length(), textRect);
        return textRect.width() + (paddingDp + ActualScreen.screenInfo(context)[2]) * 2;
    }

    public static int getTextLines(String text, TextView view, int width) {
        TextPaint paint = view.getPaint();
        String[] texts = null;
        int lines = 0;
        if (text.contains("\n")) {
            texts = text.split("\n");
        }
        if (texts != null) {
            for (int i = 0; i < texts.length; i++) {
                float w = paint.measureText(texts[i]);
                int l = (int) (w / width) + 1;
                lines = lines + l;
            }
        } else {
            float w = paint.measureText(text);
            lines = (int) (w / width) + 1;
        }
        return lines;
    }
}
