package com.landis.eoswallet.util;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.landis.eoswallet.R;

public class WebUtils {
    // a链接处理
    @Deprecated
    public static boolean openApp(Context c, String appUrl, String toast) {
        if (TextUtils.isEmpty(appUrl)) {
            return false;
        }
        try {
            c.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(appUrl)));
            return true;
        } catch (ActivityNotFoundException e) {
            ToastUtils.showShort(toast);
            return false;
        }
    }

    public static void copy(Context c, String copy) {
        ClipboardManager clipboard = (ClipboardManager) c.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("card", copy);
        clipboard.setPrimaryClip(clip);
        ToastUtils.showShort(c.getResources().getString(R.string.copy_success));
    }
}
