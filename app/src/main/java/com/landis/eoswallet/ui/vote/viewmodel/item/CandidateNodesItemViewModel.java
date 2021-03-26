package com.landis.eoswallet.ui.vote.viewmodel.item;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.landis.eoswallet.base.viewmodel.BaseItemViewModel;
import com.landis.eoswallet.net.manage.EosAccountManger;
import com.landis.eoswallet.net.manage.EosVoteManger;
import com.landis.eoswallet.net.model.VoteNodesList;
import com.landis.eoswallet.widget.dialog.VoteDialog;

import java.text.DecimalFormat;

public class CandidateNodesItemViewModel extends BaseItemViewModel<VoteNodesList.VoteNodes> {

    //2位小数
    private DecimalFormat df = new DecimalFormat("0.00");
    //4位小数
    private DecimalFormat fourdf = new DecimalFormat("0.0000");

    public final ObservableField<String> nodesName = new ObservableField<>();

    public final ObservableField<String> ranking = new ObservableField<>();

    public final ObservableField<String> totalStaked = new ObservableField<>();

    public final ObservableField<String> commissionRate = new ObservableField();
    public final ObservableField<String> rewardsPool = new ObservableField();
    public final ObservableBoolean voted = new ObservableBoolean();

    private VoteDialog voteDialog;


    public CandidateNodesItemViewModel() {
    }
    @Override
    protected void setAllModel(@NonNull VoteNodesList.VoteNodes voteNodes) {
        nodesName.set(voteNodes.name);
        ranking.set(String.valueOf(voteNodes.ranking));
        totalStaked.set(String.valueOf(voteNodes.total_staked));
        commissionRate.set(String.valueOf(voteNodes.commission_rate));
        rewardsPool.set(voteNodes.rewards_pool);
        voted.set(voteNodes.voted);
    }

    public String getcommissionRate(){
        return df.format(100 - (Long.parseLong(commissionRate.get()) / 100)) + "%";
    }

    /**
     * 投票或者更改投票
     */
    public void onClickItem() {
        VoteNodesList.VoteNodes voteNodes = getBaseItemModel();
        if (null != voteDialog) {
            voteDialog.setData(voteNodes, Float.valueOf(EosAccountManger.getInstance().getBalance("BXC")), voteNodes.voted ? 1 : 2);
            voteDialog.show();
        }
    }


    public void setContext(Context context){
        voteDialog = new VoteDialog(context);
        voteDialog.setOnItemClickListener(new VoteDialog.OnItemClickListener() {
            @Override
            public void onItemClick(int voteNumber, VoteNodesList.VoteNodes voteNodes,
                                    int voteType) {
                //必须格式化金额
                EosVoteManger.getInstance().vote(voteNodes.name, fourdf.format(voteNumber) + " " +
                        "EOS", "vote", voteType,context);
            }
        });
    }


}
