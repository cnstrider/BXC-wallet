package com.landis.eoswallet.ui.account.viewmodel;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.constant.SpConst;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;
import com.landis.eoswallet.util.CopyUtils;
import com.landis.eoswallet.util.QrCreateUtils;

public class AccountDetailViewModel extends BaseViewModel {

    /**
     * 创建二维码账户图片
     *
     * @param imageView
     */
    public void createQrImage(ImageView imageView) {
        Bitmap bpQr = QrCreateUtils.createQRCodeBitmap(SPUtils.getInstance().getString(SpConst.WALLET_NAME)
                , SizeUtils.dp2px(198), "UTF-8", "H", "0",
                Color.BLACK, Color.WHITE, null, null, 0F);
        imageView.setImageBitmap(bpQr);
    }

    /**
     * 复制账户
     * @param view
     */
    public void copyAccount(View view) {
        CopyUtils.copy(view.getContext(), "un", SPUtils.getInstance().getString(SpConst.WALLET_NAME));
        ToastUtils.showLong(view.getContext().getResources().getString(R.string.copy_wallet_address_success)
                + SPUtils.getInstance().getString(SpConst.WALLET_NAME));
    }
}
