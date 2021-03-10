package com.itvrach.model;

import java.util.List;

/**
 * Created by engineer on 5/7/2019.
 */

public class ResponseShipping {
    private Integer user_id;
    private String status;
    private String session_id;
    private String success;
    private String message;
    private List<Shipping> result;
    private User user;

    public ResponseShipping(Integer user_id, String status, String session_id, String success, String message, List<Shipping> result, User user) {
        this.user_id = user_id;
        this.status = status;
        this.session_id = session_id;
        this.success = success;
        this.message = message;
        this.result = result;
        this.user = user;
    }

    public ResponseShipping() {
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Shipping> getResult() {
        return result;
    }

    public void setResult(List<Shipping> result) {
        this.result = result;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}