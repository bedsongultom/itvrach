package com.itvrach.model;

/**
 * Created by engineer on 5/7/2019.
 */

public class Shipping {
    private int id;
    private String session_id;
    private int user_id;
    private String shipping_datetime;
    private int transaction_id;
    private String file;
    private String signature_datetime;
    private String shipped;

    public Shipping(int id, String session_id, int user_id, String shipping_datetime, int transaction_id, String file, String signature_datetime, String shipped) {
        this.id = id;
        this.session_id = session_id;
        this.user_id = user_id;
        this.shipping_datetime = shipping_datetime;
        this.transaction_id = transaction_id;
        this.file = file;
        this.signature_datetime = signature_datetime;
        this.shipped = shipped;
    }

    public Shipping() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getShipping_datetime() {
        return shipping_datetime;
    }

    public void setShipping_datetime(String shipping_datetime) {
        this.shipping_datetime = shipping_datetime;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getSignature_datetime() {
        return signature_datetime;
    }

    public void setSignature_datetime(String signature_datetime) {
        this.signature_datetime = signature_datetime;
    }

    public String getShipped() {
        return shipped;
    }

    public void setShipped(String shipped) {
        this.shipped = shipped;
    }
}
