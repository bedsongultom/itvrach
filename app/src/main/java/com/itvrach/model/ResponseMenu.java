package com.itvrach.model;

import java.util.List;

/**
 * Created by engineer on 2/19/2019.
 */

public class ResponseMenu {
    private String success;
    private List<Menu> result;

    public ResponseMenu(String success, List<Menu> result) {
        this.success = success;
        this.result = result;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<Menu> getResult() {
        return result;
    }

    public void setResult(List<Menu> result) {
        this.result = result;
    }
}

