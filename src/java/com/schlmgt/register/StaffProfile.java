/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.register;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.profile.SecondaryModel;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "staffProfile")
@ViewScoped
public class StaffProfile implements Serializable {

    private StaffModel staffModel = new StaffModel();
    private String fname;
    private String mname;
    private String lname;
    private String fullname;
    private String pnum;
    private int id;
    private String image_name;
    private String email;
    private String staffClass;
    private String staffGrade;
    private String dateEmployed;
    private String dateStopped;
    private String highQua;
    private String address;
    private List<GradeModel> grademodels;
    private List<ClassModel> classmodel;
    // private String 

    @PostConstruct
    public void init() {
        try {
            staffDetails();
            grademodels = gradeDropdowns();
            classmodel = classDropdown();
            System.out.println(getStaffGrade() + " this is the grade");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void staffDetails() {
        try {
            DbConnectionX dbConnections = new DbConnectionX();
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            con = dbConnections.mySqlDBconnection();
            String studId;

            FacesContext ctx = FacesContext.getCurrentInstance();
            StaffModel staff = (StaffModel) ctx.getExternalContext().getApplicationMap().get("staffRecord");
            //test for null...
            staffModel = staff;

            if (staffModel != null) {
                setPnum(staffModel.getPnum());
            }

            String testguid = "Select * from user_details where username=?";
            pstmt = con.prepareStatement(testguid);
            pstmt.setString(1, getPnum());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                setFname(rs.getString("first_name"));
                setMname(rs.getString("middlename"));
                setLname(rs.getString("last_name"));
                setFullname(rs.getString("last_name") + " " + rs.getString("middlename") + " " + rs.getString("first_name"));
                setImage_name(rs.getString("image_name"));
                setEmail(rs.getString("email_address"));
                setStaffClass(rs.getString("staffClass"));
                setStaffGrade("Nursery 1");
                setHighQua(rs.getString("highestqua"));
                setAddress(rs.getString("address"));
                setDateEmployed(rs.getString("dateemployed"));
                setDateStopped(rs.getString("datestopped"));
            }

            System.out.println(getImage_name() + " l");
        } catch (NullPointerException e) {
            e.printStackTrace();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<ClassModel> classDropdown() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbclass";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            //
            List<ClassModel> lst = new ArrayList<>();
            while (rs.next()) {

                ClassModel couns = new ClassModel();
                couns.setId(rs.getInt("id"));
                couns.setTbclass(rs.getString("class"));

                //
                lst.add(couns);
            }

            return lst;
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        } finally {

            if (!(con == null)) {
                con.close();
                con = null;
            }
            if (!(pstmt == null)) {
                pstmt.close();
                pstmt = null;
            }

        }
    }

    public List<ClassModel> getClassmodel() {
        return classmodel;
    }

    public void setClassmodel(List<ClassModel> classmodel) {
        this.classmodel = classmodel;
    }

    public void onClassChange() throws Exception {

        grademodels = gradeDropdowns();

    }

    public List<GradeModel> gradeDropdowns() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbgrade where class=?";
            pstmt = con.prepareStatement(query);

            pstmt.setString(1, getStaffClass());
            rs = pstmt.executeQuery();
            //
            List<GradeModel> lst = new ArrayList<>();
            while (rs.next()) {

                GradeModel couns = new GradeModel();
                couns.setId(rs.getInt("id"));
                couns.setGrade(rs.getString("grade"));
                couns.setSclass(rs.getString("class"));

                //
                lst.add(couns);
            }

            return lst;
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        } finally {

            if (!(con == null)) {
                con.close();
                con = null;
            }
            if (!(pstmt == null)) {
                pstmt.close();
                pstmt = null;
            }

        }
    }

    public List<GradeModel> getGrademodels() {
        return grademodels;
    }

    public void setGrademodels(List<GradeModel> grademodels) {
        this.grademodels = grademodels;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getHighQua() {
        return highQua;
    }

    public void setHighQua(String highQua) {
        this.highQua = highQua;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPnum() {
        return pnum;
    }

    public void setPnum(String pnum) {
        this.pnum = pnum;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStaffClass() {
        return staffClass;
    }

    public void setStaffClass(String staffClass) {
        this.staffClass = staffClass;
    }

    public String getStaffGrade() {
        return staffGrade;
    }

    public void setStaffGrade(String staffGrade) {
        this.staffGrade = staffGrade;
    }

    public String getDateEmployed() {
        return dateEmployed;
    }

    public void setDateEmployed(String dateEmployed) {
        this.dateEmployed = dateEmployed;
    }

    public String getDateStopped() {
        return dateStopped;
    }

    public void setDateStopped(String dateStopped) {
        this.dateStopped = dateStopped;
    }

    public StaffModel getStaffModel() {
        return staffModel;
    }

    public void setStaffModel(StaffModel staffModel) {
        this.staffModel = staffModel;
    }

}
