package com.landis.eoswallet.ui.ringsign.viewmodel.item;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;

import com.landis.eoswallet.base.viewmodel.BaseItemViewModel;
import com.landis.eoswallet.net.model.ProposeStateInfo;

public class ProposeDetailesItemViewModel extends BaseItemViewModel<ProposeStateInfo> {

    public final ObservableField<String> accountName = new ObservableField<>();
    public final ObservableInt state = new ObservableInt();

    @Override
    protected void setAllModel(@NonNull ProposeStateInfo proposeStateInfo) {
        accountName.set(proposeStateInfo.accountName);
        state.set(proposeStateInfo.state);
    }
}
