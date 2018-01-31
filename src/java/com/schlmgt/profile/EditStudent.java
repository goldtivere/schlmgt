/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.profile;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.logic.DateManipulation;
import com.schlmgt.login.UserDetails;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;

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
    private String dob;
    private String gfname;
    private String gmname;
    private String glname;
    private String sphone;
    private String semail;
    private String sex;
    private String relationship;
    private String relationship_other;
    private String gphone;
    private String gmemail;
    private String gcountry;
    private String state;
    private String lga;
    private String address;
    private String previousSchl;
    private String previousClass;
    private String previousGrade;
    private String currentClass;
    private String currentGrade;
    private String arm;
    private String disability;
    private String otherDis;
    private String bgroup;
    private String mname;
    private String sclass;
    private String url;
    private String messangerOfTruth;
    private Boolean shw;
    private String imagelink;
    private String someParam;
    private String scl;
    private Date dateOfBirth;
    private String studentid;
    private String cla;
    private Boolean fpanel;
    private Boolean spanel;

    @PostConstruct
    public void init() {
        try {
            setSpanel(false);
            setFpanel(true);
            StudentNumber();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String StudentId() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        FacesMessage msg;

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Map<String, String> params = externalContext.getRequestParameterMap();
        scl = params.get("studentid");
        return scl;
    }

    public void studDetails() {
        try {
            DbConnectionX dbConnections = new DbConnectionX();
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            con = dbConnections.mySqlDBconnection();
            String testguid = "Select * from student_details where studentid=?";
            pstmt = con.prepareStatement(testguid);
            pstmt.setString(1, StudentId());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                setFname(rs.getString("first_name"));
                setMname(rs.getString("middle_name"));
                setLname(rs.getString("last_name"));
                setFullname(rs.getString("fullname"));
                setDob(rs.getString("DOB"));
                setDateOfBirth(rs.getDate("DOB"));
                setStudentid(rs.getString("studentid"));
                setSphone(rs.getString("student_phone"));
                setSemail(rs.getString("student_email"));
                setSex(rs.getString("sex"));
                setGfname(rs.getString("guardian_firstname"));
                setGlname(rs.getString("guardian_lastname"));
                setGmname(rs.getString("guardian_middlename"));
                setRelationship(rs.getString("relationship"));
                setRelationship_other(rs.getString("relationship_other"));
                setGphone(rs.getString("guardian_phone"));
                setGmemail(rs.getString("guardian_email"));
                setGcountry(rs.getString("guardian_country"));
                setState(rs.getString("guardian_state"));
                setLga(rs.getString("guardian_lga"));
                setAddress(rs.getString("guardian_address"));
                setPreviousSchl(rs.getString("previous_school"));
                setPreviousClass(rs.getString("previous_grade"));
                setArm(rs.getString("arm"));
                setDisability(rs.getString("disability"));
                setOtherDis(rs.getString("other_disability"));
                setBgroup(rs.getString("bgroup"));
                setImagelink(rs.getString("image"));

            }
            System.out.println(getImagelink());
        } catch (NullPointerException e) {
            e.printStackTrace();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
            studDetails();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void updateStudentDetails(ActionEvent event) {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FacesMessage msg;
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext cont = RequestContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        String fullname = getLname() + " " + getMname() + " " + getFname();
        boolean loggedIn = true;

        try {
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            String dobs = format.format(getDateOfBirth());
            con = dbConnections.mySqlDBconnection();

            String personalDetails = "update student_details set first_name=? ,middle_name=?, last_name=?, fullname=?, dob=?,"
                    + "student_phone=? , student_email=?, sex=? ,updated_by=?,updated_id=?,date_updated=? where studentid=?";

            pstmt = con.prepareStatement(personalDetails);

            pstmt.setString(1, getFname());
            pstmt.setString(2, getMname());
            pstmt.setString(3, getLname());
            pstmt.setString(4, fullname);
            pstmt.setString(5, dobs);
            pstmt.setString(6, getSphone());
            pstmt.setString(7, getSemail());
            pstmt.setString(8, getSex());
            pstmt.setString(9, createdby);
            pstmt.setInt(10, createdId);
            pstmt.setString(11, DateManipulation.dateAndTime());
            pstmt.setString(12, getScl());
            pstmt.executeUpdate();
            setMessangerOfTruth("Personal Details Updated!!");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, msg);
            StudentNumber();
            cont.addCallbackParam("loggedIn", loggedIn);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Boolean getFpanel() {
        return fpanel;
    }

    public void setFpanel(Boolean fpanel) {
        this.fpanel = fpanel;
    }

    public Boolean getSpanel() {
        return spanel;
    }

    public void setSpanel(Boolean spanel) {
        this.spanel = spanel;
    }

    public String getArm() {
        return arm;
    }

    public void setArm(String arm) {
        this.arm = arm;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGfname() {
        return gfname;
    }

    public void setGfname(String gfname) {
        this.gfname = gfname;
    }

    public String getGmname() {
        return gmname;
    }

    public void setGmname(String gmname) {
        this.gmname = gmname;
    }

    public String getGlname() {
        return glname;
    }

    public void setGlname(String glname) {
        this.glname = glname;
    }

    public String getSphone() {
        return sphone;
    }

    public void setSphone(String sphone) {
        this.sphone = sphone;
    }

    public String getSemail() {
        return semail;
    }

    public void setSemail(String semail) {
        this.semail = semail;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getRelationship_other() {
        return relationship_other;
    }

    public void setRelationship_other(String relationship_other) {
        this.relationship_other = relationship_other;
    }

    public String getGphone() {
        return gphone;
    }

    public void setGphone(String gphone) {
        this.gphone = gphone;
    }

    public String getGmemail() {
        return gmemail;
    }

    public void setGmemail(String gmemail) {
        this.gmemail = gmemail;
    }

    public String getGcountry() {
        return gcountry;
    }

    public void setGcountry(String gcountry) {
        this.gcountry = gcountry;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLga() {
        return lga;
    }

    public void setLga(String lga) {
        this.lga = lga;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPreviousSchl() {
        return previousSchl;
    }

    public void setPreviousSchl(String previousSchl) {
        this.previousSchl = previousSchl;
    }

    public String getPreviousClass() {
        return previousClass;
    }

    public void setPreviousClass(String previousClass) {
        this.previousClass = previousClass;
    }

    public String getPreviousGrade() {
        return previousGrade;
    }

    public void setPreviousGrade(String previousGrade) {
        this.previousGrade = previousGrade;
    }

    public String getCurrentClass() {
        return currentClass;
    }

    public void setCurrentClass(String currentClass) {
        this.currentClass = currentClass;
    }

    public String getCurrentGrade() {
        return currentGrade;
    }

    public void setCurrentGrade(String currentGrade) {
        this.currentGrade = currentGrade;
    }

    public String getDisability() {
        return disability;
    }

    public void setDisability(String disability) {
        this.disability = disability;
    }

    public String getOtherDis() {
        return otherDis;
    }

    public void setOtherDis(String otherDis) {
        this.otherDis = otherDis;
    }

    public String getBgroup() {
        return bgroup;
    }

    public void setBgroup(String bgroup) {
        this.bgroup = bgroup;
    }

    public Boolean getShw() {
        return shw;
    }

    public void setShw(Boolean shw) {
        this.shw = shw;
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
