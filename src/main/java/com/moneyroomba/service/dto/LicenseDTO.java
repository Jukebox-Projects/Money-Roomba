package com.moneyroomba.service.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LicenseDTO {

    private int quantity;
    private String code;

    public LicenseDTO() {}

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
