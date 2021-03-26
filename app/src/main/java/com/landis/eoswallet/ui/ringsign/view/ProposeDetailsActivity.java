package com.landis.eoswallet.ui.ringsign.view;

import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.activity.BaseActivity;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.databinding.ActivityProposeDetailsBinding;
import com.landis.eoswallet.ui.ringsign.adapter.ProposeDetailsAdapter;
import com.landis.eoswallet.ui.ringsign.viewmodel.ProposeDetailsViewModel;
import com.landis.eoswallet.util.CopyUtils;

/**
 * 提案详情
 */
@Route(path = RouteConst.AV_PROPOSE_DETAILS)
public class ProposeDetailsActivity extends BaseActivity<ActivityProposeDetailsBinding, ProposeDetailsViewModel> {

    //提案者（账户名）
    @Autowired(name = "propose_account")
    public String proposeAccount;

    //提案名
    @Autowired(name = "propose_name")
    public String proposeName;

    //是否显示关注
    @Autowired(name = "show_follow")
    public boolean showFollow;

    @Override
    protected void init() {
        setAppBarView(mDataBinding.appbar);
        mDataBinding.ivProposeDetailsBack.setOnClickListener(v -> finish());

        mDataBinding.tvProposeDetailsFollow.setOnClickListener(v -> mViewModel.followPropose());

        mDataBinding.ivProposeAccountCopy.setOnClickListener(v -> {
            CopyUtils.copy(ProposeDetailsActivity.this, "propose_details", proposeAccount);
            ToastUtils.showShort("复制成功");
        });

        mDataBinding.ivProposeNameCopy.setOnClickListener(v -> {
            CopyUtils.copy(ProposeDetailsActivity.this, "propose_details", proposeName);
            ToastUtils.showShort("复制成功");
        });

        mDataBinding.tvProposeApprove.setOnClickListener(v -> mViewModel.msigMultiple("approve"));

        mDataBinding.tvProposeUnapprove.setOnClickListener(v -> mViewModel.msigMultiple("unapprove"));

        mDataBinding.tvProposeCancel.setOnClickListener(v -> mViewModel.msigMultiple("cancel"));

        mDataBinding.tvProposeExec.setOnClickListener(v -> mViewModel.msigMultiple("exec"));

        mDataBinding.listProposeActive.setLayoutManager(new LinearLayoutManager(this));
        mDataBinding.listProposeActive.setAdapter(new ProposeDetailsAdapter(mViewModel.proposeStateList));

        mViewModel.getProposals();
    }

    @Override
    protected void bindViewModel() {
        mDataBinding.setViewModel(mViewModel);
    }

    @Override
    protected void initViewModel() {
        ARouter.getInstance().inject(this);
        mViewModel = new ProposeDetailsViewModel();
        mViewModel.setProposeAccount(proposeAccount);
        mViewModel.setProposeName(proposeName);
        mViewModel.setShowFollow(showFollow);
        mViewModel.setActivity(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_propose_details;
    }
}
