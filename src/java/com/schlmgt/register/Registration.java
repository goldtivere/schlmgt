/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.register;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Gold
 */
@ManagedBean
@ViewScoped
public class Registration implements Serializable {

    private String registration;
    private String regType;
    private List<String> typeValue = new ArrayList<>();

    public void regDetails() {
        typeValue.add("Data Upload");
        typeValue.add("Data Entry");
    }

    public List<String> getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(List<String> typeValue) {
        this.typeValue = typeValue;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getRegType() {
        return regType;
    }

    public void setRegType(String regType) {
        this.regType = regType;
    }

}
