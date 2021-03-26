package com.landis.eoswallet.net.model;


public class AppTokenInfo {
    private int id;

    public String tokenname;
    public String amount;
    public String url;
    public String logo;
    public  double Rate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTokenName() {
        return tokenname;
    }

    public void setTokenName(String tokenname) {
        this.tokenname = tokenname;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public double getRate() {
        return Rate;
    }

    public void setRate(double rate) {
        Rate = rate;
    }
}
