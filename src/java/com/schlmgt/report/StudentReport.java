/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.report;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.updateResult.ResultModel;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author Gold
 */
@ManagedBean
@RequestScoped
public class StudentReport {

    private List<ColumnModel> personas = new ArrayList<ColumnModel>();
    private List< ResultModel> tableData;
    private List<String> tableHeaderNames;
    private List<String> subHead;

    @PostConstruct
    public void init() {
        try {
            displaySubject();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ResultModel> getTableData() {
        return tableData;
    }

    public List<String> getTableHeaderNames() {
        return tableHeaderNames;
    }

    public List<ColumnModel> getPersonas() {
        return personas;
    }

    public void setPersonas(List<ColumnModel> personas) {
        this.personas = personas;
    }

    public List<String> getSubHead() {
        return subHead;
    }

    public void setSubHead(List<String> subHead) {
        this.subHead = subHead;
    }

    public void displaySubject() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "select distinct(subject) from tbstudentresult where studentclass=? and Term=? and year=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, "Primary 4");
            pstmt.setString(2, "First Term");
            pstmt.setString(3, "2018");
            rs = pstmt.executeQuery();
            //
            List<String> lst = new ArrayList<>();
            List<String> suHead = new ArrayList<>();
            while (rs.next()) {
                lst.add(rs.getString("subject"));
            }
            int val = lst.size();
            val = val * 2;
            for (int i = 0; i < lst.size(); i++) {
                suHead.add("Total");
                suHead.add("Grade");
               
            }
            
            for(int i=0;i<suHead.size();i++)
            {
                System.out.println(suHead.get(i));
            }
            
            subHead = suHead;
            tableHeaderNames = lst;
        } catch (Exception e) {
            e.printStackTrace();

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

    public List<ResultModel> displaySub() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "select * from tbstudentresult where studentclass=? and Term=? and year=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, "Primary 4");
            pstmt.setString(2, "First Term");
            pstmt.setString(3, "2018");
            rs = pstmt.executeQuery();
            //
            List<ResultModel> lst = new ArrayList<>();
            while (rs.next()) {

                ResultModel coun = new ResultModel();
                coun.setId(rs.getInt("id"));
                coun.setSubject(rs.getString("subject"));
                coun.setFirstTest(rs.getDouble("firsttest"));
                coun.setSecondTest(rs.getDouble("secondtest"));
                coun.setExam(rs.getDouble("exam"));
                coun.setTotal(rs.getDouble("totalscore"));

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

    public void PlayListMB() {

        try {
            //Generate table header.

            tableData = displaySub();
        } catch (Exception ex) {
            Logger.getLogger(StudentReport.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
