package com.landis.eoswallet.ui.wallet.view;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.activity.BaseActivity;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.databinding.ActivityWalletDetailBinding;
import com.landis.eoswallet.ui.wallet.adapter.TransactionRecordsAdapter;
import com.landis.eoswallet.ui.wallet.viewmodel.WalletDetailViewModel;

@Route(path = RouteConst.AV_WALLET_DETAIL)
public class WalletDetailActivity extends BaseActivity<ActivityWalletDetailBinding, WalletDetailViewModel> {

    @Autowired(name = RouteConst.TOKEN_NAME)
    public String tokenName;

    @Override
    protected void init() {
        setAppBarView(mDataBinding.appbar);
        if (!TextUtils.equals("BXC", tokenName)) {
            mDataBinding.txtTransaction.setVisibility(View.VISIBLE);
            mDataBinding.txtAvailableBalance.setVisibility(View.GONE);
            mDataBinding.viewWhiteLine.setVisibility(View.GONE);
            mDataBinding.llBalanceType.setVisibility(View.GONE);
        } else {
            mViewModel.getMyVote();
        }

        mDataBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if ( !TextUtils.equals("BXC", tokenName)) {
            mDataBinding.txtBalanceLabel.setVisibility(View.GONE);
            mDataBinding.tvBalanceTitle.setVisibility(View.VISIBLE);
            mDataBinding.txtBalance.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mDataBinding.txtBalance.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int top = mDataBinding.txtBalance.getTop();
                    int height = mDataBinding.txtBalance.getHeight();
                    LogUtils.d("appBarLayout 顶点" + top + "高度" + height);

                    CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) mDataBinding.appBarLayout.getLayoutParams();
                    lp.height = top + height + SizeUtils.dp2px(40);
                    mDataBinding.appBarLayout.setLayoutParams(lp);
                }
            });
        } else {
            mDataBinding.txtBalanceLabel.setVisibility(View.VISIBLE);
            mDataBinding.tvBalanceTitle.setVisibility(View.GONE);
        }

        mDataBinding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mDataBinding.txtBalance.getLayoutParams();
                mDataBinding.txtBalance.setGravity(Gravity.CENTER_HORIZONTAL);
                mDataBinding.txtBalance.setLayoutParams(layoutParams);

                //verticalOffset  当前偏移量 appBarLayout.getTotalScrollRange() 最大高度 便宜值
                int Offset = Math.abs(i); //目的是将负数转换为绝对正数；
                //标题栏的渐变
                mDataBinding.toolBar.setBackgroundColor(mViewModel.changeAlpha(getResources().getColor(R.color.white)
                        , Math.abs(i * 1.0f) / appBarLayout.getTotalScrollRange()));
                /**
                 * 当前最大高度便宜值除以2 在减去已偏移值 获取浮动 先显示在隐藏
                 */
                if (Offset < appBarLayout.getTotalScrollRange() / 2) {
                    mDataBinding.toolBar.setAlpha((appBarLayout.getTotalScrollRange() / 2 - Offset * 1.0f) / (appBarLayout.getTotalScrollRange() / 2));
                    mDataBinding.txtBalanceLabel.setTextColor(Color.WHITE);
                    if (!TextUtils.equals("BXC", tokenName)) {
                        mDataBinding.txtBalance.setVisibility(View.VISIBLE);
                        mDataBinding.txtBalanceLabel.setVisibility(View.GONE);
                    }
                    mDataBinding.ivBack.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_back_white));
                    /**
                     * 从最低浮动开始渐显 当前 Offset就是  appBarLayout.getTotalScrollRange() / 2
                     * 所以 Offset - appBarLayout.getTotalScrollRange() / 2
                     */
                } else if (Offset > appBarLayout.getTotalScrollRange() / 2) {
                    float floate = (Offset - appBarLayout.getTotalScrollRange() / 2) * 1.0f / (appBarLayout.getTotalScrollRange() / 2);
                    mDataBinding.txtBalanceLabel.setTextColor(Color.BLACK);
                    mDataBinding.txtBalanceLabel.setVisibility(View.VISIBLE);
                    if (!TextUtils.equals("BXC", tokenName)) {
                        mDataBinding.txtBalance.setVisibility(View.GONE);
                    }
                    mDataBinding.toolBar.setAlpha(floate);
                    mDataBinding.ivBack.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_black_back));
                }
            }
        });

        mDataBinding.rvTransactionRecords.setLayoutManager(new LinearLayoutManager(this));
        mDataBinding.rvTransactionRecords.setAdapter(new TransactionRecordsAdapter(mViewModel.transactionRecords));

        initRefreshLayout();
        mViewModel.loadRecords();
    }


    private void initRefreshLayout() {
        mDataBinding.refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                mViewModel.refreshData();
            }
        });
    }


    @Override
    protected void bindViewModel() {
        mDataBinding.setViewModel(mViewModel);
    }

    @Override
    protected void initViewModel() {
        ARouter.getInstance().inject(this);
        mViewModel = new WalletDetailViewModel();
        mViewModel.setTokenName(tokenName);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_wallet_detail;
    }
}
