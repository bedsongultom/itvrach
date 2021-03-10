package com.itvrach.model;

/**
 * Created by engineer on 2/19/2019.
 */

public class Menu {
    private int id;
    private String menu_name;
    private String icon;


    public Menu(int id, String menu_name, String icon) {
        this.id = id;
        this.menu_name = menu_name;
        this.icon = icon;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


}
