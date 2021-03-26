package com.landis.eoswallet.ui.wallet.viewmodel;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.landis.eoswallet.BR;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.base.constant.SpConst;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;
import com.landis.eoswallet.enums.LoadState;
import com.landis.eoswallet.enums.RefreshState;
import com.landis.eoswallet.net.manage.EosAccountManger;
import com.landis.eoswallet.net.manage.HttpManage;
import com.landis.eoswallet.net.model.ActionInfo;
import com.landis.eoswallet.net.model.TokenTransactionRecord;
import com.landis.eoswallet.net.model.UserVoteList;
import com.landis.eoswallet.util.RxUtils;
import com.landis.eoswallet.util.eos.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;


public class WalletDetailViewModel extends BaseViewModel {

    public final ObservableList<Object> transactionRecords = new ObservableArrayList<>();

    //币种名称
    @Bindable
    public String tokenName;

    //总余额
    @Bindable
    public Float totalAssets = 0.000F;

    //投票金额
    @Bindable
    public Float voteBalance = 0.000F;

    //赎回金额
    @Bindable
    public Float redeemVoteBalance = 0.000F;

    //分红金额
    @Bindable
    public Float claimBalance = 0.000F;

    DecimalFormat df = new DecimalFormat("0.0000");

    //最新不可逆区块id
    private long lastIrreversibleBlockNum;

    private boolean mRefresh;

    public void setTokenName(String name) {
        this.tokenName = name;
        notifyPropertyChanged(BR.tokenName);
    }

    @Override
    public void reloadData() {
        loadRecords();
    }


    public void refreshData() {
        mRefresh = true;
        loadRecords();
    }

    /**
     * 获取块 信息
     */
    public void getChain(List<UserVoteList.UserVote> userVotes) {
        HttpManage.apiService.getChainInfo().compose(RxUtils.applySchedulers())
                .subscribe(chainInfo -> {
                    lastIrreversibleBlockNum = chainInfo.last_irreversible_block_num;
                    getVoteNodes(userVotes);
                }, e -> {
                    LogUtils.e("股票信息  getChainInfo:" + e.getMessage());
                });
    }

    /**
     * 获取投票信息
     */
    public void getMyVote() {
        HttpManage.apiService.getVoteRows(SPUtils.getInstance().getString(SpConst.WALLET_NAME), "eosio", "votes", true, 50)
                .compose(RxUtils.applySchedulers()).subscribe(new Consumer<UserVoteList>() {
            @Override
            public void accept(UserVoteList userVoteList) throws Exception {
                if (!userVoteList.rows.isEmpty()) {
                    getChain(userVoteList.rows);
                }else{
                    totalAssets=Float.valueOf(EosAccountManger.getInstance().getBalance(tokenName));
                    notifyPropertyChanged(BR.totalAssets);
                }
            }
        }, e -> {
            totalAssets=Float.valueOf(EosAccountManger.getInstance().getBalance(tokenName));
            notifyPropertyChanged(BR.totalAssets);
            LogUtils.e("股票信息 getVoteRows:" + e.getMessage());
        });
    }

    /**
     * 加载交易记录
     */
    public void loadRecords() {
        List<TokenTransactionRecord> tokenTransactionRecords = new ArrayList<>();
        if (!mRefresh) {
            loadStateObservableField.set(LoadState.LOADING);
        }

        HttpManage.apiService.getActions(-1, -10, SPUtils.getInstance().getString(SpConst.WALLET_NAME))
                .compose(RxUtils.applySchedulers()).subscribe(new Consumer<ActionInfo>() {
            @Override
            public void accept(ActionInfo actionInfo) throws Exception {
                if (mRefresh) {
                    setRefreshState(RefreshState.REFRESH_END);
                }
                if (!actionInfo.actions.isEmpty()) {
                    for (int i = 0; i < actionInfo.actions.size(); i++) {
                        ActionInfo.ActionsBean actionsBean = actionInfo.actions.get(i);

                        if (TextUtils.equals(actionsBean.action_trace.act.name, "transfer")&&!StringUtils.isEmpty(actionsBean.action_trace.act.data.quantity)) {
                            String currencyName=actionsBean.action_trace.act.data.quantity.split(" ")[1].replace("EOS","BXC");
                            if (TextUtils.equals(tokenName,currencyName)){
                                TokenTransactionRecord tokenTransactionRecord = new TokenTransactionRecord();
                                tokenTransactionRecord.Payer = actionsBean.action_trace.act.data.from;
                                tokenTransactionRecord.Remitter = actionsBean.action_trace.act.data.to;
                                String time = actionsBean.action_trace.block_time.substring(0, actionsBean.action_trace.block_time.length() - 4);
                                tokenTransactionRecord.blcok_time = Utils.UTCToCST(time, "yyyy-MM-dd'T'HH:mm:ss");
                                tokenTransactionRecord.Amount = actionsBean.action_trace.act.data.quantity.split(" ")[0];
                                tokenTransactionRecord.memo = actionsBean.action_trace.act.data.memo;
                                tokenTransactionRecord.Transaction_id = actionsBean.action_trace.trx_id;
                                tokenTransactionRecords.add(tokenTransactionRecord);
                            }
                        }
                    }
                }

                loadStateObservableField.set(LoadState.SUCCESS);
                if (!tokenTransactionRecords.isEmpty()) {
                    for (int i = 0; i < tokenTransactionRecords.size(); i++)  //外循环是循环的次数
                    {
                        for (int j = tokenTransactionRecords.size() - 1; j > i; j--)  //内循环是 外循环一次比较的次数
                        {
                            if (TextUtils.equals(tokenTransactionRecords.get(i).Transaction_id,tokenTransactionRecords.get(j).Transaction_id)){
                                tokenTransactionRecords.remove(j);
                            }
                        }
                    }

                    transactionRecords.clear();
                    transactionRecords.addAll(tokenTransactionRecords);
                }

            }
        }, e -> {
            if (mRefresh){
                setRefreshState(RefreshState.REFRESH_END);
            }
            loadStateObservableField.set(LoadState.SUCCESS);
        });
    }


    /**
     * 获取全部节点信息
     */
    public void getVoteNodes(List<UserVoteList.UserVote> userVotes) {
        HttpManage.apiService.getVoteNodes("eosio", "eosio", "bps", true, 50).compose(RxUtils.applySchedulers())
                .subscribe(voteNodesList -> {
                    if (!voteNodesList.rows.isEmpty()) {
                        //计算分红 投票总额  赎回总额
                        for (int i = 0; i < voteNodesList.rows.size(); i++) {
                            for (int j = 0; j < userVotes.size(); j++) {
                                if (userVotes.get(j).bpname.equals(voteNodesList.rows.get(i).name)) {
                                    voteBalance = voteBalance + Float.valueOf(userVotes.get(j).staked.replace("EOS", "").trim());
                                    redeemVoteBalance = redeemVoteBalance + Float.valueOf(userVotes.get(j).unstaking.replace("EOS", "").trim());
                                    long voteageUpdateHeight = lastIrreversibleBlockNum - userVotes.get(j).voteage_update_height;

                                    //我的最新票龄
                                    Float LatestTicketAge = userVotes.get(j).voteage + Float.valueOf(userVotes.get(j).staked.replace("EOS", "").trim()) * voteageUpdateHeight;
                                    //节点最新票龄
                                    long total_voteage = voteNodesList.rows.get(i).total_voteage;
                                    long total_staked = voteNodesList.rows.get(i).total_staked;
                                    long totalVoteageHeight = total_voteage + total_staked * (lastIrreversibleBlockNum - voteNodesList.rows.get(i).voteage_update_height);
                                    if (0 == totalVoteageHeight) {
                                        continue;
                                    }
                                    float eos = (LatestTicketAge / totalVoteageHeight) * Float.valueOf(voteNodesList.rows.get(i).rewards_pool.replace("EOS", "").trim());
                                    if (eos <= 0) {
                                        continue;
                                    }
                                    claimBalance = claimBalance + eos;
                                    LogUtils.e("股票信息  getChainInfo 分红:" + eos + "分红总额" + claimBalance);
                                    if (0 != claimBalance) {
                                        claimBalance = Float.valueOf(df.format(claimBalance));
                                        settotalAssets();
                                    }
                                }
                            }
                        }
                        if (voteBalance != 0) {
                            voteBalance = Float.valueOf(df.format(voteBalance));
                        }
                        if (redeemVoteBalance != 0) {
                            redeemVoteBalance = Float.valueOf(df.format(redeemVoteBalance));
                        }
                        settotalAssets();
                        notifyPropertyChanged(BR.voteBalance);
                        notifyPropertyChanged(BR.redeemVoteBalance);
                        notifyPropertyChanged(BR.claimBalance);
                    }
                });
    }

    /**
     * 总资产
     */
    private void settotalAssets() {
        totalAssets = voteBalance + redeemVoteBalance
                + Float.valueOf(EosAccountManger.getInstance().getBalance(tokenName)) + claimBalance;
        notifyPropertyChanged(BR.totalAssets);
    }

    /**
     * 改变透明度
     *
     * @param color
     * @param fraction
     * @return
     */
    public int changeAlpha(int color, float fraction) {

        int red = Color.red(color);

        int green = Color.green(color);

        int blue = Color.blue(color);

        int alpha = (int) (Color.alpha(color) * fraction);

        return Color.argb(alpha, red, green, blue);

    }

    public void openTransaction(View view){
        ARouter.getInstance().build(RouteConst.AV_TRANSACTION).withString(RouteConst.TOKEN_NAME, tokenName).navigation(view.getContext());
    }
}
