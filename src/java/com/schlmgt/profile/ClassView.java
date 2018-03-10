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
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "vaClass")
@ViewScoped
public class ClassView implements Serializable {

    private EditStudent edits = new EditStudent();
    private List<SubjectModel> sub;
    private String currentClass;
    private String year;
    private String term;
    private String classType;

    @PostConstruct
    public void init() {

        try {
            sub = testMic();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void studentCurrentClass() {
        try {
            edits.studDetails();
            DbConnectionX dbConnections = new DbConnectionX();
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            con = dbConnections.mySqlDBconnection();

            String testguid = "Select * from tbstudentclass where studentid=? and currentclass =  ?";
            pstmt = con.prepareStatement(testguid);
            pstmt.setString(1, edits.getStudentid());
            pstmt.setBoolean(2, true);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                setCurrentClass(rs.getString("class"));
                setClassType(rs.getString("classtype"));
                setTerm(rs.getString("term"));
                setYear(rs.getString("year"));
            }
            
            System.out.println(edits.getStudentid());
            System.out.println(getCurrentClass());
            System.out.println(getClassType());
            System.out.println(getTerm());
            System.out.println(getYear());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<SubjectModel> testMic() throws SQLException {
        studentCurrentClass();
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs
                = null;
        con = dbConnections.mySqlDBconnection();
        String studId;

        try {
            String testguid = "Select * from sessiontable where term=? and grade =? and year=?";
            pstmt = con.prepareStatement(testguid);
            pstmt.setString(1, getTerm());
            pstmt.setString(2, getCurrentClass());
            pstmt.setString(3, getYear());
            rs = pstmt.executeQuery();

            List<SubjectModel> lst = new ArrayList<>();
            while (rs.next()) {
                SubjectModel coun = new SubjectModel();
                coun.setId(rs.getInt("id"));
                coun.setSubject(rs.getString("subject"));

                lst.add(coun);
            }
            System.out.println(edits.getStudentid());
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public List<SubjectModel> getSub() {
        return sub;
    }

    public void setSub(List<SubjectModel> sub) {
        this.sub = sub;
    }

    public String getCurrentClass() {
        return currentClass;
    }

    public void setCurrentClass(String currentClass) {
        this.currentClass = currentClass;
    }

    public EditStudent getEdits() {
        return edits;
    }

    public void setEdits(EditStudent edits) {
        this.edits = edits;
    }

}
