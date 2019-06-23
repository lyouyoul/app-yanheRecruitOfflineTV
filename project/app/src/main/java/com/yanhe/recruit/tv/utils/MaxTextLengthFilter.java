package com.yanhe.recruit.tv.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Toast;

import com.mx.mnyun.manage.UIManager;

/**
 * 最大字数限制
 */
public class MaxTextLengthFilter implements InputFilter {

    private int mMaxLength;
    /**构造方法中传入最多能输入的字数*/
    public MaxTextLengthFilter(int max) {
        mMaxLength = max;
    }

    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        int keep = mMaxLength - (dest.length() - (dend - dstart));
        if (keep < (end - start)) {
            UIManager.showToast("最多只能输入" + mMaxLength + "个字", Toast.LENGTH_SHORT);
        }
        if (keep <= 0) {
            return "";
        } else if (keep >= end - start) {
            return null;
        } else {
            return source.subSequence(start, start + keep);
        }
    }
}
