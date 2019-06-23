package com.yanhe.recruit.tv.manage;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.contrarywind.view.WheelView;
import com.mx.mnyun.R;
import com.mx.mnyun.utils.AreaUtils;
import com.mx.mnyun.utils.Mount;
import com.mx.mnyun.utils.ScreenUtils;
import com.mx.mnyun.widget.BaseDialog;
import com.mx.mnyun.widget.Loading;
import com.mx.mnyun.widget.QuantityDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UIManager {
    public static  ScreenUtils mScreen         = null;
    private static Loading loadingInstance = null;

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

    public static void showQuantityDialog(QuantityDialog.DialogListener listener) {
        QuantityDialog dialog = new QuantityDialog(ActivityManager.getInstance().getTopContext());
        dialog.setListener(listener);
        dialog.show();
    }

    /** 年月日部份 */
    public final static boolean[] DATEPICKER_DATE_PART = new boolean[] { true, true, true, false, false, false };
    /** 小时分秒部份 */
    public final static boolean[] DATEPICKER_TIME_PART = new boolean[] { false, false, false, true, true, true };

    public static void showDatePicker(boolean[] parts, @Nullable Date value, @Nullable Date minDate, @Nullable Date maxDate, final OnDatePickerListener listener) {
        Context context = ActivityManager.getInstance().getTopContext();
        Resources res = context.getResources();
        Calendar curValue = new Mount(value == null ? new Date() : value).getCalendar();
        Calendar mMinValue =  minDate == null ? new Mount(2019, 1, 1).getCalendar() : new Mount(minDate).getCalendar();
        Calendar mMaxValue = maxDate == null ? new Mount(2030, 12, 1).getCalendar() : new Mount(maxDate).getCalendar();

        final TimePickerView pvTime = new TimePickerBuilder(ActivityManager.getInstance().getTopContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //选中事件回调
                if (listener != null) {
                    listener.onDateSelect(date);
                }
            }})
                /** 默认全部显示 */
                .setType(new boolean[]{true, true, true, false, false, false})
                //取消按钮文字
                .setCancelText(context.getString(R.string.title_cancel))
                //确认按钮文字
                .setSubmitText(context.getString(R.string.title_ok))
                .setTitleText("日期选择")
                //点击屏幕，点在控件外部范围时，是否取消显示
                .setOutSideCancelable(false)
                //是否循环滚动
                .isCyclic(true)
                //标题文字颜色
                .setTitleColor(res.getColor(R.color.colorPrimary, null))
                //确定按钮文字颜色
                .setSubmitColor(res.getColor(R.color.colorPrimary, null))
                // 取消按钮文字颜色
                .setCancelColor(res.getColor(R.color.contentColor, null))
                // 标题背景颜色 Night mode
                .setTitleBgColor(res.getColor(R.color.white, null))
                // 滚轮背景颜色 Night mode
                .setBgColor(res.getColor(R.color.whiteLight, null))
                // 如果不设置的话，默认是系统时间*/
                .setTextColorCenter(res.getColor(R.color.colorPrimary, null))
                .setDate(curValue)
                //起始终止年月日设定
                .setRangDate(mMinValue, mMaxValue)
                //默认设置为年月日时分秒
                .setLabel("年","月","日","时","分","秒")
                //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isCenterLabel(true)
                // 是否显示为对话框样式
                .isDialog(false)
                .setDividerColor(res.getColor(R.color.colorPrimary, null))
                .setDividerType(WheelView.DividerType.WRAP)
                .build();
        pvTime.show();
    }
    /**地区选择*/
    public static void showAreaPicker(int[] area,final OnAreaSelectListener listener){
        if(area ==null || area.length != 3){
            area = new int []{
                    0,0,0
            };
        }
        Context                  context          = ActivityManager.getInstance().getTopContext();
        ArrayList<String> provinceBeanList = new ArrayList<>();
        /**城市*/
        ArrayList<List<String>> cityList = new ArrayList<>();
        /** 区/县*/
        ArrayList<List<List<String>>> districtList = new ArrayList<>();
        Resources                            res          = context.getResources();
        /**
         * 获取assets目录下的json文件数据
         */
        String JsonData = AreaUtils.getJson(context, "province.json");
        AreaUtils.parseJson(JsonData, provinceBeanList, cityList, districtList  );
        final OptionsPickerView pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                //选中事件回调
                if (listener != null) {
                    listener.onAreaSelect(options1,option2,options3);
                }
            }
        })
                //确定按钮文字
                .setSubmitText("确定")
                //取消按钮文字
                .setCancelText("取消")
                //标题
                .setTitleText("城市选择")
                //确定和取消文字大小
                .setSubCalSize(18)
                //标题文字大小
                .setTitleSize(20)
                //标题文字颜色
                .setTitleColor(res.getColor(R.color.colorPrimary, null))
                //确定按钮文字颜色
                .setSubmitColor(res.getColor(R.color.colorPrimary, null))
                //取消按钮文字颜色
                .setCancelColor(res.getColor(R.color.contentColor, null))
                //标题背景颜色 Night mode
                .setTitleBgColor(Color.WHITE)
                //滚轮文字大小
                .setContentTextSize(18)
                //滚轮背景颜色 Night mode
                .setBgColor(Color.WHITE)
                .setTextColorCenter(res.getColor(R.color.colorPrimary, null))
                //设置是否联动，默认true
//                .setLinkage(false)
                //设置选择的三级单位
//                .setLabels("省", "市", "区")
                //循环与否
                .setCyclic(false, false, false)
                //设置默认选中项
                .setSelectOptions(area[0], area[1], area[2])
                //点击外部dismiss default true
                .setOutSideCancelable(false)
                //是否显示为对话框样式
                .isDialog(false)
                .build();
        //添加数据源
        pvOptions.setPicker(provinceBeanList, cityList, districtList);
        pvOptions.show();
    }

    public static Loading showLoading (String msg) {
        if (loadingInstance == null) {
            loadingInstance = new Loading(ActivityManager.getInstance().getTopContext());
        }
        loadingInstance.show();
        return loadingInstance;
    }

    public static void hiddenLoading () {
        if (loadingInstance != null) {
            loadingInstance.hidden();
        }
    }

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
