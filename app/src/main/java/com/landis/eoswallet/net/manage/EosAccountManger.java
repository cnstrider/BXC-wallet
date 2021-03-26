package com.landis.eoswallet.net.manage;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.landis.eoswallet.base.constant.SpConst;
import com.landis.eoswallet.net.listener.EosAccountRefListener;
import com.landis.eoswallet.net.listener.EosListenerManger;
import com.landis.eoswallet.net.model.ChainInfo;
import com.landis.eoswallet.net.model.UserAvailableList;
import com.landis.eoswallet.net.model.UserTokenAvailableList;
import com.landis.eoswallet.util.RxUtils;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class EosAccountManger {
    public Disposable disposable;
    private static EosAccountManger INSTANCE;

    private EosListenerManger listenerManger;

    public HashMap<String, String> balanceMap;
    public String name;

    public boolean isHttpError;

    public void onDestroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
        listenerManger.onDestroy();
        listenerManger = null;
        name = null;
        INSTANCE = null;
    }

    public static EosAccountManger getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EosAccountManger();
        }
        if (INSTANCE.balanceMap == null) {
            INSTANCE.balanceMap = new HashMap<>();
        }
        if (INSTANCE.listenerManger == null) {
            INSTANCE.listenerManger = new EosListenerManger();
        }
        INSTANCE.checkBalance(SPUtils.getInstance().getString(SpConst.WALLET_NAME));
        return INSTANCE;
    }

    public void addEosAccountRefListeners(EosAccountRefListener listener) {
        listenerManger.addEosAccountRefListeners(listener);
    }

    public void removeEosAccountRefListeners(EosAccountRefListener listener) {
        listenerManger.removeEosAccountRefListeners(listener);
    }


    //查询钱包
    @SuppressLint("CheckResult")
    public void checkBalance(String accountName) {
        if (disposable != null && !disposable.isDisposed()) return;

        disposable = Observable.interval(50, 3000, TimeUnit.MILLISECONDS)
                .filter(aLong -> NetworkUtils.isConnected())
                .forEach(chainInfo -> Observable.zip(EosAccountManger.this.getBaseBalance(accountName),EosAccountManger.this.getTokenBalance(accountName), (o1, o2) -> {

                    LogUtils.d("刷新余额=========");

                    if (null!=balanceMap){
                        balanceMap.clear();
                    }
                    //账号不存在重新创建或导入
                    if (o1.rows.isEmpty()){
                        ToastUtils.showShort("账号不存在，请重新创建或导入");
                    }

                    if (o1.rows != null && o1.rows.size() > 0) {
                        for (UserAvailableList.UserAvailable row : o1.rows) {
                            name = o1.rows.get(0).name;
                            String available = row.available;
                            String[] balance = available.split(" ");
                            if (balanceMap == null) {
                                balanceMap = new HashMap<>();
                            }
                            balanceMap.put(balance[1].replace("EOS", "BXC"), balance[0]);
                        }
                    }
                    if (o2.rows != null && o2.rows.size() > 0) {
                        for (UserTokenAvailableList.TokenAvailable row : o2.rows) {
                            String available = row.balance;
                            String[] balance = available.split(" ");
                            if (balanceMap == null) {
                                balanceMap = new HashMap<>();
                            }
                            balanceMap.put(balance[1], balance[0]);
                        }
                    }
//                                if (o4 != null) {
//                                    headBlockProducer = o4.head_block_producer;
//                                    headBlockNum = o4.head_block_num;
//                                }
                    return balanceMap;
                }).compose(RxUtils.applySchedulers()).subscribe(r -> {
                    if (balanceMap != null && !balanceMap.isEmpty()) {
                        isHttpError = false;
                        if (listenerManger != null) listenerManger.balanceFlushed();
                        return;
                    }
                    isHttpError = true;
                }, e -> {
                    isHttpError = true;
                    e.printStackTrace();
                    LogUtils.d("刷新余额====="+e.getMessage());
                }));
        LogUtils.d("刷新余额=====结束");
    }

    @SuppressLint("CheckResult")
    public void checkBalanceManual() {
        String accountName = SPUtils.getInstance().getString(SpConst.WALLET_NAME);
        if (TextUtils.isEmpty(accountName)) {
            return;
        }
        Observable.zip(getBaseBalance(accountName), getTokenBalance(accountName), (o1, o2) -> {

            if (o1.rows != null && o1.rows.size() > 0) {
                for (UserAvailableList.UserAvailable row : o1.rows) {
                    name = o1.rows.get(0).name;
                    String available = row.available;
                    String[] balance = available.split(" ");
                    if (balanceMap == null) {
                        balanceMap = new HashMap<>();
                    }
                    balanceMap.put(balance[1].replace("EOS", "BXC"), balance[0]);
                }
            }
            if (o2.rows != null && o2.rows.size() > 0) {
                for (UserTokenAvailableList.TokenAvailable row : o2.rows) {
                    String available = row.balance;
                    String[] balance = available.split(" ");
                    if (balanceMap == null) {
                        balanceMap = new HashMap<>();
                    }
                    balanceMap.put(balance[1], balance[0]);
                }
            }
//                if (o4 != null) {
//                    headBlockProducer = o4.head_block_producer;
//                    headBlockNum = o4.head_block_num;
//                }
            return balanceMap;
        }).compose(RxUtils.applySchedulers()).subscribe(r -> {
            if (balanceMap != null && !balanceMap.isEmpty()) {
                isHttpError = false;
                if (listenerManger != null) listenerManger.balanceFlushed();
                return;
            }
            isHttpError = true;
        }, e -> {
            isHttpError = true;
            e.printStackTrace();
        });
    }


    public Observable<UserTokenAvailableList> getTokenBalance(String accountName) {
        return HttpManage.apiService.getTokenBalance(accountName, "eosio.token", "accounts", "", 1000, true);
    }

    public Observable<UserAvailableList> getBaseBalance(String accountName) {
        return HttpManage.apiService.getTableRows("eosio", "eosio", "accounts", accountName, 20, true);
    }

    public Observable<ChainInfo> getInfo() {
        return HttpManage.apiService.getChainInfo();
    }

    public String getBalance(String token) {
//        LogUtils.d("获取余额成功"+isHttpError);
//        if (isHttpError) return "0.0000";
        LogUtils.d("获取余额："+SPUtils.getInstance().getString(SpConst.WALLET_NAME));
        token = token.replace("EOS", "BXC");
        if (balanceMap != null && balanceMap.containsKey(token)) {
            return balanceMap.get(token);
        }
        return "0.0000";
    }


}
