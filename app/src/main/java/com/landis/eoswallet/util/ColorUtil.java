package com.landis.eoswallet.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.graphics.Palette;
import android.widget.ProgressBar;
import android.widget.TextView;



public class ColorUtil {
    public static Drawable tintDrawableWithColor(Context c, @DrawableRes int drawableRes, @ColorRes int colorRes) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Drawable drawable = ContextCompat.getDrawable(c, drawableRes);
            DrawableCompat.setTint(drawable, ContextCompat.getColor(c, colorRes));
            return drawable;
        } else {
            Drawable drawable = DrawableCompat.wrap(ContextCompat.getDrawable(c, drawableRes));
            DrawableCompat.setTint(drawable, ContextCompat.getColor(c, colorRes));
            return drawable;
        }
    }

    public static Drawable tintDrawableWithColor(Drawable drawable, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Drawable drawable = ContextCompat.getDrawable(c, drawableRes);
            DrawableCompat.setTint(drawable, color);
            return drawable;
        } else {
//            Drawable drawable = DrawableCompat.wrap(ContextCompat.getDrawable(c, drawableRes));
            DrawableCompat.setTint(drawable, color);
            return drawable;
        }
    }

    public static Drawable createDrawableStateList(Context c, @DrawableRes int drawableRes, @ColorRes int colorRes, @ColorRes int changeColorRes) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Drawable drawable = ContextCompat.getDrawable(c, drawableRes);
            DrawableCompat.setTintList(drawable, createColorStateList(c, colorRes, changeColorRes));
            return drawable;
        } else {
            Drawable drawable = DrawableCompat.wrap(ContextCompat.getDrawable(c, drawableRes));
            DrawableCompat.setTintList(drawable, createColorStateList(c, colorRes, changeColorRes));
            return drawable;
        }
    }

    public static ColorStateList createColorStateList(Context c, @ColorRes int colorRes, @ColorRes int pressedColorRes) {
        int color = ContextCompat.getColor(c, colorRes);
        int pressColor = ContextCompat.getColor(c, pressedColorRes);

        int[] colors = new int[]{pressColor, pressColor, color};
        int[][] states = new int[3][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{android.R.attr.state_selected};
        states[2] = new int[]{};
        return new ColorStateList(states, colors);
    }

    public static GradientDrawable getGradientDrawable(Bitmap bitmap) {
        Palette palette = Palette.from(bitmap).generate();
        int colors[] = {palette.getDarkVibrantColor(Color.BLACK), palette.getDarkMutedColor(Color.BLACK)};
        return new GradientDrawable(GradientDrawable.Orientation.BR_TL, colors);
    }

    public static void changeProgress(ProgressBar progressBar, int color, int layerIndex) {
//    //获取层次drawable对象
        LayerDrawable layerDrawable = (LayerDrawable) progressBar.getProgressDrawable();
        //因为画背景图时候第二进度背景图没有画,所以直接为1
        Drawable drawable = layerDrawable.getDrawable(layerIndex);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC);

//        int layers = layerDrawable.getNumberOfLayers();
//        Drawable[] drawables = new Drawable[layers];
    }

    public static int getColor(Context c, @ColorRes int colorRes) {
        return c.getResources().getColor(colorRes);
    }

    public static void changeGradientDrawableStroke(Context c, TextView view, @ColorRes int color) {
        ((GradientDrawable) view.getBackground()).setStroke(ScreenUtils.dpToPx(c, 1), getColor(c, color));
//        press.setColor
//        press.setCornerRadius
//        press.setStroke
    }

    public static void changeGradientDrawableFill(TextView view, long color) {
        ((GradientDrawable) view.getBackground()).setColor((int) color);
//        ((GradientDrawable) view.getBackground()).setColor(Color.RED);
    }

}
