/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.register;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.imgupload.UploadImagesX;
import com.schlmgt.logic.DateManipulation;
import com.schlmgt.login.UserDetails;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.poi.ss.usermodel.DataFormatter;
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

    public String regStudent() throws Exception {

        return "registerStudent.xhtml?faces-redirect=true";

    }

    public String regStaff() throws Exception {

        return "register.xhtml?faces-redirect=true";

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
        FacesMessage msg;
        FacesMessage message;
        FacesContext context = FacesContext.getCurrentInstance();
        UploadImagesX uploadImagesX = new UploadImagesX();
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        con = dbConnections.mySqlDBconnection();
        try {
            FreshReg reg = new FreshReg();
            StaffModel mode = new StaffModel();
            List<String> studentDetails = new ArrayList<>();
            studentDetails.add("FirstName");
            studentDetails.add("MiddleName");
            studentDetails.add("LastName");
            studentDetails.add("EmailAddress");
            studentDetails.add("PhoneNumber");
            studentDetails.add("HighestQualification");
            studentDetails.add("Address");
            studentDetails.add("StaffClass");
            studentDetails.add("StaffGrade");
            studentDetails.add("DateEmployed");

            InputStream mn = event.getFile().getInputstream();
            XSSFWorkbook wb = new XSSFWorkbook(mn);
            XSSFSheet ws = wb.getSheetAt(0);
            Row row;
            row = (Row) ws.getRow(0);
            int rowNum = ws.getLastRowNum() + 1;
            int val = 0;
            int studentId;

            String fullname = null;
            String gfullname = null;
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            String does = null;
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            String createdId
                    = String.valueOf(userObj.getId());
            DataFormatter df = new DataFormatter();
            int success = 0;
            int fail = 0;
            if (studentDetails.size() == row.getPhysicalNumberOfCells()) {
                for (int i = 0; i < studentDetails.size(); i++) {
                    if (row.getCell(i).toString().equalsIgnoreCase(studentDetails.get(i))) {
                        val++;
                    } else {
                        setMessangerOfTruth("Excel is in wrong format. It should be in this format: " + studentDetails.toString());
                        message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                        context.addMessage(null, message);
                        break;
                    }
                }

                if (val == row.getPhysicalNumberOfCells()) {

//                    if (getPnum().isEmpty() || getPnum().equals("")) {
//                        setPnum(null);
//                    }
//                    if (getEmail().isEmpty() || getEmail().equals("")) {
//                        setEmail(null);
//                    }
//                    if (getGpnum().isEmpty() || getGpnum().equals("")) {
//                        setGpnum(null);
//                    }
//                    if (getGemail().isEmpty() || getGemail().equals("")) {
//                        setGemail(null);
//                    }
                    Row ro = null;
                    for (int i = 1; i < rowNum; i++) {
                        ro = (Row) ws.getRow(i);
                        if (ro.getCell(0) != null) {
                            mode.setFname(ro.getCell(0).getStringCellValue());
                        } else {
                            setMessangerOfTruth("First Name is required: Row " + (i + 1));
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        }

                        if (ro.getCell(1) != null) {
                            mode.setMname(ro.getCell(1).getStringCellValue());
                        } else {
                            mode.setMname(null);
                        }

                        if (ro.getCell(2) != null) {
                            mode.setLname(ro.getCell(2).getStringCellValue());
                        } else {
                            setMessangerOfTruth("Last Name is required: Row " + i);
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        }
                        if (ro.getCell(3) != null) {
                            mode.setEmail(ro.getCell(3).getStringCellValue());
                        } else {
                            setMessangerOfTruth("Email Address is required: Row " + i);
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        }
                        if (ro.getCell(4) != null) {
                            mode.setPnum(df.formatCellValue(ro.getCell(4)));
                        } else {
                            setMessangerOfTruth("Phone Number is required: Row " + i);
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        }
                        if (ro.getCell(5) != null) {
                            mode.setHighQua(ro.getCell(5).getStringCellValue());
                        } else {
                            mode.setHighQua(null);
                        }
                        if (ro.getCell(6) != null) {
                            mode.setAddress(ro.getCell(6).getStringCellValue());
                        } else {
                            mode.setAddress(null);
                        }
                        if (ro.getCell(7) != null) {
                            mode.setStaffClass(ro.getCell(7).getStringCellValue());
                        } else {
                            setMessangerOfTruth("Staff Class is required: Row " + i);
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        }
                        if (ro.getCell(8) != null) {
                            mode.setStaffGrade(ro.getCell(8).getStringCellValue());
                        } else {
                            setMessangerOfTruth("Staff Grade is required: Row " + i);
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        }
                        if (ro.getCell(9) != null) {
                            mode.setDoe(ro.getCell(9).getDateCellValue());
                            does = format.format(mode.getDoe());
                        } else {
                            setMessangerOfTruth("Date employed is required: Row " + i);
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        }
                        if (!"Nursery".equalsIgnoreCase(mode.getStaffClass()) && !"Primary".equalsIgnoreCase(mode.getStaffClass()) && !"Secondary".equalsIgnoreCase(mode.getStaffClass())) {
                            setMessangerOfTruth("Staff Class field should be either ; Nursery, Primary or Secondary: Row " + (i + 1));
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        } else if ("Nursery".equalsIgnoreCase(mode.getStaffClass()) && (!"Nursery 1".equalsIgnoreCase(mode.getStaffGrade()) && !"Nursery 2".equalsIgnoreCase(mode.getStaffGrade()) && !"Nursery 3".equalsIgnoreCase(mode.getStaffGrade()))) {
                            setMessangerOfTruth("Staff Class Nursery must have current grade Nursery 1,Nursery 2 or Nursery 3: Row " + (i + 1));
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        } else if ("Primary".equalsIgnoreCase(mode.getStaffClass()) && (!"Primary 1".equalsIgnoreCase(mode.getStaffGrade()) && !"Primary 2".equalsIgnoreCase(mode.getStaffGrade()) && !"Primary 3".equalsIgnoreCase(mode.getStaffGrade()) && !"Primary 4".equalsIgnoreCase(mode.getStaffGrade()) && !"Primary 5".equalsIgnoreCase(mode.getStaffGrade()))) {
                            setMessangerOfTruth("Current Class Primary must have current grade Primary 1,Primary 2, Primary 3, Primary 4 or Primary 5: Row " + (i + 1));
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        } else if ("Secondary".equalsIgnoreCase(mode.getStaffClass()) && (!"Jss 1".equalsIgnoreCase(mode.getStaffGrade()) && !"Jss 2".equalsIgnoreCase(mode.getStaffGrade()) && !"Jss 3".equalsIgnoreCase(mode.getStaffGrade()) && !"SS 1".equalsIgnoreCase(mode.getStaffGrade()) && !"SS 2".equalsIgnoreCase(mode.getStaffGrade()) && !"SS 3".equalsIgnoreCase(mode.getStaffGrade()))) {
                            setMessangerOfTruth("Previous Class Secondary must have current grade Nursery 1,Nursery 2 or Nursery 3: Row " + (i + 1));
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        }
                        String testflname = "Select * from user_details where first_name=? and last_name=? and is_deleted=?";
                        pstmt = con.prepareStatement(testflname);
                        pstmt.setString(1, mode.getFname());
                        pstmt.setString(2, mode.getLname());
                        pstmt.setBoolean(3, false);
                        rs = pstmt.executeQuery();
                        if (rs.next()) {
                            setMessangerOfTruth("First Name and Last Name Aleady exists!!");
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        } else {

                            String testusername = "Select * from user_details where username=? and is_deleted=?";
                            pstmt = con.prepareStatement(testusername);
                            pstmt.setString(1, mode.getPnum());
                            pstmt.setBoolean(2, false);
                            rs = pstmt.executeQuery();
                            if (rs.next()) {
                                setMessangerOfTruth("Phone Number Aleady exists!!");
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            } else {

                                String testemail = "Select * from user_details where email_address=? and is_deleted=?";
                                pstmt = con.prepareStatement(testemail);
                                pstmt.setString(1, mode.getEmail());
                                pstmt.setBoolean(2, false);
                                rs = pstmt.executeQuery();
                                if (rs.next()) {
                                    setMessangerOfTruth("Email Aleady exists!!");
                                    msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                    context.addMessage(null, msg);
                                    break;

                                } else {
                                    UUID idOne = UUID.randomUUID();
                                    //InputStream fin2 = file.getInputstream();                                    
                                    String insert = "insert into user_details (first_name,middlename,last_name,username,email_address,role_id,"
                                            + "date_created,date_time_created,created_by,is_deleted,staffclass,staffgrade,highestqua,address,dateemployed) "
                                            + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                                    pstmt = con.prepareStatement(insert);

                                    pstmt.setString(1, mode.getFname());
                                    pstmt.setString(2, mode.getMname());
                                    pstmt.setString(3, mode.getLname());
                                    pstmt.setString(4, mode.getPnum());
                                    pstmt.setString(5, mode.getEmail());
                                    pstmt.setString(6, createdId);
                                    pstmt.setString(7, DateManipulation.dateAlone());
                                    pstmt.setString(8, DateManipulation.dateAndTime());
                                    pstmt.setString(9, createdby);
                                    pstmt.setBoolean(10, false);
                                    pstmt.setString(11, mode.getStaffClass());
                                    pstmt.setString(12, mode.getStaffGrade());
                                    pstmt.setString(13, mode.getHighQua());
                                    pstmt.setString(14, mode.getAddress());
                                    pstmt.setString(15, does);

                                    pstmt.executeUpdate();

                                    String fullnames = mode.getLname() + " " + mode.getFname();
                                    String slink = "http://localhost:8080/SchlMgt/faces/pages/createStaff/index.xhtml?id=";
                                    String insertemail = "insert into staffstatus (guid,fullname,status,datelogged,staffemail,datetime,staffphone,link)"
                                            + "values(?,?,?,?,?,?,?,?)";

                                    pstmt = con.prepareStatement(insertemail);
                                    pstmt.setString(1, idOne.toString());
                                    pstmt.setString(2, fullname);
                                    pstmt.setBoolean(3, false);
                                    pstmt.setString(4, DateManipulation.dateAlone());
                                    pstmt.setString(5, mode.getEmail());
                                    pstmt.setString(6, DateManipulation.dateAndTime());
                                    pstmt.setString(7, mode.getPnum());
                                    pstmt.setString(8, slink + idOne.toString());

                                    pstmt.executeUpdate();
                                    success++;
                                    /**
                                     * MailSender send = new MailSender();
                                     * send.sendMail(user, pass, getEmailadd(),
                                     * sub, content);*
                                     */
                                }
                            }
                        }

                    }
                    setMessangerOfTruth(success + " Staff Data Upload Successful");
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                    context.addMessage(null, message);
                } else {
                    setMessangerOfTruth("Excel is in wrong format. It should be in this format: " + studentDetails.toString());
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                    context.addMessage(null, message);
                }
            } else {
                setMessangerOfTruth("Excel is in wrong format. It should be in this format: " + studentDetails.toString());
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, message);
            }
        } catch (IllegalStateException e) {
            setMessangerOfTruth("Please format Phone number field to take text and not number");
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, message);
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

    public void studentUpload(FileUploadEvent event) throws SQLException {
        FacesMessage msg;
        FacesMessage message;
        FacesContext context = FacesContext.getCurrentInstance();
        UploadImagesX uploadImagesX = new UploadImagesX();
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        con = dbConnections.mySqlDBconnection();
        try {
            FreshReg reg = new FreshReg();
            StudentModel mode = new StudentModel();
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
            studentDetails.add("PhoneNumber");
            studentDetails.add("EmailAddress");
            studentDetails.add("Address");
            studentDetails.add("PreviousEdu");
            studentDetails.add("PrevClass");
            studentDetails.add("PrevGrade");
            studentDetails.add("CurrentClass");
            studentDetails.add("CurrentGrade");
            studentDetails.add("Arm");
            studentDetails.add("Term");
            studentDetails.add("Year");

            InputStream mn = event.getFile().getInputstream();
            XSSFWorkbook wb = new XSSFWorkbook(mn);
            XSSFSheet ws = wb.getSheetAt(0);
            Row row;
            row = (Row) ws.getRow(0);
            int rowNum = ws.getLastRowNum() + 1;
            int val = 0;
            int studentId;

            String fullname = null;
            String gfullname = null;
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            String dob = null;
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();
            DataFormatter df = new DataFormatter();
            int success = 0;
            int fail = 0;
            if (studentDetails.size() == row.getPhysicalNumberOfCells()) {
                for (int i = 0; i < studentDetails.size(); i++) {
                    if (row.getCell(i).toString().equalsIgnoreCase(studentDetails.get(i))) {
                        val++;
                    } else {
                        setMessangerOfTruth("Excel is in wrong format. It should be in this format: " + studentDetails.toString());
                        message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                        context.addMessage(null, message);
                        break;
                    }
                }

                if (val == row.getPhysicalNumberOfCells()) {

                    if (reg.studentIdCheck() == 0) {
                        studentId = 0 + 1;
                    } else {
                        studentId = reg.studentIdCheck() + 1;
                    }

//                    if (getPnum().isEmpty() || getPnum().equals("")) {
//                        setPnum(null);
//                    }
//                    if (getEmail().isEmpty() || getEmail().equals("")) {
//                        setEmail(null);
//                    }
//                    if (getGpnum().isEmpty() || getGpnum().equals("")) {
//                        setGpnum(null);
//                    }
//                    if (getGemail().isEmpty() || getGemail().equals("")) {
//                        setGemail(null);
//                    }
                    Row ro = null;
                    for (int i = 1; i < rowNum; i++) {
                        ro = (Row) ws.getRow(i);
                        if (ro.getCell(0) != null) {
                            mode.setFname(ro.getCell(0).getStringCellValue());
                        } else {
                            mode.setFname(null);
                        }

                        if (ro.getCell(1) != null) {
                            mode.setMname(ro.getCell(1).getStringCellValue());
                        } else {
                            mode.setMname(null);
                        }

                        if (ro.getCell(2) != null) {
                            mode.setLname(ro.getCell(2).getStringCellValue());
                        } else {
                            mode.setLname(null);
                        }

                        if (ro.getCell(3) != null) {
                            mode.setDob(ro.getCell(3).getDateCellValue());
                            dob = format.format(mode.getDob());
                        } else {
                            mode.setDob(null);
                        }

                        if (ro.getCell(4) != null) {
                            mode.setPnum(df.formatCellValue(ro.getCell(4)));
                        } else {
                            mode.setPnum(null);
                        }

                        if (ro.getCell(5) != null) {
                            mode.setEmail(ro.getCell(5).getStringCellValue());
                        } else {
                            mode.setEmail(null);
                        }

                        if (ro.getCell(6) != null) {
                            mode.setSex(ro.getCell(6).getStringCellValue());
                        } else {
                            mode.setSex(null);
                        }

                        if (ro.getCell(7) != null) {
                            mode.setPfname(ro.getCell(7).getStringCellValue());
                        } else {
                            mode.setPfname(null);
                        }
                        if (ro.getCell(8) != null) {
                            mode.setPmname(ro.getCell(8).getStringCellValue());

                        } else {
                            mode.setPmname(null);
                        }

                        if (ro.getCell(9) != null) {
                            mode.setPlname(ro.getCell(9).getStringCellValue());
                        } else {
                            mode.setPlname(null);
                        }

                        if (ro.getCell(10) != null) {
                            mode.setPpnum(df.formatCellValue(ro.getCell(10)));
                        } else {
                            mode.setPpnum(null);
                        }

                        if (ro.getCell(11) != null) {
                            mode.setPemail(ro.getCell(11).getStringCellValue());
                        } else {
                            mode.setPemail(null);
                        }

                        if (ro.getCell(12) != null) {
                            mode.setAddress(ro.getCell(12).getStringCellValue());
                        } else {
                            mode.setAddress(null);
                        }

                        if (ro.getCell(13) != null) {
                            mode.setPreviousEdu(ro.getCell(13).getStringCellValue());
                        } else {
                            mode.setPreviousEdu(null);
                        }

                        if (ro.getCell(14) != null) {
                            mode.setPreviousClass(ro.getCell(14).getStringCellValue());
                        } else {
                            mode.setPreviousClass(null);
                        }

                        if (ro.getCell(15) != null) {
                            mode.setPreviousGrade(ro.getCell(15).getStringCellValue());
                        } else {
                            mode.setPreviousGrade(null);
                        }

                        if (ro.getCell(16) != null) {
                            mode.setCurrentClass(ro.getCell(16).getStringCellValue());
                        } else {
                            mode.setCurrentClass(null);
                        }

                        if (ro.getCell(17) != null) {
                            mode.setCurrentGrade(ro.getCell(17).getStringCellValue());
                        } else {
                            mode.setCurrentGrade(null);
                        }

                        if (ro.getCell(18) != null) {
                            mode.setArm(ro.getCell(18).getStringCellValue());
                        } else {
                            mode.setArm(null);
                        }

                        if (ro.getCell(19) != null) {
                            mode.setTerm(ro.getCell(19).getStringCellValue());
                        } else {
                            mode.setTerm(null);
                        }

                        if (ro.getCell(20) != null) {
                            mode.setYear(df.formatCellValue(ro.getCell(20)));
                        } else {
                            mode.setYear(null);
                        }

                        fullname = mode.getLname() + " " + mode.getMname() + " " + mode.getFname();
                        gfullname = mode.getPlname() + " " + mode.getPmname() + " " + mode.getPfname();
                        if (reg.studentNameCheck(mode.getFname(), mode.getLname())) {

                            setMessangerOfTruth("Firstname: " + mode.getFname() + " and Lastname: " + mode.getLname() + " exists in row " + (i + 1));
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        } else if (!"Male".equalsIgnoreCase(mode.getSex()) && !"Female".equalsIgnoreCase(mode.getSex())) {

                            setMessangerOfTruth("Sex is either Male or Female: Row " + (i + 1));
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        } else if ((mode.getFname().isEmpty() || mode.getFname() == null) || (mode.getLname().isEmpty() || mode.getLname() == null)) {
                            setMessangerOfTruth("Student FirstName and LastName is required: Row " + (i + 1));
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        } else if ((mode.getPfname().isEmpty() || mode.getPfname() == null) || (mode.getPlname().isEmpty() || mode.getPlname() == null)) {
                            setMessangerOfTruth("Parent FirstName and LastName is required: Row " + (i + 1));
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        } else if (mode.getPpnum().isEmpty() || mode.getPpnum() == null) {
                            setMessangerOfTruth("Parent Phone Number is required: Row " + (i + 1));
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        } else if (mode.getPemail().isEmpty() || mode.getPemail() == null) {
                            setMessangerOfTruth("Parent Email Address is required: Row " + (i + 1));
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        } else if (!"Nursery".equalsIgnoreCase(mode.getPreviousClass()) && !"Primary".equalsIgnoreCase(mode.getPreviousClass()) && !"Secondary".equalsIgnoreCase(mode.getPreviousClass())) {
                            setMessangerOfTruth("Previous Class field should be either ; Nursery, Primary or Secondary: Row " + (i + 1));
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        } else if (!"Nursery".equalsIgnoreCase(mode.getCurrentClass()) && !"Primary".equalsIgnoreCase(mode.getCurrentClass()) && !"Secondary".equalsIgnoreCase(mode.getCurrentClass())) {
                            setMessangerOfTruth("Current Class field should be either ; Nursery, Primary or Secondary: Row " + (i + 1));
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        } else if ("Nursery".equalsIgnoreCase(mode.getCurrentClass()) && (!"Nursery 1".equalsIgnoreCase(mode.getCurrentGrade()) && !"Nursery 2".equalsIgnoreCase(mode.getCurrentGrade()) && !"Nursery 3".equalsIgnoreCase(mode.getCurrentGrade()))) {
                            setMessangerOfTruth("Current Class Nursery must have current grade Nursery 1,Nursery 2 or Nursery 3: Row " + (i + 1));
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        } else if ("Primary".equalsIgnoreCase(mode.getCurrentClass()) && (!"Primary 1".equalsIgnoreCase(mode.getCurrentGrade()) && !"Primary 2".equalsIgnoreCase(mode.getCurrentGrade()) && !"Primary 3".equalsIgnoreCase(mode.getCurrentGrade()) && !"Primary 4".equalsIgnoreCase(mode.getCurrentGrade()) && !"Primary 5".equalsIgnoreCase(mode.getCurrentGrade()))) {
                            setMessangerOfTruth("Current Class Primary must have current grade Primary 1,Primary 2, Primary 3, Primary 4 or Primary 5: Row " + (i + 1));
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        } else if ("Nursery".equalsIgnoreCase(mode.getPreviousClass()) && (!"Nursery 1".equalsIgnoreCase(mode.getPreviousGrade()) && !"Nursery 2".equalsIgnoreCase(mode.getPreviousGrade()) && !"Nursery 3".equalsIgnoreCase(mode.getPreviousGrade()))) {
                            setMessangerOfTruth("Previous Class Nursery must have current grade Nursery 1,Nursery 2 or Nursery 3: Row " + (i + 1));
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        } else if ("Primary".equalsIgnoreCase(mode.getPreviousClass()) && (!"Primary 1".equalsIgnoreCase(mode.getPreviousGrade()) && !"Primary 2".equalsIgnoreCase(mode.getPreviousGrade()) && !"Primary 3".equalsIgnoreCase(mode.getPreviousGrade()) && !"Primary 4".equalsIgnoreCase(mode.getPreviousGrade()) && !"Primary 5".equalsIgnoreCase(mode.getPreviousGrade()))) {
                            setMessangerOfTruth("Previous Class Primary must have current grade Primary 1,Primary 2, Primary 3, Primary 4 or Primary 5: Row " + (i + 1));
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        } else if ("Secondary".equalsIgnoreCase(mode.getPreviousClass()) && (!"Jss 1".equalsIgnoreCase(mode.getPreviousGrade()) && !"Jss 2".equalsIgnoreCase(mode.getPreviousGrade()) && !"Jss 3".equalsIgnoreCase(mode.getPreviousGrade()) && !"SS 1".equalsIgnoreCase(mode.getPreviousGrade()) && !"SS 2".equalsIgnoreCase(mode.getPreviousGrade()) && !"SS 3".equalsIgnoreCase(mode.getPreviousGrade()))) {
                            setMessangerOfTruth("Previous Class Secondary must have current grade Nursery 1,Nursery 2 or Nursery 3: Row " + (i + 1));
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        } else if ("Secondary".equalsIgnoreCase(mode.getCurrentClass()) && (!"Jss 1".equalsIgnoreCase(mode.getCurrentGrade()) && !"Jss 2".equalsIgnoreCase(mode.getCurrentGrade()) && !"Jss 3".equalsIgnoreCase(mode.getCurrentGrade()) && !"SS 1".equalsIgnoreCase(mode.getCurrentGrade()) && !"SS 2".equalsIgnoreCase(mode.getCurrentGrade()) && !"SS 3".equalsIgnoreCase(mode.getCurrentGrade()))) {
                            setMessangerOfTruth("Current Class Secondary must have current grade Primary 1,Primary 2, Primary 3, Primary 4 or Primary 5: Row " + (i + 1));
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        } else {
                            //generate unique identifier
                            UUID idOne = UUID.randomUUID();
                            String insertStudentDetails = "insert into Student_details"
                                    + "(first_name,middle_name,last_name,fullname,DOB,student_phone,student_email,sex,Guardian_firstname,"
                                    + "Guardian_middlename,Guardian_lastname,Guardian_fullname,Guardian_phone,"
                                    + "Guardian_email,guardian_address,previous_school,"
                                    + "previous_class,previous_grade,current_class,current_grade,Arm,created_by,"
                                    + "date_created,datetime_created,is_deleted,studentId)"
                                    + "values"
                                    + "(?,?,?,?,?,"
                                    + "?,?,?,?,?,"
                                    + "?,?,?,?,?,?,"
                                    + "?,?,?,?,?,"
                                    + "?,?,?,?,?)";
                            pstmt = con.prepareStatement(insertStudentDetails);
                            pstmt.setString(1, mode.getFname());
                            pstmt.setString(2, mode.getMname());
                            pstmt.setString(3, mode.getLname());
                            pstmt.setString(4, fullname);
                            pstmt.setString(5, dob);
                            pstmt.setString(6, mode.getPnum());
                            pstmt.setString(7, mode.getEmail());
                            pstmt.setString(8, mode.getSex());
                            pstmt.setString(9, mode.getPfname());
                            pstmt.setString(10, mode.getPmname());
                            pstmt.setString(11, mode.getPlname());
                            pstmt.setString(12, gfullname);
                            pstmt.setString(13, mode.getPpnum());
                            pstmt.setString(14, mode.getPemail());
                            pstmt.setString(15, mode.getAddress());
                            pstmt.setString(16, mode.getPreviousEdu());
                            pstmt.setString(17, mode.getPreviousClass());
                            pstmt.setString(18, mode.getPreviousGrade());
                            pstmt.setString(19, mode.getCurrentClass());
                            pstmt.setString(20, mode.getCurrentGrade());
                            pstmt.setString(21, mode.getArm());
                            pstmt.setString(22, createdby);
                            pstmt.setString(23, DateManipulation.dateAlone());
                            pstmt.setString(24, DateManipulation.dateAndTime());
                            pstmt.setBoolean(25, false);
                            pstmt.setInt(26, studentId);

                            pstmt.executeUpdate();

                            String slink = "http://localhost:8080/SchlMgt/faces/pages/create/index.xhtml?id=";
                            String insertEmail = "insert into studentstatus (guid,full_name,status,datelogged,studentemail,date_time,studentId,link)"
                                    + "values(?,?,?,?,?,?,?,?)";

                            pstmt = con.prepareStatement(insertEmail);
                            pstmt.setString(1, idOne.toString());
                            pstmt.setString(2, fullname);
                            pstmt.setBoolean(3, false);
                            pstmt.setString(4, DateManipulation.dateAlone());
                            pstmt.setString(5, mode.getPemail());
                            pstmt.setString(6, DateManipulation.dateAndTime());
                            pstmt.setInt(7, studentId);
                            pstmt.setString(8, slink + idOne.toString());

                            pstmt.executeUpdate();
                            classUpload(studentId, mode.getFname(), mode.getMname(), mode.getLname(), mode.getCurrentClass(), mode.getArm(), mode.getTerm(), mode.getYear(), createdby, fullname, mode.getCurrentGrade());

                            success++;

                        }

                    }
                    setMessangerOfTruth(success + " Student Data Upload Successful");
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                    context.addMessage(null, message);
                } else {
                    setMessangerOfTruth("Excel is in wrong format. It should be in this format: " + studentDetails.toString());
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                    context.addMessage(null, message);
                }
            } else {
                setMessangerOfTruth("Excel is in wrong format. It should be in this format: " + studentDetails.toString());
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, message);
            }
        } catch (IllegalStateException e) {
            setMessangerOfTruth("Please format Phone number field to take text and not number");
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, message);
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

    public void classUpload(int studentId, String fname, String mname, String lname, String grade, String arm, String term, String year, String createdby, String fullname, String currentclass) {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dbConnections.mySqlDBconnection();

            String nurseryInsert = "insert into tbstudentclass (studentid,first_name,middle_name,last_name,full_name,class,"
                    + "classtype,isdeleted,datecreated,datetime_created,createdby,Arm,currentclass,term,year) values "
                    + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            pstmt = con.prepareStatement(nurseryInsert);

            pstmt.setInt(1, studentId);
            pstmt.setString(2, fname);
            pstmt.setString(3, mname);
            pstmt.setString(4, lname);
            pstmt.setString(5, fullname);
            pstmt.setString(6, currentclass);
            pstmt.setString(7, grade);
            pstmt.setBoolean(8, false);
            pstmt.setString(9, DateManipulation.dateAlone());
            pstmt.setString(10, DateManipulation.dateAndTime());
            pstmt.setString(11, createdby);
            pstmt.setString(12, arm);
            pstmt.setBoolean(13, true);
            pstmt.setString(14, term);
            pstmt.setString(15, year);
            pstmt.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleFileUpload(FileUploadEvent event) throws SQLException {

        FacesMessage message;
        FacesContext context = FacesContext.getCurrentInstance();

        try {

            if ("1".equalsIgnoreCase(getRegistration())) {
                studentUpload(event);
                setCsv(null);
            } else if ("2".equalsIgnoreCase(getRegistration())) {
                staffUpload(event);
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
