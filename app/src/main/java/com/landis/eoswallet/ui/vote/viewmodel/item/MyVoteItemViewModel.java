package com.landis.eoswallet.ui.vote.viewmodel.item;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.landis.eoswallet.BR;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.viewmodel.BaseItemViewModel;
import com.landis.eoswallet.net.manage.EosAccountManger;
import com.landis.eoswallet.net.manage.EosVoteManger;
import com.landis.eoswallet.net.manage.HttpManage;
import com.landis.eoswallet.net.model.VoteNodesList;
import com.landis.eoswallet.util.RxUtils;
import com.landis.eoswallet.widget.dialog.ReceiveClaimDialog;
import com.landis.eoswallet.widget.dialog.VoteDialog;

import java.text.DecimalFormat;

public class MyVoteItemViewModel extends BaseItemViewModel<VoteNodesList.VoteNodes> {

    //2位小数
    private DecimalFormat df = new DecimalFormat("0.00");

    //4位小数
    private DecimalFormat fourdf = new DecimalFormat("0.0000");

    public final ObservableField<String> nodesName = new ObservableField<>();

    public final ObservableField<String> ranking = new ObservableField<>();

    public final ObservableField<String> totalStaked = new ObservableField<>();

    public final ObservableField<String> amount = new ObservableField<>();
    public final ObservableField<String> commissionRate = new ObservableField();
    public final ObservableField<String> rewardsPool = new ObservableField();
    public final ObservableField<String> voteStaked = new ObservableField();
    public final ObservableField<String> unStaking = new ObservableField();
    public final ObservableField<String> claimBalance = new ObservableField();
    public final ObservableField<String> unstakeHeight = new ObservableField();

    @Bindable
    public String unStakeTime;

    private Context context;

    private VoteDialog voteDialog;

    private ReceiveClaimDialog receiveClaimDialog;

    private long lastIrreversibleBlockNum;

    public MyVoteItemViewModel() {
    }

    @Override
    protected void setAllModel(@NonNull VoteNodesList.VoteNodes voteNodes) {
        nodesName.set(voteNodes.name);
        ranking.set(String.valueOf(voteNodes.ranking));
        totalStaked.set(String.valueOf(voteNodes.total_staked));
        amount.set(voteNodes.amount);
        commissionRate.set(String.valueOf(voteNodes.commission_rate));
        rewardsPool.set(voteNodes.rewards_pool);
        voteStaked.set(voteNodes.vote_staked);
        unStaking.set(voteNodes.unstaking);
        claimBalance.set(voteNodes.claim_balance);
        unstakeHeight.set(String.valueOf(voteNodes.unstake_height));
        getUnStakeTime();
    }

    public String getcommissionRate() {
        return df.format(100 - (Long.parseLong(commissionRate.get()) / 100)) + "%";
    }


    public void getUnStakeTime() {
        if (TextUtils.isEmpty(unStaking.get())) {
            return;
        }
        HttpManage.apiService.getChainInfo().compose(RxUtils.applySchedulers())
                .subscribe(chainInfo -> {
                    lastIrreversibleBlockNum = chainInfo.last_irreversible_block_num;
                    if ((lastIrreversibleBlockNum - Long.parseLong(unstakeHeight.get())) >= 86400) {
                        unStakeTime = context.getResources().getString(R.string.thawable);
                    } else {
                        long time =
                                (86400 - (lastIrreversibleBlockNum - Long.parseLong(unstakeHeight.get()))) / 1200;
                        unStakeTime =
                                (((time > 72 ? "72" : time) + context.getResources().getString(R.string.hour_thawable)));
                    }
                    notifyPropertyChanged(BR.unStakeTime);
                });

    }

    /**
     * 投票或者更改投票
     */
    public void onVoteItemClick() {
        VoteNodesList.VoteNodes voteNodes = getBaseItemModel();
        if (null != voteDialog) {
            voteDialog.setData(voteNodes,
                    Float.valueOf(EosAccountManger.getInstance().getBalance("BXC")),
                    voteNodes.voted ? 1 : 2);
            voteDialog.show();
        }
    }

    public void onClaimItemClick() {
        VoteNodesList.VoteNodes voteNodes = getBaseItemModel();
        if (null != receiveClaimDialog) {
            receiveClaimDialog.setData(voteNodes, 3);
            receiveClaimDialog.show();
        }
    }

    public void onUnStakeItemClick() {
        VoteNodesList.VoteNodes voteNodes = getBaseItemModel();
        if ((lastIrreversibleBlockNum - voteNodes.unstake_height) >= 86400) {
            receiveClaimDialog.setData(voteNodes, 4);
            receiveClaimDialog.show();
        }
    }

    public void setContext(Context context) {
        this.context = context;
        voteDialog = new VoteDialog(context);
        voteDialog.setOnItemClickListener(new VoteDialog.OnItemClickListener() {
            @Override
            public void onItemClick(int voteNumber, VoteNodesList.VoteNodes voteNodes,
                                    int voteType) {
                //必须格式化金额
                EosVoteManger.getInstance().vote(voteNodes.name, fourdf.format(voteNumber) + " " +
                        "EOS", "vote", voteType, context);
            }
        });

        receiveClaimDialog = new ReceiveClaimDialog(context);
        receiveClaimDialog.setOnItemReceiveClaimClickListener((voteNodes, voteType) ->
                EosVoteManger.getInstance().vote(voteNodes.name, 4 == voteType ? voteNodes.unstaking : "", 4 == voteType ? "unfreeze" : "claim", voteType, context));
    }
}
