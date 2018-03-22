/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.logic;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.register.ClassModel;
import com.schlmgt.register.GradeModel;
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
@ManagedBean(name = "classmode")
@ViewScoped
public class ClassGrade implements Serializable {

    private List<ClassModel> classmodel;
    private List<GradeModel> grademodels;
    private List<String> termList;
    private List<String> term;
    private List<String> arm;

    @PostConstruct
    public void init() {
        try {

            classmodel = classDropdown();
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

                ClassModel coun = new ClassModel();
                coun.setId(rs.getInt("id"));
                coun.setTbclass(rs.getString("class"));

                //
                lst.add(coun);
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

    public List<String> yearDropdown(String term) throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT distinct year FROM yearterm where term=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, term);
            rs = pstmt.executeQuery();
            //
            List<String> lst = new ArrayList<>();
            while (rs.next()) {

                lst.add(rs.getString("year"));

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

    public void ontermChanges(String tbclass) throws Exception {
        System.out.println(tbclass);
        term = yearDropdown(tbclass);
        System.out.println(tbclass);

    }

    public void onarmChanges() throws Exception {

        arm = armDropdown();

    }

    public List<GradeModel> gradeDropdowns(String tbclass) throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbgrade where class=?";
            pstmt = con.prepareStatement(query);
            System.out.println(tbclass);
            pstmt.setString(1, tbclass);
            rs = pstmt.executeQuery();
            //
            List<GradeModel> lst = new ArrayList<>();
            while (rs.next()) {

                GradeModel coun = new GradeModel();
                coun.setId(rs.getInt("id"));
                coun.setGrade(rs.getString("grade"));
                coun.setSclass(rs.getString("class"));

                //
                lst.add(coun);
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

    public List<String> termDropdown() throws Exception {

        //
        try {
            List<String> lst = new ArrayList<>();
            lst.add("First Term");
            lst.add("Second Term");
            lst.add("Third Term");
            return lst;

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }

    public void onTermDropDown() {

    }

    public List<String> armDropdown() throws Exception {

        //
        try {
            List<String> lst = new ArrayList<>();
            lst.add("A");
            lst.add("B");
            lst.add("C");
            lst.add("D");
            return lst;

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }

    public List<String> getArm() {
        return arm;
    }

    public void setArm(List<String> arm) {
        this.arm = arm;
    }

    public void ongradeChanges() throws Exception {

        termList = termDropdown();

    }

    public void onClassChanges(String tbclass) throws Exception {

        grademodels = gradeDropdowns(tbclass);

    }

    public List<String> getTerm() {
        return term;
    }

    public void setTerm(List<String> term) {
        this.term = term;
    }

    public List<String> getTermList() {
        return termList;
    }

    public void setTermList(List<String> termList) {
        this.termList = termList;
    }

    public List<GradeModel> getGrademodels() {
        return grademodels;
    }

    public void setGrademodels(List<GradeModel> grademodels) {
        this.grademodels = grademodels;
    }

    public List<ClassModel> getClassmodel() {
        return classmodel;
    }

    public void setClassmodel(List<ClassModel> classmodel) {
        this.classmodel = classmodel;
    }

}
