package com.landis.eoswallet.ui.home.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.SPUtils;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.base.constant.SpConst;
import com.landis.eoswallet.base.fragment.BaseFragment;
import com.landis.eoswallet.databinding.FragmentTokenBinding;
import com.landis.eoswallet.ui.home.adapter.AppTokenAdapter;
import com.landis.eoswallet.ui.home.viewmodel.TokenViewModel;
import com.landis.eoswallet.ui.other.view.ScanActivity;

public class TokenFragment extends BaseFragment<FragmentTokenBinding, TokenViewModel> {

    @Override
    protected void init() {
        mDataBinding.rvToken.setAdapter(new AppTokenAdapter(mViewModel.appTokenInfoObservableList));
        mDataBinding.rvToken.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewModel.getMemory(getActivity());
        mViewModel.loadTokenList(getActivity());
        mViewModel.setAccountName(SPUtils.getInstance().getString(SpConst.WALLET_NAME));
        mDataBinding.refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                mViewModel.loadTokenList(getActivity());
            }
        });


        mDataBinding.createForFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator.forSupportFragment(TokenFragment.this)
                        .setCaptureActivity(ScanActivity.class)
                        .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)// 扫码的类型,可选：一维码，二维码，一/二维码
                        .setPrompt("")// 设置提示语
                        .setCameraId(0)// 选择摄像头,可使用前置或者后置
                        .setBeepEnabled(false)// 是否开启声音,扫完码之后会"哔"的一声
                        .setBarcodeImageEnabled(false)// 扫完码之后生成二维码的图片
                        .initiateScan();// 初始化扫码
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAppBarView(mDataBinding.appbar);
        addPaddingTopEqualStatusBarHeight();
    }

    @Override
    protected void bindViewModel() {
        mDataBinding.setViewModel(mViewModel);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_token;
    }

    @Override
    protected void initViewModel() {
        mViewModel = new TokenViewModel();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
            } else {
                ARouter.getInstance().build(RouteConst.AV_CREATE_FOR_FRIEND).withString("key", result.getContents()).navigation(getContext());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
