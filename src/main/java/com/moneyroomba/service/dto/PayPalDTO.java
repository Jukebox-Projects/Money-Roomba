package com.moneyroomba.service.dto;

public class PayPalDTO {

    private boolean isGift;
    private String email;

    public PayPalDTO() {}

    public boolean isGift() {
        return isGift;
    }

    public void setGift(boolean gift) {
        isGift = gift;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
