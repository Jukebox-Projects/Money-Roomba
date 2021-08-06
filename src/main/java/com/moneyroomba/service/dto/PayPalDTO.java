package com.moneyroomba.service.dto;

public class PayPalDTO {

    private String isGiftString;
    private String email;

    public PayPalDTO() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsGiftString() {
        return isGiftString;
    }

    public void setIsGiftString(String isGiftString) {
        this.isGiftString = isGiftString;
    }
}
