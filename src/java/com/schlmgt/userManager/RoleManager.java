/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.userManager;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.updateResult.ResultModel;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;

/**
 *
 * @author Gold
 */
public class RoleManager implements Serializable{
    
    public List<RoleManagerModel> displayResult() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM user_details where isdeleted=?,suspendedstatus=?";
            pstmt = con.prepareStatement(query);           
            pstmt.setBoolean(4, false);
            rs = pstmt.executeQuery();
            //
            List<RoleManagerModel> lst = new ArrayList<>();
            while (rs.next()) {

                RoleManagerModel coun = new RoleManagerModel();
                coun.setId(rs.getInt("id"));
                coun.setFirstName(rs.getString("first_name"));
                coun.setMiddleName(rs.getString("middlename"));
                coun.setLastName(rs.getString("last_name"));
                coun.setCanUpdateResult(rs.getBoolean("canupdateresult"));
                coun.setCanUpdateSubject(rs.getBoolean("canupdatesubject"));
                coun.setCanRegisterStudent(rs.getBoolean("canregisterstudent"));
                coun.setCanRegisterStaff(rs.getBoolean("canregisterstaff"));
                coun.setCanSendMessage(rs.getBoolean("cansendText"));
    
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
    
}
