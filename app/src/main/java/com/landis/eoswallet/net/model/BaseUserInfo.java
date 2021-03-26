package com.landis.eoswallet.net.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BaseUserInfo {
    public String authToken;
    public String walletName;
    public String UserAddr;

    @SerializedName("account_names")
    public List<String> accountNames;

    @Override
    public String toString() {
        return "BaseUserInfo{" +
                "authToken='" + authToken + '\'' +
                ", walletName='" + walletName + '\'' +
                ", UserAddr='" + UserAddr + '\'' +
                ", accountNames=" + accountNames +
                '}';
    }
}
