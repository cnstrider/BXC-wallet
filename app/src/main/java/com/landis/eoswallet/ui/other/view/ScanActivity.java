package com.landis.eoswallet.ui.other.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;

import com.journeyapps.barcodescanner.CaptureManager;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.activity.BaseActivity;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;
import com.landis.eoswallet.databinding.ActivityScanBinding;

public class ScanActivity extends BaseActivity<ActivityScanBinding, BaseViewModel> {

    private CaptureManager captureManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppBarView(mDataBinding.appbar);
        mDataBinding.ivScanBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        captureManager=new CaptureManager(this,mDataBinding.scanCodeView);

        captureManager.initializeFromIntent(getIntent(),savedInstanceState);

        captureManager.decode();
    }

    @Override
    protected void init() {
    }

    @Override
    protected void bindViewModel() {

    }

    @Override
    protected void initViewModel() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_scan;
    }

    @Override
    protected void onResume() {
        super.onResume();
        captureManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        captureManager.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureManager.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        captureManager.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        captureManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mDataBinding.scanCodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}
