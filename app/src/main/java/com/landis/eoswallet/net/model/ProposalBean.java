package com.landis.eoswallet.net.model;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

public class ProposalBean extends LitePalSupport {

    /**
     * proposal_name : aaaaa
     * packed_transaction : 3f2a9a5f2f796f38df4d00000000010000000000ea3055000000572d3ccdcd0100e008cd1503228500000000a8ed32322e00e008cd1503228500380bfb1288a678102700000000000004454f53000000000d6d756c7469706c65207465737400640000000000000004454f5300000000
     */
    @SerializedName("proposalname")
    public String proposal_name;
    public String packed_transaction;
    public String proposer;
}
