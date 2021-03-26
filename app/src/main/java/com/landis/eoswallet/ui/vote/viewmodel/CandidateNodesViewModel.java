package com.landis.eoswallet.ui.vote.viewmodel;

import android.annotation.SuppressLint;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.landis.eoswallet.base.constant.SpConst;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;
import com.landis.eoswallet.enums.LoadState;
import com.landis.eoswallet.enums.RefreshState;
import com.landis.eoswallet.net.manage.HttpManage;
import com.landis.eoswallet.net.model.UserVoteList;
import com.landis.eoswallet.net.model.VoteNodesList;
import com.landis.eoswallet.util.RxUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CandidateNodesViewModel extends BaseViewModel {

    public final ObservableList<Object> nodesVotes = new ObservableArrayList<>();

    //最新不可逆区块id
    private long lastIrreversibleBlockNum;
    //2位小数
    private DecimalFormat df = new DecimalFormat("0.00");
    //4位小数
    private DecimalFormat fourdf = new DecimalFormat("0.0000");
    //格式化去掉小数点和0
    private NumberFormat numberFormat = new DecimalFormat("0.####");

    private List<UserVoteList.UserVote> userVotes = new ArrayList<>();

    private boolean mRefresh;

    private VoteViewModel voteViewModel;

    public void getChainInfo() {
        HttpManage.apiService.getChainInfo().compose(RxUtils.applySchedulers())
                .subscribe(chainInfo -> lastIrreversibleBlockNum = chainInfo.last_irreversible_block_num);
    }

    public void loadData(VoteViewModel voteViewModel){
        mRefresh = false;
        this.voteViewModel=voteViewModel;
        getVoteData();
    }

    @Override
    public void reloadData() {
        getVoteData();
    }

    public void refreshData(VoteViewModel voteViewModel) {
        this.voteViewModel=voteViewModel;
        mRefresh = true;
        getVoteData();
    }

    /**
     * 加载数据
     */
    public void getVoteData() {

        if (!mRefresh){
            loadStateObservableField.set(LoadState.LOADING);
        }

        HttpManage.apiService.getVoteNodes("eosio", "eosio", "bps", true, 50).compose(RxUtils.applySchedulers()).subscribe(voteNodesList -> {
            if (mRefresh) {
                setRefreshState(RefreshState.REFRESH_END);
            }
            if (!voteNodesList.rows.isEmpty()) {
                Collections.sort(voteNodesList.rows, new Comparator<VoteNodesList.VoteNodes>() {
                    @Override
                    public int compare(VoteNodesList.VoteNodes voteNodes, VoteNodesList.VoteNodes t1) {
                        try {
                            return (int) (t1.total_staked - voteNodes.total_staked);
                        } catch (Exception e) {
                            return 0;
                        }
                    }
                });
                List<VoteNodesList.VoteNodes> voteNodes = new ArrayList<>();
                for (int i = 0; i < voteNodesList.rows.size(); i++) {
                    if (i > 22) {
                        voteNodesList.rows.get(i).ranking = i + 1;
                        LogUtils.d("请求节点时间    获取出块" + System.currentTimeMillis());
                        voteNodesList.rows.get(i).amount = voteViewModel.getAmount(voteNodesList.rows.get(i).name);
                        voteNodes.add(voteNodesList.rows.get(i));
                    }
                }
                getMyVote(voteNodes);
            } else {
                loadStateObservableField.set(LoadState.NO_DATA);
            }
        }, e -> {
            loadStateObservableField.set(LoadState.ERROR);
        });
    }

    /**
     * 获取投票信息
     */
    @SuppressLint("CheckResult")
    private void getMyVote(List<VoteNodesList.VoteNodes> voteNodes) {

        HttpManage.apiService.getVoteRows(SPUtils.getInstance().getString(SpConst.WALLET_NAME), "eosio",
                "votes",
                true, 50)
                .compose(RxUtils.applySchedulers()).subscribe(userVoteList -> {
            if (!userVoteList.rows.isEmpty()) {

                userVotes = userVoteList.rows;
                for (int i = 0; i < voteNodes.size(); i++) {
                    for (int j = 0; j < userVotes.size(); j++) {
                        if (userVotes.get(j).bpname.equals(voteNodes.get(i).name)) {
                            voteNodes.get(i).voted = true;
                            //格式化赎回票数
                            Float unStaking = Float.valueOf(userVotes.get(j).unstaking.replace(" EOS", ""));
                            if (unStaking > 0) {
                                voteNodes.get(i).unstaking = numberFormat.format(unStaking);
                                voteNodes.get(i).unstake_height = userVotes.get(j).unstake_height;

                            }
                            //格式化投票数
                            String formatVoteStaked = numberFormat.format(Float.valueOf(userVotes.get(j).staked.replace(" EOS", "")));
                            voteNodes.get(i).vote_staked = formatVoteStaked;
                            voteNodes.get(i).claim_balance = fourdf.format(calculation(voteNodes.get(i), userVotes.get(j)));
                        }
                    }
                }
            }
            if (null!=nodesVotes&&nodesVotes.size()>0){
                nodesVotes.clear();
            }
            nodesVotes.addAll(voteNodes);
            if (nodesVotes.size()>0){
                if (!mRefresh) {
                    loadStateObservableField.set(LoadState.SUCCESS);
                }
            }else{
                if (!mRefresh) {
                    loadStateObservableField.set(LoadState.NO_DATA);
                }
            }
        },e->{
            loadStateObservableField.set(LoadState.ERROR);
        });
    }

    /**
     * 计算分红
     *
     * @param voteNodes
     * @param userVote
     * @return
     */
    private Float calculation(VoteNodesList.VoteNodes voteNodes, UserVoteList.UserVote userVote) {
        long voteageUpdateHeight = lastIrreversibleBlockNum - userVote.voteage_update_height;

        //我的最新票龄
        Float LatestTicketAge = userVote.voteage + Float.valueOf(userVote.staked.replace("EOS", "").trim()) * voteageUpdateHeight;
        //节点最新票龄
        long total_voteage = voteNodes.total_voteage;
        long total_staked = voteNodes.total_staked;
        long totalVoteageHeight = total_voteage + total_staked * (lastIrreversibleBlockNum - voteNodes.voteage_update_height);
        float claimBalance = (LatestTicketAge / totalVoteageHeight) * Float.valueOf(voteNodes.rewards_pool.replace("EOS", "").trim());
        return claimBalance > 0 ? claimBalance : 0;
    }
}
