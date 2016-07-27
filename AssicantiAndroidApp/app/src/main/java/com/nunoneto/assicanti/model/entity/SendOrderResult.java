package com.nunoneto.assicanti.model.entity;

/**
 * Created by NB20301 on 27/07/2016.
 */
public class SendOrderResult {

    private String orderId;
    private String data;

    public SendOrderResult(String orderId, String data) {
        this.orderId = orderId;
        this.data = data;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
