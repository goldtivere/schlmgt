/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.profile;

import com.schlmgt.updateResult.ResultModel;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "report")
@ViewScoped
public class ReportCard implements Serializable {

    private ResultModel mode = new ResultModel();
    private SessionModel sec = new SessionModel();
    private SecondaryModel secModel = new SecondaryModel();
    private String name;

    @PostConstruct
    public void init() {
        resultDetails();
    }

    public void resultDetails() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ResultModel secResult = (ResultModel) ctx.getExternalContext().getApplicationMap().get("resultDetails");
        //test for null...
        mode = secResult;

        if (secModel != null) {
            setName(secModel.getStudentid());
            System.out.println("This is it: "+ secModel.getArm());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResultModel getMode() {
        return mode;
    }

    public void setMode(ResultModel mode) {
        this.mode = mode;
    }

    public SessionModel getSec() {
        return sec;
    }

    public void setSec(SessionModel sec) {
        this.sec = sec;
    }

}
