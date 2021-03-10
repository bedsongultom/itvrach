package com.itvrach.model;

/**
 * Created by engineer on 10/24/2018.
 */

public class Transaction {
    private int id;
    private String session_id;
    private int user_id;
    private String item_code;
    private String item_name;
    private float weight;
    private int qty;
    private float disc;
    private int price;
    private int total_price;



    public Transaction() {
        this.id = id;
        this.session_id = session_id;
        this.user_id = user_id;
        this.item_code = item_code;
        this.item_name = item_name;
        this.weight = weight;
        this.qty = qty;
        this.disc = disc;
        this.price = price;
        this.total_price = total_price;
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

    public int setUser_id(int user_id) {
        this.user_id = user_id;
        return user_id;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }


    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public float getDisc() {
        return disc;
    }

    public void setDisc(float disc) {
        this.disc = disc;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }
}
