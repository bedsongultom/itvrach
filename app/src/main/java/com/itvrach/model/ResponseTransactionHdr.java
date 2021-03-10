package com.itvrach.model;

import java.util.List;

/**
 * Created by engineer on 2/28/2019.
 */

public class ResponseTransactionHdr {

    private Integer transaction_id;
    private String session_id;
    private String success;
    private String message;
    private List<Transaction_hdr> result;


    public ResponseTransactionHdr(Integer transaction_id, String session_id, String success, String message, List<Transaction_hdr> result) {
        this.transaction_id = transaction_id;
        this.session_id = session_id;
        this.success = success;
        this.message = message;
        this.result = result;
    }

    public Integer getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(Integer transaction_id) {
        this.transaction_id = transaction_id;
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

    public List<Transaction_hdr> getResult() {
        return result;
    }

    public void setResult(List<Transaction_hdr> result) {
        this.result = result;
    }
}
