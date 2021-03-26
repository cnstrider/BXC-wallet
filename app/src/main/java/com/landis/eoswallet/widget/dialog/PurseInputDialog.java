package com.landis.eoswallet.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.landis.eoswallet.R;
import com.landis.eoswallet.filter.EditInputFilter;

/**
 * 密码输入
 */
public class PurseInputDialog extends Dialog {

    private View.OnClickListener onClickListener;

    public String action, remark, to, quantity;

    public  void setOnClickListener(View.OnClickListener onClickListener){
        this.onClickListener=onClickListener;
    }

    public PurseInputDialog(@NonNull Context context) {
        super(context, R.style.dialog);
        setContentView(R.layout.dialog_purse_input);
        setCanceledOnTouchOutside(true);

        InputFilter[] inputFilters = {new EditInputFilter()};

        ((EditText) findViewById(R.id.edit_password)).setFilters(inputFilters);

        findViewById(R.id.btn_define).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = ((EditText) findViewById(R.id.edit_password)).getText().toString();
                if (TextUtils.isEmpty(password)) {
                    ToastUtils.showShort(context.getResources().getString(R.string.password_not_empty));
                    return;
                }
                if (password.length() < 6) {
                    ToastUtils.showShort(context.getResources().getString(R.string.please_enter_six_digit_password));
                    return;
                }
                v.setTag(password);
                onClickListener.onClick(v);
                dismiss();
            }
        });

        //设置宽高  位置
        Window window = getWindow();
        if (null!=window){
            WindowManager.LayoutParams attributes = window.getAttributes();
            if (null!=attributes){
                attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                attributes.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                attributes.gravity = Gravity.CENTER;//设置dialog 在布局中的位置
            }

        }
    }

    @Override
    public void dismiss() {
        EditText editTextPassword = findViewById(R.id.edit_password);
        if (null!=editTextPassword){
            editTextPassword.setText("");
        }
        super.dismiss();
    }

    /**
     * 设置数据及回调
     * @param action 合约动作
     * @param remark 备注
     * @param to 对方账号
     * @param quantity 金额
     * @param onClickListener 确认监听
     */
    public void setData(String action, String remark, String to, String quantity,
                        View.OnClickListener onClickListener){
        this.action=action;
        this.remark=remark;
        this.to=to;
        this.quantity=quantity;
        this.onClickListener=onClickListener;
    }
}
