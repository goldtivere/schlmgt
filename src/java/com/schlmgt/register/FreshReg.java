/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.register;

import com.schlmgt.dbconn.DbConnectionX;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "freg")
@ViewScoped
public class FreshReg implements Serializable {

    private String fname;
    private String mname;
    private String lname;
    private Date dob;
    private String pnum;
    private String email;
    private String gfname;
    private String gmname;
    private String glname;
    private String grelation;
    private String gpnum;
    private String gemail;
    private String gcount;
    private String gstate;
    private String glga;
    private String gaddress;
    private String pschl;
    private String sclass;
    private String grade;
    private Boolean disability;
    private String bgroup;
    private String disab;
    private UploadedFile uploadImage;
    private List<RelationshipModel> relation;
    private RelationshipModel modes = new RelationshipModel();
    
    
    @PostConstruct
    public void init(){
        try{
            relation=relationModel();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public List<RelationshipModel> relationModel() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM relationship";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            //
            List<RelationshipModel> lst = new ArrayList<>();
            while (rs.next()) {

                RelationshipModel rela = new RelationshipModel();
                rela.setId(rs.getInt("Id"));
                rela.setRelation(rs.getString("relation"));
                //
                lst.add(rela);
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

    public void handleFileUpload(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public List<RelationshipModel> getRelation() {
        return relation;
    }

    public void setRelation(List<RelationshipModel> relation) {
        this.relation = relation;
    }

    public RelationshipModel getModes() {
        return modes;
    }

    public void setModes(RelationshipModel modes) {
        this.modes = modes;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getPnum() {
        return pnum;
    }

    public void setPnum(String pnum) {
        this.pnum = pnum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGfname() {
        return gfname;
    }

    public void setGfname(String gfname) {
        this.gfname = gfname;
    }

    public String getGmname() {
        return gmname;
    }

    public void setGmname(String gmname) {
        this.gmname = gmname;
    }

    public String getGlname() {
        return glname;
    }

    public void setGlname(String glname) {
        this.glname = glname;
    }

    public String getGrelation() {
        return grelation;
    }

    public void setGrelation(String grelation) {
        this.grelation = grelation;
    }

    public String getGpnum() {
        return gpnum;
    }

    public void setGpnum(String gpnum) {
        this.gpnum = gpnum;
    }

    public String getGemail() {
        return gemail;
    }

    public void setGemail(String gemail) {
        this.gemail = gemail;
    }

    public String getGcount() {
        return gcount;
    }

    public void setGcount(String gcount) {
        this.gcount = gcount;
    }

    public String getGstate() {
        return gstate;
    }

    public void setGstate(String gstate) {
        this.gstate = gstate;
    }

    public String getGlga() {
        return glga;
    }

    public void setGlga(String glga) {
        this.glga = glga;
    }

    public String getGaddress() {
        return gaddress;
    }

    public void setGaddress(String gaddress) {
        this.gaddress = gaddress;
    }

    public String getPschl() {
        return pschl;
    }

    public void setPschl(String pschl) {
        this.pschl = pschl;
    }

    public String getSclass() {
        return sclass;
    }

    public void setSclass(String sclass) {
        this.sclass = sclass;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Boolean getDisability() {
        return disability;
    }

    public void setDisability(Boolean disability) {
        this.disability = disability;
    }

    public String getBgroup() {
        return bgroup;
    }

    public void setBgroup(String bgroup) {
        this.bgroup = bgroup;
    }

    public String getDisab() {
        return disab;
    }

    public void setDisab(String disab) {
        this.disab = disab;
    }

    public UploadedFile getUploadImage() {
        return uploadImage;
    }

    public void setUploadImage(UploadedFile uploadImage) {
        this.uploadImage = uploadImage;
    }
 
}
