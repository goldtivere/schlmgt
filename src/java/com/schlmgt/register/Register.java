/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.register;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.imgupload.UploadImagesX;
import com.schlmgt.logic.AESencrp;
import com.schlmgt.logic.DateManipulation;
import com.schlmgt.logic.LoadPPTfile;
import com.schlmgt.login.UserDetails;
import com.schlmgt.mailsender.MailSender;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "reg")
@ViewScoped
public class Register implements Serializable {

    private String fname;
    private String lname;
    private String username;
    private String password;
    private String emailadd;
    private static final int BUFFER_SIZE = 6124;
    private UploadedFile passport_file;
    private String messangerOfTruth;
    private String repassword;
    private String passport_url;
    private String ref_number;

    public String getRef_number() {
        return ref_number;
    }

    public void setRef_number(String ref_number) {
        this.ref_number = ref_number;
    }

    public String getPassport_url() {
        return passport_url;
    }

    public void setPassport_url(String passport_url) {
        this.passport_url = passport_url;
    }

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }

    public String getMessangerOfTruth() {
        return messangerOfTruth;
    }

    public void setMessangerOfTruth(String messangerOfTruth) {
        this.messangerOfTruth = messangerOfTruth;
    }

    public UploadedFile getPassport_file() {
        return passport_file;
    }

    public void setPassport_file(UploadedFile passport_file) {
        this.passport_file = passport_file;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailadd() {
        return emailadd;
    }

    public void setEmailadd(String emailadd) {
        this.emailadd = emailadd;
    }

    public Register() {
        ref_number = generateRefNo();
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

    public void refresh() {
        setFname("");
        setLname("");
        setUsername("");
        setPassword("");
        setEmailadd("");

    }

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
            setPassport_file(null);
            passport_file = null;
            setPassport_url("");

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void handleFileUpload(FileUploadEvent event) {

        setPassport_file(event.getFile());
        setPassport_url("");

        //byte fileNameByte[] = getFile().getContents();
        //System.out.println("fileNameByte:" + fileNameByte);
        FacesMessage message;
        UploadImagesX uploadImagesX = new UploadImagesX();

        try {

            if (!(uploadImagesX.uploadImg(getPassport_file(), "pix".concat(String.valueOf(getRef_number()))))) {

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

    public void register() {

        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FacesMessage msg;

        UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
        String on = String.valueOf(userObj);

        if (userObj == null) {
            setMessangerOfTruth("Expired Session, pleasere - login " + on);
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    getMessangerOfTruth(), getMessangerOfTruth()
            );
            context.addMessage(null, msg);
        }

        String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
        String createdId
                = String.valueOf(userObj.getId());
        String roleId
                = String.valueOf(userObj.getRole_id());

        con = dbConnections.mySqlDBconnection();

        try {

            String testflname = "Select * from user_details where first_name=? and last_name=? and is_deleted=?";
            pstmt = con.prepareStatement(testflname);
            pstmt.setString(1, getFname());
            pstmt.setString(2, getLname());
            pstmt.setBoolean(3, false);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                setMessangerOfTruth("First Name and Last Name Aleady exists!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);

            } else {

                String testusername = "Select * from user_details where username=? and is_deleted=?";
                pstmt = con.prepareStatement(testusername);
                pstmt.setString(1, getUsername());
                pstmt.setBoolean(2, false);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    setMessangerOfTruth("Username Aleady exists!!");
                    msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                    context.addMessage(null, msg);

                } else {

                    String testemail = "Select * from user_details where email_address=? and is_deleted=?";
                    pstmt = con.prepareStatement(testemail);
                    pstmt.setString(1, getEmailadd());
                    pstmt.setBoolean(2, false);
                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        setMessangerOfTruth("Email Aleady exists!!");
                        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                        context.addMessage(null, msg);

                    } else {

                        con.setAutoCommit(false);
                        //InputStream fin2 = file.getInputstream();

                        String insert = "insert into user_details (first_name,last_name,username,password,image_name,email_address,role_id,"
                                + "date_created,date_time_created,created_by,is_deleted) "
                                + "values(?,?,?,?,?,?,?,?,?,?,?)";

                        pstmt = con.prepareStatement(insert);

                        pstmt.setString(1, getFname());
                        pstmt.setString(2, getLname());
                        pstmt.setString(3, getUsername());
                        pstmt.setString(4, AESencrp.encrypt(getPassword()));
                        pstmt.setString(5, getPassport_url());
                        pstmt.setString(6, getEmailadd());
                        pstmt.setString(7, "1");
                        pstmt.setString(8, DateManipulation.dateAlone());
                        pstmt.setString(9, DateManipulation.dateAndTime());
                        pstmt.setString(10, "Osawota Gold");
                        pstmt.setBoolean(11, false);

                        pstmt.executeUpdate();

                        String selectactivemail = "Select * from senderdetails where activate=? and isdeleted=?";
                        pstmt = con.prepareStatement(selectactivemail);
                        pstmt.setBoolean(1, true);
                        pstmt.setBoolean(2, false);
                        rs = pstmt.executeQuery();
                        if (rs.next()) {

                            String user = rs.getString("username");
                            String pass = AESencrp.decrypt(rs.getString("password"));
                            String sub = "Account Activation";
                            String content = "Hello " + getFname() + " " + getLname() + "\n Your username is +\n Username: " + getUsername() + "\n Password: " + getPassword();

                            String insertemail = "insert into tbemail (frommail,tomail,submail,content,date_sent,date_time_sent,sentby,senderid)"
                                    + "values(?,?,?,?,?,?,?,?)";

                            pstmt = con.prepareStatement(insertemail);
                            pstmt.setString(1, user);
                            pstmt.setString(2, getEmailadd());
                            pstmt.setString(3, sub);
                            pstmt.setString(4, content);
                            pstmt.setString(5, DateManipulation.dateAlone());
                            pstmt.setString(6, DateManipulation.dateAndTime());
                            pstmt.setString(7, "Osawota Gold");
                            pstmt.setString(8, "1");

                            pstmt.executeUpdate();

                          /**  MailSender send = new MailSender();
                            send.sendMail(user, pass, getEmailadd(), sub, content);**/

                            setMessangerOfTruth("User Created!!");
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);

                            refresh();
                        } else {

                            setMessangerOfTruth("Error!!");
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            refresh();

                        }
                        con.commit();
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
