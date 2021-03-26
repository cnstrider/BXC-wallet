package com.landis.eoswallet.net.model;

import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;
import com.landis.eoswallet.base.constant.SpConst;

/**
 * 历史交易记录
 */
public class TokenTransactionRecord {

    /**
     * id : 1914
     * Transaction_id : 8906667afff296bbfd5827ce9e66d11796cf9fda81ddd2f958aa57cd3c7889fb
     * Payer : wm2majolzek
     * Remitter : .nioqhvrlr5
     * Amount : 4.5000
     * Token_name : BXC
     * pay_fee : 0.0100
     * Sys_time : 2020-08-09 20:23:37
     * block_num : 517661
     * blcok_time : 2020-08-05 08:58:48
     */

    public int id;
    public String Transaction_id;
    public String Payer;
    public String Remitter;
    public String Amount;
    public String Token_name;
    public String pay_fee;
    public String Sys_time;
    public int block_num;
    public String blcok_time;
    public String memo;


    public boolean isIncome() {
        return TextUtils.equals(SPUtils.getInstance().getString(SpConst.WALLET_NAME), Remitter);
    }
}
