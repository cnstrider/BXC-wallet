package com.landis.eoswallet.base.constant;

/**
 * 常量
 */
public interface Constant {

     String BASE_URL = "http://129.211.115.231:8888/v1/chain/";
    //新建账户使用
     String JSON_BIN_BASE_URL = "https://w1.eosforce.cn:443/v1/chain/";

    // 获取区块链状态
   String  get_chain_info =  "get_info";
    // 获取区块链
   String  get_chain_block = "get_block";
    // 交易JSON序列化
  String  get_abi_json_to_bin = "abi_json_to_bin";
    // 获取交易手续费
   String  get_required_fee = "get_required_fee";
    // 发起交易
    String  push_transaction = "push_transaction";

    //通过公钥获取用户名
     String  get_key_accounts = "/v1/history/get_key_accounts";

    //获取执行合约记录
    public static final String  get_actions = "/v1/history/get_actions";

    //通用接口
//    /v1/chain/get_table_rows
     String  get_table_rows = "get_table_rows";


    // 获取账户名  用于校验随机生成的用户名是否存在
     String  get_account =  "get_account";


    //投票届数（第几届投票）
     String  get_producer_schedule =  "get_producer_schedule";


    //获取代币信息
    String  get_currency_stats =  "get_currency_stats";

    long TIMEOUT_CONNECT = 10;
    long TIMEOUT_READ = 10;
    long TIMEOUT_WRITE = 10;

}
