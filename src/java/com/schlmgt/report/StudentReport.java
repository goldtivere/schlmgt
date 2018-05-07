/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.report;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.updateResult.ResultModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Gold
 */
@ManagedBean
@RequestScoped
public class StudentReport {

    private List<ColumnModel> personas = new ArrayList<ColumnModel>();
    private List<ResultModel> tableData;
    private List<String> tableHeaderNames;
    private List<String> subHead;

    @PostConstruct
    public void init() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ResultModel> getTableData() {
        return tableData;
    }

    public void setTableData(List<ResultModel> tableData) {
        this.tableData = tableData;
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

    public void postProcessXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow header = sheet.getRow(0);

        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            HSSFCell cell = header.getCell(i);

            cell.setCellStyle(cellStyle);
        }
    }

    public List<String> displaySubject() throws Exception {
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
                coun.setStudentId(rs.getString("studentreg"));
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

    public void writeToExcel() throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("resultSheet");
        Font headerFont = workbook.createFont();
        headerFont.setBoldweight((short) 20);
        headerFont.setFontHeightInPoints((short) 17);
        headerFont.setColor(IndexedColors.RED.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        Row headerRow = sheet.createRow(6);
        tableHeaderNames = displaySubject();
        int val = 1;
        int lav = 2;

        Cell cells = headerRow.createCell(0);
        cells.setCellValue("Student Number");
        for (int i = 0; i < tableHeaderNames.size(); i++) {
            Cell cell = headerRow.createCell(val);
            cell.setCellValue(tableHeaderNames.get(i));

            System.out.println("Done" + val);
            System.out.println("Done" + lav);
            System.out.println(tableHeaderNames.get(i));
            sheet.addMergedRegion(new CellRangeAddress(
                    6, //first row (0-based)
                    6, //last row  (0-based)
                    val, //first column (0-based)
                    lav //last column  (0-based)                     
            ));

            val = val + 2;
            lav = lav + 2;
        }
        Cell cel = headerRow.createCell(lav);
        cel.setCellValue("Grand Total");
        Cell ce = headerRow.createCell(lav+1);
        ce.setCellValue("Class Average");
        Cell c = headerRow.createCell(lav+2);
        c.setCellValue("Position");
        FileOutputStream fileOut = new FileOutputStream("C:/contact.xlsx");
        workbook.write(fileOut);
        fileOut.close();
        System.out.println("Done");

//        Workbook wb = new XSSFWorkbook();
//        Sheet sheet = wb.createSheet("new sheet");
//
//        Row row = sheet.createRow(0);
//        Cell cell = row.createCell(0);
//       
//        cell.setCellValue("This is a test of merging");
//
//        sheet.addMergedRegion(new CellRangeAddress(
//                0, //first row (0-based)
//                0, //last row  (0-based)
//                0, //first column (0-based)
//                1 //last column  (0-based)
//        ));
//        
//         Cell cells = row.createCell(2);
//       
//        cells.setCellValue("test of merging");
//
//        sheet.addMergedRegion(new CellRangeAddress(
//                0, //first row (0-based)
//                0, //last row  (0-based)
//                2, //first column (0-based)
//                3 //last column  (0-based)
//        ));
//        
//        
//        // Write the output to a file
//        try (OutputStream fileOut = new FileOutputStream("C:/workbook.xlsx")) {
//            wb.write(fileOut);
//        }
    }

}
