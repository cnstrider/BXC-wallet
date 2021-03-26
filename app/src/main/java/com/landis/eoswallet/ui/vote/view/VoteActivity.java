package com.landis.eoswallet.ui.vote.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.activity.BaseActivity;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.databinding.ActivityVoteBinding;
import com.landis.eoswallet.ui.vote.viewmodel.VoteViewModel;

import java.util.ArrayList;
import java.util.List;

@Route(path = RouteConst.AV_VOTE_HOME)
public class VoteActivity extends BaseActivity<ActivityVoteBinding, VoteViewModel> {

    private String[] tabsTitle = new String[3];
    private List<Fragment> voteFragments = new ArrayList<>();

    @Override
    protected void init() {

        mDataBinding.ivVoteBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tabsTitle[0] = getResources().getString(R.string.node_vote);
        tabsTitle[1] = getResources().getString(R.string.candidate_node);
        tabsTitle[2] = getResources().getString(R.string.my_voted);

        for (int i = 0; i < tabsTitle.length; i++) {
            mDataBinding.voteTabLayout.addTab(mDataBinding.voteTabLayout.newTab().setText(tabsTitle[i]));
        }

        SuperNodesFragment superNodesFragment = new SuperNodesFragment();
        superNodesFragment.setVoteViewModel(mViewModel);
        voteFragments.add(superNodesFragment);

        CandidateNodesFragment candidateNodesFragment = new CandidateNodesFragment();
        candidateNodesFragment.setVoteViewModel(mViewModel);
        voteFragments.add(candidateNodesFragment);

        MyVoteFragment myVoteFragment = new MyVoteFragment();
        myVoteFragment.setVoteViewModel(mViewModel);
        voteFragments.add(myVoteFragment);

        mDataBinding.ivVoteBack.setOnClickListener(view -> finish());

        mDataBinding.voteViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return voteFragments.get(position);
            }

            @Override
            public int getCount() {
                return voteFragments.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return tabsTitle[position];
            }
        });
        mDataBinding.voteTabLayout.setupWithViewPager(mDataBinding.voteViewPager);
        mDataBinding.voteViewPager.setOffscreenPageLimit(voteFragments.size());

        mViewModel.getProducerSchedule();
    }

    @Override
    protected void bindViewModel() {
        mDataBinding.setViewModel(mViewModel);
    }

    @Override
    protected void initViewModel() {
        mViewModel = new VoteViewModel();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_vote;
    }

    @Override
    protected boolean isSupportLoad() {
        return true;
    }

}
