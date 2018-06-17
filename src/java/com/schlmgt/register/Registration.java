/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.register;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.imgupload.UploadImagesX;
import com.schlmgt.login.UserDetails;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
@ManagedBean
@ViewScoped
public class Registration implements Serializable {

    private String registration;
    private String regType;
    private List<String> typeValue = new ArrayList<>();
    private boolean studentStatus;
    private boolean staffStatus;
    private boolean but;
    private boolean put;
    private UploadedFile csv;
    private String messangerOfTruth;

    @PostConstruct
    public void init() {
        studentStatus = false;
        staffStatus = false;
        but = false;

    }

    public void regTypeChanges() {
        if ("1".equalsIgnoreCase(getRegistration()) && "Data upload".equalsIgnoreCase(getRegType())) {
            setStaffStatus(true);
            setBut(false);
            setPut(false);
        } else if ("2".equalsIgnoreCase(getRegistration()) && "Data upload".equalsIgnoreCase(getRegType())) {
            setStaffStatus(true);
            setBut(false);
            setPut(false);
        } else if ("1".equalsIgnoreCase(getRegistration()) && "Data Entry".equalsIgnoreCase(getRegType())) {
            setStaffStatus(false);
            setBut(true);
            setPut(false);
        } else if ("2".equalsIgnoreCase(getRegistration()) && "Data entry".equalsIgnoreCase(getRegType())) {
            setStaffStatus(false);
            setBut(false);
            setPut(true);
        }
    }

    public void staffUpload(FileUploadEvent event) throws SQLException {
        UploadImagesX uploadImagesX = new UploadImagesX();
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        con = dbConnections.mySqlDBconnection();
        try {

            InputStream mn = event.getFile().getInputstream();
            XSSFWorkbook wb = new XSSFWorkbook(mn);
            XSSFSheet ws = wb.getSheetAt(0);
            Row row;
        } catch (Exception exx) {

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

    public void studentUpload(FileUploadEvent event) throws SQLException {
        FacesMessage message;
        FacesContext context = FacesContext.getCurrentInstance();
        UploadImagesX uploadImagesX = new UploadImagesX();
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        con = dbConnections.mySqlDBconnection();
        try {
            List<String> studentDetails = new ArrayList<>();
            studentDetails.add("SFirstName");
            studentDetails.add("SMiddleName");
            studentDetails.add("SLastName");
            studentDetails.add("SDateOfBirth");
            studentDetails.add("SPhoneNumber");
            studentDetails.add("SEmailAddress");
            studentDetails.add("Sex");
            studentDetails.add("PFirstName");
            studentDetails.add("PMiddleName");
            studentDetails.add("PLastName");
            studentDetails.add("Relationship");
            studentDetails.add("PhoneNumber");
            studentDetails.add("EmailAddress");
            studentDetails.add("Country");
            studentDetails.add("State");
            studentDetails.add("LGA");
            studentDetails.add("Address");
            studentDetails.add("PreviousEdu");
            studentDetails.add("PrevClass");
            studentDetails.add("PrevGrade");
            studentDetails.add("CurrentClass");
            studentDetails.add("CurrentGrade");
            studentDetails.add("Arm");
            studentDetails.add("Term");
            studentDetails.add("Year");
            studentDetails.add("Disability");
            studentDetails.add("BloodGroup");

            InputStream mn = event.getFile().getInputstream();
            XSSFWorkbook wb = new XSSFWorkbook(mn);
            XSSFSheet ws = wb.getSheetAt(0);
            Row row;
            row = (Row) ws.getRow(0);
            System.out.println(studentDetails.size() + " This is it " + row.getLastCellNum() + " damn " + row.getPhysicalNumberOfCells());
            if (studentDetails.size() == row.getPhysicalNumberOfCells()) {
                for (int i = 0; i < studentDetails.size(); i++) {
                    if (row.getCell(i).toString().equalsIgnoreCase(studentDetails.get(i))) {
                        System.out.println("not in right format" + row.getCell(i) + " and " + studentDetails.get(i) + " count " + i);
                    } else {
                        setMessangerOfTruth("Excel is in wrong format. It should be in this format: " + studentDetails.toString());
                        message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                        context.addMessage(null, message);
                        break;
                    }
                }
            } else {
                setMessangerOfTruth("Excel is in wrong format. It should be in this format: " + studentDetails.toString());
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, message);
            }

        } catch (Exception exx) {
            exx.printStackTrace();
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

    public void handleFileUpload(FileUploadEvent event) throws SQLException {

        FacesMessage message;
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();

            if ("1".equalsIgnoreCase(getRegistration())) {
                studentUpload(event);
                System.out.println(getRegistration() + "    " + getRegType() + "  Student");
                setCsv(null);
            } else if ("2".equalsIgnoreCase(getRegistration())) {
                System.out.println(getRegistration() + "    " + getRegType() + "  Staff");
                setCsv(null);
            }
        } catch (Exception ex) {
            setMessangerOfTruth(ex.getMessage());
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, message);

        }
    }

    public void regDetails() {
        typeValue.clear();
        typeValue.add("Data Upload");
        typeValue.add("Data Entry");
    }

    public UploadedFile getCsv() {
        return csv;
    }

    public String getMessangerOfTruth() {
        return messangerOfTruth;
    }

    public void setMessangerOfTruth(String messangerOfTruth) {
        this.messangerOfTruth = messangerOfTruth;
    }

    public void setCsv(UploadedFile csv) {
        this.csv = csv;
    }

    public boolean isPut() {
        return put;
    }

    public void setPut(boolean put) {
        this.put = put;
    }

    public boolean isBut() {
        return but;
    }

    public void setBut(boolean but) {
        this.but = but;
    }

    public boolean isStudentStatus() {
        return studentStatus;
    }

    public void setStudentStatus(boolean studentStatus) {
        this.studentStatus = studentStatus;
    }

    public boolean isStaffStatus() {
        return staffStatus;
    }

    public void setStaffStatus(boolean staffStatus) {
        this.staffStatus = staffStatus;
    }

    public List<String> getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(List<String> typeValue) {
        this.typeValue = typeValue;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getRegType() {
        return regType;
    }

    public void setRegType(String regType) {
        this.regType = regType;
    }

}
