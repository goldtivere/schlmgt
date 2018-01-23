/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.profile;

import com.schlmgt.dbconn.DbConnectionX;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "edit")
@ViewScoped
public class EditStudent implements Serializable {

    private String fullname;
    private String fname;
    private String lname;
    private String mname;
    private String sclass;
    private String imagelink;
    private String someParam;
    private String scl;
    private String studentid;
    private String cla;
    private String messangerOfTruth;

    @PostConstruct
    public void init() {
        try {
            StudentNumber();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String classTest() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            Map<String, String> params = externalContext.getRequestParameterMap();
            scl = params.get("studentid");
            someParam = params.get("cla");
            System.out.println(" " + someParam);
            if (someParam.equalsIgnoreCase("nursery")) {
                return "nursery";
            } else if (someParam.equalsIgnoreCase("primary")) {
                return "primary";
            } else if (someParam.equalsIgnoreCase("secondary")) {
                return "secondary";
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return "false";
    }

    public void StudentNumber() throws SQLException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        FacesMessage msg;
        try {
            DbConnectionX dbConnections = new DbConnectionX();
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            Map<String, String> params = externalContext.getRequestParameterMap();
            scl = params.get("studentid");
            System.out.println(" " + scl);
            con = dbConnections.mySqlDBconnection();
            if (classTest().equalsIgnoreCase("nursery")) {
                String testguid = "Select * from tbnursery where studentid=?";
                pstmt = con.prepareStatement(testguid);
                pstmt.setString(1, scl);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    setFname(rs.getString("first_name"));
                    setMname(rs.getString("middle_name"));
                    setLname(rs.getString("last_name"));
                    setFullname(rs.getString("full_name"));
                    setSclass(rs.getString("class"));
                    setScl(rs.getString("arm"));
                    setImagelink(rs.getString("imagelink"));

                }
                System.out.println(getImagelink());

            } else if (classTest().equalsIgnoreCase("primary")) {
                System.out.println(" " + scl);      
                String testguid = "Select * from tbprimary where studentid=?";
                pstmt = con.prepareStatement(testguid);
                pstmt.setString(1, scl);
                System.out.println(" " + scl);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    setFname(rs.getString("first_name"));
                    setMname(rs.getString("middle_name"));
                    setLname(rs.getString("last_name"));
                    setFullname(rs.getString("full_name"));
                    setSclass(rs.getString("class"));
                    setScl(rs.getString("arm"));
                    setImagelink(rs.getString("imagelink"));

                }

            } else if (classTest().equalsIgnoreCase("secondary")) {
                String testguid = "Select * from tbsecondary where studentid=?";
                pstmt = con.prepareStatement(testguid);
                pstmt.setString(1, scl);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    setFname(rs.getString("first_name"));
                    setMname(rs.getString("middle_name"));
                    setLname(rs.getString("last_name"));
                    setFullname(rs.getString("full_name"));
                    setSclass(rs.getString("class"));
                    setScl(rs.getString("arm"));
                    setImagelink(rs.getString("imagelink"));

                }

            } else {
                setMessangerOfTruth("Student Doesnt Exist!!");

                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                facesContext.addMessage(null, msg);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public String getScl() {
        return scl;
    }

    public void setScl(String scl) {
        this.scl = scl;
    }

    public String getMessangerOfTruth() {
        return messangerOfTruth;
    }

    public void setMessangerOfTruth(String messangerOfTruth) {
        this.messangerOfTruth = messangerOfTruth;
    }

    public String getSomeParam() {
        return someParam;
    }

    public void setSomeParam(String someParam) {
        this.someParam = someParam;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getCla() {
        return cla;
    }

    public void setCla(String cla) {
        this.cla = cla;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getSclass() {
        return sclass;
    }

    public void setSclass(String sclass) {
        this.sclass = sclass;
    }

    public String getImagelink() {
        return imagelink;
    }

    public void setImagelink(String imagelink) {
        this.imagelink = imagelink;
    }

}
