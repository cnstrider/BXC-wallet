package com.landis.eoswallet.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class CopyUtils {

    public static void copy(Context context, String label, String value) {
        ClipboardManager cbm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData txtCd = ClipData.newPlainText(label, value);
        cbm.setPrimaryClip(txtCd);
    }
}
