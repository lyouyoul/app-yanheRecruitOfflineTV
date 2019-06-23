package com.yanhe.recruit.tv.utils;

import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

public class StringUtils {
    /**
     * 字符串是否为空检查
     * @param value
     * @return
     */
    public static Boolean isEmpty(String value) {
        return value == null || "".equals(value);
    }
    /**
     * 数组转字符串
     * @param delimiter
     * @param args
     * @return
     */
    public static String join(String delimiter, @Nullable Object... args) {
        if (args == null) {
            return "";
        } else {
            String s = "";
            for (Object arg : args) {
                if (!isEmpty(s)) s += delimiter;
                s += arg;
            }
            return s;
        }
    }

    public static void showOrHideContent(final TextView content, final TextView btn, final int maxLines, String btntext){
        Layout layout = content.getLayout();
        if (layout != null) {
            int lineCount = layout.getLineCount();
            if (lineCount > maxLines) {
                btn.setText(btntext);
                btn.setVisibility(View.VISIBLE);
            } else {
                /**小米note2下返回0*/
                int ellipsisCount = layout.getEllipsisCount(lineCount - 1);
                if (ellipsisCount > 0) {
                    btn.setVisibility(View.VISIBLE);
                } else {
                    btn.setVisibility(View.GONE);
                }
            }
        } else {
            ViewTreeObserver observer = content.getViewTreeObserver();
            if (observer.isAlive()) {
                observer.addOnPreDrawListener(
                        new ViewTreeObserver.OnPreDrawListener() {
                            @Override
                            public boolean onPreDraw() {
                                Layout layout = content.getLayout();
                                if (layout != null) {
                                    int lineCount = layout.getLineCount();
                                    /**小米note2的兼容处理*/
                                    if (lineCount > maxLines) {
                                        btn.setVisibility(View.VISIBLE);
                                    } else {
                                        /**小米note2下返回0*/
                                        int ellipsisCount = layout.getEllipsisCount(lineCount - 1);
                                        if (ellipsisCount > 0) {
                                            btn.setVisibility(View.VISIBLE);
                                        } else {
                                            btn.setVisibility(View.GONE);
                                        }
                                    }
                                }
                                try {
                                    (content.getViewTreeObserver()).removeOnPreDrawListener(this);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return true;
                            }
                        }
                );
            }
        }
    }
}
