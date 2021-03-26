package com.landis.eoswallet.ui.vote.view;

import android.support.v7.widget.LinearLayoutManager;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.fragment.BaseFragment;
import com.landis.eoswallet.databinding.FragmentMyVoteBinding;
import com.landis.eoswallet.event.Event;
import com.landis.eoswallet.ui.vote.adapter.MyVoteAdapter;
import com.landis.eoswallet.ui.vote.viewmodel.MyVoteViewModel;
import com.landis.eoswallet.ui.vote.viewmodel.VoteViewModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MyVoteFragment extends BaseFragment<FragmentMyVoteBinding, MyVoteViewModel> {

    private VoteViewModel voteViewModel;

    @Override
    protected void init() {
        mDataBinding.rvMyVote.setAdapter(new MyVoteAdapter(mViewModel.myVoteList,
                getContext()));
        mDataBinding.rvMyVote.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewModel.getChainInfo();
        initRefreshLayout();
    }

    private void initRefreshLayout() {
        mDataBinding.nodesVoteRefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                mViewModel.refreshData(voteViewModel);
            }
        });
    }

    @Override
    protected void bindViewModel() {
        mDataBinding.setViewModel(mViewModel);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_my_vote;
    }

    @Override
    protected void initViewModel() {
        mViewModel = new MyVoteViewModel();
    }


    public void setVoteViewModel(VoteViewModel voteViewModel){
        this.voteViewModel=voteViewModel;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void LoadVoteBlockOutSuccess(Event.LoadVoteBlockOutSuccess LoadVoteBlockOutSuccess) {
        mViewModel.loadData(voteViewModel);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshVote(Event.RefreshVote refreshVote) {
        mViewModel.refreshData(voteViewModel);
    }

    @Override
    protected boolean isSupportLoad() {
        return true;
    }
}
