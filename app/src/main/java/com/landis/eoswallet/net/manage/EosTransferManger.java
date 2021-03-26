package com.landis.eoswallet.net.manage;

import android.annotation.SuppressLint;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.landis.eoswallet.base.constant.SpConst;
import com.landis.eoswallet.net.model.ChainBlock;
import com.landis.eoswallet.net.model.ChainInfo;
import com.landis.eoswallet.net.model.CreateAccountInfo;
import com.landis.eoswallet.net.model.CreatePossessionInfo;
import com.landis.eoswallet.net.model.EosAccountInfo;
import com.landis.eoswallet.net.model.TransactionPushRequest;
import com.landis.eoswallet.net.model.TransactionRequest;
import com.landis.eoswallet.net.model.TransferMessageInfo;
import com.landis.eoswallet.util.GsonUtils;
import com.landis.eoswallet.util.RxUtils;
import com.landis.eoswallet.util.eos.ec.EcDsa;
import com.landis.eoswallet.util.eos.ec.EcSignature;
import com.landis.eoswallet.util.eos.ec.EosPrivateKey;
import com.landis.eoswallet.util.eos.types.TypeChainId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 步骤：
 * 一、序列化转账（创建账号或其他）交易的 json
 * 二、获取 EOS 区块链的最新区块号
 * 三、获取最新区块的具体信息
 * 四、创建一个trasaction
 * 五、获取交易费用
 * 六、签名并推送交易
 */
public class EosTransferManger {

    private static EosTransferManger INSTANCE;

    public static EosTransferManger getInstance() {
        if (INSTANCE == null) INSTANCE = new EosTransferManger();
        return INSTANCE;
    }

    //格式化数据
    @SuppressLint("CheckResult")
    public void getJson2Bin(String action, String memo, String to, String quantity, String from,
                            Callback callback) {
        //Sample   mome = "测试转账"    to="eosio"   quantity="0.0100 EOS"  from="test"
        TransferMessageInfo info = new TransferMessageInfo(memo, to, quantity, from);
        String message = GsonUtils.getInstance().toJson(info);
        JsonObject obj = GsonUtils.getInstance().fromJson(message, JsonObject.class);
        JsonObject object = new JsonObject();
        object.addProperty("action", "transfer");
        object.addProperty("code", action);//.token
        object.add("args", obj);
        LogUtils.d("序列化数据" + object.toString());
        HttpManage.apiService.json2bean(object).compose(RxUtils.applySchedulers()).subscribe(o -> {
            if (o.has("binargs")) {
                callback.onback(true, o.get("binargs").getAsString());
//                getChainInfo(action, o.get("binargs").getAsString());
            } else {
//                ToastUtils.showShort("序列化数据失败");
                callback.onback(false, "序列化数据失败");
            }
        }, throwable -> {
            throwable.printStackTrace();
            callback.onback(false, "序列化数据失败" + throwable.getMessage());
        });
    }

    //获取区块链状态
    @SuppressLint("CheckResult")
    private void getChainInfo(String action, String data, Callback callback) {
        HttpManage.apiService.getChainInfo().compose(RxUtils.applySchedulers()).subscribe(o -> {
            callback.onback(true, o);
            LogUtils.d("区块id" + o.head_block_num);
//            getChainBlock(action, data, o);
        }, throwable -> {
            throwable.printStackTrace();
            callback.onback(false, "获取最新区块号失败" + throwable.getMessage());

        });
    }

    //
    @SuppressLint("CheckResult")
    private void getChainBlock(String strAction, String data, ChainInfo info, String actionName,
                               Callback callback) {
        HttpManage.apiService.getChainBlock(String.valueOf(info.head_block_num)).compose(RxUtils.applySchedulers()).subscribe(o -> {
            callback.onback(true, createTransactionRe(strAction, o, data, actionName, null));
        }, throwable -> {
            throwable.printStackTrace();
            callback.onback(false, "最新区块的信息失败" + throwable.getMessage());
        });
    }

    @SuppressLint("CheckResult")
    private void getRequiredFee(ChainInfo info, TransactionRequest transactionRe,
                                Callback callback) {
        JsonObject object = new JsonObject();
        object.add("transaction",
                GsonUtils.getInstance().fromJson(GsonUtils.getInstance().toJson(transactionRe),
                        JsonObject.class));
        LogUtils.d("交易参数" + object.toString());
        HttpManage.apiService.getRequiredFee(object).compose(RxUtils.applySchedulers()).subscribe(o -> {
            transactionRe.fee = o.required_fee;
            LogUtils.d("手续费----" + transactionRe.actions.get(0).name + "----" + o.required_fee);
            callback.onback(true, transactionRe);
        }, throwable -> {
            throwable.printStackTrace();
            callback.onback(false, "获取交易费用失败" + throwable.getMessage());
        });
    }


    @SuppressLint("CheckResult")
    private void signAndPushTransaction(String key, ChainInfo info,
                                        TransactionRequest transactionRe, Callback callback) {
        EcSignature sign =
                EcDsa.sign(transactionRe.getDigestForSignature(new TypeChainId(info.chain_id)),
                        new EosPrivateKey(key));
        transactionRe.signatures.add(sign.toString());
        TransactionPushRequest request = new TransactionPushRequest();
        request.transaction = transactionRe;
        request.compression = "none";
        request.signatures = new ArrayList<>();
        request.signatures.add(sign.toString());
        JsonObject object =
                GsonUtils.getInstance().fromJson(GsonUtils.getInstance().toJson(request),
                        JsonObject.class);

        LogUtils.d("签名参数" + object.toString());
        HttpManage.apiService.pushTransaction(object).compose(RxUtils.applySchedulers()).subscribe(o -> {
            if (o.has("transaction_id")) {
                callback.onback(true,
                        o.get("transaction_id").getAsString() + "time=" + o.getAsJsonObject(
                                "processed").get("block_time").getAsString());
            } else {
                callback.onback(false, "签名推送到链失败");
            }
        }, e -> {
            e.printStackTrace();
            callback.onback(false, "签名推送到链失败" + e.getMessage());
        });
    }

    private TransactionRequest createTransactionRe(String strAction, ChainBlock o, String data,
                                                   String actionName, String actor) {
        TransactionRequest request = new TransactionRequest();
        request.ctxFreeData = new ArrayList<>();
        request.signatures = new ArrayList<>();
        request.actions = new ArrayList<>();
        TransactionRequest.Action action = new TransactionRequest.Action();
        action.account = strAction;
        action.authorization = new ArrayList<>();
        TransactionRequest.Auth auth = new TransactionRequest.Auth();
        if (StringUtils.isEmpty(actor)) {
            auth.actor = SPUtils.getInstance().getString(SpConst.WALLET_NAME);
        } else {
            auth.actor = actor;
        }
        auth.permission = "active";
        action.authorization.add(auth);
        action.data = data;
        action.name = actionName;
//        if (createAccount) {
//            action.name = "newaccount";
//        } else {
//            action.name = "transfer";
//        }
        request.actions.add(action);
        request.context_free_actions = new ArrayList<>();
        request.transaction_extensions = new ArrayList<>();
        if (o != null) {
            request.expiration = o.getTimeAfterHeadBlockTime(3000000);
            request.ref_block_num = o.block_num;
            request.ref_block_prefix = o.ref_block_prefix;
        }
        return request;
    }

    public interface Callback {
        void onback(boolean isSuccess, Object o);
    }


    public void pay(String key, String action, String memo, String to, String quantity,
                    String from, Callback callback) {
        quantity = quantity.replace("BXC", "EOS");
        getJson2Bin(action, memo, to, quantity, from, (isSuccess, o) -> {
            if (isSuccess) {
                getChainInfo(action, (String) o, (isSuccess1, o1) -> {
                    if (isSuccess1) {
                        getChainBlock(action, (String) o, (ChainInfo) o1, "transfer", (isSuccess2
                                , o2) -> {
                            if (isSuccess2) {
                                getRequiredFee((ChainInfo) o1, (TransactionRequest) o2,
                                        (isSuccess3, o3) -> {
                                    if (isSuccess3) {
                                        signAndPushTransaction(key, (ChainInfo) o1,
                                                (TransactionRequest) o3, callback);
                                    } else {
                                        callback.onback(false, o3);
                                    }
                                });
                            } else {
                                callback.onback(false, o2);
                            }
                        });
                    } else {
                        callback.onback(false, o1);
                    }
                });
            } else {
                callback.onback(false, o);
            }
        });
    }


    public void getRequiredFee(String action, String memo, String to, String quantity,
                               String from, Callback callback) {
        getJson2Bin(action, memo, to, quantity, from, (isSuccess, o) -> {
            if (isSuccess) {
                getChainInfo(action, (String) o, (isSuccess1, o1) -> {
                    if (isSuccess1) {
                        getChainBlock(action, (String) o, (ChainInfo) o1, "transfer", (isSuccess2
                                , o2) -> {
                            if (isSuccess2) {
                                getRequiredFee((ChainInfo) o1, (TransactionRequest) o2, callback);
                            } else {
                                callback.onback(false, "");
                            }
                        });
                    } else {
                        callback.onback(false, "");
                    }
                });
            } else {
                callback.onback(false, "");
            }
        });
    }
//    public static void UTCtoCST(String utc){//CST可视为美国、澳大利亚、古巴或中国的标准时间,北京时间与utc时间相差8小时
//        ZonedDateTime zdt  = ZonedDateTime.parse(utc);
//        LocalDateTime localDateTime = zdt.toLocalDateTime();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM HH:mm:ss");
//        System.out.println("北京时间："+formatter.format(localDateTime.plusHours(8)));
//    }"yyyy-MM-dd'T'HH:mm:ss"


    //格式化数据
    @SuppressLint("CheckResult")
    public void createAccountJson2Bin(String newUserName, String mOwnerKeyStr, String mActiveKey,
                                      Callback callback) {
        //创建账户 json转bin

        CreateAccountInfo createAccountBean = new CreateAccountInfo();

        CreateAccountInfo.ArgsBean argsBean = new CreateAccountInfo.ArgsBean();
        argsBean.setCreator(SPUtils.getInstance().getString(SpConst.WALLET_NAME));
        argsBean.setName(newUserName);

        CreateAccountInfo.ArgsBean.ActiveBean activeBean =
                new CreateAccountInfo.ArgsBean.ActiveBean();
        activeBean.setThreshold(1);


        List<CreateAccountInfo.ArgsBean.ActiveBean.KeysBeanX> keysBeanXList = new ArrayList<>();
        CreateAccountInfo.ArgsBean.ActiveBean.KeysBeanX keysBeanX =
                new CreateAccountInfo.ArgsBean.ActiveBean.KeysBeanX();
        keysBeanX.setKey(mActiveKey);
        keysBeanX.setWeight(1);
        keysBeanXList.add(keysBeanX);
        activeBean.setKeys(keysBeanXList);
        activeBean.setWaits(new ArrayList<>());
        activeBean.setAccounts(new ArrayList<>());

        argsBean.setActive(activeBean);
        CreateAccountInfo.ArgsBean.OwnerBean ownerBean = new CreateAccountInfo.ArgsBean.OwnerBean();

        ownerBean.setThreshold(1);
        ownerBean.setWaits(new ArrayList<>());
        ownerBean.setAccounts(new ArrayList<>());
        List<CreateAccountInfo.ArgsBean.OwnerBean.KeysBean> keysBeans = new ArrayList<>();
        CreateAccountInfo.ArgsBean.OwnerBean.KeysBean keysBean =
                new CreateAccountInfo.ArgsBean.OwnerBean.KeysBean();
        keysBean.setKey(mOwnerKeyStr);
        keysBean.setWeight(1);
        keysBeans.add(keysBean);
        ownerBean.setKeys(keysBeans);

        argsBean.setOwner(ownerBean);

        createAccountBean.setArgs(argsBean);

        createAccountBean.setCode("eosio");
        createAccountBean.setAction("newaccount");
        String accountBean = GsonUtils.getInstance().toJson(createAccountBean);
        JsonObject obj = GsonUtils.getInstance().fromJson(accountBean, JsonObject.class);
        LogUtils.d("新建账户数据："+obj.toString());
        HttpManage.apiService.jsonTobean(obj).compose(RxUtils.applySchedulers()).subscribe(o -> {
            if (o.has("binargs")) {
                callback.onback(true, o.get("binargs").getAsString());
//                getChainInfo(action, o.get("binargs").getAsString());
            } else {
//                ToastUtils.showShort("序列化数据失败");
                callback.onback(false, "序列化数据失败");
            }
        }, throwable -> {
            throwable.printStackTrace();
            callback.onback(false, "序列化数据失败" + throwable.getMessage());
            LogUtils.d("序列化数据失败" + throwable.getMessage());
        });
    }

    /**
     * 创建账号
     *
     * @param newUserName
     * @param publicKey
     * @param callback
     */
    public void createAccount(String newUserName, String decryptPrivateKey, String publicKey,
                              Callback callback) {

        createAccountJson2Bin(newUserName, publicKey, publicKey, (isSuccess, o) -> {
            if (isSuccess) {
                getChainInfo("newaccount", (String) o, (isSuccess1, o1) -> {
                    if (isSuccess1) {
                        getChainBlock("eosio", (String) o, (ChainInfo) o1, "newaccount",
                                (isSuccess2, o2) -> {
                            if (isSuccess2) {
                                getRequiredFee((ChainInfo) o1, (TransactionRequest) o2,
                                        (isSuccess3, o3) -> {
                                    if (isSuccess3) {
                                        signAndPushTransaction(decryptPrivateKey, (ChainInfo) o1,
                                                (TransactionRequest) o3, callback);
                                    } else {
                                        callback.onback(false, o3);
                                    }
                                });
                            } else {
                                callback.onback(false, o2);
                            }
                        });
                    } else {
                        callback.onback(false, o1);
                    }
                });
            } else {
                callback.onback(false, o);
            }
        });
    }

    /**
     * 投票 json转bin
     *
     * @param bpname   投票节点
     * @param stake    金额
     * @param callback
     */
    private void voteJsonBin(String bpname, String stake, String actionName, Callback callback) {

        JsonObject argsObject = new JsonObject();
        argsObject.addProperty("bpname", bpname);
        argsObject.addProperty("voter", SPUtils.getInstance().getString(SpConst.WALLET_NAME));
        if (actionName.equals("vote")) {//投票
            argsObject.addProperty("stake", stake);
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", actionName);
        jsonObject.addProperty("code", "eosio");
        jsonObject.add("args", argsObject);

//        EosHttp.app.jsonTobean(obj).compose(RxUtils.observableAsync()).subscribe(o -> {
        LogUtils.d(jsonObject.toString());
        HttpManage.apiService.json2bean(jsonObject).compose(RxUtils.applySchedulers()).subscribe(new Consumer<JsonObject>() {
            @Override
            public void accept(JsonObject jsonObject) throws Exception {
                if (jsonObject.has("binargs")) {
                    callback.onback(true, jsonObject.get("binargs").getAsString());
                } else {
                    callback.onback(false, "序列化数据失败");
                }
            }
        }, e -> {
            e.printStackTrace();
            callback.onback(false, "序列化数据失败" + e.getMessage());
        });

    }

    /**
     * 投票
     *
     * @param bpname 超级节点 账户名
     * @param stake  投票金额
     */
    public void vote(String key, String bpname, String stake, String actionName,
                     Callback callback) {
        voteJsonBin(bpname, stake, actionName, (isSuccess, o) -> {
            if (isSuccess) {
                getChainInfo("eosio", (String) o, (isSuccess1, o1) -> {
                    if (isSuccess1) {
                        getChainBlock("eosio", (String) o, (ChainInfo) o1, actionName,
                                (isSuccess2, o2) -> {
                            if (isSuccess2) {
                                getRequiredFee((ChainInfo) o1, (TransactionRequest) o2,
                                        (isSuccess3, o3) -> {
                                    if (isSuccess3) {
                                        signAndPushTransaction(key, (ChainInfo) o1,
                                                (TransactionRequest) o3, callback);
                                    } else {
                                        callback.onback(false, o3);
                                    }
                                });
                            } else {
                                callback.onback(false, o2);
                            }
                        });
                    } else {
                        callback.onback(false, o1);
                    }
                });
            } else {
                callback.onback(false, o);
            }
        });
    }

    /**
     * 转让权限
     *
     * @param userName 环签账户
     * @param account  权限用户
     * @param priKey   私钥
     * @param callback
     */
    public void setPossession(String userName, String[] account, String priKey, Callback callback) {
        createPossession(userName, account, (isSuccess, o) -> {
            if (isSuccess) {
                getChainInfo("updateauth", (String) o, new Callback() {
                    @Override
                    public void onback(boolean isSuccess1, Object o1) {
                        if (isSuccess1) {
                            getPossessionChainBlock("eosio", (String) o, (ChainInfo) o1,
                                    "updateauth", userName, (isSuccess2, o2) -> {
                                if (isSuccess2) {
                                    getRequiredFee((ChainInfo) o1, (TransactionRequest) o2,
                                            (isSuccess3, o3) -> {
                                        if (isSuccess3) {
                                            signAndPushTransaction(priKey, (ChainInfo) o1,
                                                    (TransactionRequest) o3, callback);
                                        } else {
                                            callback.onback(false, o3);
                                        }
                                    });
                                } else {
                                    callback.onback(false, o2);
                                }
                            });
                        } else {
                            callback.onback(false, o1);
                        }
                    }
                });
            } else {
                callback.onback(false, o);
            }
        });
    }

    /**
     * 转让权限json 转bin
     *
     * @param userName 用户名
     * @param account  权重用户
     * @param callback
     */
    private void createPossession(String userName, String[] account, Callback callback) {

        CreatePossessionInfo createPossessionInfo = new CreatePossessionInfo();
        createPossessionInfo.setAction("updateauth");
        createPossessionInfo.setCode("eosio");
        CreatePossessionInfo.ArgsBean argsBean = new CreatePossessionInfo.ArgsBean();
        argsBean.setAccount(userName);
        argsBean.setParent("owner");
        argsBean.setPermission("active");
        CreatePossessionInfo.ArgsBean.AuthBean authBean =
                new CreatePossessionInfo.ArgsBean.AuthBean();
        authBean.setKeys(new ArrayList<>());
        authBean.setWaits(new ArrayList<>());
        authBean.setThreshold(account.length);

        List<CreatePossessionInfo.ArgsBean.AuthBean.AccountsBean> accountsBeanList =
                new ArrayList<>();

        Arrays.sort(account, String.CASE_INSENSITIVE_ORDER);
        for (String str : account) {
            CreatePossessionInfo.ArgsBean.AuthBean.AccountsBean accountsBean =
                    new CreatePossessionInfo.ArgsBean.AuthBean.AccountsBean();
            accountsBean.setWeight(1);
            CreatePossessionInfo.ArgsBean.AuthBean.AccountsBean.PermissionBean permissionBean =
                    new CreatePossessionInfo.ArgsBean.AuthBean.AccountsBean.PermissionBean();
            permissionBean.setActor(str);
            permissionBean.setPermission("active");
            accountsBean.setPermission(permissionBean);
            accountsBeanList.add(accountsBean);
        }

        authBean.setAccounts(accountsBeanList);
        argsBean.setAuth(authBean);
        createPossessionInfo.setArgs(argsBean);

        String accountBean = GsonUtils.getInstance().toJson(createPossessionInfo);
        JsonObject obj = GsonUtils.getInstance().fromJson(accountBean, JsonObject.class);
        LogUtils.d("转让权限json 转bin  " + obj.toString());
        HttpManage.apiService.json2bean(obj).compose(RxUtils.applySchedulers()).subscribe(new Consumer<JsonObject>() {
            @Override
            public void accept(JsonObject jsonObject) throws Exception {
                if (jsonObject.has("binargs")) {
                    callback.onback(true, jsonObject.get("binargs").getAsString());
//                getChainInfo(action, o.get("binargs").getAsString());
                } else {
//                ToastUtils.showShort("序列化数据失败");
                    callback.onback(false, "序列化数据失败");
                }
            }
        }, e -> {
            e.printStackTrace();
            callback.onback(false, "序列化数据失败" + e.getMessage());
        });
    }

    @SuppressLint("CheckResult")
    private void getPossessionChainBlock(String strAction, String data, ChainInfo info,
                                         String actionName, String actor, Callback callback) {
        HttpManage.apiService.getChainBlock(String.valueOf(info.head_block_num)).compose(RxUtils.applySchedulers()).subscribe(o -> {
//            createTransactionRe(o, data);
            callback.onback(true, createTransactionRe(strAction, o, data, actionName, actor));
//            getRequiredFee(info, createTransactionRe(strAction, o, data));
        }, throwable -> {
            throwable.printStackTrace();
            callback.onback(false, "最新区块的信息失败" + throwable.getMessage());
//            ToastUtils.showShort("最新区块的信息失败");
        });
    }


    /**
     * 多签提案
     * @param key 私钥
     * @param multipleSignName 多签提案别名
     * @param action 合约名
     * @param money 金额
     * @param formAccount 多签账户
     * @param toAccount 转帐账户
     * @param memo 备注
     * @param callback 回调
     */
    public void multipleSignatures(String key, String multipleSignName, String action, String money, String formAccount, String toAccount, String memo, Callback callback) {
        HttpManage.apiService.checkJointlyAccount(formAccount).compose(RxUtils.applySchedulers()).subscribe(eosAccountInfo -> {
            if (!eosAccountInfo.permissions.isEmpty()) {
                for (int i = 0; i < eosAccountInfo.permissions.size(); i++) {
                    if (eosAccountInfo.permissions.get(i).required_auth.threshold > 1) {
                        getJson2Bin(action, memo, toAccount, money, formAccount, (isSuccess, o) -> {
                            if (isSuccess) {
                                getChainInfo(action, (String) o, (isSuccess1, o1) -> {
                                    if (isSuccess1) {
                                        getMsigChainBlock(action, (String) o, (ChainInfo) o1, "transfer",formAccount, (isSuccess2, o2) -> {
                                            if (isSuccess2) {
                                                getRequiredFee((ChainInfo) o1, (TransactionRequest) o2, (isSuccess3, o3) -> {
                                                    if (isSuccess3) {
                                                        TransactionRequest request = (TransactionRequest) o2;
                                                        EcSignature sign = EcDsa.sign(request.getDigestForSignature(new TypeChainId(((ChainInfo) o1).chain_id)), new EosPrivateKey(key));
                                                        request.signatures.add(sign.toString());
                                                        LogUtils.e(request.expiration);
                                                        multipleJsonToBin(key, multipleSignName,eosAccountInfo.permissions.get(0).required_auth.accounts,request, new Callback() {
                                                            @SuppressLint("CheckResult")
                                                            @Override
                                                            public void onback(boolean isSuccess4, Object o4) {
                                                                if (isSuccess4) {
                                                                    pay(key, "eosio",
                                                                            "multiple" + SPUtils.getInstance().getString(SpConst.WALLET_NAME) + "_" + multipleSignName + "_" + formAccount + "_" + toAccount + "_" + money+ "_"+request.expiration+ "_" +action+"_" + memo, "zzzzz", "0.0001 EOS", SPUtils.getInstance().getString(SpConst.WALLET_NAME), new Callback() {
                                                                                @Override
                                                                                public void onback(boolean isSuccess5, Object o5) {
                                                                                    if (isSuccess5) {
                                                                                        LogUtils.d(
                                                                                                "multiple" + SPUtils.getInstance().getString(SpConst.WALLET_NAME) + "_" + multipleSignName + "_" + formAccount + "_" + toAccount + "_" + money + "_"+request.expiration+ "_" +action+"_"  + memo);
                                                                                        callback.onback(true, o4);
                                                                                    } else {
                                                                                        callback.onback(false, "错误码1001 " + o5);
                                                                                    }

                                                                                }
                                                                            });
                                                                } else {
                                                                    callback.onback(false,"多签提案失败" + o4.toString());
                                                                    LogUtils.d("多签提案失败" + o4.toString());
                                                                }
                                                            }
                                                        });
                                                    } else {
                                                        callback.onback(false, o3);
                                                    }
                                                });
                                            } else {
                                                callback.onback(false, o2);
                                            }
                                        });
                                    } else {
                                        callback.onback(false, o1);
                                    }
                                });
                            } else {
                                callback.onback(false, o);
                            }
                        });
                        break;
                    }
                }


            }else{
                callback.onback(false,"请检查账号是否正确");
            }
        }, e->{
            callback.onback(false,e.getMessage());
        });
    }

    /**
     * @param key              私钥
     * @param multipleSignName 多签别名
     * @param accounts
     * @param transactionRe
     * @param callback
     */
    @SuppressLint("CheckResult")
    private void multipleJsonToBin(String key, String multipleSignName,
                                   List<EosAccountInfo.PermissionsBean.RequiredAuthBean.AccountsBean> accounts, TransactionRequest transactionRe, Callback callback) {
        JsonObject jsonObject = new JsonObject();
        JsonObject argsObject = new JsonObject();
        jsonObject.addProperty("action", "propose");
        jsonObject.addProperty("code", "eosio.msig");
        argsObject.addProperty("proposal_name", multipleSignName);
        argsObject.addProperty("proposer", SPUtils.getInstance().getString(SpConst.WALLET_NAME));
        argsObject.add("trx",
                GsonUtils.getInstance().fromJson(GsonUtils.getInstance().toJson(transactionRe),
                        JsonObject.class));

        JsonArray jsonElements = new JsonArray();
        String[] array = new String[accounts.size()];


        for (int i = 0; i < accounts.size(); i++) {
            array[i] = accounts.get(i).permission.actor;
        }
        Arrays.sort(array, String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < array.length; i++) {
            JsonObject object = new JsonObject();
            object.addProperty("actor", array[i]);
            object.addProperty("permission", "active");
            jsonElements.add(object);
        }

        argsObject.add("requested", jsonElements);

        jsonObject.add("args", argsObject);

        LogUtils.d(jsonObject.toString());
        HttpManage.apiService.json2bean(jsonObject).compose(RxUtils.applySchedulers()).subscribe((Consumer<JsonObject>) jsonObject1 -> {
            if (jsonObject1.has("binargs")) {
                getChainInfo("eosio.msig", jsonObject1.get("binargs").getAsString(), (isSuccess1,
                                                                                      o1) -> {
                    if (isSuccess1) {
                        getChainBlock("eosio.msig", jsonObject1.get("binargs").getAsString(),
                                (ChainInfo) o1, "propose", (isSuccess2, o2) -> {
                            if (isSuccess2) {
                                getRequiredFee((ChainInfo) o1, (TransactionRequest) o2,
                                        (isSuccess3, o3) -> {
                                    if (isSuccess3) {
                                        TransactionRequest request = (TransactionRequest) o2;
                                        EcSignature sign =
                                                EcDsa.sign(request.getDigestForSignature(new TypeChainId(((ChainInfo) o1).chain_id)), new EosPrivateKey(key));
                                        request.signatures.add(sign.toString());
                                        LogUtils.e(request.expiration);
                                        signAndPushTransaction(key, (ChainInfo) o1,
                                                (TransactionRequest) o3, callback);
                                    } else {
                                        callback.onback(false, o3);
                                    }
                                });
                            } else {
                                callback.onback(false, o2);
                            }
                        });
                    } else {
                        callback.onback(false, o1);
                    }
                });
            } else {
                callback.onback(false, "序列化数据失败");
            }

        }, e -> {
            callback.onback(false, e.getMessage());
        });
    }

    @SuppressLint("CheckResult")
    private void getMsigChainBlock(String strAction, String data, ChainInfo info,
                                   String actionName, String actor, Callback callback) {
        HttpManage.apiService.getChainBlock(String.valueOf(info.head_block_num)).compose(RxUtils.applySchedulers()).subscribe(o -> {
//            createTransactionRe(o, data);
            callback.onback(true, createTransactionRe(strAction, o, data, actionName, actor));
//            getRequiredFee(info, createTransactionRe(strAction, o, data));
        }, throwable -> {
            throwable.printStackTrace();
            callback.onback(false, "最新区块的信息失败" + throwable.getMessage());
//            ToastUtils.showShort("最新区块的信息失败");
        });
    }

    /**
     * 同意多签提案
     *
     * @param key              私钥
     * @param multipleSignName 提案别名
     * @param proposer         提议人
     * @param callback         回调
     */
    public void msigMultiple(String key, String multipleSignName, String proposer, String action,
                             Callback callback) {
        msigJsonToBin(multipleSignName, proposer, action, (Callback) (isSuccess, o) -> {
            if (isSuccess) {
                getChainInfo("eosio.msig", o.toString(), (isSuccess1, o1) -> {
                    if (isSuccess1) {
                        getChainBlock("eosio.msig", (String) o.toString(), (ChainInfo) o1, action
                                , (isSuccess2, o2) -> {
                            if (isSuccess2) {
                                getRequiredFee((ChainInfo) o1, (TransactionRequest) o2,
                                        (isSuccess3, o3) -> {
                                    if (isSuccess3) {
                                        signAndPushTransaction(key, (ChainInfo) o1,
                                                (TransactionRequest) o3, callback);
                                    } else {
                                        callback.onback(false, o3);
                                    }
                                });
                            } else {
                                callback.onback(false, o2);
                            }
                        });
                    } else {
                        callback.onback(false, o1);
                    }
                });
            } else {
                callback.onback(false, o);
            }
        });
    }

    /**
     * 同意/拒绝/取消/执行提案  序列化数据
     *
     * @param multipleSignName
     * @param proposer
     * @param callback
     */
    private void msigJsonToBin(String multipleSignName, String proposer, String action,
                               Callback callback) {

        SPUtils.getInstance().getString(SpConst.WALLET_NAME);

        JsonObject jsonObject = new JsonObject();
        JsonObject argsObject = new JsonObject();

        jsonObject.addProperty("action", action);
        jsonObject.addProperty("code", "eosio.msig");
        argsObject.addProperty("proposal_name", multipleSignName);
        argsObject.addProperty("proposer", proposer);

        if (action.equals("cancel")) {
            argsObject.addProperty("canceler",
                    SPUtils.getInstance().getString(SpConst.WALLET_NAME));
        } else if (action.equals("exec")) {
            argsObject.addProperty("executer",
                    SPUtils.getInstance().getString(SpConst.WALLET_NAME));
        } else {
            JsonObject object = new JsonObject();
            object.addProperty("actor", SPUtils.getInstance().getString(SpConst.WALLET_NAME));
            object.addProperty("permission", "active");
            argsObject.add("level", object);
        }
        jsonObject.add("args", argsObject);
        LogUtils.d("多签JSON" + jsonObject.toString());
        HttpManage.apiService.json2bean(jsonObject).compose(RxUtils.applySchedulers()).subscribe(jsonObject1 -> {
            if (jsonObject1.has("binargs")) {
                callback.onback(true, jsonObject1.get("binargs").getAsString());
            } else {
                callback.onback(false, "序列化数据失败");
            }
        }, e -> {
            callback.onback(false, e.getMessage());
        });
    }

    /**
     * 发币
     *
     * @param currencyNumber    数量
     * @param decryptPrivateKey 私钥
     * @param callback          回调
     */
    public void issueCurrency(String tokenName, String currencyNumber, String decryptPrivateKey, Callback callback) {
        issueCurrencyJsonBin(tokenName, currencyNumber, false, (isSuccess, o) -> {
            if (isSuccess) {
                getChainInfo("create", (String) o, (isSuccess1, o1) -> {
                    if (isSuccess1) {
                        getChainBlock("eosio.token", (String) o, (ChainInfo) o1, "create", (isSuccess2, o2) -> {
                            if (isSuccess2) {
                                getRequiredFee((ChainInfo) o1, (TransactionRequest) o2, new Callback() {
                                    @Override
                                    public void onback(boolean isSuccess3, Object o3) {
                                        if (isSuccess3) {
                                            signAndPushTransaction(decryptPrivateKey, (ChainInfo) o1, (TransactionRequest) o3, (isSuccess4, o4) -> {
                                                if (isSuccess4) {
                                                    issue(currencyNumber, decryptPrivateKey, callback);
                                                } else {
                                                    callback.onback(false, o4);
                                                }
                                            });
                                        } else {
                                            callback.onback(false, o3);
                                        }
                                    }
                                });
                            } else {
                                callback.onback(false, o2);
                            }
                        });
                    } else {
                        callback.onback(false, o1);
                    }
                });
            } else {
                callback.onback(false, o);
            }
        });
    }

    private void issue(String currencyNumber, String decryptPrivateKey, Callback callback) {
        issueCurrencyJsonBin("", currencyNumber, true, (isSuccess, o) -> {
            if (isSuccess) {
                getChainInfo("issue", (String) o, (isSuccess1, o1) -> {
                    if (isSuccess1) {
                        getChainBlock("eosio.token", (String) o, (ChainInfo) o1, "issue", (isSuccess2, o2) -> {
                            if (isSuccess2) {
                                getRequiredFee((ChainInfo) o1, (TransactionRequest) o2, (isSuccess3, o3) -> {
                                    if (isSuccess3) {
                                        signAndPushTransaction(decryptPrivateKey, (ChainInfo) o1, (TransactionRequest) o3, callback);
                                    } else {
                                        callback.onback(false, o3);
                                    }
                                });
                            } else {
                                callback.onback(false, o2);
                            }
                        });
                    } else {
                        callback.onback(false, o1);
                    }
                });
            } else {
                callback.onback(false, o);
            }
        });
    }


    /**
     * 发币 json转bin
     *
     * @param tokenName
     * @param currencyNumber
     * @param callback
     */
    private void issueCurrencyJsonBin(String tokenName, String currencyNumber, boolean issue, Callback callback) {

        JsonObject argsObject = new JsonObject();
        JsonObject jsonObject = new JsonObject();
        if (issue) {
            argsObject.addProperty("to", SPUtils.getInstance().getString(SpConst.WALLET_NAME));
            argsObject.addProperty("quantity", currencyNumber);
            argsObject.addProperty("memo", "");
            jsonObject.addProperty("action", "issue");
        } else {
            argsObject.addProperty("issuer", SPUtils.getInstance().getString(SpConst.WALLET_NAME));
            argsObject.addProperty("maximum_supply", currencyNumber);
            argsObject.addProperty("name", tokenName);
            jsonObject.addProperty("action", "create");
        }
        jsonObject.addProperty("code", "eosio.token");
        jsonObject.add("args", argsObject);
        LogUtils.d(jsonObject.toString());
        HttpManage.apiService.json2bean(jsonObject).compose(RxUtils.applySchedulers()).subscribe(new Consumer<JsonObject>() {
            @Override
            public void accept(JsonObject jsonObject) throws Exception {
                if (jsonObject.has("binargs")) {
                    callback.onback(true, jsonObject.get("binargs").getAsString());
//                getChainInfo(action, o.get("binargs").getAsString());
                } else {
//                ToastUtils.showShort("序列化数据失败");
                    callback.onback(false, "序列化数据失败");
                }
            }
        }, e -> {
            e.printStackTrace();
            callback.onback(false, "序列化数据失败" + e.getMessage());
        });

    }
}
