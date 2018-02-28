/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.updateSubject;

import com.schlmgt.dbconn.DbConnectionX;
import static com.schlmgt.imgupload.ExcelUpload.cellToString;
import com.schlmgt.imgupload.UploadImagesX;
import com.schlmgt.login.UserDetails;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Gold
 */
@ManagedBean
@ViewScoped
public class SubjectUpdate implements Serializable {

    private String studentClass;
    private String studentGrade;
    private String term;
    private String year;
    private List<SessionTable> sesTab;
    private boolean status;
    private UploadedFile csv;
    private String csvUrl;
    private String ref_number;
    private String csvLocation;
    private String messangerOfTruth;

    @PostConstruct
    public void init() {
        setStatus(false);
    }

    public SubjectUpdate() {
        //
        ref_number = generateRefNo();

    }

    public String generateRefNo() {

        try {

            String timeStamp = new SimpleDateFormat("yyMMddHHmmss").format(Calendar.getInstance().getTime());

            int rnd = new Random().nextInt(99999753);
            String temp_val = String.valueOf(rnd).concat(timeStamp);
            return temp_val;

        } catch (Exception ex) {

            ex.printStackTrace();
            return null;

        }

    }//end generateRefNo(...)s

    public List<SessionTable> displaySubject() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM sessiontable where class=? and term=? and year=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, getStudentClass());
            pstmt.setString(2, getTerm());
            pstmt.setString(3, getYear());
            rs = pstmt.executeQuery();
            //
            List<SessionTable> lst = new ArrayList<>();
            while (rs.next()) {

                SessionTable coun = new SessionTable();
                coun.setId(rs.getInt("id"));
                coun.setSclass(rs.getString("class"));
                coun.setSubject(rs.getString("subject"));
                coun.setTerm(rs.getString("term"));
                coun.setGrade(rs.getString("grade"));

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

    public void handleFileUpload(FileUploadEvent event) {

        FacesMessage message;
        FacesContext context = FacesContext.getCurrentInstance();
        UploadImagesX uploadImagesX = new UploadImagesX();
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

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
                
                String personalDetails = "insert into sessiontable (term,class,grade,year,subject,createdby,datecreated,isdeleted) values("
                        + "?,?,?,?,?,?,?,?)";
                for (int i = 1; i <= ws.getLastRowNum(); i++) {
                    row = (Row) ws.getRow(i);

                    if (row.getCell(0) == null) {
                        int s=i+1;
                        setMessangerOfTruth("Cell in row "+String.valueOf(s)+" is empty");
                        message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                        context.addMessage(null, message);
                        break;
                    }
                    System.out.println(row.getCell(0).toString());
                    System.out.println(row.getCell(1).toString());

                }
                setMessangerOfTruth("File Upload Successful");
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, message);
            } else {
                setMessangerOfTruth("Excel not in correct format");
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, message);
            }

        } catch (Exception ex) {

            ex.printStackTrace();
            message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);

        }

    }

    public void onYearChange() throws Exception {
        setStatus(true);
        sesTab = displaySubject();
    }

    public String getMessangerOfTruth() {
        return messangerOfTruth;
    }

    public void setMessangerOfTruth(String messangerOfTruth) {
        this.messangerOfTruth = messangerOfTruth;
    }

    public String getCsvLocation() {
        return csvLocation;
    }

    public void setCsvLocation(String csvLocation) {
        this.csvLocation = csvLocation;
    }

    public String getRef_number() {
        return ref_number;
    }

    public void setRef_number(String ref_number) {
        this.ref_number = ref_number;
    }

    public String getCsvUrl() {
        return csvUrl;
    }

    public void setCsvUrl(String csvUrl) {
        this.csvUrl = csvUrl;
    }

    public UploadedFile getCsv() {
        return csv;
    }

    public void setCsv(UploadedFile csv) {
        this.csv = csv;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<SessionTable> getSesTab() {
        return sesTab;
    }

    public void setSesTab(List<SessionTable> sesTab) {
        this.sesTab = sesTab;
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

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public String getStudentGrade() {
        return studentGrade;
    }

    public void setStudentGrade(String studentGrade) {
        this.studentGrade = studentGrade;
    }

}
