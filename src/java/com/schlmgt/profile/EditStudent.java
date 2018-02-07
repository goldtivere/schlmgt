/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.profile;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.logic.DateManipulation;
import com.schlmgt.login.UserDetails;
import com.schlmgt.register.ClassModel;
import com.schlmgt.register.CountryModel;
import com.schlmgt.register.FreshReg;
import com.schlmgt.register.GradeModel;
import com.schlmgt.register.LgaModel;
import com.schlmgt.register.RelationshipModel;
import com.schlmgt.register.StateModel;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "edit")
@ViewScoped
public class EditStudent implements Serializable {

    private String fullname;
    private String fname;
    private String lname;
    private String dob;
    private String gfname;
    private String gmname;
    private String glname;
    private String sphone;
    private String semail;
    private String sex;
    private String relationship;
    private String relationship_other;
    private String gphone;
    private String gmemail;
    private String gcountry;
    private String state;
    private String lga;
    private String address;
    private String previousSchl;
    private String previousClass;
    private String previousGrade;
    private String currentClass;
    private String currentGrade;
    private String arm;
    private String fullClass;
    private String disability;
    private String otherDis;
    private String bgroup;
    private String mname;
    private String sclass;
    private String url;
    private String messangerOfTruth;
    private Boolean shw;
    private String imagelink;
    private String someParam;
    private String scl;
    private String cras;
    private Date dateOfBirth;
    private String studentid;
    private String cla;
    private Boolean fpanel;
    private Boolean spanel;
    private Boolean relatio;
    private List<GradeModel> grademodels;
    private GradeModel mod = new GradeModel();
    private List<RelationshipModel> relation;
    private List<ClassModel> classmodel;
    private List<StateModel> states;
    private List<CountryModel> coun;
    private List<LgaModel> lgamodel;

    @PostConstruct
    public void init() {
        try {
            setSpanel(false);
            setFpanel(true);
            relation = relationModel();
            StudentNumber();
            studentClass();
            grademodels = gradeDropdowns();
            coun = countryModel();
            classmodel = classDropdown();
            states = cityModel();
            lgamodel = lgaModels();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<GradeModel> gradeDropdowns() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbgrade where class=?";
            pstmt = con.prepareStatement(query);
            System.out.println(getPreviousClass() + "a");
            System.out.println(getFullClass());
            pstmt.setString(1, getFullClass());
            rs = pstmt.executeQuery();
            //
            List<GradeModel> lst = new ArrayList<>();
            while (rs.next()) {

                GradeModel couns = new GradeModel();
                couns.setId(rs.getInt("id"));
                couns.setGrade(rs.getString("grade"));
                couns.setSclass(rs.getString("class"));

                //
                lst.add(couns);
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

                ClassModel couns = new ClassModel();
                couns.setId(rs.getInt("id"));
                couns.setTbclass(rs.getString("class"));

                //
                lst.add(couns);
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

    public void onClassChange() throws Exception {

        grademodels = gradeDropdowns();

    }

    public void onStateChange() {
        if (getState() != null && !getState().equalsIgnoreCase("")) {
            try {
                lgamodel = lgaModels();
            } catch (Exception ex) {
                Logger.getLogger(FreshReg.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public List<LgaModel> lgaModels() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbstatelga where state=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, getState());
            rs = pstmt.executeQuery();
            //
            List<LgaModel> lst = new ArrayList<>();
            while (rs.next()) {

                LgaModel coun = new LgaModel();
                coun.setId(rs.getInt("Id"));
                coun.setState(rs.getString("state"));
                coun.setLga(rs.getString("lga"));
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

    public List<CountryModel> countryModel() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbcountry";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            //
            List<CountryModel> lst = new ArrayList<>();
            while (rs.next()) {

                CountryModel coun = new CountryModel();
                coun.setId(rs.getInt("Id"));
                coun.setCountry(rs.getString("country"));
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

    public void onCountryChange() throws Exception {

        if (getGcountry() == null || getGcountry().trim().equalsIgnoreCase("")) {
            states = nullmodel();
            FacesMessage msg;

            msg = new FacesMessage(getGcountry() + "d");

            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg;

            msg = new FacesMessage(getGcountry() + "s");

            FacesContext.getCurrentInstance().addMessage(null, msg);
            states = cityModel();

        }
    }

    public List<StateModel> cityModel() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbstates";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            //
            List<StateModel> lst = new ArrayList<>();
            while (rs.next()) {

                StateModel coun = new StateModel();
                coun.setId(rs.getInt("Id"));
                coun.setState(rs.getString("states"));
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

    public List<StateModel> nullmodel() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        return null;
    }

    public void onRelationshipChange() {
        if (getRelationship().equalsIgnoreCase("other")) {
            setRelatio(true);
        } else {
            setRelatio(false);
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

    private String StudentId() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        FacesMessage msg;

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Map<String, String> params = externalContext.getRequestParameterMap();
        scl = params.get("studentid");
        return scl;
    }

    private void studentClass() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        FacesMessage msg;

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Map<String, String> params = externalContext.getRequestParameterMap();
        String c = params.get("class");
        setCras(c);

        String words[] = c.split(" ");
        setFullClass(words[0]);
    }

    public void studDetails() {
        try {
            DbConnectionX dbConnections = new DbConnectionX();
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            con = dbConnections.mySqlDBconnection();
            String testguid = "Select * from student_details where studentid=?";
            pstmt = con.prepareStatement(testguid);
            pstmt.setString(1, StudentId());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                setFname(rs.getString("first_name"));
                setMname(rs.getString("middle_name"));
                setLname(rs.getString("last_name"));
                setFullname(rs.getString("fullname"));
                setDob(rs.getString("DOB"));
                setDateOfBirth(rs.getDate("DOB"));
                setStudentid(rs.getString("studentid"));
                setSphone(rs.getString("student_phone"));
                setSemail(rs.getString("student_email"));
                setSex(rs.getString("sex"));
                setGfname(rs.getString("guardian_firstname"));
                setGlname(rs.getString("guardian_lastname"));
                setGmname(rs.getString("guardian_middlename"));
                setRelationship(rs.getString("relationship"));
                setRelationship_other(rs.getString("relationship_other"));
                setGphone(rs.getString("guardian_phone"));
                setGmemail(rs.getString("guardian_email"));
                setGcountry(rs.getString("guardian_country"));
                setState(rs.getString("guardian_state"));
                setLga(rs.getString("guardian_lga"));
                setAddress(rs.getString("guardian_address"));
                setPreviousSchl(rs.getString("previous_school"));
                setPreviousClass(rs.getString("previous_grade"));
                setArm(rs.getString("arm"));
                setDisability(rs.getString("disability"));
                setOtherDis(rs.getString("other_disability"));
                setBgroup(rs.getString("bgroup"));
                setImagelink(rs.getString("image"));

            }
            System.out.println(getImagelink());
        } catch (NullPointerException e) {
            e.printStackTrace();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void StudentNumber() throws SQLException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        FacesMessage msg;
        try {
            DbConnectionX dbConnections = new DbConnectionX();
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            studDetails();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void updateClass() {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FacesMessage msg;
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext cont = RequestContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        String fullname = getLname() + " " + getMname() + " " + getFname();
        try {
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            String dobs = format.format(getDateOfBirth());
            con = dbConnections.mySqlDBconnection();
            if (getFullClass().equalsIgnoreCase("nursery")) {
                String personalDetails = "update tbnursery set first_name=? ,middle_name=?, last_name=?, full_name=?,"
                        + "updatedby=?,updaterid=?,dateupdated=? where studentid=? and class=?";

                pstmt = con.prepareStatement(personalDetails);

                pstmt.setString(1, getFname());
                pstmt.setString(2, getMname());
                pstmt.setString(3, getLname());
                pstmt.setString(4, fullname);
                pstmt.setString(5, createdby);
                pstmt.setInt(6, createdId);
                pstmt.setString(7, DateManipulation.dateAndTime());
                pstmt.setString(8, studentid);
                pstmt.setString(9, getCras());

                pstmt.executeUpdate();
            } else if (getFullClass().equalsIgnoreCase("primary")) {
                String personalDetails = "update tbprimary set first_name=? ,middle_name=?, last_name=?, full_name=?,"
                        + "updatedby=?,updaterid=?,dateupdated=? where studentid=? and class=?";

                pstmt = con.prepareStatement(personalDetails);

                pstmt.setString(1, getFname());
                pstmt.setString(2, getMname());
                pstmt.setString(3, getLname());
                pstmt.setString(4, fullname);
                pstmt.setString(5, createdby);
                pstmt.setInt(6, createdId);
                pstmt.setString(7, DateManipulation.dateAndTime());
                pstmt.setString(8, studentid);
                pstmt.setString(9, getCras());

                pstmt.executeUpdate();
                System.out.println("o" + getCras());
            } else if (getFullClass().equalsIgnoreCase("secondary")) {
                String personalDetails = "update tbsecondary set first_name=? ,middle_name=?, last_name=?, full_name=?,"
                        + "updatedby=?,updaterid=?,dateupdated=? where studentid=? and class=?";

                pstmt = con.prepareStatement(personalDetails);

                pstmt.setString(1, getFname());
                pstmt.setString(2, getMname());
                pstmt.setString(3, getLname());
                pstmt.setString(4, fullname);
                pstmt.setString(5, createdby);
                pstmt.setInt(6, createdId);
                pstmt.setString(7, DateManipulation.dateAndTime());
                pstmt.setString(8, studentid);
                pstmt.setString(9, getCras());

                pstmt.executeUpdate();
                System.out.println("o" + getCras());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateStudentDetails(ActionEvent event) {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FacesMessage msg;
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext cont = RequestContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        String fullname = getLname() + " " + getMname() + " " + getFname();
        boolean loggedIn = true;

        try {
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            String dobs = format.format(getDateOfBirth());
            con = dbConnections.mySqlDBconnection();

            String personalDetails = "update student_details set first_name=? ,middle_name=?, last_name=?, fullname=?, dob=?,"
                    + "student_phone=? , student_email=?, sex=? ,updated_by=?,updated_id=?,date_updated=? where studentid=?";

            pstmt = con.prepareStatement(personalDetails);

            pstmt.setString(1, getFname());
            pstmt.setString(2, getMname());
            pstmt.setString(3, getLname());
            pstmt.setString(4, fullname);
            pstmt.setString(5, dobs);
            pstmt.setString(6, getSphone());
            pstmt.setString(7, getSemail());
            pstmt.setString(8, getSex());
            pstmt.setString(9, createdby);
            pstmt.setInt(10, createdId);
            pstmt.setString(11, DateManipulation.dateAndTime());
            pstmt.setString(12, studentid);
            System.out.println(studentid);
            pstmt.executeUpdate();
            updateClass();
            setMessangerOfTruth("Personal Details Updated!!");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, msg);
            StudentNumber();
            cont.addCallbackParam("loggedIn", loggedIn);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateParent() {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FacesMessage msg;
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext cont = RequestContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        String fullname = getLname() + " " + getMname() + " " + getFname();
        boolean loggedIn = true;

        try {
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();
            con = dbConnections.mySqlDBconnection();

            String personalDetails = "update student_details set guardian_firstname=? ,guardian_middlename=?, guardian_lastname=?, relationship=?,"
                    + "relationship_other=?,"
                    + "guardian_phone=? , guardian_email=?, guardian_country=? ,guardian_state=?,guardian_lga=?,guardian_address=?,updated_by=?,updated_id=?,date_updated=? where studentid=?";

            pstmt = con.prepareStatement(personalDetails);

            pstmt.setString(1, getGfname());
            pstmt.setString(2, getGmname());
            pstmt.setString(3, getGlname());
            pstmt.setString(4, getRelationship());
            pstmt.setString(5, getRelationship_other());
            pstmt.setString(6, getGphone());
            pstmt.setString(7, getGmemail());
            pstmt.setString(8, getGcountry());
            pstmt.setString(9, getState());
            pstmt.setString(10, getLga());
            pstmt.setString(11, getAddress());
            pstmt.setString(12, createdby);
            pstmt.setInt(13, createdId);
            pstmt.setString(14, DateManipulation.dateAndTime());
            pstmt.setString(15, studentid);
            System.out.println(studentid);
            pstmt.executeUpdate();
            updateClass();
            setMessangerOfTruth("Parent/Guardian Details Updated!!");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, msg);
            StudentNumber();
            cont.addCallbackParam("loggedIn", loggedIn);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public GradeModel getMod() {
        return mod;
    }

    public void setMod(GradeModel mod) {
        this.mod = mod;
    }

    public List<GradeModel> getGrademodels() {
        return grademodels;
    }

    public void setGrademodels(List<GradeModel> grademodels) {
        this.grademodels = grademodels;
    }

    public List<ClassModel> getClassmodel() {
        return classmodel;
    }

    public void setClassmodel(List<ClassModel> classmodel) {
        this.classmodel = classmodel;
    }

    public List<LgaModel> getLgamodel() {
        return lgamodel;
    }

    public void setLgamodel(List<LgaModel> lgamodel) {
        this.lgamodel = lgamodel;
    }

    public List<StateModel> getStates() {
        return states;
    }

    public void setStates(List<StateModel> states) {
        this.states = states;
    }

    public List<CountryModel> getCoun() {
        return coun;
    }

    public void setCoun(List<CountryModel> coun) {
        this.coun = coun;
    }

    public List<RelationshipModel> getRelation() {
        return relation;
    }

    public void setRelation(List<RelationshipModel> relation) {
        this.relation = relation;
    }

    public Boolean getRelatio() {
        return relatio;
    }

    public void setRelatio(Boolean relatio) {
        this.relatio = relatio;
    }

    public String getFullClass() {
        return fullClass;
    }

    public void setFullClass(String fullClass) {
        this.fullClass = fullClass;
    }

    public String getCras() {
        return cras;
    }

    public void setCras(String cras) {
        this.cras = cras;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Boolean getFpanel() {
        return fpanel;
    }

    public void setFpanel(Boolean fpanel) {
        this.fpanel = fpanel;
    }

    public Boolean getSpanel() {
        return spanel;
    }

    public void setSpanel(Boolean spanel) {
        this.spanel = spanel;
    }

    public String getArm() {
        return arm;
    }

    public void setArm(String arm) {
        this.arm = arm;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
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

    public String getSphone() {
        return sphone;
    }

    public void setSphone(String sphone) {
        this.sphone = sphone;
    }

    public String getSemail() {
        return semail;
    }

    public void setSemail(String semail) {
        this.semail = semail;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getRelationship_other() {
        return relationship_other;
    }

    public void setRelationship_other(String relationship_other) {
        this.relationship_other = relationship_other;
    }

    public String getGphone() {
        return gphone;
    }

    public void setGphone(String gphone) {
        this.gphone = gphone;
    }

    public String getGmemail() {
        return gmemail;
    }

    public void setGmemail(String gmemail) {
        this.gmemail = gmemail;
    }

    public String getGcountry() {
        return gcountry;
    }

    public void setGcountry(String gcountry) {
        this.gcountry = gcountry;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLga() {
        return lga;
    }

    public void setLga(String lga) {
        this.lga = lga;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPreviousSchl() {
        return previousSchl;
    }

    public void setPreviousSchl(String previousSchl) {
        this.previousSchl = previousSchl;
    }

    public String getPreviousClass() {
        return previousClass;
    }

    public void setPreviousClass(String previousClass) {
        this.previousClass = previousClass;
    }

    public String getPreviousGrade() {
        return previousGrade;
    }

    public void setPreviousGrade(String previousGrade) {
        this.previousGrade = previousGrade;
    }

    public String getCurrentClass() {
        return currentClass;
    }

    public void setCurrentClass(String currentClass) {
        this.currentClass = currentClass;
    }

    public String getCurrentGrade() {
        return currentGrade;
    }

    public void setCurrentGrade(String currentGrade) {
        this.currentGrade = currentGrade;
    }

    public String getDisability() {
        return disability;
    }

    public void setDisability(String disability) {
        this.disability = disability;
    }

    public String getOtherDis() {
        return otherDis;
    }

    public void setOtherDis(String otherDis) {
        this.otherDis = otherDis;
    }

    public String getBgroup() {
        return bgroup;
    }

    public void setBgroup(String bgroup) {
        this.bgroup = bgroup;
    }

    public Boolean getShw() {
        return shw;
    }

    public void setShw(Boolean shw) {
        this.shw = shw;
    }

    public String getScl() {
        return scl;
    }

    public void setScl(String scl) {
        this.scl = scl;
    }

    public String getMessangerOfTruth() {
        return messangerOfTruth;
    }

    public void setMessangerOfTruth(String messangerOfTruth) {
        this.messangerOfTruth = messangerOfTruth;
    }

    public String getSomeParam() {
        return someParam;
    }

    public void setSomeParam(String someParam) {
        this.someParam = someParam;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getCla() {
        return cla;
    }

    public void setCla(String cla) {
        this.cla = cla;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getSclass() {
        return sclass;
    }

    public void setSclass(String sclass) {
        this.sclass = sclass;
    }

    public String getImagelink() {
        return imagelink;
    }

    public void setImagelink(String imagelink) {
        this.imagelink = imagelink;
    }

}
