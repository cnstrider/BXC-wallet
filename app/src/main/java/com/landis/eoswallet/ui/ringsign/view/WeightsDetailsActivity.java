package com.landis.eoswallet.ui.ringsign.view;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.activity.BaseActivity;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.databinding.ActivityWeightsDetailsBinding;
import com.landis.eoswallet.ui.ringsign.adapter.WeightAdapter;
import com.landis.eoswallet.ui.ringsign.viewmodel.WeightsDetailsViewModel;

/**
 * 环签权重界面
 */
@Route(path = RouteConst.AV_WEIGHTS_DETAILS)
public class WeightsDetailsActivity extends BaseActivity<ActivityWeightsDetailsBinding, WeightsDetailsViewModel> {

    @Autowired(name = "propose_account")
    public String proposeAccount;

    @Override
    protected void init() {
        setAppBarView(mDataBinding.appbar);

        mDataBinding.ivWeightsDetailsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDataBinding.rvWeights.setAdapter(new WeightAdapter(mViewModel.weightList));
        mDataBinding.rvWeights.setLayoutManager(new LinearLayoutManager(this));

        mViewModel.queryWeights(proposeAccount);
    }

    @Override
    protected void bindViewModel() {
        mDataBinding.setViewModel(mViewModel);
    }

    @Override
    protected void initViewModel() {
        ARouter.getInstance().inject(this);
        mViewModel=new WeightsDetailsViewModel();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_weights_details;
    }
}
