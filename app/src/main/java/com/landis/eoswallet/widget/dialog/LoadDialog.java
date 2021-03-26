package com.landis.eoswallet.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.WindowManager;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.landis.eoswallet.R;

public class LoadDialog extends Dialog {

    private TextView tvLoadContent;

    private Handler mHandler=new Handler(Looper.getMainLooper());

    public LoadDialog(@NonNull Context context) {
        super(context, R.style.my_loading_dialog);
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_loading);
        tvLoadContent=findViewById(R.id.tv_load_content);
        setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.alpha=0.8f;
        getWindow().setAttributes(attributes);
        setCancelable(false);
    }

    public void setTipContent(String tipContent){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (null!=tvLoadContent&&!StringUtils.isEmpty(tipContent)&&isShowing()){
                    tvLoadContent.setText(tipContent+getContext().getResources().getString(R.string.please_wait));
                }
            }
        });
    }
}
