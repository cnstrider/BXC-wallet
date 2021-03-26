package com.landis.eoswallet.ui.ringsign.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.base.constant.SpConst;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;
import com.landis.eoswallet.enums.LoadState;
import com.landis.eoswallet.net.manage.HttpManage;
import com.landis.eoswallet.net.model.ProposalBean;
import com.landis.eoswallet.net.model.ProposalInfo;
import com.landis.eoswallet.net.model.RingSignAccountInfo;
import com.landis.eoswallet.net.model.UserAvailableList;
import com.landis.eoswallet.util.RxUtils;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class RingSignViewModel extends BaseViewModel {

    public final ObservableList<Object> ringSignAccounts = new ObservableArrayList<>();

    public final ObservableList<Object> proposalInfos = new ObservableArrayList<>();

    //余额请求次数标记
    private int requestIndex;

    /**
     * 加载本地环签账号
     */
    public void loadRingSignAccount() {
        loadStateObservableField.set(LoadState.LOADING);
        List<RingSignAccountInfo> ringSignAccountInfos = LitePal.findAll(RingSignAccountInfo.class);
        if (ringSignAccountInfos.isEmpty()) {
            loadStateObservableField.set(LoadState.NO_DATA);
        } else {
            getProposeAccountBalance(ringSignAccountInfos);
        }
    }

    /**
     * 获取联合账号余额
     */
    private void getProposeAccountBalance(List<RingSignAccountInfo> ringSignAccountInfos) {
        requestIndex = 0;
        for (RingSignAccountInfo ringSignAccountInfo : ringSignAccountInfos) {
            HttpManage.apiService.getTableRows("eosio", "eosio", "accounts",
                    ringSignAccountInfo.accountName, 20,
                    true).compose(RxUtils.applySchedulers()).subscribe(new Consumer<UserAvailableList>() {
                @Override
                public void accept(UserAvailableList userAvailableList) throws Exception {
                    if (null != userAvailableList && !userAvailableList.rows.isEmpty()) {
                        ringSignAccountInfo.balance =
                                userAvailableList.rows.get(0).available.replace(" EOS", "");
                    } else {
                        ringSignAccountInfo.balance = "0.0000";
                    }
                    setRingSignAccounts(ringSignAccountInfos);
                }
            }, e -> {
                ringSignAccountInfo.balance = "0.0000";
                setRingSignAccounts(ringSignAccountInfos);
            });
        }
    }

    private void setRingSignAccounts(List<RingSignAccountInfo> ringSignAccountInfos) {
        requestIndex++;
        ringSignAccounts.clear();
        ringSignAccounts.addAll(ringSignAccountInfos);
    }

    /**
     * 获取提案
     */
    public void getMyProposal() {
        HttpManage.apiService.getProposals(SPUtils.getInstance().getString(SpConst.WALLET_NAME),
                "eosio.msig", "proposal", 1000, true).compose(RxUtils.applySchedulers()).subscribe(new Consumer<ProposalInfo>() {
            @Override
            public void accept(ProposalInfo proposalInfo) throws Exception {

                loadLocaProposal(proposalInfo.rows);
            }
        }, e -> {
            loadLocaProposal(new ArrayList<>());
        });
    }

    /**
     * 加载本地关注的提案
     *
     * @param rowsBeans
     */
    private void loadLocaProposal(List<ProposalInfo.RowsBean> rowsBeans) {
        List<ProposalBean> proposalBeans = LitePal.findAll(ProposalBean.class);
        if (!proposalBeans.isEmpty()) {
            for (ProposalBean bean : proposalBeans) {
                ProposalInfo.RowsBean rowsBean = new ProposalInfo.RowsBean();
                rowsBean.proposal_name = bean.proposal_name;
                rowsBean.proposer = bean.proposer;
                rowsBeans.add(rowsBean);
            }
        }
        proposalInfos.clear();
        proposalInfos.addAll(rowsBeans);
        loadStateObservableField.set(LoadState.SUCCESS);
    }

    /**
     * 查询提案
     * @param proposerAccount 提案人账户
     * @param proposerName 提案名称
     */
    public void queryPropose(String proposerAccount,String  proposerName){
        if (StringUtils.isEmpty(proposerAccount)){
            ToastUtils.showShort(R.string.proposer_empty);
            return;
        }

        if (StringUtils.isEmpty(proposerName)) {
            ToastUtils.showShort(R.string.proposal_name_empty);
            return;
        }
        ARouter.getInstance().build(RouteConst.AV_PROPOSE_DETAILS).withString(
                "propose_account", proposerAccount)
                .withString("propose_name", proposerName)
                .withBoolean("show_follow", true).navigation();
    }

    /**
     * 打开创建环签账户界面
     */
    public void openRingSign(){
        ARouter.getInstance().build(RouteConst.AV_CREATE_RING_SIGN_ACCOUNT).navigation();
    }
}
