/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.profile;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.updateResult.ResultModel;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "resultView")
@ViewScoped
public class ResultView implements Serializable{

    private EditStudent edits = new EditStudent();
    private String sclass;
    private String grade;
    private String arm;
    private String term;
    private String year;
    private List<ResultModel> resultmodel;
    private List<ResultModel> resultmodel1;

    public List<ResultModel> displayResult() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        edits.studDetails();

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbstudentresult where studentclass=? and arm=? and term=? and year=? and isdeleted=? and studentreg=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, getGrade());
            pstmt.setString(2, getArm());
            pstmt.setString(3, getTerm());
            pstmt.setString(4, getYear());
            pstmt.setBoolean(5, false);
            pstmt.setString(6, edits.getStudentid());
            rs = pstmt.executeQuery();
            //
            List<ResultModel> lst = new ArrayList<>();
            while (rs.next()) {

                ResultModel coun = new ResultModel();
                coun.setId(rs.getInt("id"));
                coun.setStudentId(rs.getString("studentreg"));
                coun.setSubject(rs.getString("subject"));
                coun.setFirstTest(rs.getDouble("firsttest"));
                coun.setSecondTest(rs.getDouble("secondtest"));
                coun.setExam(rs.getDouble("exam"));
                coun.setTotal(rs.getDouble("totalscore"));

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
    
    public void viewResult() throws Exception
    {
        resultmodel=displayResult();
    }

    public List<ResultModel> getResultmodel1() {
        return resultmodel1;
    }

    public void setResultmodel1(List<ResultModel> resultmodel1) {
        this.resultmodel1 = resultmodel1;
    }

    public List<ResultModel> getResultmodel() {
        return resultmodel;
    }

    public void setResultmodel(List<ResultModel> resultmodel) {
        this.resultmodel = resultmodel;
    }

    
    public EditStudent getEdits() {
        return edits;
    }

    public void setEdits(EditStudent edits) {
        this.edits = edits;
    }

    public String getSclass() {
        return sclass;
    }

    public void setSclass(String sclass) {
        this.sclass = sclass;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getArm() {
        return arm;
    }

    public void setArm(String arm) {
        this.arm = arm;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

}
