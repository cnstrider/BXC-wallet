package com.landis.eoswallet.ui.home.view;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.activity.BaseActivity;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.base.constant.SpConst;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;
import com.landis.eoswallet.databinding.ActivityMainBinding;
import com.landis.eoswallet.net.manage.EosAccountManger;
import com.landis.eoswallet.ui.me.view.MeFragment;
import com.landis.eoswallet.util.ColorUtil;
import com.landis.eoswallet.util.GenerateTestUserSig;
import com.landis.eoswallet.util.StatusBarUtil;

@Route(path = RouteConst.AV_MAIN)
public class MainActivity extends BaseActivity<ActivityMainBinding, BaseViewModel> {

    public Fragment[] fragments;

    @Override
    protected void init() {
        StatusBarUtil.transparencyBar(this); //设置状态栏全透明
        StatusBarUtil.StatusBarLightMode(this, true); //设置白底黑字
        fragments = new Fragment[]{
                new AppFragment(),
                new TokenFragment(),
                new MeFragment()};
        RefreshEosAccount();
        LogUtils.d( "用户签名"+ GenerateTestUserSig.genTestUserSig("admin"));

        mDataBinding.flFragmentContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (null!=mDataBinding.tabs.getTabAt(i)){
                    mDataBinding.tabs.getTabAt(i).select();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        mDataBinding.flFragmentContainer.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments[i];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        });

//        mDataBinding.flFragmentContainer.setAdapter(new TabPagerAdapter(getSupportFragmentManager(),
//                new String[]{getResources().getString(R.string.application),
//                        getResources().getString(R.string.wallet), getResources().getString(R.string.my)}
//                , true) {
//            @Override
//            public Fragment getItem(int position) {
//               ;
//            }
//        });

        mDataBinding.tabs.setupWithViewPager(mDataBinding.flFragmentContainer);
        mDataBinding.tabs.getTabAt(0).setIcon(R.drawable.selector_tab_app).setText(getString(R.string.application));
        mDataBinding.tabs.getTabAt(1).setIcon(R.drawable.selector_tab_qbao).setText(getResources().getString(R.string.wallet));
        mDataBinding.tabs.getTabAt(2).setIcon(R.drawable.selector_tab_presonal).setText(getResources().getString(R.string.my));
        mDataBinding.tabs.setTabTextColors(ColorUtil.createColorStateList(this,
                R.color.normal_text_color, R.color.tab_select_color));
        changeIconImgBottomMargin(mDataBinding.tabs, 4);

        mDataBinding.flFragmentContainer.setCurrentItem(1);
    }

    private void changeIconImgBottomMargin(ViewGroup parent, int px) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof ViewGroup) {
                changeIconImgBottomMargin((ViewGroup) child, px);
            } else if (child instanceof ImageView) {
                ViewGroup.MarginLayoutParams lp =
                        ((ViewGroup.MarginLayoutParams) child.getLayoutParams());
                lp.bottomMargin = px;
                child.requestLayout();
            }
        }
    }

    /**
     * 刷新账号余额
     */
    private void RefreshEosAccount() {
        EosAccountManger.getInstance().checkBalance(SPUtils.getInstance().getString(SpConst.WALLET_NAME));
    }

    @Override
    protected void bindViewModel() {
        mDataBinding.setViewModel(mViewModel);
    }

    @Override
    protected void initViewModel() {

        mViewModel=new BaseViewModel();

    }
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EosAccountManger.getInstance().onDestroy();
    }
}