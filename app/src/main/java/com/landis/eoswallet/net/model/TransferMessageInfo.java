package com.landis.eoswallet.net.model;

public class TransferMessageInfo {
    public String memo;
    public String to;
    public String quantity;
    public String from;

    public TransferMessageInfo(String memo, String to, String quantity, String from) {
        this.memo = memo;
        this.to = to;
        this.quantity = quantity;
        this.from = from;
    }
}
