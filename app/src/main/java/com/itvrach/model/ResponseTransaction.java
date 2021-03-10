package com.itvrach.model;

import java.util.List;

/**
 * Created by engineer on 10/24/2018.
 */

public class ResponseTransaction {
    private Integer user_id;
    private String session_id;
    private String success;
    private String message;
    private List<Transaction> result;
    private User user;


    public ResponseTransaction(Integer user_id, String session_id, String success, String message, List<Transaction> result, User user) {
        this.user_id = user_id;
        this.session_id = session_id;
        this.success = success;
        this.message = message;
        this.result = result;
        this.user = user;
    }



   /* public ResponseTransaction(String success, List<Transactiondetail> result) {
        this.success = success;
        this.result = result;
    }*/

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
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


    public List<Transaction> getResult() {
        return result;
    }

    public void setResult(List<Transaction> result) {
        this.result = result;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
