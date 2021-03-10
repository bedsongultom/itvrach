package com.itvrach.model;

/**
 * Created by engineer on 8/19/2018.
 */

public class Books {
    private int id;
    private String item_code;
    private String item_name;
    private String description;
    private float weight;
    private float disc;
    private float stock;
    private float price;
    private String file;

    public Books(int id, String item_code, String item_name,String description, float weight, float disc, float stock, float price, String file) {
        this.id = id;
        this.item_code = item_code;
        this.item_name = item_name;
        this.description = description;
        this.weight = weight;
        this.disc = disc;
        this.stock = stock;
        this.price = price;
        this.file = file;
    }

    public Books() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getDisc() {
        return disc;
    }

    public void setDisc(float disc) {
        this.disc = disc;
    }

    public float getStock() {
        return stock;
    }

    public void setStock(float stock) {
        this.stock = stock;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}