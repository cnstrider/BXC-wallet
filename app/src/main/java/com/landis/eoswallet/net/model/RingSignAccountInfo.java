package com.landis.eoswallet.net.model;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class RingSignAccountInfo extends LitePalSupport implements Serializable {
    @SerializedName("accountname")
    public String accountName;
    public int state;
    public boolean showConfig;
    public String balance;
}
