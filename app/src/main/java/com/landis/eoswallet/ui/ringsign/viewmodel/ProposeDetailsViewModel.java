package com.landis.eoswallet.ui.ringsign.viewmodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.text.TextUtils;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.landis.eoswallet.BR;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.constant.SpConst;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;
import com.landis.eoswallet.event.Event;
import com.landis.eoswallet.net.manage.EosAccountManger;
import com.landis.eoswallet.net.manage.EosTransferManger;
import com.landis.eoswallet.net.manage.HttpManage;
import com.landis.eoswallet.net.model.ActionInfo;
import com.landis.eoswallet.net.model.ApprovalInfo;
import com.landis.eoswallet.net.model.ProposalBean;
import com.landis.eoswallet.net.model.ProposeStateInfo;
import com.landis.eoswallet.ui.wallet.viewmodel.WalletViewModel;
import com.landis.eoswallet.util.RxUtils;
import com.landis.eoswallet.util.eos.Utils;
import com.landis.eoswallet.widget.dialog.LoadDialog;
import com.landis.eoswallet.widget.dialog.PurseInputDialog;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class ProposeDetailsViewModel extends BaseViewModel {

    //提案者（账户名）
    public String proposeAccount;

    //提案名
    public String proposeName;

    //是否显示关注
    @Bindable
    public boolean showFollow;

    //是否显示空数据
    @Bindable
    public boolean showEmpty;

    //bxc 余额
    private Float bxcBalance;

    //是否有操作权限
    private boolean operationPermission = false;

    //授权权重
    private int authority = 0;

    //提案者（查询结果）
    @Bindable
    public String mProposer;

    //提案名称（查询结果）
    @Bindable
    public String mProposalName;

    //合约名
    @Bindable
    public String mActionName;

    //币种名称
    @Bindable
    public String mTokenName;

    //付款人
    @Bindable
    public String mFrom;

    //收款人
    @Bindable
    public String mTo;

    //金额
    @Bindable
    public String mAmount;


    //备注
    @Bindable
    public String mMemo;

    //过期时间
    @Bindable
    public String mExpirationTime;

    private LoadDialog loadDialog;

    public final ObservableList<Object> proposeStateList = new ObservableArrayList<>();

    private WeakReference<Activity> referenceActivity;

    public void setActivity(Activity activity){
        referenceActivity=new WeakReference<>(activity);
        initLoadDialog();
    }

    private void initLoadDialog(){
        loadDialog = new LoadDialog(referenceActivity.get());
    }

    /**
     * 提案账户
     *
     * @param account
     */
    public void setProposeAccount(String account) {
        this.proposeAccount = account;
        bxcBalance = Float.valueOf(EosAccountManger.getInstance().getBalance("BXC"));
    }

    /**
     * 提案名称
     *
     * @param name
     */
    public void setProposeName(String name) {
        this.proposeName = name;
    }

    /**
     * 是否显示关注按钮
     *
     * @param follow
     */
    public void setShowFollow(boolean follow) {
        this.showFollow = follow;
    }

    /**
     * 查询提案是否存在
     */
    public void getProposals() {
        loadDialog.show();
        loadDialog.setTipContent(referenceActivity.get().getResources().getString(R.string.querying));
        HttpManage.apiService.getProposals(proposeAccount,
                "eosio.msig", "proposal", 1000, true)
                .compose(RxUtils.applySchedulers()).subscribe(proposalInfo -> {
                    if (proposalInfo.rows.isEmpty()) {
                        showEmpty = true;
                        showFollow=false;
                        notifyPropertyChanged(BR.showEmpty);
                        notifyPropertyChanged(BR.showFollow);
                        loadDialog.dismiss();
                    } else {
                        getApproves();
                        getTransferData();
                    }
                }, e -> {
            loadDialog.dismiss();
            showEmpty = true;
            showFollow=false;
            notifyPropertyChanged(BR.showFollow);
            notifyPropertyChanged(BR.showEmpty);
        });
    }

    /**
     * 获取提案状态数据
     */
    private void getApproves() {

        HttpManage.apiService.getApproves(proposeAccount, "eosio.msig", "approvals", 1000, true)
                .compose(RxUtils.applySchedulers()).subscribe(approvalInfo -> {
            loadDialog.dismiss();
            List<ProposeStateInfo> proposeStateInfos = new ArrayList<>();
            for (ApprovalInfo.RowsBean rowsBean : approvalInfo.rows) {
                if (TextUtils.equals(proposeName, rowsBean.proposal_name)) {
                    if (!rowsBean.requested_approvals.isEmpty()) {
                        for (ApprovalInfo.RowsBean.RequestedApprovalsBean requestedApprovalsBean : rowsBean.requested_approvals) {
                            ProposeStateInfo proposeStateInfo = new ProposeStateInfo();
                            proposeStateInfo.accountName = requestedApprovalsBean.actor;
                            proposeStateInfo.state = 0;
                            proposeStateInfos.add(proposeStateInfo);
                            if (proposeStateInfo.accountName.equals(SPUtils.getInstance().getString(SpConst.WALLET_NAME))) {
                                operationPermission = true;
                            }
                        }
                    }
                    if (!rowsBean.provided_approvals.isEmpty()) {
                        for (ApprovalInfo.RowsBean.RequestedApprovalsBean requestedApprovalsBean : rowsBean.provided_approvals) {
                            ProposeStateInfo proposeStateInfo = new ProposeStateInfo();
                            proposeStateInfo.accountName = requestedApprovalsBean.actor;
                            proposeStateInfo.state = 1;
                            proposeStateInfos.add(proposeStateInfo);
                            if (proposeStateInfo.accountName.equals(SPUtils.getInstance().getString(SpConst.WALLET_NAME))) {
                                operationPermission = true;
                            }
                        }
                    }
                }
            }
            if (proposeStateInfos.isEmpty()) {
                showEmpty = true;
                showFollow=false;
                notifyPropertyChanged(BR.showEmpty);
                notifyPropertyChanged(BR.showFollow);
            } else {
                showEmpty = false;
                notifyPropertyChanged(BR.showEmpty);
                notifyPropertyChanged(BR.showFollow);
                proposeStateList.addAll(proposeStateInfos);
            }
        }, e -> {
            loadDialog.dismiss();
            showEmpty = true;
            showFollow=false;
            notifyPropertyChanged(BR.showEmpty);
            notifyPropertyChanged(BR.showFollow);
        });
    }

    /**
     * 获取提案的转账交易数据
     */
    private void getTransferData() {
        HttpManage.apiService.getActions(-1, -1000, "zzzzz")
                .compose(RxUtils.applySchedulers()).subscribe(new Consumer<ActionInfo>() {
            @SuppressLint("CheckResult")
            @Override
            public void accept(ActionInfo actionInfo) throws Exception {
                if (!actionInfo.actions.isEmpty()) {
                    for (int i = 0; i < actionInfo.actions.size(); i++) {
                        ActionInfo.ActionsBean actionsBean = actionInfo.actions.get(i);
                        if (TextUtils.equals(actionsBean.action_trace.act.name, "transfer") &&
                                !StringUtils.isEmpty(actionsBean.action_trace.act.data.memo) &&
                                actionsBean.action_trace.act.data.memo.startsWith("multiple")) {
                            String transferMemo = actionsBean.action_trace.act.data.memo.replace(
                                    "multiple", "");
                            String[] split = transferMemo.split("_");

                            if (TextUtils.equals(split[0], proposeAccount) && TextUtils.equals(split[1], proposeName)) {
                                String[] amount = split[4].split(" ");
                                mProposer = split[0];
                                mProposalName= split[1];

                                mActionName=split[6] ;

                                mTokenName= amount[1].replace("EOS", "BXC");

                                mFrom= split[2];

                                mTo= split[3];

                                mAmount=amount[0];
                                if (split.length > 7){
                                    mMemo=split[7];
                                }

                                mExpirationTime=Utils.UTCToCST(split[5], "yyyy-MM-dd'T'HH:mm:ss");
                                notifyAll();
                                queryAuthority(split[2]);
                            }
                        }
                    }
                }
            }
        }, e -> {
            LogUtils.d(e.getMessage());
        });
    }

    /**
     * 查询环签账户权限比重
     *
     * @param accountName
     */
    private void queryAuthority(String accountName) {
        HttpManage.apiService.checkJointlyAccount(accountName).compose(RxUtils.applySchedulers())
                .subscribe(eosAccountInfo -> {
                    if (!eosAccountInfo.permissions.isEmpty()) {
                        for (int i = 0; i < eosAccountInfo.permissions.size(); i++) {
                            if (eosAccountInfo.permissions.get(i).required_auth.threshold > 1) {
                                authority = eosAccountInfo.permissions.get(i).required_auth.threshold;
                                LogUtils.d("授权权重：" + authority);
                                return;
                            }
                        }
                    }
                });
    }

    public void followPropose() {
        //用户发起的提案  本地不保存
        if (proposeAccount.equals(SPUtils.getInstance().getString(SpConst.WALLET_NAME))) {
            ToastUtils.showShort(R.string.concerned);
            return;
        }

        ProposalBean proposalInfo = new ProposalBean();
        proposalInfo.proposal_name = proposeName;
        proposalInfo.proposer = proposeAccount;
        upFollowData(proposalInfo);
        EventBus.getDefault().post(new Event.RefreshPropose());
        ToastUtils.showShort(R.string.care_success);
    }

    /**
     * 更新数据
     *
     * @param proposalBean
     */
    private void upFollowData(ProposalBean proposalBean) {
        List<ProposalBean> all = LitePal.findAll(ProposalBean.class);
        for (ProposalBean rowsBean : all) {
            if (rowsBean.proposal_name.equals(proposalBean.proposal_name)) {
                rowsBean.delete();
                proposalBean.save();
                return;
            }
        }
        proposalBean.save();
    }


    /**
     * 提案操作
     *
     * @param action 操作类型
     */
    public void msigMultiple(String action) {

        if ((TextUtils.equals("approve", action) || TextUtils.equals("unapprove", action)) && !operationPermission) {
            ToastUtils.showShort(R.string.no_permission_to_execute);
            return;
        }

        if (TextUtils.equals("cancel", action)) {
            if (!proposeAccount.equals(SPUtils.getInstance().getString(SpConst.WALLET_NAME))) {
                ToastUtils.showShort(R.string.cancel_proposal_insufficient_authority);
                return;
            }
        }

        if (TextUtils.equals("exec", action)) {
            int authorityCount = 0;
            for (int i = 0; i < proposeStateList.size(); i++) {
                ProposeStateInfo proposeStateInfo =
                        (ProposeStateInfo) proposeStateList.get(i);
                if (proposeStateInfo.state == 1) {
                    authorityCount++;
                }
            }
            if (authority == 0 && authorityCount != proposeStateList.size()) {
                ToastUtils.showShort(R.string.execute_authorization_insufficient);
                return;
            } else if (authorityCount < authority) {
                ToastUtils.showShort(R.string.execute_authorization_insufficient);
                return;
            }

        }

        if (bxcBalance < 1F) {
            ToastUtils.showShort(R.string.balance_insufficient);
            return;
        }
        PurseInputDialog inputDialog = new PurseInputDialog(referenceActivity.get());
        inputDialog.setOnClickListener(view -> {
            String key = (String) view.getTag();
            String text = SPUtils.getInstance().getString(SpConst.P_K);
            if (TextUtils.isEmpty(text)) {
                return;
            }
            WalletViewModel walletVM = new WalletViewModel();
            byte[] bytes = EncryptUtils.decryptHexStringAES(text,
                    EncryptUtils.encryptMD5ToString(key.getBytes()).getBytes(), "AES/CBC" +
                            "/PKCS5Padding", walletVM.hexString2Bytes(walletVM.iv));
            if (bytes == null || bytes.length == 0) {
                ToastUtils.showShort(R.string.password_error);
                return;
            }
            String pri = new String(bytes);
            loadDialog.show();
            loadDialog.setTipContent(referenceActivity.get().getResources().getString(R.string.submitting));
            //多签提案操作
            EosTransferManger.getInstance().msigMultiple(pri, proposeName, proposeAccount, action
                    , (isSuccess, o) -> {
                        loadDialog.dismiss();
                        if (!isSuccess) {
                            ToastUtils.showShort(R.string.fail + o.toString());
                        } else {
                            ToastUtils.showShort(R.string.success);
                            if (TextUtils.equals("exec", action) || TextUtils.equals("cancel", action)) {
                                EventBus.getDefault().post(new Event.RefreshPropose());
                                referenceActivity.get().finish();
                            } else {
                                getApproves();
                            }
                        }
                    });

        });
        inputDialog.show();
    }
}
