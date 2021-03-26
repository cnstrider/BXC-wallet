package com.landis.eoswallet.ui.account.view;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.SizeUtils;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.activity.BaseActivity;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.databinding.ActivityFriendCreateAccountBinding;
import com.landis.eoswallet.event.Event;
import com.landis.eoswallet.ui.account.viewmodel.FriendCreateAccountViewModel;
import com.landis.eoswallet.util.QrCreateUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 请朋友创建
 */
@Route(path = RouteConst.AV_FRIEND_CREATE_ACCOUNT)
public class FriendCreateAccountActivity extends BaseActivity<ActivityFriendCreateAccountBinding,
        FriendCreateAccountViewModel> {
    @Override
    protected void init() {
        Bitmap bpQr = QrCreateUtils.createQRCodeBitmap(mViewModel.pubKey, SizeUtils.dp2px(198),
                "UTF-8", "H", "0", Color.BLACK, Color.WHITE, null, null, 0F);
        mDataBinding.imageQr.setImageBitmap(bpQr);

        mDataBinding.btnFriendCreateAccountCreated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.checkAccount();
            }
        });

        SpannableString spannableString =
                new SpannableString(getResources().getString(R.string.friend_create_account_tip));

        SpannableString spannableClick =
                new SpannableString(getResources().getString(R.string.click_refresh));
        spannableClick.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                mViewModel.checkAccount();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                //设置字体颜色
                int color = getResources().getColor(R.color.text_blue);
                ds.setColor(color);
                //去掉下划线
                ds.setUnderlineText(false);

            }
        }, 0, spannableClick.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mDataBinding.tvFriendCreateAccountTip.append(spannableString);
        mDataBinding.tvFriendCreateAccountTip.append(spannableClick);

        mDataBinding.tvFriendCreateAccountTip.setMovementMethod(LinkMovementMethod.getInstance());

        mDataBinding.tvPrivateKeyCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.copyPriKey(getApplicationContext());
            }
        });

        mDataBinding.tvPublicKeyCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.copyPubKey(getApplicationContext());
            }
        });

        mDataBinding.btnFriendCreateAccountSaveQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.saveQr(FriendCreateAccountActivity.this,bpQr);
            }
        });
    }

    @Override
    protected void bindViewModel() {
        mDataBinding.setViewModel(mViewModel);
    }

    @Override
    protected void initViewModel() {
        mViewModel = new FriendCreateAccountViewModel();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_friend_create_account;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            return true;
        }else{
            return super.onKeyDown(keyCode,event);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void createOk(Event.CreateOk ok) {
        finish();
    }
}
