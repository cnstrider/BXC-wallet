package com.landis.eoswallet.net.model;

import java.util.List;

public class TransactionPushRequest {

    public String compression;
    public List<String> signatures;

    public TransactionRequest transaction;

}
