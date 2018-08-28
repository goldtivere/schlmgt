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
import java.util.Objects;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.el.PropertyNotFoundException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import org.apache.poi.ss.util.CellRangeAddress;
import javax.faces.context.FacesContext;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.context.RequestContext;
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
    private List<ResultModel> resultmodel;
    private List<ResultModel> resultmodel1;
    private List<ResultModel> resultmodel2;
    private ResultModel modelResult = new ResultModel();

    @PostConstruct
    public void init() {
        setStatus(false);
    }

    public void onyearchange() throws Exception {
        setStatus(true);
        resultmodel = displayResult();
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
            String query = "SELECT * FROM tbstudentresult where studentreg=? and studentclass=? and term=? and year=? and subject=? and isdeleted=?";

            for (int i = 0; i < excelValue.size(); i++) {

                for (int ii = 0; ii < sub.size(); ii++) {
                    pstmt = con.prepareStatement(query);
                    pstmt.setString(1, excelValue.get(i));

                    pstmt.setString(2, getGrade());

                    pstmt.setString(3, getTerm());
                    pstmt.setString(4, getYear());
                    pstmt.setString(5, sub.get(ii));
                    pstmt.setBoolean(6, false);
                    rs = pstmt.executeQuery();

                    if (rs.next()) {
                        valExist = "Student Registration Number: " + excelValue.get(i) + " and Subject: " + sub.get(ii) + " already have record for this term and year";
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

    public void populatePosition() throws SQLException {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "select average, COUNT(average) as countPosition from tbresultcompute where studentclass=? and term=? and year=? and isdeleted=? GROUP BY average HAVING COUNT(average) > 0 order by average desc";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, getGrade());
            pstmt.setString(2, getTerm());
            pstmt.setString(3, getYear());
            pstmt.setBoolean(4, false);
            rs = pstmt.executeQuery();
            //
            List<Double> lst = new ArrayList<>();
            List<Double> dbd = new ArrayList<>();

            while (rs.next()) {

                ReportModel coun = new ReportModel();

                lst.add(rs.getDouble("average"));
                dbd.add(rs.getDouble("countPosition"));
                //

            }

            String updatePosition = "update tbresultcompute set postion=? where average=? and studentclass=? and term=? and year=?";
            pstmt = con.prepareStatement(updatePosition);
            int rank = 0;
            for (int i = 0; i < lst.size(); i++) {

                pstmt.setString(1, String.valueOf(rank + 1));
                pstmt.setDouble(2, lst.get(i));
                pstmt.setString(3, getGrade());
                pstmt.setString(4, getTerm());
                pstmt.setString(5, getYear());
                pstmt.executeUpdate();
                rank += dbd.get(i);
            }

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

    public void populatePositionArm() throws SQLException {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "select average, COUNT(average) as countPosition from tbresultcompute where studentclass=? and term=? and year=? and isdeleted=? GROUP BY average HAVING COUNT(average) > 0 order by average desc";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, getGrade());
            pstmt.setString(2, getTerm());
            pstmt.setString(3, getYear());
            pstmt.setBoolean(4, false);
            rs = pstmt.executeQuery();
            //
            List<Double> lst = new ArrayList<>();
            List<Double> dbd = new ArrayList<>();

            while (rs.next()) {

                ReportModel coun = new ReportModel();

                lst.add(rs.getDouble("average"));
                dbd.add(rs.getDouble("countPosition"));
                //

            }

            String queryArm = "select distinct(arm) from tbresultcompute where studentclass=? and term=? and year=? and isdeleted=?";
            pstmt = con.prepareStatement(queryArm);
            pstmt.setString(1, getGrade());
            pstmt.setString(2, getTerm());
            pstmt.setString(3, getYear());
            pstmt.setBoolean(4, false);
            rs = pstmt.executeQuery();
            //

            List<String> rm = new ArrayList<>();

            while (rs.next()) {

                ReportModel coun = new ReportModel();
                rm.add(rs.getString("arm"));
                //

            }

            String queryArms = "select average, COUNT(average) as countPosition from tbresultcompute where studentclass=? and term=? and year=? and arm=? and isdeleted=? GROUP BY average HAVING COUNT(average) > 0 order by average desc";
            pstmt = con.prepareStatement(queryArms);
            List<Double> avgss = new ArrayList<>();
            List<Double> posi = new ArrayList<>();

            for (int i = 0; i < rm.size(); i++) {
                pstmt.setString(1, getGrade());
                pstmt.setString(2, getTerm());
                pstmt.setString(3, getYear());
                pstmt.setString(4, rm.get(i));
                pstmt.setBoolean(5, false);
                rs = pstmt.executeQuery();

                while (rs.next()) {

                    ReportModel coun = new ReportModel();

                    avgss.add(rs.getDouble("average"));
                    posi.add(rs.getDouble("countPosition"));
                    //

                }

                String updatePositionArm = "update tbresultcompute set postionArm=? where average=? and studentclass=? and term=? and year=? and arm=?";
                pstmt = con.prepareStatement(updatePositionArm);
                int rank = 0;
                for (int t = 0; t < avgss.size(); t++) {

                    pstmt.setString(1, String.valueOf(rank + 1));
                    pstmt.setDouble(2, avgss.get(t));
                    pstmt.setString(3, getGrade());
                    pstmt.setString(4, getTerm());
                    pstmt.setString(5, getYear());
                    pstmt.setString(6, rm.get(i));
                    pstmt.executeUpdate();
                    rank += posi.get(t);
                }

            }
            //           

            String updatePosition = "update tbresultcompute set postion=? where average=? and studentclass=? and term=? and year=?";
            pstmt = con.prepareStatement(updatePosition);
            int rank = 0;
            for (int i = 0; i < lst.size(); i++) {

                pstmt.setString(1, String.valueOf(rank + 1));
                pstmt.setDouble(2, lst.get(i));
                pstmt.setString(3, getGrade());
                pstmt.setString(4, getTerm());
                pstmt.setString(5, getYear());
                pstmt.executeUpdate();
                rank += dbd.get(i);
            }

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

    public void populatePositionFinal() throws SQLException {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "select average, COUNT(average) as countPosition from tbfinalcompute where studentclass=? and year=? and isdeleted=? GROUP BY average HAVING COUNT(average) > 0 order by average desc";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, getGrade());
            pstmt.setString(2, getYear());
            pstmt.setBoolean(3, false);
            rs = pstmt.executeQuery();
            //            
            List<Double> lst = new ArrayList<>();
            List<Double> dbd = new ArrayList<>();

            while (rs.next()) {

                ReportModel coun = new ReportModel();

                lst.add(rs.getDouble("average"));
                dbd.add(rs.getDouble("countPosition"));
                //

            }

            String updatePosition = "update tbfinalcompute set position=? where average=? and studentclass=? and year=?";
            pstmt = con.prepareStatement(updatePosition);
            int rank = 0;
            for (int i = 0; i < lst.size(); i++) {

                pstmt.setString(1, String.valueOf(rank + 1));
                pstmt.setDouble(2, lst.get(i));
                pstmt.setString(3, getGrade());
                pstmt.setString(4, getYear());
                pstmt.executeUpdate();
                rank += dbd.get(i);
            }

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
            List<String> studentArm = new ArrayList<>();

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

            int total = 0;

            if (resultExist(studentIds, lst).equalsIgnoreCase("true")) {
                if (statusOfStudent(lst)) {
                    con.setAutoCommit(false);
                    int rowCount = 0;
                    String testId = "select * from tbstudentclass where studentid=? and class=? and currentclass=?";

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
                        pstmt.setString(2, getGrade());
                        pstmt.setBoolean(3, true);

                        rs = pstmt.executeQuery();

                        if (rs.next()) {
                            testCol = true;
                            rowCount++;
                        } else {

                            setMessangerOfTruth("Student with Id: " + studentId.get(i) + " doesnt exist in " + getGrade());
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
                                        total += ro.getCell(j).getNumericCellValue();

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
                                            total = 0;
                                            y++;
                                            x = 0;
                                        }
                                        val++;
                                    }

                                }

                            }

                            con.commit();
                            updateStudentArm();
                            resultmodel = displayResult();
                            updateStudentGrade();
                            updateCompute(studentId);
                            populatePosition();
                            populatePositionArm();
                            updateComputeFinal(studentId);
                            populatePositionFinal();
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

            setMessangerOfTruth(ex.getMessage());
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, message);

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
            String query = "SELECT * FROM tbstudentresult where studentclass=? and term=? and year=? and isdeleted=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, getGrade());
            pstmt.setString(2, getTerm());
            pstmt.setString(3, getYear());
            pstmt.setBoolean(4, false);
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
                coun.setArm(rs.getString("arm"));
                coun.setGrade(rs.getString("studentclass"));
                coun.setTerm(rs.getString("term"));
                coun.setYear(rs.getString("year"));

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

    public void updateResultCompute(double total, String reg) {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FacesMessage msg;
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext cont = RequestContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        boolean loggedIn = true;

        try {
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();
            con = dbConnections.mySqlDBconnection();

            String updateSubject = "update tbresultcompute set totalscore=?,Average=?,updatedby=?,dateupdated=? where studentreg=? and studentClass=? and term=? and year=?";

            pstmt = con.prepareStatement(updateSubject);
            System.out.println(total + " total");

            pstmt.setDouble(1, total);
            pstmt.setDouble(2, total / numberOfSubjects());
            pstmt.setString(3, createdby);
            pstmt.setString(4, DateManipulation.dateAndTime());
            pstmt.setString(5, reg);
            pstmt.setString(6, getGrade());
            pstmt.setString(7, getTerm());
            pstmt.setString(8, getYear());
            pstmt.executeUpdate();

            updateComputeFinal(reg);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateResult() {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FacesMessage msg;
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext cont = RequestContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        boolean loggedIn = true;

        try {
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();
            con = dbConnections.mySqlDBconnection();

            String updateSubject = "update tbstudentresult set firsttest=?,secondtest=?,exam=?,totalscore=? ,updatedby=?,dateupdated=?,datetimeupdated=?,grade=? where id=?";

            pstmt = con.prepareStatement(updateSubject);
            double total = modelResult.getFirstTest() + modelResult.getSecondTest() + modelResult.getExam();
            pstmt.setDouble(1, modelResult.getFirstTest());
            pstmt.setDouble(2, modelResult.getSecondTest());
            pstmt.setDouble(3, modelResult.getExam());
            pstmt.setDouble(4, total);
            pstmt.setString(5, createdby);
            pstmt.setString(6, DateManipulation.dateAlone());
            pstmt.setString(7, DateManipulation.dateAndTime());
            if (total > 74) {
                pstmt.setString(8, "A");

            } else if (total < 40) {
                pstmt.setString(8, "F");

            } else if (total > 39 && total < 50) {
                pstmt.setString(8, "D");

            } else if (total >= 50 && total < 65) {
                pstmt.setString(8, "C");

            } else if (total > 64 && total < 75) {
                pstmt.setString(8, "B");

            }
            pstmt.setInt(9, modelResult.getId());
            pstmt.executeUpdate();
            updateStudentGrade();
            updateStudentArm();
            resultmodel = displayResult();
            updateResultCompute(totalScore(modelResult.getStudentId()), modelResult.getStudentId());
            System.out.println(modelResult.getStudentId());
            updateComputeFinal(modelResult.getStudentId());
            averagePosition(modelResult.getStudentId());
            populatePosition();
            populatePositionFinal();
            setMessangerOfTruth("Result Updated!!");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, msg);
            cont.addCallbackParam("loggedIn", loggedIn);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deleteRecordCompute() {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FacesMessage msg;
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext cont = RequestContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();

        try {
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();
            con = dbConnections.mySqlDBconnection();
            if (resultmodel1 == null) {
                setMessangerOfTruth("Item(s) not selected!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
            } else {

                String updateSubject = "update tbresultcompute set isdeleted=?,deletedby=?,dateDeleted=?,datedeletedalone=? where studentreg=? and studentclass=? and term=? and year=?";

                pstmt = con.prepareStatement(updateSubject);
                for (ResultModel ta : resultmodel1) {
                    pstmt.setBoolean(1, true);
                    pstmt.setString(2, createdby);
                    pstmt.setString(3, DateManipulation.dateAndTime());
                    pstmt.setString(4, DateManipulation.dateAlone());
                    pstmt.setString(5, ta.getStudentId());
                    pstmt.setString(6, ta.getGrade());
                    pstmt.setString(7, ta.getTerm());
                    pstmt.setString(8, ta.getYear());
                    pstmt.executeUpdate();

                }

            }
        } catch (PropertyNotFoundException e) {

            setMessangerOfTruth("Item(s) not selected!!");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deleteRecord() {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FacesMessage msg;
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext cont = RequestContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();

        try {
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();
            con = dbConnections.mySqlDBconnection();
            if (resultmodel1 == null) {
                setMessangerOfTruth("Item(s) not selected!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
            } else {

                String updateSubject = "update tbstudentresult set isdeleted=?,deletedby=?,datetimeDeleted=? where studentreg=? and studentclass=? and term=? and year=?";

                pstmt = con.prepareStatement(updateSubject);
                for (ResultModel ta : resultmodel1) {
                    pstmt.setBoolean(1, true);
                    pstmt.setString(2, createdby);
                    pstmt.setString(3, DateManipulation.dateAndTime());
                    pstmt.setString(4, ta.getStudentId());
                    pstmt.setString(5, ta.getGrade());
                    pstmt.setString(6, ta.getTerm());
                    pstmt.setString(7, ta.getYear());
                    pstmt.executeUpdate();

                }
                deleteRecordCompute();
                updateDelete();
                populatePosition();
                resultmodel = displayResult();

                setMessangerOfTruth("Result Deleted!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
            }
        } catch (PropertyNotFoundException e) {

            setMessangerOfTruth("Item(s) not selected!!");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<ResultModel> getResultmodel2() {
        return resultmodel2;
    }

    public void updateStudentArm() {
        try {

            DbConnectionX dbConnections = new DbConnectionX();
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            List<String> studentID = new ArrayList<>();
            List<String> arm = new ArrayList<>();

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT distinct(studentreg) FROM tbstudentresult where studentclass=? and term=? and year=? and isdeleted=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, getGrade());
            pstmt.setString(2, getTerm());
            pstmt.setString(3, getYear());
            pstmt.setBoolean(4, false);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                studentID.add(rs.getString("studentreg"));
            }

            String query1 = "SELECT arm FROM tbstudentclass where studentid=? and isdeleted=? and currentclass=?";
            pstmt = con.prepareStatement(query1);

            for (int i = 0; i < studentID.size(); i++) {
                pstmt.setString(1, studentID.get(i));
                pstmt.setBoolean(2, false);
                pstmt.setBoolean(3, true);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    arm.add(rs.getString("arm"));

                }

            }
            String query2 = "update tbstudentresult set arm=? where studentclass=? and term=? and year=? and isdeleted=? and studentreg=?";
            pstmt = con.prepareStatement(query2);
            for (int i = 0; i < studentID.size(); i++) {
                pstmt.setString(1, arm.get(i));
                pstmt.setString(2, getGrade());
                pstmt.setString(3, getTerm());
                pstmt.setString(4, getYear());
                pstmt.setBoolean(5, false);
                pstmt.setString(6, studentID.get(i));
                pstmt.executeUpdate();

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateStudentGrade() {
        try {

            DbConnectionX dbConnections = new DbConnectionX();
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            List<Double> studentID = new ArrayList<>();
            List<Integer> arm = new ArrayList<>();
            con = dbConnections.mySqlDBconnection();

            String query = "SELECT * FROM tbstudentresult where studentclass=? and term=? and year=? and isdeleted=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, getGrade());
            pstmt.setString(2, getTerm());
            pstmt.setString(3, getYear());
            pstmt.setBoolean(4, false);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                studentID.add(rs.getDouble("totalscore"));
                arm.add(rs.getInt("id"));
            }

            String query2 = "update tbstudentresult set grade=? where studentclass=? and term=? and year=? and isdeleted=? and id=?";
            pstmt = con.prepareStatement(query2);
            for (int i = 0; i < studentID.size(); i++) {
                if (studentID.get(i) > 74) {
                    pstmt.setString(1, "A");

                } else if (studentID.get(i) < 40) {
                    pstmt.setString(1, "F");

                } else if (studentID.get(i) > 39 && studentID.get(i) < 50) {
                    pstmt.setString(1, "D");

                } else if (studentID.get(i) >= 50 && studentID.get(i) < 65) {
                    pstmt.setString(1, "C");

                } else if (studentID.get(i) > 64 && studentID.get(i) < 75) {
                    pstmt.setString(1, "B");

                }
                pstmt.setString(2, getGrade());
                pstmt.setString(3, getTerm());
                pstmt.setString(4, getYear());
                pstmt.setBoolean(5, false);
                pstmt.setInt(6, arm.get(i));
                pstmt.executeUpdate();

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<String> studentNum() throws Exception {

        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "select distinct(Studentreg) from tbstudentresult where studentclass=? and Term=? and year=? and isdeleted=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, getGrade());
            pstmt.setString(2, getTerm());
            pstmt.setString(3, getYear());
            pstmt.setBoolean(4, false);
            rs = pstmt.executeQuery();
            //
            List<String> lst = new ArrayList<>();
            List<String> suHead = new ArrayList<>();
            while (rs.next()) {
                lst.add(rs.getString("studentreg"));
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

    public double totalScore(String id) {
        try {

            DbConnectionX dbConnections = new DbConnectionX();
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            List<Double> studentID = new ArrayList<>();
            List<Integer> arm = new ArrayList<>();
            con = dbConnections.mySqlDBconnection();
            FacesContext context = FacesContext.getCurrentInstance();
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();

            String query = "SELECT sum(totalscore) as total FROM tbstudentresult where studentclass=? and term=? and year=? and studentreg=? and isdeleted=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, getGrade());
            pstmt.setString(2, getTerm());
            pstmt.setString(3, getYear());
            pstmt.setString(4, id);
            pstmt.setBoolean(5, false);

            rs = pstmt.executeQuery();
            double total = 0;
            if (rs.next()) {

                total = rs.getDouble("total");
            }

            return total;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void updateCompute(List<String> studentId) {
        try {

            DbConnectionX dbConnections = new DbConnectionX();
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            List<Double> studentID = new ArrayList<>();
            List<Integer> arm = new ArrayList<>();
            con = dbConnections.mySqlDBconnection();
            FacesContext context = FacesContext.getCurrentInstance();
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();
            List<ResultModel> arrayDude = new ArrayList<>();
            for (int i = 0; i < studentId.size(); i++) {
                String query = "SELECT distinct(studentreg) as regNum,sum(totalscore) as total,studentclass,term,arm,year FROM tbstudentresult where studentclass=? and term=? and year=? and studentreg=? and isdeleted=?";
                pstmt = con.prepareStatement(query);
                pstmt.setString(1, getGrade());
                pstmt.setString(2, getTerm());
                pstmt.setString(3, getYear());
                pstmt.setString(4, studentId.get(i));
                pstmt.setBoolean(5, false);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    ResultModel mode = new ResultModel();
                    mode.setStudentId(rs.getString("regNum"));
                    mode.setTotal(rs.getDouble("total"));
                    mode.setAvg(rs.getDouble("total") / numberOfSubjects());
                    mode.setArm(rs.getString("arm"));
                    mode.setNumofsub(numberOfSubjects());
                    mode.setStudentclass(rs.getString("studentclass"));
                    mode.setTerm(rs.getString("term"));
                    mode.setYear(rs.getString("year"));

                    arrayDude.add(mode);
                }
            }

            String resultDetail = "insert into tbresultcompute (studentreg,studentclass,term,arm,year,totalscore,numberofsubject,average,createdby,datecreated,datealone,isdeleted) values("
                    + "?,?,?,?,?,?,?,?,?,?,?,?)";
            pstmt = con.prepareStatement(resultDetail);

            for (ResultModel mm : arrayDude) {
                pstmt.setString(1, mm.getStudentId());
                pstmt.setString(2, mm.getStudentclass());
                pstmt.setString(3, mm.getTerm());
                pstmt.setString(4, mm.getArm());
                pstmt.setString(5, mm.getYear());
                pstmt.setDouble(6, mm.getTotal());
                pstmt.setInt(7, mm.getNumofsub());
                pstmt.setDouble(8, mm.getAvg());
                pstmt.setString(9, createdby);
                pstmt.setString(10, DateManipulation.dateAndTime());
                pstmt.setString(11, DateManipulation.dateAlone());
                pstmt.setBoolean(12, false);
                pstmt.executeUpdate();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateComputeFinal(List<String> studentId) {
        try {

            DbConnectionX dbConnections = new DbConnectionX();
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            List<Double> studentID = new ArrayList<>();
            List<Integer> arm = new ArrayList<>();
            con = dbConnections.mySqlDBconnection();
            FacesContext context = FacesContext.getCurrentInstance();
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();
            List<ResultComputeModel> arrayDude = new ArrayList<>();
            List<String> regStud = new ArrayList<>();
            String query = "SELECT * FROM tbresultcompute where studentclass=? and term=? and year=? and studentreg=? and datealone=?";
            for (int i = 0; i < studentId.size(); i++) {

                pstmt = con.prepareStatement(query);
                pstmt.setString(1, getGrade());
                pstmt.setString(2, getTerm());
                pstmt.setString(3, getYear());
                pstmt.setString(4, studentId.get(i));
                pstmt.setString(5, DateManipulation.dateAlone());
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    ResultComputeModel mode = new ResultComputeModel();
                    mode.setStudentReg(rs.getString("studentreg"));
                    mode.setGrade(rs.getString("Studentclass"));
                    mode.setTerm(rs.getString("term"));
                    mode.setYear(rs.getString("year"));
                    mode.setTermTotal(rs.getDouble("totalScore"));
                    regStud.add(rs.getString("Studentreg"));
                    arrayDude.add(mode);
                }
            }
            int uuu = 0;
            String querys = "SELECT * FROM tbfinalcompute where studentclass=? and year=? and studentreg=?";
            for (ResultComputeModel mm : arrayDude) {

                pstmt = con.prepareStatement(querys);
                pstmt.setString(1, getGrade());
                pstmt.setString(2, getYear());
                pstmt.setString(3, mm.getStudentReg());
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    if ("1".equalsIgnoreCase(getTerm())) {
                        String resultDetail = "update tbfinalcompute set firstterm=?,dateupdated=?,updatedby=? where studentclass=?"
                                + "and year=? and studentreg=?";
                        pstmt = con.prepareStatement(resultDetail);

                        pstmt.setDouble(1, mm.getTermTotal());
                        pstmt.setString(2, DateManipulation.dateAndTime());
                        pstmt.setString(3, createdby);
                        pstmt.setString(4, mm.getGrade());
                        pstmt.setString(5, mm.getYear());
                        pstmt.setString(6, mm.getStudentReg());

                        pstmt.executeUpdate();

                    } else if ("2".equalsIgnoreCase(getTerm())) {
                        String resultDetail = "update tbfinalcompute set secondterm=?,dateupdated=?,updatedby=? where studentclass=?"
                                + " and year=? and studentreg=?";
                        pstmt = con.prepareStatement(resultDetail);

                        pstmt.setDouble(1, mm.getTermTotal());
                        pstmt.setString(2, DateManipulation.dateAndTime());
                        pstmt.setString(3, createdby);
                        pstmt.setString(4, mm.getGrade());
                        pstmt.setString(5, mm.getYear());
                        pstmt.setString(6, mm.getStudentReg());
                        pstmt.executeUpdate();

                    } else if ("3".equalsIgnoreCase(getTerm())) {
                        String resultDetail = "update tbfinalcompute set thirdterm=?,dateupdated=?,updatedby=? where studentclass=?"
                                + "and year=? and studentreg=?";
                        pstmt = con.prepareStatement(resultDetail);

                        pstmt.setDouble(1, mm.getTermTotal());
                        pstmt.setString(2, DateManipulation.dateAndTime());
                        pstmt.setString(3, createdby);
                        pstmt.setString(4, mm.getGrade());
                        pstmt.setString(5, mm.getYear());
                        pstmt.setString(6, mm.getStudentReg());
                        pstmt.executeUpdate();

                    }
                } else if (!rs.next()) {

                    if ("1".equalsIgnoreCase(getTerm())) {
                        String resultDetail = "insert into tbfinalcompute (studentreg,studentclass,term,year,firstterm,datecreated,createdby,isdeleted) values("
                                + "?,?,?,?,?,?,?,?)";
                        pstmt = con.prepareStatement(resultDetail);

                        pstmt.setString(1, mm.getStudentReg());

                        pstmt.setString(2, mm.getGrade());
                        pstmt.setString(3, mm.getTerm());
                        pstmt.setString(4, mm.getYear());
                        pstmt.setDouble(5, mm.getTermTotal());
                        pstmt.setString(6, DateManipulation.dateAndTime());
                        pstmt.setString(7, createdby);
                        pstmt.setBoolean(8, false);
                        pstmt.executeUpdate();

                    } else if ("2".equalsIgnoreCase(getTerm())) {
                        String resultDetail = "insert into tbfinalcompute (studentreg,studentclass,term,year,secondterm,datecreated,createdby,isdeleted) values("
                                + "?,?,?,?,?,?,?,?)";
                        pstmt = con.prepareStatement(resultDetail);

                        pstmt.setString(1, mm.getStudentReg());
                        pstmt.setString(2, mm.getGrade());
                        pstmt.setString(3, mm.getTerm());
                        pstmt.setString(4, mm.getYear());
                        pstmt.setDouble(5, mm.getTermTotal());
                        pstmt.setString(6, DateManipulation.dateAndTime());
                        pstmt.setString(7, createdby);
                        pstmt.setBoolean(8, false);
                        pstmt.executeUpdate();

                    } else if ("3".equalsIgnoreCase(getTerm())) {
                        String resultDetail = "insert into tbfinalcompute (studentreg,studentclass,term,year,thirdterm,datecreated,createdby,isdeleted) values("
                                + "?,?,?,?,?,?,?,?)";
                        pstmt = con.prepareStatement(resultDetail);

                        pstmt.setString(1, mm.getStudentReg());
                        pstmt.setString(2, mm.getGrade());
                        pstmt.setString(3, mm.getTerm());
                        pstmt.setString(4, mm.getYear());
                        pstmt.setDouble(5, mm.getTermTotal());
                        pstmt.setString(6, DateManipulation.dateAndTime());
                        pstmt.setString(7, createdby);
                        pstmt.setBoolean(8, false);
                        pstmt.executeUpdate();

                    }
                }
            }
            averagePosition(studentId);
            populatePositionFinal();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateDelete() throws Exception {
        try {

            DbConnectionX dbConnections = new DbConnectionX();
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            con = dbConnections.mySqlDBconnection();
            FacesContext context = FacesContext.getCurrentInstance();
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            List<ResultComputeModel> arrayDude = new ArrayList<>();
            String query = "select sum(totalscore) as total,term,studentreg,studentclass,year from tbresultcompute where studentreg=? and term=? and year=? and studentclass=? and isdeleted=? and datedeletedalone=?";
            for (ResultModel m : resultmodel1) {

                pstmt = con.prepareStatement(query);
                pstmt.setString(1, m.getStudentId());
                pstmt.setString(2, getTerm());
                pstmt.setString(3, getYear());
                pstmt.setString(4, getGrade());
                pstmt.setBoolean(5, true);
                pstmt.setString(6, DateManipulation.dateAlone());

                rs = pstmt.executeQuery();

                while (rs.next()) {
                    ResultComputeModel mode = new ResultComputeModel();
                    mode.setTermTotal(rs.getDouble("total"));
                    mode.setTerm(rs.getString("term"));
                    mode.setStudentReg(rs.getString("Studentreg"));
                    mode.setGrade(rs.getString("studentclass"));
                    mode.setYear(rs.getString("year"));
                    arrayDude.add(mode);
                }

            }

            for (ResultComputeModel mm : arrayDude) {

                String qu = "select * from tbfinalcompute where studentreg=? and year=? and studentclass=? and isdeleted=?";
                pstmt = con.prepareStatement(qu);
                pstmt.setString(1, mm.getStudentReg());
                pstmt.setString(2, getYear());
                pstmt.setString(3, getGrade());
                pstmt.setBoolean(4, false);

                rs = pstmt.executeQuery();
                double val = 0;
                while (rs.next()) {

                    val = rs.getDouble("totalscore");

                }

                if ("1".equalsIgnoreCase(mm.getTerm())) {
                    String resultDetail = "update tbfinalcompute set firstterm=?,totalscore=?, isdeleted=?,datedeleted=?,deletedby=? where studentclass=?"
                            + "and year=? and studentreg=?";
                    pstmt = con.prepareStatement(resultDetail);
                    pstmt.setDouble(1, 0);
                    pstmt.setDouble(2, val - mm.getTermTotal());
                    pstmt.setBoolean(3, true);
                    pstmt.setString(4, DateManipulation.dateAndTime());
                    pstmt.setString(5, createdby);
                    pstmt.setString(6, mm.getGrade());
                    pstmt.setString(7, mm.getYear());
                    pstmt.setString(8, mm.getStudentReg());

                    pstmt.executeUpdate();
                } else if ("2".equalsIgnoreCase(mm.getTerm())) {
                    String resultDetail = "update tbfinalcompute set secondterm=?,totalscore=?, isdeleted=?,datedeleted=?,deletedby=? where studentclass=?"
                            + "and year=? and studentreg=?";
                    pstmt = con.prepareStatement(resultDetail);
                    pstmt.setDouble(1, 0);
                    pstmt.setDouble(2, val - mm.getTermTotal());
                    pstmt.setBoolean(3, true);
                    pstmt.setString(4, DateManipulation.dateAndTime());
                    pstmt.setString(5, createdby);
                    pstmt.setString(6, mm.getGrade());
                    pstmt.setString(7, mm.getYear());
                    pstmt.setString(8, mm.getStudentReg());

                    pstmt.executeUpdate();
                } else if ("3".equalsIgnoreCase(getTerm())) {
                    String resultDetail = "update tbfinalcompute set thirdterm=?,totalscore=?, isdeleted=?,datedeleted=?,deletedby=? where studentclass=?"
                            + "and year=? and studentreg=?";
                    pstmt = con.prepareStatement(resultDetail);
                    pstmt.setDouble(1, 0);
                    pstmt.setDouble(2, val - mm.getTermTotal());
                    pstmt.setBoolean(3, true);
                    pstmt.setString(4, DateManipulation.dateAndTime());
                    pstmt.setString(5, createdby);
                    pstmt.setString(6, mm.getGrade());
                    pstmt.setString(7, mm.getYear());
                    pstmt.setString(8, mm.getStudentReg());

                    pstmt.executeUpdate();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void averagePosition(List<String> studentId) throws Exception {
        try {

            DbConnectionX dbConnections = new DbConnectionX();
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            con = dbConnections.mySqlDBconnection();
            FacesContext context = FacesContext.getCurrentInstance();
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            List<ResultComputeModel> arrayDude = new ArrayList<>();
            String query = "select sum(totalscore) as sumtotal,studentreg,studentclass,year from schlmgt.tbresultcompute where studentreg=? and year=? and studentclass=? and isdeleted=? and datealone=?";
            for (int i = 0; i < studentId.size(); i++) {

                pstmt = con.prepareStatement(query);
                pstmt.setString(1, studentId.get(i));
                pstmt.setString(2, getYear());
                pstmt.setString(3, getGrade());
                pstmt.setBoolean(4, false);
                pstmt.setString(5, DateManipulation.dateAlone());

                rs = pstmt.executeQuery();

                while (rs.next()) {
                    ResultComputeModel mode = new ResultComputeModel();
                    mode.setSumTotal(rs.getDouble("sumtotal"));
                    mode.setStudentReg(rs.getString("Studentreg"));
                    mode.setGrade(rs.getString("studentclass"));
                    mode.setYear(rs.getString("year"));
                    arrayDude.add(mode);
                }

                String resultDetail = "update tbfinalcompute set totalscore=?,average=?,updatedby=? where studentclass=?"
                        + "and year=? and studentreg=?";

                for (ResultComputeModel mm : arrayDude) {
                    pstmt = con.prepareStatement(resultDetail);
                    pstmt.setDouble(1, mm.getSumTotal());
                    pstmt.setDouble(2, mm.getSumTotal() / 3);
                    pstmt.setString(3, createdby);
                    pstmt.setString(4, mm.getGrade());
                    pstmt.setString(5, mm.getYear());
                    pstmt.setString(6, mm.getStudentReg());
                    pstmt.executeUpdate();
                }
            }
            populatePositionFinal();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void averagePosition(String studentId) throws Exception {
        try {

            DbConnectionX dbConnections = new DbConnectionX();
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            con = dbConnections.mySqlDBconnection();
            FacesContext context = FacesContext.getCurrentInstance();
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            List<ResultComputeModel> arrayDude = new ArrayList<>();
            String query = "select sum(totalscore) as sumtotal,studentreg,studentclass,year from schlmgt.tbresultcompute where studentreg=? and year=? and studentclass=? and isdeleted=?";

            pstmt = con.prepareStatement(query);
            pstmt.setString(1, studentId);
            pstmt.setString(2, getYear());
            pstmt.setString(3, getGrade());
            pstmt.setBoolean(4, false);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                ResultComputeModel mode = new ResultComputeModel();
                mode.setSumTotal(rs.getDouble("sumtotal"));
                mode.setStudentReg(rs.getString("Studentreg"));
                mode.setGrade(rs.getString("studentclass"));
                mode.setYear(rs.getString("year"));
                arrayDude.add(mode);

                String resultDetail = "update tbfinalcompute set totalscore=?,average=?,updatedby=? where studentclass=?"
                        + "and year=? and studentreg=? and isdeleted=?";

                for (ResultComputeModel mm : arrayDude) {
                    pstmt = con.prepareStatement(resultDetail);
                    pstmt.setDouble(1, mm.getSumTotal());
                    pstmt.setDouble(2, mm.getSumTotal() / 3);
                    pstmt.setString(3, createdby);
                    pstmt.setString(4, mm.getGrade());
                    pstmt.setString(5, mm.getYear());
                    pstmt.setString(6, mm.getStudentReg());
                    pstmt.setBoolean(7, false);
                    pstmt.executeUpdate();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateComputeFinal(String studentReg) throws SQLException {
        try {

            DbConnectionX dbConnections = new DbConnectionX();
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            List<Double> studentID = new ArrayList<>();
            List<Integer> arm = new ArrayList<>();
            con = dbConnections.mySqlDBconnection();
            FacesContext context = FacesContext.getCurrentInstance();
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();
            List<ResultComputeModel> arrayDude = new ArrayList<>();
            String query = "SELECT * FROM tbresultcompute where studentclass=? and term=? and year=? and studentreg=? and isdeleted=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, getGrade());
            pstmt.setString(2, getTerm());
            pstmt.setString(3, getYear());
            pstmt.setString(4, studentReg);
            pstmt.setBoolean(5, false);
            rs = pstmt.executeQuery();
            ResultComputeModel mode = new ResultComputeModel();
            if (rs.next()) {

                mode.setStudentReg(rs.getString("studentreg"));
                mode.setGrade(rs.getString("Studentclass"));
                mode.setTerm(rs.getString("term"));
                mode.setYear(rs.getString("year"));
                mode.setTermTotal(rs.getDouble("totalScore"));

                arrayDude.add(mode);
            }

            if ("1".equalsIgnoreCase(mode.getTerm())) {
                String updatePosition = "update tbfinalcompute set firstterm=?,dateupdated=?,updatedby=? where StudentReg=? and studentclass=? and year=?";

                pstmt = con.prepareStatement(updatePosition);

                pstmt.setDouble(1, mode.getTermTotal());
                pstmt.setString(2, DateManipulation.dateAndTime());
                pstmt.setString(3, createdby);
                pstmt.setString(4, mode.getStudentReg());
                pstmt.setString(5, mode.getGrade());
                pstmt.setString(6, mode.getYear());
                pstmt.executeUpdate();

            } else if ("2".equalsIgnoreCase(mode.getTerm())) {
                String updatePosition = "update tbfinalcompute set secondterm=?,dateupdated=?,updatedby=? where StudentReg=? and studentclass=? and year=?";

                pstmt = con.prepareStatement(updatePosition);

                pstmt.setDouble(1, mode.getTermTotal());
                pstmt.setString(2, DateManipulation.dateAndTime());
                pstmt.setString(3, createdby);
                pstmt.setString(4, mode.getStudentReg());
                pstmt.setString(5, mode.getGrade());
                pstmt.setString(6, mode.getYear());
                pstmt.executeUpdate();

            } else if ("3".equalsIgnoreCase(mode.getTerm())) {
                String updatePosition = "update tbfinalcompute set thirdterm=?,dateupdated=?,updatedby=? where StudentReg=? and studentclass=? and year=?";

                pstmt = con.prepareStatement(updatePosition);

                pstmt.setDouble(1, mode.getTermTotal());
                pstmt.setString(2, DateManipulation.dateAndTime());
                pstmt.setString(3, createdby);
                pstmt.setString(4, mode.getStudentReg());
                pstmt.setString(5, mode.getGrade());
                pstmt.setString(6, mode.getYear());
                pstmt.executeUpdate();

            }
            populatePositionFinal();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void DeleteComputeFinal(String studentReg) throws SQLException {
        try {

            DbConnectionX dbConnections = new DbConnectionX();
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            List<Double> studentID = new ArrayList<>();
            List<Integer> arm = new ArrayList<>();
            con = dbConnections.mySqlDBconnection();
            FacesContext context = FacesContext.getCurrentInstance();
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();
            List<ResultComputeModel> arrayDude = new ArrayList<>();

            String query = "SELECT * FROM tbresultcompute where studentclass=? and term=? and year=? and studentreg=? and isdeleted=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, getGrade());
            pstmt.setString(2, getTerm());
            pstmt.setString(3, getYear());
            pstmt.setString(4, studentReg);
            pstmt.setBoolean(5, false);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ResultComputeModel mode = new ResultComputeModel();
                mode.setStudentReg(rs.getString("studentreg"));
                mode.setGrade(rs.getString("Studentclass"));
                mode.setTerm(rs.getString("term"));
                mode.setYear(rs.getString("year"));
                mode.setTermTotal(rs.getDouble("totalScore"));

                arrayDude.add(mode);
            }
            for (ResultComputeModel mm : arrayDude) {
                if ("1".equalsIgnoreCase(mm.getTerm())) {
                    String updatePosition = "update tbfinalcompute set isdeleted=?,datedeleted=?,deletedby=? where StudentReg=? and studentclass=? and term=? and year=?";

                    pstmt = con.prepareStatement(updatePosition);

                    pstmt.setBoolean(1, true);
                    pstmt.setString(2, DateManipulation.dateAndTime());
                    pstmt.setString(3, createdby);
                    pstmt.setString(3, mm.getStudentReg());
                    pstmt.setString(4, mm.getGrade());
                    pstmt.setString(5, mm.getTerm());
                    pstmt.setString(6, mm.getYear());
                    pstmt.executeUpdate();

                } else if ("2".equalsIgnoreCase(mm.getTerm())) {
                    String updatePosition = "update tbfinalcompute set secondterm=?, datedeleted=?,deletedby=? where StudentReg=? and studentclass=? and term=? and year=?";

                    pstmt = con.prepareStatement(updatePosition);

                    pstmt.setBoolean(1, true);
                    pstmt.setString(2, DateManipulation.dateAndTime());
                    pstmt.setString(3, createdby);
                    pstmt.setString(3, mm.getStudentReg());
                    pstmt.setString(4, mm.getGrade());
                    pstmt.setString(5, mm.getTerm());
                    pstmt.setString(6, mm.getYear());
                    pstmt.executeUpdate();

                } else if ("3".equalsIgnoreCase(mm.getTerm())) {
                    String updatePosition = "update tbfinalcompute set thirdterm=?, datedeleted=?,deletedby=? where StudentReg=? and studentclass=? and term=? and year=?";

                    pstmt.setBoolean(1, true);
                    pstmt.setString(2, DateManipulation.dateAndTime());
                    pstmt.setString(3, createdby);
                    pstmt.setString(3, mm.getStudentReg());
                    pstmt.setString(4, mm.getGrade());
                    pstmt.setString(5, mm.getTerm());
                    pstmt.setString(6, mm.getYear());
                    pstmt.executeUpdate();

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void DeleteComputeFinal() throws SQLException {
        try {

            DbConnectionX dbConnections = new DbConnectionX();
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            List<Double> studentID = new ArrayList<>();
            List<Integer> arm = new ArrayList<>();
            con = dbConnections.mySqlDBconnection();
            FacesContext context = FacesContext.getCurrentInstance();
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();
            List<ResultComputeModel> arrayDude = new ArrayList<>();

            for (ResultModel moe : resultmodel1) {

                if ("1".equalsIgnoreCase(moe.getTerm())) {
                    String updatePosition = "update tbfinalcompute set isdeleted=?,datedeleted=?,deletedby=? where StudentReg=? and studentclass=? and year=?";

                    pstmt = con.prepareStatement(updatePosition);

                    pstmt.setBoolean(1, true);
                    pstmt.setString(2, DateManipulation.dateAndTime());
                    pstmt.setString(3, createdby);
                    System.out.println(moe.getStudentId() + " B: " + moe.getGrade() + " C: " + moe.getTerm() + " D: " + moe.getYear());
                    pstmt.setString(4, moe.getStudentId());
                    pstmt.setString(5, moe.getGrade());
                    pstmt.setString(6, moe.getYear());
                    pstmt.executeUpdate();

                } else if ("2".equalsIgnoreCase(moe.getTerm())) {
                    String updatePosition = "update tbfinalcompute set isdeleted=?,datedeleted=?,deletedby=? where StudentReg=? and studentclass=? and year=?";

                    pstmt = con.prepareStatement(updatePosition);

                    pstmt.setBoolean(1, true);
                    pstmt.setString(2, DateManipulation.dateAndTime());
                    pstmt.setString(3, createdby);
                    pstmt.setString(4, moe.getStudentId());
                    pstmt.setString(5, moe.getGrade());
                    pstmt.setString(6, moe.getTerm());
                    pstmt.setString(7, moe.getYear());
                    pstmt.executeUpdate();

                } else if ("3".equalsIgnoreCase(moe.getTerm())) {
                    String updatePosition = "update tbfinalcompute set thirdterm=?, datedeleted=?,deletedby=? where StudentReg=? and studentclass=? and term=? and year=?";

                    pstmt = con.prepareStatement(updatePosition);

                    pstmt.setBoolean(1, true);
                    pstmt.setString(2, DateManipulation.dateAndTime());
                    pstmt.setString(3, createdby);
                    pstmt.setString(4, moe.getStudentId());
                    pstmt.setString(5, moe.getGrade());
                    pstmt.setString(6, moe.getTerm());
                    pstmt.setString(7, moe.getYear());
                    pstmt.executeUpdate();

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setResultmodel2(List<ResultModel> resultmodel2) {
        this.resultmodel2 = resultmodel2;
    }

    public ResultModel getModelResult() {
        return modelResult;
    }

    public void setModelResult(ResultModel modelResult) {
        this.modelResult = modelResult;
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
