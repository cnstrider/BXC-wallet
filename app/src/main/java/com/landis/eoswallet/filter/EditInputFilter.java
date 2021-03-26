package com.landis.eoswallet.filter;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 过滤
 */
public class EditInputFilter  implements InputFilter {

    /**
     * 最大数字
     */
    public static final int MAX_VALUE = 500;

    /**
     * 小数点后的数字的位数
     */
    public static final int POINTER_LENGTH = 2;

    private static final String POINTER = ".";

    Pattern p;

    public EditInputFilter() {
        //用于匹配输入的是0-9  .  这几个数字和字符
        p = Pattern.compile("([0-9])*");
    }

    /**
     * source    新输入的字符串
     * start    新输入的字符串起始下标，一般为0
     * end    新输入的字符串终点下标，一般为source长度-1
     * dest    输入之前文本框内容
     * dstart    原内容起始坐标，一般为0
     * dend    原内容终点坐标，一般为dest长度-1
     */

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {

        String sourceText = source.toString();
//        String destText = dest.toString();
        Matcher matcher = p.matcher(source);
        if (!matcher.matches()) {
            return "";
        }
        if (dest.length() > 5) {
            return "";
        }
        return source.toString();
    }
}
