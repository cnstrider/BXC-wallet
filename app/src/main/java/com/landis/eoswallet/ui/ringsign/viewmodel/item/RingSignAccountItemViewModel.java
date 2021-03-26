package com.landis.eoswallet.ui.ringsign.viewmodel.item;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.base.viewmodel.BaseItemViewModel;
import com.landis.eoswallet.net.model.RingSignAccountInfo;

public class RingSignAccountItemViewModel extends BaseItemViewModel<RingSignAccountInfo> {


    public final ObservableField<String> accountName = new ObservableField<>();
    public final ObservableField<String> balance = new ObservableField<>();
    public final ObservableBoolean showConfig = new ObservableBoolean();



    public RingSignAccountItemViewModel() {
    }

    @Override
    protected void setAllModel(@NonNull RingSignAccountInfo ringSignAccountInfo) {
        accountName.set(ringSignAccountInfo.accountName);
        balance.set(ringSignAccountInfo.balance);
        showConfig.set(ringSignAccountInfo.showConfig);

    }

    /**
     * 创建提案
     */
    public void onCreateProposeClickItem() {
       RingSignAccountInfo ringSignAccountInfo = getBaseItemModel();
       if (ringSignAccountInfo.showConfig){
           ToastUtils.showShort(R.string.please_config_weights);
           return;
       }
        ARouter.getInstance().build(RouteConst.AV_CREATE_PROPOSE).withString(
                "propose_account", ringSignAccountInfo.accountName).navigation();

    }

    /**
     * 配置权重
     */
    public void onConfigWeightClickItem() {
        RingSignAccountInfo ringSignAccountInfo = getBaseItemModel();
        if (ringSignAccountInfo.showConfig) {
            ARouter.getInstance().build(RouteConst.AV_CONFIG_RING_SIGN_AUTH).withSerializable("config_account", ringSignAccountInfo).navigation();
        } else {
            ARouter.getInstance().build(RouteConst.AV_WEIGHTS_DETAILS).withString("propose_account", ringSignAccountInfo.accountName).navigation();
        }
    }
}
