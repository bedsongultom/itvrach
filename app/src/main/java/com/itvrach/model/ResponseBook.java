package com.itvrach.model;

import java.util.List;


public class ResponseBook {
    private String success;
    private List<Books> result;

    public ResponseBook(String success, List<Books> result) {
        this.success = success;
        this.result = result;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<Books> getResult() {
        return result;
    }

    public void setResult(List<Books> result) {
        this.result = result;
    }
}
