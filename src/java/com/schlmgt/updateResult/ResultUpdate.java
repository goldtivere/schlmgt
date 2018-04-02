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
            String query = "SELECT * FROM sessiontable where class=? and Grade=? and term=? and year=? and isdeleted=?";
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
            Row rows;
            rows = (Row) ws.getRow(0);
            int wsCount = ws.getLastRowNum() - 1;
            CellRangeAddress regions = (CellRangeAddress) ws.getMergedRegion(0);
            //int colNum = regions.getLastColumn();
            int colNum = ws.getRow(0).getLastCellNum();
            int finalSize = colNum - 1;
            List<String> lst = new ArrayList<>();

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

            if (statusOfStudent(lst)) {
                con.setAutoCommit(false);
                int rowCount = 1;
                String testId = "select * from tbstudentclass where studentid=?";
                pstmt = con.prepareStatement(testId);

                for (Row row : ws) {

                    for (Cell cell : row) {
                        if (cell.getRowIndex() >= 2 && cell.getColumnIndex() == 0 && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {                            
                            int b=(int)cell.getNumericCellValue();
                            pstmt.setString(1, String.valueOf(b));
                            
                            rs = pstmt.executeQuery();

                            if (rs.next()) {
                                System.out.println("Perfect:" +b);
                                rowCount++;
                            } else {
                                setMessangerOfTruth("Student with Id: "+ b +" in row: "+ cell.getRowIndex()+" doesnt exist in "+ getGrade());
                                message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, message);
                            }
                        } else if (cell.getRowIndex() >= 2 && cell.getColumnIndex() > 0 && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                           

                        } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {

                            if (cell.getStringCellValue().equalsIgnoreCase("regnumber")) {

                            } else {
                                // System.out.println(cell.getStringCellValue());
                            }
                        }
                    }

                }
                if (rowCount == wsCount) {
                    con.commit();
                    System.out.println("Dude Gold: " + rowCount);
                }

            } else {

                setMessangerOfTruth("Please make sure subject match in Database and also equal to total subjects in Database");
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, message);
            }

            setCsv(null);

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
