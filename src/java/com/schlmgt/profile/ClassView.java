/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.profile;

import com.schlmgt.dbconn.DbConnectionX;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "views")
@ViewScoped
public class ClassView implements Serializable {

    private EditStudent edits = new EditStudent();
    private List<SubjectModel> sub;

    @PostConstruct
    public void init() {
        //testMic();
    }

    /**
     * public List<SubjectModel> testMic() throws SQLException{
     * edits.studDetails(); DbConnectionX dbConnections = new DbConnectionX();
     * Connection con = null; PreparedStatement pstmt = null; ResultSet rs =
     * null; con = dbConnections.mySqlDBconnection(); String studId;
     *
     * try { String testguid = "Select * from studentclass where studentid=? and
     * currentclass=?"; pstmt = con.prepareStatement(testguid);
     * pstmt.setString(1, edits.getStudentid()); pstmt.setBoolean(2, true); rs =
     * pstmt.executeQuery();
     *
     * List<SubjectModel> lst = new ArrayList<>(); while (rs.next()) {
     * SubjectModel coun = new SubjectModel(); coun.setId(rs.getInt("id"));
     * coun.setSubject(rs.getString("bloodgroup"));
     *
     * // lst.add(coun);
     *
     * }
     * rs = pstmt.executeQuery();
     *
     * System.out.println(edits.getStudentid()); } catch (Exception ex) {
     *
     * } finally {
     *
     * if (!(con == null)) { con.close(); con = null; } if (!(pstmt == null)) {
     * pstmt.close(); pstmt = null; }
     *
     * }
     * }
     *
     */
    public EditStudent getEdits() {
        return edits;
    }

    public void setEdits(EditStudent edits) {
        this.edits = edits;
    }

}
