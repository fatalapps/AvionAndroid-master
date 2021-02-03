package com.avion.app.models;

import java.io.Serializable;

public class Payment implements Serializable {
    public String type, currency;
    private Double pay_online, pay_offline;
    private Long currencyType;
    public Integer currencyValue = 0;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setPay_online(Double amount) {
        this.pay_online = amount;
    }

    public Double getPay_online() {
        return pay_online;
    }

    public void setPay_offline(Double amount) {
        this.pay_offline = amount;
    }

    public Double getPay_offline() {
        return pay_offline;
    }

    public void setCurrency(String type) {
        this.currency = type;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrencyType(Object type) {
        this.currency = String.valueOf(type);
    }

    public String getCurrencyType() {
        return currency;
    }

    public void setCurrencyValue(Integer type) {
        this.currencyValue = type;
    }

    public Integer getCurrencyValue() {
        return currencyValue;
    }

}
