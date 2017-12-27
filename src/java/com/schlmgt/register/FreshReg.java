/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.register;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.imgupload.UploadImagesX;
import com.schlmgt.logic.DateManipulation;
import com.schlmgt.logic.LoadPPTfile;
import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private String disability;
    private String bgroup;
    private String disab;
    private UploadedFile uploadImage;
    private List<RelationshipModel> relation;
    private RelationshipModel modes = new RelationshipModel();
    private List<CountryModel> country;
    private List<StateModel> state;
    private CountryModel counts = new CountryModel();
    private StateModel citys = new StateModel();
    private Boolean relatio;
    private Boolean dStatus;
    private Boolean confirmPanel;
    private Boolean studentPanel;
    private LgaModel lgas = new LgaModel();
    private List<LgaModel> lgamodel;
    private List<ClassModel> classmodel;
    private ClassModel modelclass = new ClassModel();
    private ClassModel classmode = new ClassModel();
    private List<GradeModel> grademodel;
    private List<GradeModel> grademodels;
    private GradeModel modelgrade = new GradeModel();
    private GradeModel gradeMode = new GradeModel();
    private BloodGroupModel grpmodel = new BloodGroupModel();
    private List<BloodGroupModel> modelgroup;
    private DisabilityModel modeldis = new DisabilityModel();
    private List<DisabilityModel> dismodel;
    private UploadedFile passport;
    private String messangerOfTruth;
    private String passport_url;
    private String ref_number;
    private String disoption;
    private String relationOption;
    
    @PostConstruct
    public void init() {
        try {
            confirmPanel = false;
            studentPanel = true;
            dismodel = disabilityDropdown();
            modelgroup = bloodgroupDropdown();
            classmodel = classDropdown();
            relation = relationModel();
            country = countryModel();
            relatio = false;
            dStatus = false;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void onRelationshipChange() {
        if (modes.getRelation().equalsIgnoreCase("other")) {
            setRelatio(true);
        } else {
            setRelatio(false);
        }
    }
    
    public void onDisabilityChange() {
        if (modeldis.getDisability().equalsIgnoreCase("YES")) {
            setdStatus(true);
        } else {
            setdStatus(false);
        }
    }
    
    public void onStateChange() {
        if (citys.getState() != null && !citys.getState().equalsIgnoreCase("")) {
            try {
                lgamodel = lgaModels();
            } catch (Exception ex) {
                Logger.getLogger(FreshReg.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void onClassChange() throws Exception {
        
        grademodel = gradeDropdown();
        
    }
    
    public void onClassChanges() throws Exception {
        
        grademodels = gradeDropdowns();
        
    }
    
    public void onCountryChange() throws Exception {
        
        if (counts.getCountry() == null || counts.getCountry().trim().equalsIgnoreCase("")) {
            state = nullmodel();
            FacesMessage msg;
            
            msg = new FacesMessage(counts.getCountry() + "d");
            
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg;
            
            msg = new FacesMessage(counts.getCountry() + "s");
            
            FacesContext.getCurrentInstance().addMessage(null, msg);
            state = cityModel();
            
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
    
    public List<BloodGroupModel> bloodgroupDropdown() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        
        try {
            
            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbbloodgroup";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            //
            List<BloodGroupModel> lst = new ArrayList<>();
            while (rs.next()) {
                
                BloodGroupModel coun = new BloodGroupModel();
                coun.setId(rs.getInt("id"));
                coun.setBloodgroup(rs.getString("bloodgroup"));

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
    
    public List<DisabilityModel> disabilityDropdown() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        
        try {
            
            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbdisability";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            //
            List<DisabilityModel> lst = new ArrayList<>();
            while (rs.next()) {
                
                DisabilityModel coun = new DisabilityModel();
                coun.setId(rs.getInt("id"));
                coun.setDisability(rs.getString("disability"));

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
    
    public List<GradeModel> gradeDropdown() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        
        try {
            
            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbgrade where class=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, modelclass.getTbclass());
            rs = pstmt.executeQuery();
            //
            List<GradeModel> lst = new ArrayList<>();
            while (rs.next()) {
                
                GradeModel coun = new GradeModel();
                coun.setId(rs.getInt("id"));
                coun.setGrade(rs.getString("grade"));
                coun.setSclass(rs.getString("class"));

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
            pstmt.setString(1, classmode.getTbclass());
            rs = pstmt.executeQuery();
            //
            List<GradeModel> lst = new ArrayList<>();
            while (rs.next()) {
                
                GradeModel coun = new GradeModel();
                coun.setId(rs.getInt("id"));
                coun.setGrade(rs.getString("grade"));
                coun.setSclass(rs.getString("class"));

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
            pstmt.setString(1, citys.getState());
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
    
    public void handleFileUpload(FileUploadEvent event) {
        
        setPassport(event.getFile());
        setPassport_url("");

        //byte fileNameByte[] = getFile().getContents();
        //System.out.println("fileNameByte:" + fileNameByte);
        FacesMessage message;
        UploadImagesX uploadImagesX = new UploadImagesX();
        
        try {
            
            if (!(uploadImagesX.uploadImg(getPassport(), "pix".concat(String.valueOf(getRef_number()))))) {
                
                message = new FacesMessage(FacesMessage.SEVERITY_FATAL, uploadImagesX.getMessangerOfTruth(), uploadImagesX.getMessangerOfTruth());
                FacesContext.getCurrentInstance().addMessage(null, message);

                //value.setPst_url(null);
                return;
                
            }
            
            message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            setPassport_url(uploadImagesX.getPst_url());
            FacesContext.getCurrentInstance().addMessage(null, message);
            
        } catch (Exception ex) {
            
            ex.printStackTrace();
            message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            
        }
        
    }
    
    public String generateRefNo() {
        
        try {
            
            String timeStamp = new SimpleDateFormat("yyMMddHHmmss").format(Calendar.getInstance().getTime());
            
            int rnd = new Random().nextInt(99999753);
            String temp_val = String.valueOf(rnd).concat(timeStamp);
            return temp_val;
            
        } catch (Exception ex) {
            
            ex.printStackTrace();
            return null;
            
        }
        
    }//end generateRefNo(...)

    public void clearPix() {
        
        LoadPPTfile loadPPTfile = new LoadPPTfile();
        
        try {
            
            String file_ = "pix".concat(String.valueOf(getRef_number())).concat(".jpg");
            
            if (!(loadPPTfile.isLoadPPtFile())) {
                setMessangerOfTruth("Cannot load configuration file...");
                setMessangerOfTruth("Operation failed");
                return;
            }
            //
            Properties ppt = loadPPTfile.getPptFile();
            String url = ppt.getProperty("pst_location");
            
            File file = new File(url + "".concat(file_));
            file.delete();
            //
            setPassport(null);
            passport = null;
            setPassport_url("");
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    public void confirmDetails() {
        setConfirmPanel(true);
        setStudentPanel(false);
        
        setFname(getFname());
        setMname(getMname());
        setLname(getLname());
        setDob(DateManipulation.defineDate(getDob().toString()));
        setPnum(getPnum());
        setEmail(getEmail());
        
        setGfname(getGfname());
        setGmname(getGmname());
        setGlname(getGlname());
        setGrelation(getGrelation());
        setRelationOption(getRelationOption());
        setGpnum(getGpnum());
        setGemail(getGemail());
        setGcount(counts.getCountry());
        setGstate(citys.getState());
        setGlga(lgas.getLga());
        setGaddress(getGaddress());
        
        setPschl(getPschl());
        modelclass.setTbclass(modelclass.getTbclass());
        modelgrade.setGrade(modelgrade.getGrade());
        
        classmode.setTbclass(classmode.getTbclass());
        gradeMode.setGrade(gradeMode.getGrade());
        
        modeldis.setDisability(modeldis.getDisability());
        setDisoption(getDisoption());
        grpmodel.setBloodgroup(grpmodel.getBloodgroup());
        
        setPassport_url(getPassport_url());
        
    }
    
    public void back()
    {
        setConfirmPanel(false);
        setStudentPanel(true);
    }
    
    public String getRelationOption() {
        return relationOption;
    }
    
    public void setRelationOption(String relationOption) {
        this.relationOption = relationOption;
    }
    
    public String getDisoption() {
        return disoption;
    }
    
    public void setDisoption(String disoption) {
        this.disoption = disoption;
    }
    
    public UploadedFile getPassport() {
        return passport;
    }
    
    public List<GradeModel> getGrademodels() {
        return grademodels;
    }
    
    public void setGrademodels(List<GradeModel> grademodels) {
        this.grademodels = grademodels;
    }
    
    public Boolean getConfirmPanel() {
        return confirmPanel;
    }
    
    public void setConfirmPanel(Boolean confirmPanel) {
        this.confirmPanel = confirmPanel;
    }
    
    public Boolean getStudentPanel() {
        return studentPanel;
    }
    
    public void setStudentPanel(Boolean studentPanel) {
        this.studentPanel = studentPanel;
    }
    
    public Boolean getdStatus() {
        return dStatus;
    }
    
    public void setdStatus(Boolean dStatus) {
        this.dStatus = dStatus;
    }
    
    public void setPassport(UploadedFile passport) {
        this.passport = passport;
    }
    
    public String getMessangerOfTruth() {
        return messangerOfTruth;
    }
    
    public void setMessangerOfTruth(String messangerOfTruth) {
        this.messangerOfTruth = messangerOfTruth;
    }
    
    public String getPassport_url() {
        return passport_url;
    }
    
    public void setPassport_url(String passport_url) {
        this.passport_url = passport_url;
    }
    
    public String getRef_number() {
        return ref_number;
    }
    
    public void setRef_number(String ref_number) {
        this.ref_number = ref_number;
    }
    
    public DisabilityModel getModeldis() {
        return modeldis;
    }
    
    public void setModeldis(DisabilityModel modeldis) {
        this.modeldis = modeldis;
    }
    
    public List<DisabilityModel> getDismodel() {
        return dismodel;
    }
    
    public void setDismodel(List<DisabilityModel> dismodel) {
        this.dismodel = dismodel;
    }
    
    public BloodGroupModel getGrpmodel() {
        return grpmodel;
    }
    
    public void setGrpmodel(BloodGroupModel grpmodel) {
        this.grpmodel = grpmodel;
    }
    
    public List<BloodGroupModel> getModelgroup() {
        return modelgroup;
    }
    
    public void setModelgroup(List<BloodGroupModel> modelgroup) {
        this.modelgroup = modelgroup;
    }
    
    public GradeModel getGradeMode() {
        return gradeMode;
    }
    
    public void setGradeMode(GradeModel gradeMode) {
        this.gradeMode = gradeMode;
    }
    
    public ClassModel getClassmode() {
        return classmode;
    }
    
    public void setClassmode(ClassModel classmode) {
        this.classmode = classmode;
    }
    
    public List<ClassModel> getClassmodel() {
        return classmodel;
    }
    
    public void setClassmodel(List<ClassModel> classmodel) {
        this.classmodel = classmodel;
    }
    
    public ClassModel getModelclass() {
        return modelclass;
    }
    
    public void setModelclass(ClassModel modelclass) {
        this.modelclass = modelclass;
    }
    
    public List<GradeModel> getGrademodel() {
        return grademodel;
    }
    
    public void setGrademodel(List<GradeModel> grademodel) {
        this.grademodel = grademodel;
    }
    
    public GradeModel getModelgrade() {
        return modelgrade;
    }
    
    public void setModelgrade(GradeModel modelgrade) {
        this.modelgrade = modelgrade;
    }
    
    public LgaModel getLgas() {
        return lgas;
    }
    
    public void setLgas(LgaModel lgas) {
        this.lgas = lgas;
    }
    
    public List<LgaModel> getLgamodel() {
        return lgamodel;
    }
    
    public void setLgamodel(List<LgaModel> lgamodel) {
        this.lgamodel = lgamodel;
    }
    
    public List<StateModel> getState() {
        return state;
    }
    
    public void setState(List<StateModel> state) {
        this.state = state;
    }
    
    public StateModel getCitys() {
        return citys;
    }
    
    public void setCitys(StateModel citys) {
        this.citys = citys;
    }
    
    public Boolean getRelatio() {
        return relatio;
    }
    
    public void setRelatio(Boolean relatio) {
        this.relatio = relatio;
    }
    
    public List<CountryModel> getCountry() {
        return country;
    }
    
    public void setCountry(List<CountryModel> country) {
        this.country = country;
    }
    
    public CountryModel getCounts() {
        return counts;
    }
    
    public void setCounts(CountryModel counts) {
        this.counts = counts;
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
    
    public String getDisability() {
        return disability;
    }
    
    public void setDisability(String disability) {
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
