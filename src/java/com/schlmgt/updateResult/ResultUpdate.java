/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.updateResult;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.imgupload.UploadImagesX;
import com.schlmgt.logic.DateManipulation;
import com.schlmgt.login.UserDetails;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "result")
@ViewScoped
public class ResultUpdate implements Serializable {

    private String grade;
    private String sclass;
    private String term;
    private String year;
    private String arm;
    private String messangerOfTruth;
    private boolean status;
    private UploadedFile csv;

    @PostConstruct
    public void init() {
        setStatus(false);
    }
    
    public void onyearchange()
    {
        setStatus(true);
    }

    public void handleFileUpload(FileUploadEvent event) {

        FacesMessage message;
        FacesContext context = FacesContext.getCurrentInstance();
        UploadImagesX uploadImagesX = new UploadImagesX();
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean stat = false;

        con = dbConnections.mySqlDBconnection();

        try {

            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();

            InputStream mn = event.getFile().getInputstream();
            XSSFWorkbook wb = new XSSFWorkbook(mn);
            XSSFSheet ws = wb.getSheetAt(0);
            Row row;
            row = (Row) ws.getRow(0);

            if ("subject".equalsIgnoreCase(row.getCell(0).toString())) {
                con.setAutoCommit(false);
                String subjectDetail = "insert into sessiontable (term,class,grade,year,subject,createdby,datecreated,isdeleted) values("
                        + "?,?,?,?,?,?,?,?)";
                pstmt = con.prepareStatement(subjectDetail);
                for (int i = 1; i <= ws.getLastRowNum(); i++) {
                    row = (Row) ws.getRow(i);

                    if (row.getCell(0) == null) {
                        int s = i + 1;
                        setMessangerOfTruth("Cell in row " + String.valueOf(s) + " is empty");
                        message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                        context.addMessage(null, message);
                        break;
                    }

                    /**
                     * pstmt.setString(1, getTerm()); pstmt.setString(2,
                     * getStudentClass()); pstmt.setString(3,
                     * getStudentGrade()); pstmt.setString(4, getYear());
                     * pstmt.setString(5, row.getCell(0).toString());
                     * pstmt.setString(6, createdby); pstmt.setString(7,
                     * DateManipulation.dateAndTime()); pstmt.setBoolean(8,
                     * false); pstmt.executeUpdate();
                     * System.out.println(row.getCell(0).toString()); stat =
                     * true;
                     *
                     */
                }
                if (stat == true) {
                    con.commit();
                    //sesTab = displaySubject();
                    setMessangerOfTruth("File Upload Successful");
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                    context.addMessage(null, message);
                } else {

                }
            } else {
                setMessangerOfTruth("Excel not in correct format");
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, message);
            }
            setCsv(null);
            mn.close();
        } catch (Exception ex) {

            ex.printStackTrace();
            message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);

        }

    }

    public String getMessangerOfTruth() {
        return messangerOfTruth;
    }

    public void setMessangerOfTruth(String messangerOfTruth) {
        this.messangerOfTruth = messangerOfTruth;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public UploadedFile getCsv() {
        return csv;
    }

    public void setCsv(UploadedFile csv) {
        this.csv = csv;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSclass() {
        return sclass;
    }

    public void setSclass(String sclass) {
        this.sclass = sclass;
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

    public String getArm() {
        return arm;
    }

    public void setArm(String arm) {
        this.arm = arm;
    }

}
