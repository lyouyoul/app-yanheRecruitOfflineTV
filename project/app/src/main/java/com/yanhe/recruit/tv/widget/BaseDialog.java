package com.yanhe.recruit.tv.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yanhe.recruit.tv.R;

/**
 * 全局样式对话框
 */

public class BaseDialog extends Dialog {
    private TextView tvTitle;
    private TextView tvMsg;
    private TextView tvLeft;
    private TextView tvRight;
    private View vwButtonDivider;
    private OnLeftClickListener onDefaultClickListener = new OnLeftClickListener() {
        @Override
        public void onClick(BaseDialog dialog) {
            cancel();
        }
    };
    private OnLeftClickListener onLeftListener = onDefaultClickListener;
    private OnRightClickListener onRightListener = null;
    private BaseDialog.OnCancelListener onCancelListener = null;
    private Dialog.OnCancelListener mOnCancelClickListener = new Dialog.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            if (onCancelListener != null) {
                onCancelListener.onClick(BaseDialog.this);
            }
        }
    };
    private String mTitle;
    private String mMessage;
    private String leftText;
    private String rightText;

    private BaseDialog(Context context) {
        super(context, R.style.BaseDialog);
        this.setOnCancelListener(mOnCancelClickListener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_dialog);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvMsg = (TextView) findViewById(R.id.tv_message);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvRight = (TextView) findViewById(R.id.tv_right);
        vwButtonDivider = (View) findViewById(R.id.vw_button_divider);
    }

    /**
     * 调用完Builder类的create()方法后显示该对话框的方法
     */
    @Override
    public void show() {
        super.show();
        show(this);
    }

    private void show(final BaseDialog mDialog) {
        if (!TextUtils.isEmpty(mDialog.mTitle)) {
            mDialog.tvTitle.setText(mDialog.mTitle);
        }

        if (!TextUtils.isEmpty(mDialog.mMessage)) {
            mDialog.tvMsg.setText(mDialog.mMessage);
        }
        if (mDialog.onRightListener != null) {
            mDialog.tvRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.onRightListener.onClick(mDialog);
                }
            });
            if (!TextUtils.isEmpty(mDialog.rightText)) {
                mDialog.tvRight.setText(mDialog.rightText);
            }
            tvRight.setVisibility(View.VISIBLE);
            vwButtonDivider.setVisibility(View.VISIBLE);
        } else {
            tvRight.setVisibility(View.GONE);
            vwButtonDivider.setVisibility(View.GONE);
        }
        mDialog.tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.onLeftListener.onClick(mDialog);
            }
        });
        if (!TextUtils.isEmpty(mDialog.leftText)) {
            mDialog.tvLeft.setText(mDialog.leftText);
        }
    }

    public static class Builder {

        private BaseDialog mDialog;

        public Builder(Context context) {
            mDialog = new BaseDialog(context);
        }

        /**
         * 设置对话框标题
         *
         * @param title
         */
        public Builder setTitle(String title) {
            mDialog.mTitle = title;
            return this;
        }

        /**
         * 设置对话框文本内容,如果调用了setView()方法，该项失效
         *
         * @param msg
         */
        public Builder setMessage(String msg) {
            mDialog.mMessage = msg;
            return this;
        }

        /**
         * 设置确认按钮的回调
         *
         * @param onClickListener
         */
        public Builder setLeftClick(OnLeftClickListener onClickListener) {
            mDialog.onLeftListener = onClickListener;
            return this;
        }

        /**
         * 设置确认按钮的回调
         *
         * @param btnText,onClickListener
         */
        public Builder setLeftClick(String btnText, OnLeftClickListener onClickListener) {
            mDialog.leftText = btnText;
            mDialog.onLeftListener = onClickListener;
            return this;
        }

        /**
         * 设置取消按钮的回掉
         *
         * @param onClickListener
         */
        public Builder setRightClick(OnRightClickListener onClickListener) {
            mDialog.onRightListener = onClickListener;
            return this;
        }

        /**
         * 设置取消按钮的回调
         *
         * @param btnText,onClickListener
         */
        public Builder setRightClick(String btnText, OnRightClickListener onClickListener) {
            mDialog.rightText = btnText;
            mDialog.onRightListener = onClickListener;
            return this;
        }


        /**
         * 设置该对话框能否被Cancel掉，默认可以
         *
         * @param cancelable
         */
        public Builder setCancelable(boolean cancelable) {
            mDialog.setCancelable(cancelable);
            return this;
        }

        /**
         * 设置对话框被cancel对应的回调接口，cancel()方法被调用时才会回调该接口
         *
         * @param onCancelListener
         */
        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            mDialog.onCancelListener = onCancelListener;
            return this;
        }

        /**
         * 设置对话框消失对应的回调接口，一切对话框消失都会回调该接口
         *
         * @param onDismissListener
         */
        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            mDialog.setOnDismissListener(onDismissListener);
            return this;
        }

        /**
         * 通过Builder类设置完属性后构造对话框的方法
         */
        public BaseDialog create() {
            return mDialog;
        }
    }

    public interface OnLeftClickListener {
        void onClick(BaseDialog dialog);
    }

    public interface OnRightClickListener {
        void onClick(BaseDialog dialog);
    }

    public interface OnCancelListener {
        void onClick(BaseDialog dialog);
    }
}
