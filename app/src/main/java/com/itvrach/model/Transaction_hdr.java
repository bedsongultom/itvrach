package com.itvrach.model;

/**
 * Created by engineer on 2/28/2019.
 */

public class Transaction_hdr {
    private int transaction_id;
    private String session_id;
    private String transaction_date;
    private int user_id;
    private int sub_item;
    private int sub_qty;
    private float sub_weight;
    private double sub_disc;
    private double sub_total;
    private String shipping_options;
    private double shipping_charge;
    private String payment_options;
    private String payment_detail_from;
    private String payment_detail_to;
    private String payment_datetime;
    private double payment_total;
    private int approved;
    private int approved_id;
    private int confirmation;
    private boolean isChecked;


    public Transaction_hdr(int transaction_id, String session_id, String transaction_date, int user_id, int sub_item, int sub_qty, float sub_weight, double sub_disc, double sub_total, String shipping_options, double shipping_charge, String payment_options, String payment_detail_from, String payment_detail_to, String payment_datetime, double payment_total, int approved, int approved_id, int confirmation, boolean isChecked) {
        this.transaction_id = transaction_id;
        this.session_id = session_id;
        this.transaction_date = transaction_date;
        this.user_id = user_id;
        this.sub_item = sub_item;
        this.sub_qty = sub_qty;
        this.sub_weight = sub_weight;
        this.sub_disc = sub_disc;
        this.sub_total = sub_total;
        this.shipping_options = shipping_options;
        this.shipping_charge = shipping_charge;
        this.payment_options = payment_options;
        this.payment_detail_from = payment_detail_from;
        this.payment_detail_to = payment_detail_to;
        this.payment_datetime = payment_datetime;
        this.payment_total = payment_total;
        this.approved = approved;
        this.approved_id = approved_id;
        this.confirmation = confirmation;
        this.isChecked = isChecked;
    }

    public Transaction_hdr() {

    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getSub_item() {
        return sub_item;
    }

    public void setSub_item(int sub_item) {
        this.sub_item = sub_item;
    }

    public int getSub_qty() {
        return sub_qty;
    }

    public void setSub_qty(int sub_qty) {
        this.sub_qty = sub_qty;
    }

    public float getSub_weight() {
        return sub_weight;
    }

    public void setSub_weight(float sub_weight) {
        this.sub_weight = sub_weight;
    }

    public double getSub_disc() {
        return sub_disc;
    }

    public void setSub_disc(double sub_disc) {
        this.sub_disc = sub_disc;
    }

    public double getSub_total() {
        return sub_total;
    }

    public void setSub_total(double sub_total) {
        this.sub_total = sub_total;
    }

    public String getShipping_options() {
        return shipping_options;
    }

    public void setShipping_options(String shipping_options) {
        this.shipping_options = shipping_options;
    }

    public double getShipping_charge() {
        return shipping_charge;
    }

    public void setShipping_charge(double shipping_charge) {
        this.shipping_charge = shipping_charge;
    }

    public String getPayment_options() {
        return payment_options;
    }

    public void setPayment_options(String payment_options) {
        this.payment_options = payment_options;
    }

    public String getPayment_detail_from() {
        return payment_detail_from;
    }

    public void setPayment_detail_from(String payment_detail_from) {
        this.payment_detail_from = payment_detail_from;
    }

    public String getPayment_detail_to() {
        return payment_detail_to;
    }

    public void setPayment_detail_to(String payment_detail_to) {
        this.payment_detail_to = payment_detail_to;
    }

    public String getPayment_datetime() {
        return payment_datetime;
    }

    public void setPayment_datetime(String payment_datetime) {
        this.payment_datetime = payment_datetime;
    }

    public double getPayment_total() {
        return payment_total;
    }

    public void setPayment_total(double payment_total) {
        this.payment_total = payment_total;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }


    public int getApproved_id() {
        return approved_id;
    }

    public void setApproved_id(int approved_id) {
        this.approved_id = approved_id;
    }

    public int getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(int confirmation) {
        this.confirmation = confirmation;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
