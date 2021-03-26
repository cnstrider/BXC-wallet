package com.landis.eoswallet.ui.vote.viewmodel;

import android.databinding.Bindable;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.landis.eoswallet.BR;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;
import com.landis.eoswallet.event.Event;
import com.landis.eoswallet.net.manage.HttpManage;
import com.landis.eoswallet.net.model.ProducerScheduleInfo;
import com.landis.eoswallet.net.model.ProducersInfo;
import com.landis.eoswallet.util.RxUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;
import java.util.List;

import io.reactivex.functions.Consumer;

public class VoteViewModel extends BaseViewModel {

    @Bindable
    public String voteVersion;

    private List<ProducersInfo.RowsBean.ProducersBean> producersBeans;

    /**
     * 获取投票界数
     */
    public void getProducerSchedule() {
        HttpManage.apiService.getProducerSchedule().compose(RxUtils.applySchedulers()).subscribe(new Consumer<ProducerScheduleInfo>() {
            @Override
            public void accept(ProducerScheduleInfo producerScheduleInfo) throws Exception {
                voteVersion = String.valueOf(producerScheduleInfo.active.version);
                notifyPropertyChanged(BR.voteVersion);
                getVoteBlockOut();
            }
        }, e -> {
            EventBus.getDefault().postSticky(new Event.LoadVoteBlockOutSuccess());
        });
    }

    /**
     * 获取出块高度
     * @param bpname
     * @return
     */
    public String getAmount(String bpname) {
        if (null == producersBeans || producersBeans.size() == 0) {
            return "0";
        }
        Iterator<ProducersInfo.RowsBean.ProducersBean> iterator = producersBeans.iterator();
        for (ProducersInfo.RowsBean.ProducersBean producersBean : producersBeans) {
            if (TextUtils.equals(producersBean.getBpname(), bpname)) {
                return String.valueOf(iterator.next().getAmount());
            }
        }
        return "0";
    }

    /**
     * 获取出块数
     */
    public void getVoteBlockOut() {
        HttpManage.apiService.getVoteBlockOut("eosio", "eosio", "schedules", voteVersion, 50, true)
                .compose(RxUtils.applySchedulers()).subscribe(new Consumer<ProducersInfo>() {
            @Override
            public void accept(ProducersInfo producersInfo) throws Exception {
                producersBeans = producersInfo.getRows().get(0).getProducers();
                LogUtils.d("请求节点时间" + System.currentTimeMillis());
                EventBus.getDefault().post(new Event.LoadVoteBlockOutSuccess());
            }
        }, e -> {
            EventBus.getDefault().post(new Event.LoadVoteBlockOutSuccess());
        });
    }

}
