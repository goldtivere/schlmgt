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
@ManagedBean(name = "editClass")
@ViewScoped
public class ClassEdit implements Serializable {

    private EditStudent edits = new EditStudent();
    private List<SecondaryModel> subModel;

    @PostConstruct
    public void init() {
        try {
            subModel = clasEdit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<SecondaryModel> clasEdit() throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        edits.studDetails();
        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbstudentclass where studentid=? and currentclass=? order by id desc";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, edits.getStudentid());
            System.out.println(edits.getStudentid());
            pstmt.setBoolean(2, false);
            rs = pstmt.executeQuery();
            //
            List<SecondaryModel> lst = new ArrayList<>();
            while (rs.next()) {

                SecondaryModel coun = new SecondaryModel();
                coun.setId(rs.getInt("id"));
                coun.setCurrentClass(rs.getString("class"));
                coun.setTerm(rs.getString("term"));
                coun.setYear(rs.getString("year"));
                coun.setNoOfTimesRepeated(rs.getString("NoOfTimesRepeated"));

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

    public EditStudent getEdits() {
        return edits;
    }

    public void setEdits(EditStudent edits) {
        this.edits = edits;
    }

    public List<SecondaryModel> getSubModel() {
        return subModel;
    }

    public void setSubModel(List<SecondaryModel> subModel) {
        this.subModel = subModel;
    }

}
