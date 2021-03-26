package com.landis.eoswallet.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;

import com.landis.eoswallet.R;

public class LoadingProgress {
    private static Dialog _progress;

    public static void init(Context context, boolean canceled) {
        _progress = new Dialog(context, R.style.Loading);
        _progress.setContentView(R.layout.layout_loading_progress);
//        ContentLoadingProgressBar progressBar = _progress.findViewById(R.id.imgLoading);
//        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        _progress.setCanceledOnTouchOutside(false);
        _progress.setCancelable(canceled);
        _progress.findViewById(R.id.tv_loading).setVisibility(View.GONE);
    }

    public static void show(Context context, boolean canceled, ProgressCancelListener cancelListener) {
        try {
            dismissProgress();
            init(context, canceled);
            setCancelListener(cancelListener);
            showProgress();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void show(Context context, boolean canceled) {
        show(context, canceled, () -> {
        });
    }

    public static void dismissProgress() {
        try {
            if (_progress != null && _progress.isShowing()) {
                Context context = ((ContextWrapper) _progress.getContext()).getBaseContext();
                if (context instanceof Activity) {
                    if (!((Activity) context).isFinishing())
                        _progress.dismiss();
                } else //if the Context used wasnt an Activity, then dismiss it too
                    _progress.dismiss();
            }
            _progress = null;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showProgress() {
        if (_progress != null) {
            Context context = ((ContextWrapper) _progress.getContext()).getBaseContext();
            if (context instanceof Activity) {
                if (!((Activity) context).isFinishing())
                    _progress.show();
            } else {//if the Context used wasnt an Activity, then dismiss it too
                _progress.show();
            }
        }
    }

    public static void setCancelListener(ProgressCancelListener cancelListener) {
        if (_progress != null && cancelListener != null) {
            _progress.setOnCancelListener(dialog -> cancelListener.onCancelProgress());
        }
    }

    public interface ProgressCancelListener {
        void onCancelProgress();
    }
}
