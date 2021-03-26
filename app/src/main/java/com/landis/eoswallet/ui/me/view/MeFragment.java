 package com.landis.eoswallet.ui.me.view;

import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.base.constant.SpConst;
import com.landis.eoswallet.base.fragment.BaseFragment;
import com.landis.eoswallet.databinding.FragmentMeBinding;
import com.landis.eoswallet.ui.me.viewmodel.MeViewModel;
import com.landis.eoswallet.ui.wallet.viewmodel.WalletViewModel;
import com.landis.eoswallet.widget.dialog.PurseInputDialog;

public class MeFragment  extends BaseFragment<FragmentMeBinding, MeViewModel> {


    private PurseInputDialog purseInputDialog ;

    @Override
    protected void init() {
        mViewModel.getAccountName();

    }

    @Override
    protected void bindViewModel() {
         mDataBinding.setViewModel(mViewModel);
         mDataBinding.btnPriKey.setOnClickListener(v -> {
            if (null==purseInputDialog){
                purseInputDialog = new PurseInputDialog(getActivity());
            }
             purseInputDialog.setOnClickListener(v1 -> {
                 String password = (String) v1.getTag();
                 String priKey = SPUtils.getInstance().getString(SpConst.P_K);
                 if (TextUtils.isEmpty(priKey)){
                    return;
                 }
                 WalletViewModel walletViewModel = new WalletViewModel();
                 byte[] bytes = EncryptUtils.decryptHexStringAES(priKey,
                         EncryptUtils.encryptMD5ToString(password.getBytes()).getBytes(),
                         "AES/CBC/PKCS5Padding",
                         walletViewModel.hexString2Bytes(walletViewModel.iv));
                 if (null==bytes || 0==bytes.length){
                     ToastUtils.showShort(getString(R.string.password_error));
                     return;
                 }
                 String decryptPriKey = new String(bytes);
                 ARouter.getInstance().build(RouteConst.AV_WALLET_KEY)
                         .withString(RouteConst.TITLE, getResources().getString(R.string.backup_private_key))
                         .withString(RouteConst.OPERATION_TEXT, getResources().getString(R.string.pri_key))
                         .withString(RouteConst.WALLET_KEY, decryptPriKey).navigation(getActivity());
             });
             purseInputDialog.show();
         });

         mDataBinding.btnPubKey.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ARouter.getInstance().build(RouteConst.AV_WALLET_KEY)
                         .withString(RouteConst.TITLE, getResources().getString(R.string.account_public_key_tips))
                         .withString(RouteConst.OPERATION_TEXT, getResources().getString(R.string.pub_key))
                         .withString(RouteConst.WALLET_KEY, SPUtils.getInstance().getString(SpConst.P_B_K)).navigation(getActivity());
             }
         });

         mDataBinding.btnAbout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ARouter.getInstance().build(RouteConst.AV_ABOUT_US).navigation(getActivity());
             }
         });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initViewModel() {
        mViewModel = new MeViewModel();
    }
}
