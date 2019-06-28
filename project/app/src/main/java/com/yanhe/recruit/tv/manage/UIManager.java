package com.yanhe.recruit.tv.manage;

import android.widget.Toast;

import com.yanhe.recruit.tv.utils.ScreenUtils;
import com.yanhe.recruit.tv.widget.BaseDialog;

import java.util.Date;

public class UIManager {
    public static ScreenUtils mScreen         = null;

    public static ScreenUtils getScreen() {
        if (mScreen == null) {
            mScreen = new ScreenUtils(ActivityManager.getInstance().getTopContext());
        }
        return mScreen;
    }

    /**
     * 显示确认对话框
     * @param title
     * @param msg
     * @param listener
     */
    public static void showConfirm(String title, String msg, final OnDialogListener listener) {
        final BaseDialog dialog = new BaseDialog.Builder(ActivityManager.getInstance().getTopContext())
                .setTitle(title)
                .setMessage(msg)
                .setLeftClick("确定",
                              new BaseDialog.OnLeftClickListener() {
                                  @Override
                                  public void onClick(BaseDialog dialog) {
                                      if (listener != null) {
                                          listener.onButtonClick(dialog, true, "ok");
                                      }
                                  }
                              })
                .setRightClick("取消",
                               new BaseDialog.OnRightClickListener() {
                                   @Override
                                   public void onClick(BaseDialog dialog) {
                                       if (listener != null) {
                                           listener.onButtonClick(dialog, false, "cancel");
                                       }
                                   }
                               })
                .create();
        dialog.show();
    }



    /**
     * 显示消息对话框
     * @param title
     * @param msg
     * @param listener
     */
    public static void showMessage(String title, String msg, final OnDialogListener listener) {
        new BaseDialog.Builder(ActivityManager.getInstance().getTopContext())
                .setTitle(title)
                .setMessage(msg)
                .setLeftClick("确定",
                              new BaseDialog.OnLeftClickListener() {
                                  @Override
                                  public void onClick(BaseDialog dialog) {
                                      if (listener != null) {
                                          listener.onButtonClick(dialog, true, "ok");
                                      }
                                      dialog.dismiss();
                                  }
                              })
                .create()
                .show();
    }

    /**
     * 显示轻消息
     * @param value
     */
    public static void showToast(String value) {
        Toast.makeText(ActivityManager.getInstance().getTopActivity(), value, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示
     * @param value
     */
    public static void showToast(String value, int timeOut) {
        Toast.makeText(ActivityManager.getInstance().getTopActivity(), value, timeOut).show();
    }


    /** 年月日部份 */
    public final static boolean[] DATEPICKER_DATE_PART = new boolean[] { true, true, true, false, false, false };
    /** 小时分秒部份 */
    public final static boolean[] DATEPICKER_TIME_PART = new boolean[] { false, false, false, true, true, true };

    /**
     * 对话框回调接口
     */
    public interface OnDialogListener {
        /**
         * 对话框按扭点击回调事件
         * @param dialog 对话框实例
         * @param confirm 是否点击确认按扭，点取取消或对话框外时为false
         * @param buttonID 按扭id, 确定为ok,取消为cancel
         */
        void onButtonClick(BaseDialog dialog, boolean confirm, String buttonID);
    }

    /**
     * 时间选择对话框回调事件接口
     */
    public interface OnDatePickerListener {
        /**
         * 选择时间时回调
         * @param date 日期
         */
        void onDateSelect(Date date);
    }

    /**
     * 地区选择对话框回调事件接口
     */
    public interface OnAreaSelectListener {
        /**
         * 选择地区时回调
         * @param options1
         * @param option2
         * @param options3
         * @param v
         */
        void onAreaSelect(int options1, int option2, int options3);
    }

}
