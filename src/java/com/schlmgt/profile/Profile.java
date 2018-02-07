/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.profile;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.register.GradeModel;
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
import javax.swing.table.TableModel;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "prof")
@ViewScoped
public class Profile implements Serializable {

    private List<ClassModel> classmodel;
    private List<GradeModel> grademodel;
    private List<NurseryModel> nursModel;
    private List<PrimaryModel> priModel;
    private List<SecondaryModel> secModel;
    private ClassModel model = new ClassModel();
    private GradeModel gradModel = new GradeModel();
    private String student_class;
    private String nfname;
    private String pfname;
    private String sfname;
    private String gohome;
    private String search_item;
    private String grade;
    private Boolean nursery, primary, secondary;
    private Boolean nbool, pbool, sbool, fbool;

    @PostConstruct
    public void init() {
        try {
            classmodel = classDropdown();
            nursery = false;
            primary = false;
            secondary = false;
            nbool = false;
            pbool = false;
            sbool = false;
            fbool = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }   

    public void searchTab() {
        try {
            if (model.getTbclass().equalsIgnoreCase("nursery") && !getNfname().isEmpty()) {
                nursModel = onNurserySearch(getNfname());

            } else if (model.getTbclass().equalsIgnoreCase("primary") && !getPfname().isEmpty()) {
                priModel = onPrimarySearch(getPfname());

            } else if (model.getTbclass().equalsIgnoreCase("secondary") && !getSfname().isEmpty()) {
                secModel = onSecondarySearch(getSfname());
            }
            System.out.println(getNfname() + " f " + getPfname() + " a " + getSfname());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public List<NurseryModel> onNurserySearch(String fullname) throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbnursery where full_name=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, fullname);
            rs = pstmt.executeQuery();
            //
            List<NurseryModel> lst = new ArrayList<>();
            while (rs.next()) {

                NurseryModel coun = new NurseryModel();
                coun.setId(rs.getInt("id"));
                coun.setStudentid(rs.getInt("studentid"));
                coun.setFirst_name(rs.getString("first_name"));
                coun.setMiddle_name(rs.getString("middle_name"));
                coun.setLast_name(rs.getString("last_name"));
                coun.setFull_name(rs.getString("full_name"));
                coun.setSclass(rs.getString("class"));
                coun.setPromoted(rs.getBoolean("promoted"));
                coun.setImageLink(rs.getString("imagelink"));
                coun.setArm(rs.getString("arm"));

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

    public List<PrimaryModel> onPrimarySearch(String fullname) throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbprimary where full_name=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, fullname);
            rs = pstmt.executeQuery();
            //
            List<PrimaryModel> lst = new ArrayList<>();
            while (rs.next()) {

                PrimaryModel coun = new PrimaryModel();
                coun.setId(rs.getInt("id"));
                coun.setStudentid(rs.getInt("studentid"));
                coun.setFirst_name(rs.getString("first_name"));
                coun.setMiddle_name(rs.getString("middle_name"));
                coun.setLast_name(rs.getString("last_name"));
                coun.setFull_name(rs.getString("full_name"));
                coun.setSclass(rs.getString("class"));
                coun.setPromoted(rs.getBoolean("promoted"));
                coun.setImageLink(rs.getString("imagelink"));
                coun.setArm(rs.getString("arm"));

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

    public List<SecondaryModel> onSecondarySearch(String fullname) throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbsecondary where full_name=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, fullname);
            rs = pstmt.executeQuery();
            //
            List<SecondaryModel> lst = new ArrayList<>();
            while (rs.next()) {

                SecondaryModel coun = new SecondaryModel();
                coun.setId(rs.getInt("id"));
                coun.setStudentid(rs.getInt("studentid"));
                coun.setFirst_name(rs.getString("first_name"));
                coun.setMiddle_name(rs.getString("middle_name"));
                coun.setLast_name(rs.getString("last_name"));
                coun.setFull_name(rs.getString("full_name"));
                coun.setSclass(rs.getString("class"));
                coun.setPromoted(rs.getBoolean("promoted"));
                coun.setImageLink(rs.getString("imagelink"));
                coun.setArm(rs.getString("arm"));

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

    public List<NurseryModel> onNurseryChange() throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbnursery";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            //
            List<NurseryModel> lst = new ArrayList<>();
            while (rs.next()) {

                NurseryModel coun = new NurseryModel();
                coun.setId(rs.getInt("id"));
                coun.setStudentid(rs.getInt("studentid"));
                coun.setFirst_name(rs.getString("first_name"));
                coun.setMiddle_name(rs.getString("middle_name"));
                coun.setLast_name(rs.getString("last_name"));
                coun.setFull_name(rs.getString("full_name"));
                coun.setSclass(rs.getString("class"));
                coun.setPromoted(rs.getBoolean("promoted"));
                coun.setImageLink(rs.getString("imagelink"));
                coun.setArm(rs.getString("arm"));

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

    public List<PrimaryModel> onPrimaryChange() throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbprimary";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            //
            List<PrimaryModel> lst = new ArrayList<>();
            while (rs.next()) {

                PrimaryModel coun = new PrimaryModel();
                coun.setId(rs.getInt("id"));
                coun.setStudentid(rs.getInt("studentid"));
                coun.setFirst_name(rs.getString("first_name"));
                coun.setMiddle_name(rs.getString("middle_name"));
                coun.setLast_name(rs.getString("last_name"));
                coun.setFull_name(rs.getString("full_name"));
                coun.setSclass(rs.getString("class"));
                coun.setPromoted(rs.getBoolean("promoted"));
                coun.setImageLink(rs.getString("imagelink"));
                coun.setArm(rs.getString("arm"));

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

    public List<SecondaryModel> onSecondaryChange() throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbsecondary";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            //
            List<SecondaryModel> lst = new ArrayList<>();
            while (rs.next()) {

                SecondaryModel coun = new SecondaryModel();
                coun.setId(rs.getInt("id"));
                coun.setStudentid(rs.getInt("studentid"));
                coun.setFirst_name(rs.getString("first_name"));
                coun.setMiddle_name(rs.getString("middle_name"));
                coun.setLast_name(rs.getString("last_name"));
                coun.setFull_name(rs.getString("full_name"));
                coun.setSclass(rs.getString("class"));
                coun.setPromoted(rs.getBoolean("promoted"));
                coun.setImageLink(rs.getString("imagelink"));
                coun.setArm(rs.getString("arm"));

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

    public List<String> nuseryAuto() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbnursery";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            //
            List<String> lst = new ArrayList<>();
            String fullname;
            while (rs.next()) {

                fullname = rs.getString("full_name");

                lst.add(fullname);
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

    public List<String> completNursery(String val) {
        List<String> com = new ArrayList();
        try {
            for (String value : nuseryAuto()) {
                if (value.toUpperCase().contains(val.toUpperCase())) {
                    com.add(value);
                }

            }
            return com;
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }

    }

    public List<String> primaryAuto() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbprimary";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            //
            List<String> lst = new ArrayList<>();
            String fullname;
            while (rs.next()) {

                fullname = rs.getString("full_name");

                lst.add(fullname);
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

    public List<String> completPrimary(String val) {
        List<String> com = new ArrayList();
        try {
            for (String value : primaryAuto()) {
                if (value.toUpperCase().contains(val.toUpperCase())) {
                    com.add(value);
                }

            }
            return com;
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }

    }

    public List<String> secondaryAuto() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbsecondary";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            //
            List<String> lst = new ArrayList<>();
            String fullname;
            while (rs.next()) {

                fullname = rs.getString("full_name");

                lst.add(fullname);
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

    public List<String> completSecondary(String val) {
        List<String> com = new ArrayList();
        try {
            for (String value : secondaryAuto()) {
                if (value.toUpperCase().contains(val.toUpperCase())) {
                    com.add(value);
                }

            }
            return com;
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }

    }

    public void onClassChange() throws Exception {

        if (model.getTbclass().equalsIgnoreCase("nursery")) {
            nursModel = onNurseryChange();
            setNursery(true);
            setPrimary(false);
            setSecondary(false);
            setNbool(true);
            setFbool(false);
            setPbool(false);
            setSbool(false);

        } else if (model.getTbclass().equalsIgnoreCase("primary")) {
            priModel = onPrimaryChange();
            setPrimary(true);
            setNursery(false);
            setSecondary(false);
            setNbool(false);
            setFbool(false);
            setPbool(true);
            setSbool(false);

        } else if (model.getTbclass().equalsIgnoreCase("secondary")) {
            secModel = onSecondaryChange();
            setSecondary(true);
            setNursery(false);
            setPrimary(false);
            setNbool(false);
            setFbool(false);
            setPbool(false);
            setSbool(true);

        }
    }

    public List<ClassModel> classDropdown() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbclass";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            //
            List<ClassModel> lst = new ArrayList<>();
            while (rs.next()) {

                ClassModel coun = new ClassModel();
                coun.setId(rs.getInt("id"));
                coun.setTbclass(rs.getString("class"));

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

    public String getNfname() {
        return nfname;
    }

    public void setNfname(String nfname) {
        this.nfname = nfname;
    }

    public String getPfname() {
        return pfname;
    }

    public void setPfname(String pfname) {
        this.pfname = pfname;
    }

    public String getSfname() {
        return sfname;
    }

    public void setSfname(String sfname) {
        this.sfname = sfname;
    }

    public String getGohome() {
        return gohome;
    }

    public void setGohome(String gohome) {
        this.gohome = gohome;
    }

    public Boolean getNbool() {
        return nbool;
    }

    public void setNbool(Boolean nbool) {
        this.nbool = nbool;
    }

    public Boolean getPbool() {
        return pbool;
    }

    public void setPbool(Boolean pbool) {
        this.pbool = pbool;
    }

    public Boolean getSbool() {
        return sbool;
    }

    public void setSbool(Boolean sbool) {
        this.sbool = sbool;
    }

    public Boolean getFbool() {
        return fbool;
    }

    public void setFbool(Boolean fbool) {
        this.fbool = fbool;
    }

    public List<ClassModel> getClassmodel() {
        return classmodel;
    }

    public void setClassmodel(List<ClassModel> classmodel) {
        this.classmodel = classmodel;
    }

    public List<NurseryModel> getNursModel() {
        return nursModel;
    }

    public void setNursModel(List<NurseryModel> nursModel) {
        this.nursModel = nursModel;
    }

    public List<PrimaryModel> getPriModel() {
        return priModel;
    }

    public void setPriModel(List<PrimaryModel> priModel) {
        this.priModel = priModel;
    }

    public List<SecondaryModel> getSecModel() {
        return secModel;
    }

    public void setSecModel(List<SecondaryModel> secModel) {
        this.secModel = secModel;
    }

    public Boolean getNursery() {
        return nursery;
    }

    public void setNursery(Boolean nursery) {
        this.nursery = nursery;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public Boolean getSecondary() {
        return secondary;
    }

    public void setSecondary(Boolean secondary) {
        this.secondary = secondary;
    }

    public List<GradeModel> getGrademodel() {
        return grademodel;
    }

    public void setGrademodel(List<GradeModel> grademodel) {
        this.grademodel = grademodel;
    }

    public ClassModel getModel() {
        return model;
    }

    public void setModel(ClassModel model) {
        this.model = model;
    }

    public GradeModel getGradModel() {
        return gradModel;
    }

    public void setGradModel(GradeModel gradModel) {
        this.gradModel = gradModel;
    }

    public String getStudent_class() {
        return student_class;
    }

    public void setStudent_class(String student_class) {
        this.student_class = student_class;
    }

    public String getSearch_item() {
        return search_item;
    }

    public void setSearch_item(String search_item) {
        this.search_item = search_item;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

}
