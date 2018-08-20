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
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "roleManager")
@ViewScoped
public class RoleManager implements Serializable {

    private List<RoleManagerModel> roleManager;
    private List<RoleManagerModel> roleManager1;

    @PostConstruct
    public void init() {
        try {
            roleManager = displayRole();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<RoleManagerModel> displayRole() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM user_details where is_deleted=? and suspendedstatus=? and roleassigned=2";
            pstmt = con.prepareStatement(query);
            pstmt.setBoolean(1, false);
            pstmt.setBoolean(2, false);
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

    public List<RoleManagerModel> getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(List<RoleManagerModel> roleManager) {
        this.roleManager = roleManager;
    }

    public List<RoleManagerModel> getRoleManager1() {
        return roleManager1;
    }

    public void setRoleManager1(List<RoleManagerModel> roleManager1) {
        this.roleManager1 = roleManager1;
    }

}
