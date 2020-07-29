package com.daytodayhealth.rating.response;

public class ResponseAddEdit {
    int responseCode;
    String responseMessage;
    public ResponseAddEdit() {
    }
    public ResponseAddEdit(int responseCode, String responseMessage) {
        super();
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;

    }

    public int getResponseCode() {
        return responseCode;
    }
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
    public String getResponseMessage() {
        return responseMessage;
    }
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }



}
