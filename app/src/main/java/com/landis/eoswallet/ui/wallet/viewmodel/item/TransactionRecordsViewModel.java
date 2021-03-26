package com.landis.eoswallet.ui.wallet.viewmodel.item;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.landis.eoswallet.base.viewmodel.BaseItemViewModel;
import com.landis.eoswallet.net.model.TokenTransactionRecord;

public class TransactionRecordsViewModel extends BaseItemViewModel<TokenTransactionRecord> {

    public final  ObservableBoolean income = new ObservableBoolean();
    public final ObservableField<String> payer = new ObservableField<>();
    public final ObservableField<String> remitter = new ObservableField<>();
    public final ObservableField<String> memo = new ObservableField<>();
    public final ObservableField<String> blcokTime = new ObservableField<>();
    public final ObservableField<String> amount = new ObservableField<>();


    @Override
    protected void setAllModel(@NonNull TokenTransactionRecord tokenTransactionRecord) {
        income.set(tokenTransactionRecord.isIncome());
        payer.set(tokenTransactionRecord.Payer);
        remitter.set(tokenTransactionRecord.Remitter);
        memo.set(tokenTransactionRecord.memo);
        blcokTime.set(tokenTransactionRecord.blcok_time);
        amount.set(tokenTransactionRecord.Amount);

    }
}
