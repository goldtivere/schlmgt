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
import com.schlmgt.profile.SecondaryModel;
import com.schlmgt.updateSubject.SessionTable;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.poi.ss.util.CellRangeAddress;
import javax.faces.context.FacesContext;
import org.apache.poi.ss.usermodel.Cell;
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

    public void onyearchange() {
        setStatus(true);
    }

    public Boolean statusOfStudent(List<String> excelValue) throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Boolean valueStatus = false;
        try {
            List<String> lst = new ArrayList<>();
            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM sessiontable where class=? and Grade=? and term=? and year=? and isdeleted=? order by id asc";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, getSclass());
            pstmt.setString(2, getGrade());
            pstmt.setString(3, getTerm());
            pstmt.setString(4, getYear());
            pstmt.setBoolean(5, false);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                lst.add(rs.getString("subject"));

            }

            int actualSize = excelValue.size();

            if (numberOfSubjects() == lst.size()) {
                for (int i = 0; i < lst.size(); i++) {
                    if (excelValue.get(i).equalsIgnoreCase(lst.get(i))) {
                        valueStatus = true;
                    } else {
                        valueStatus = false;
                        break;
                    }
                }
            } else {
                valueStatus = false;

            }
            return valueStatus;

        } catch (Exception e) {
            e.printStackTrace();
            return false;

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

    public int numberOfSubjects() throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT count(*) countValue FROM sessiontable where class=? and Grade=? and term=? and year=? and isdeleted=? order by id desc";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, getSclass());
            pstmt.setString(2, getGrade());
            pstmt.setString(3, getTerm());
            pstmt.setString(4, getYear());
            pstmt.setBoolean(5, false);
            rs = pstmt.executeQuery();

            rs.next();
            count = rs.getInt("countValue");

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
        return count;
    }

    public String resultExist(List<String> excelValue, List<String> sub) throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Boolean valueStatus = false;
        try {
            List<String> lst = new ArrayList<>();
            String value = null;
            con = dbConnections.mySqlDBconnection();
            String valExist = "true";
            String query = "SELECT * FROM tbstudentresult where studentreg=? and studentclass=? and arm=? and term=? and year=? and subject=? and isdeleted=?";

            for (int i = 0; i < excelValue.size(); i++) {

                for (int ii = 0; ii < sub.size(); ii++) {
                    pstmt = con.prepareStatement(query);
                    pstmt.setString(1, excelValue.get(i));

                    pstmt.setString(2, getGrade());
                    
                    pstmt.setString(3, getArm());
                    pstmt.setString(4, getTerm());
                    pstmt.setString(5, getYear());
                    pstmt.setString(6, sub.get(ii));
                    pstmt.setBoolean(7, false);
                    rs = pstmt.executeQuery();

                    if (rs.next()) {
                        valExist = "Student Registration Number: " + excelValue.get(i) + " and Subject: " + sub.get(ii) +" already have record for this term and year";
                        return valExist;
                        
                    }
                }
            }
            
            return valExist;

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

    public void handleFileUpload(FileUploadEvent event) throws SQLException {

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
            Row rows;
            rows = (Row) ws.getRow(0);
            int wsCount = ws.getLastRowNum() - 1;
            CellRangeAddress regions = (CellRangeAddress) ws.getMergedRegion(0);
            //int colNum = regions.getLastColumn();
            int colNum = ws.getRow(0).getLastCellNum();
            int rowNum = ws.getLastRowNum() + 1;
            int finalSize = colNum - 1;
            Boolean testCol = false;
            List<String> lst = new ArrayList<>();
            List<String> studentId = new ArrayList<>();
            List<String> studentIds = new ArrayList<>();

            for (int i = 0; i < 1; i++) {
                rows = (Row) ws.getRow(0);

                for (Cell cell : rows) {
                    if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {

                    } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {

                        if (cell.getStringCellValue().equalsIgnoreCase("regnumber")) {

                        } else {
                            lst.add(cell.getStringCellValue());
                        }
                    }
                }

            }

            for (Row rowa : ws) {

                for (Cell cell : rowa) {
                    if (cell.getRowIndex() >= 2 && cell.getColumnIndex() == 0 && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        int b = (int) cell.getNumericCellValue();
                        studentIds.add(String.valueOf(b));

                    }

                }
            }

            Set<String> hss = new HashSet<>();
            hss.addAll(studentIds);
            studentIds.clear();
            studentIds.addAll(hss);
            
            int total=0;

            if (resultExist(studentIds, lst).equalsIgnoreCase("true")) {
                if (statusOfStudent(lst)) {
                    con.setAutoCommit(false);
                    int rowCount = 0;
                    String testId = "select * from tbstudentclass where studentid=? and arm=?";

                    String resultDetail = "insert into tbstudentresult (studentreg,firsttest,secondtest,exam,totalscore,subject,studentclass,term,arm,year,createdby,datecreated,datetimecreated,isdeleted) values("
                            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                    for (Row row : ws) {

                        for (Cell cell : row) {
                            if (cell.getRowIndex() >= 2 && cell.getColumnIndex() == 0 && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                int b = (int) cell.getNumericCellValue();
                                studentId.add(String.valueOf(b));

                            }

                        }
                    }
                    Set<String> hs = new HashSet<>();
                    hs.addAll(studentId);
                    studentId.clear();
                    studentId.addAll(hs);

                    //
                    for (int i = 0; i < studentId.size(); i++) {
                        pstmt = con.prepareStatement(testId);
                        pstmt.setString(1, studentId.get(i));
                        pstmt.setString(2, getArm());

                        rs = pstmt.executeQuery();

                        if (rs.next()) {
                            testCol = true;
                            rowCount++;
                        } else {

                            setMessangerOfTruth("Student with Id: " + studentId.get(i) + " doesnt exist in " + getGrade() + " and Arm: " + getArm());
                            message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, message);
                        }

                    }

                    //
                    if (rowCount == wsCount) {
                        if (testCol == true) {
                            pstmt = con.prepareStatement(resultDetail);

                            Row ro = null;
                            String nm = null;
                            int y = 0;
                            int x = 0;

                            int val = 2;
                            for (int i = 2; i < rowNum; i++) {
                                ro = (Row) ws.getRow(i);
                                for (int j = 0; j < colNum; j++) {

                                    if (x == 0) {
                                        int a = (int) ro.getCell(x).getNumericCellValue();
                                        nm = String.valueOf(a);
                                        pstmt.setString(1, nm);

                                        x++;
                                    }

                                    if (val > 4) {

                                        val = 2;

                                    }
                                    if (j > 0) {                                        

                                        pstmt.setDouble(val, ro.getCell(j).getNumericCellValue());
                                        total+=ro.getCell(j).getNumericCellValue();

                                        if (lst.size() == y) {
                                            y = 0;
                                        }

                                        if (j % 3 == 0) {
                                            pstmt.setDouble(5, total);
                                            pstmt.setString(6, lst.get(y));
                                            pstmt.setString(7, getGrade());
                                            pstmt.setString(8, getTerm());
                                            pstmt.setString(9, getArm());
                                            pstmt.setString(10, getYear());
                                            pstmt.setString(11, createdby);
                                            pstmt.setString(12, DateManipulation.dateAlone());
                                            pstmt.setString(13, DateManipulation.dateAndTime());
                                            pstmt.setBoolean(14, false);
                                            pstmt.executeUpdate();
                                            System.out.println(" Total: "+ total);
                                            total=0;
                                            y++;
                                            x = 0;
                                        }
                                        val++;
                                    }

                                }

                            }

                            con.commit();
                            setMessangerOfTruth("Records Successfully Updated!!!.");
                            message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, message);
                        } else {
                            setMessangerOfTruth("Student Registration number do not match.");
                            message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, message);
                        }
                    } else {
                        setMessangerOfTruth("Student Registration number do not match.");
                        message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                        context.addMessage(null, message);
                    }

                } else {

                    setMessangerOfTruth("Please make sure subject match in Database and also equal to total subjects in Database");
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                    context.addMessage(null, message);
                }

            } else {
                setMessangerOfTruth(resultExist(studentIds, lst));
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, message);

            }
            setCsv(null);
            csv = null;
            mn.close();
        } catch (IllegalStateException e) {
            setMessangerOfTruth("Cell is not numeric." + e.getMessage());
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, message);
        } catch (NullPointerException e) {
            setMessangerOfTruth("Empty cell present in excel sheet." + e.getMessage());
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, message);

        } catch (Exception ex) {

            ex.printStackTrace();
            message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);

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
    
    public List<ResultModel> displayResult() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbstudentresult where studentclass=? and arm=? and term=? and year=? and isdeleted=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, getGrade());
            pstmt.setString(2, getArm());
            pstmt.setString(3, getTerm());
            pstmt.setString(4, getYear());
            pstmt.setBoolean(5, false);
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
