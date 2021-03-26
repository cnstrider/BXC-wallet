package com.landis.eoswallet.ui.ringsign.view;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.activity.BaseActivity;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.databinding.ActivityRingSignBinding;
import com.landis.eoswallet.event.Event;
import com.landis.eoswallet.ui.ringsign.adapter.ProposalInfoAdapter;
import com.landis.eoswallet.ui.ringsign.adapter.RingSignAccountAdapter;
import com.landis.eoswallet.ui.ringsign.viewmodel.RingSignViewModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@Route(path = RouteConst.AV_RING_SIGN)
public class RingSignActivity extends BaseActivity<ActivityRingSignBinding, RingSignViewModel> {


    @Override
    protected void init() {

        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setAppBarView(mDataBinding.appbar);
        mDataBinding.ivRingSignBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDataBinding.searchViewRingSign.findViewById(android.support.v7.appcompat.R.id.search_plate).setBackground(null);
        mDataBinding.searchViewRingSign.findViewById(android.support.v7.appcompat.R.id.submit_area).setBackground(null);
        TextView searchTextView = mDataBinding.searchViewRingSign.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchTextView.setTextSize(13);
        searchTextView.setTextColor(Color.GRAY);
        searchTextView.setHintTextColor(Color.GRAY);
        searchTextView.setGravity(Gravity.CENTER);
        searchTextView.setMaxHeight(30);
        mDataBinding.searchViewRingSign.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mDataBinding.searchViewRingSign.clearFocus();
                searchTextView.setText("");
                searchTextView.setHint(getResources().getString(R.string.enter_query_ring_sing_account));
                ARouter.getInstance().build(RouteConst.AV_RING_SIGN_SEARCH).withString("search_key", query.trim()).navigation();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mDataBinding.tvQueryPropose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String proposerAccount = mDataBinding.edProposer.getText().toString().trim();
                String proposerName = mDataBinding.edProposalName.getText().toString().trim();
                mViewModel.queryPropose(proposerAccount,proposerName);
                mDataBinding.edProposer.clearFocus();
                mDataBinding.edProposalName.clearFocus();
                mDataBinding.edProposer.setText("");
                mDataBinding.edProposalName.setText("");
            }
        });


        mDataBinding.listRingSignAccount.setAdapter(new RingSignAccountAdapter(mViewModel.ringSignAccounts));
        mDataBinding.listRingSignAccount.setLayoutManager(new LinearLayoutManager(this));

        mDataBinding.listPropose.setAdapter(new ProposalInfoAdapter(mViewModel.proposalInfos));
        mDataBinding.listPropose.setLayoutManager(new LinearLayoutManager(this));

        mViewModel.loadRingSignAccount();
        mViewModel.getMyProposal();
    }

    @Override
    protected void bindViewModel() {
        mDataBinding.setViewModel(mViewModel);
    }

    @Override
    protected void initViewModel() {
        mViewModel = new RingSignViewModel();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ring_sign;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ringSignAccountFollow(Event.RingSignAccountFollow ringSignAccountFollow) {
        mViewModel.loadRingSignAccount();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshPropose(Event.RefreshPropose refreshPropose) {
        mViewModel.getMyProposal();
    }
}
