package com.landis.eoswallet.ui.ringsign.view;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.activity.BaseActivity;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.databinding.ActivityCreateProposeBinding;
import com.landis.eoswallet.event.Event;
import com.landis.eoswallet.ui.ringsign.viewmodel.CreateProposeViewModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 创建提案
 */
@Route(path = RouteConst.AV_CREATE_PROPOSE)
public class CreateProposeActivity extends BaseActivity<ActivityCreateProposeBinding,
        CreateProposeViewModel> {

    @Autowired(name = "propose_account")
    public String proposeAccount;

    @Override
    protected void init() {
        setAppBarView(mDataBinding.appbar);
        mDataBinding.ivCreateProposeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDataBinding.tvCreatePropose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String proposeName = mDataBinding.edCreateProposalName.getText().toString().trim();
                String proposalTransferAccount = mDataBinding.edCreateProposalTransferAccount.getText().toString().trim();
                String     proposalTransferAmount = mDataBinding.edCreateProposalTransferAmount.getText().toString().trim();
                String memo = mDataBinding.edCreateProposalTransferMemo.getText().toString().trim();
                mViewModel.createPropose(proposeName,proposalTransferAccount,
                        proposalTransferAmount,memo);
            }
        });

        mDataBinding.rlSelectedToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.showSelectTokenDialog();
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
        mViewModel = new CreateProposeViewModel();
        mViewModel.getBxcBalance();
        mViewModel.getProposeAccountBalance();
        mViewModel.setContext(this);
        mViewModel.setProposeAccount(proposeAccount);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_create_propose;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshPropose(Event.RefreshPropose refreshPropose) {
        finish();
    }
}
