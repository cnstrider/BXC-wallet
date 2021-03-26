package com.landis.eoswallet.ui.vote.view;

import android.support.v7.widget.LinearLayoutManager;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.fragment.BaseFragment;
import com.landis.eoswallet.databinding.FragmentSuperNodesBinding;
import com.landis.eoswallet.event.Event;
import com.landis.eoswallet.ui.vote.adapter.SuperNodesAdapter;
import com.landis.eoswallet.ui.vote.viewmodel.SuperNodesViewModel;
import com.landis.eoswallet.ui.vote.viewmodel.VoteViewModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SuperNodesFragment extends BaseFragment<FragmentSuperNodesBinding, SuperNodesViewModel> {

    private VoteViewModel voteViewModel;

    @Override
    protected void init() {
        mDataBinding.rvNodesVote.setAdapter(new SuperNodesAdapter(mViewModel.nodesVotes,getActivity()));
        mDataBinding.rvNodesVote.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewModel.getChainInfo();
        initRefreshLayout();
    }

    @Override
    protected void bindViewModel() {
        mDataBinding.setViewModel(mViewModel);
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
    protected int getLayoutResId() {
        return R.layout.fragment_super_nodes;
    }

    @Override
    protected void initViewModel() {
            mViewModel = new SuperNodesViewModel();
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
}
