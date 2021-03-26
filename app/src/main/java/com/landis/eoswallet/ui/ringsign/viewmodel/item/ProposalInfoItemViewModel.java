package com.landis.eoswallet.ui.ringsign.viewmodel.item;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.base.constant.SpConst;
import com.landis.eoswallet.base.viewmodel.BaseItemViewModel;
import com.landis.eoswallet.net.model.ProposalInfo;

public class ProposalInfoItemViewModel extends BaseItemViewModel<ProposalInfo.RowsBean> {

    public final ObservableField<String> proposalName = new ObservableField<>();
    public final ObservableField<String> proposer = new ObservableField<>();

    public ProposalInfoItemViewModel() {
    }

    @Override
    protected void setAllModel(@NonNull ProposalInfo.RowsBean rowsBean) {
        proposalName.set(rowsBean.proposal_name);
        proposer.set(rowsBean.proposer);
    }

    /**
     * 查看提案详情
     */
    public void seeProposeDetails() {
        ProposalInfo.RowsBean rowsBean = getBaseItemModel();
        ARouter.getInstance()
                .build(RouteConst.AV_PROPOSE_DETAILS).withString(
                "propose_account", StringUtils.isEmpty(rowsBean.proposer) ? SPUtils.getInstance().getString(SpConst.WALLET_NAME) : rowsBean.proposer)
                .withString("propose_name", rowsBean.proposal_name).navigation();
    }
}
