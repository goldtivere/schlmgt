/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.filter;

import com.schlmgt.dbconn.DbConnectionX;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Gold
 */
public class ThreadRunnerEmail implements Runnable {

    private SITembeddedImageEmailUtil embeddedImageEmailUtil = new SITembeddedImageEmailUtil();
    private SITembeddedImageEmailUtil embeddedImageEmailUtils = new SITembeddedImageEmailUtil();

    public void run() {

        int i = 0;

        while (true) {

            try {

                //get information the connect to the internet that's all
                String tempData = "";//stores fomatted data, delete if repeatition...

                if (doTransaction() && doUrlSend()) {
                    //good stuff
                } else {
                    //good stuff, but ehnn
                }

                Thread t = new Thread();
                //t.sleep(20000);
                t.sleep(1000);

            } catch (Exception ex) {

                //ex.printStackTrace();
                System.out.println("Exception.... mother of Exceptions....");
                ex.printStackTrace();
                //System.exit(1);

            }

        }//end of while...

    }//end of run method...

    public boolean doTransaction() throws Exception {

        Connection con = null;
        DbConnectionX dbCon = new DbConnectionX();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            con = dbCon.mySqlDBconnection();
            String querySMSDetails = "select * from Staffstatus where status=0";
            pstmt = con.prepareStatement(querySMSDetails);
            rs = pstmt.executeQuery();
            //
            int k = 0;
            while (rs.next()) {

                int unityOrNot = 1;//1 unity 2=others
                String updateData = "";

                String user_id = rs.getString("staffPhone");
                String fullname = rs.getString("fullname");
                String slink = rs.getString("link");
                String id = rs.getString("id");

                String account = rs.getString("staffemail");//email account

                System.out.println("guid:" + fullname + ",request time:" + new java.util.Date().toString());

                String message = "Dear " + fullname + ", \n Please click on the link or copy to your browser to create an account."
                        + "Link expires in 3days.  "
                        + slink
                        + "\n Login ID:" + user_id;

                String subject = "Account Details";
                String title_ = "<title>Account Details</title>";

                //StringBuffer body = new StringBuffer("<html>Dear Customer, Please find the attached with this mail a PDF file containing <br>");
                StringBuffer body = new StringBuffer("<html>");
                body.append("<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
                body.append(title_);
                body.append("<style type=\text/css\">");
                body.append(".t {font-size: 24px;font-family: Verdana, Geneva, sans-serif;}");
                body.append(".b {font-weight: bold;}");
                body.append(".sdads {color: #808080;}");
                body.append(".deepred {	color: #370000;}");
                body.append("</style></head>");

                body.append("<br>");
                body.append("<body>");
                body.append("<div align=\"center\">");
                body.append("<h4 align=\"center\"");
                body.append("class=\"sdads\">");
                body.append("<h3 align=\"center\" class=\"sdads\"><strong>Account Details</strong></h3>");

                body.append("<table width=\"409\" border=\"0\" align=\"center\" cellpadding=\"1\" cellspacing=\"1\">");

                //
                body.append("<tr>");
                body.append("<td align=\"left\" bgcolor=\"#F4FFF8\"><strong class=\"sdads\"></strong></td>");
                body.append("<td bgcolor=\"#F4FFF8\"><strong class=\"deepred\">" + message + "</strong></td>");
                body.append("</tr>");
                //
                body.append("</table>");

                //body.append(message);
                //body.append("<img src=\"cid:image2\" width=\"30%\" height=\"30%\" /><br>");
                body.append("</div>");
                body.append("</body>");
                body.append("</html>");

                try {

                    if (embeddedImageEmailUtil.sendOut(account,
                            subject, body.toString())) {

                        updateData = "update staffstatus set status=1 "
                                + "where staffphone='" + user_id + "' and "
                                + "fullname='" + fullname + "' and id='" + id + "'";
                        pstmt = con.prepareStatement(updateData);
                        pstmt.executeUpdate();
                        System.out.println("Hi");
                    } else {

                        updateData = "update Staffstatus set status=0 "
                                + "where user_id='" + user_id + "' and "
                                + "fullname='" + fullname + "' and id='" + id + "'";

                        pstmt = con.prepareStatement(updateData);
                        pstmt.executeUpdate();
                        System.out.println("Low");
                    }

                } catch (Exception ex) {

                    System.out.println("Could not send email.");
                    ex.printStackTrace();

                }

                //return true;
            }//end while

            return true;

        } catch (Exception e) {

            System.out.println("Exception from doTransaction method.....");
            e.printStackTrace();
            return false;

        } finally {

            //System.out.println("inside finally...");
            if (pstmt != null) {
                pstmt.close();
                //System.out.println("inside finally...close stmtUpdate");
            }

            if (pstmt != null) {
                pstmt.close();
                //System.out.println("inside finally...close stmt1");
            }

            if (rs != null) {
                rs.close();
                //System.out.println("inside finally...close rs1");
            }

        }

    }//end doTransact()

    public boolean doUrlSend() throws Exception {

        Connection con = null;
        DbConnectionX dbCon = new DbConnectionX();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            con = dbCon.mySqlDBconnection();

            String querySMSDetails = "select * from studentstatus where status=0";
            pstmt = con.prepareStatement(querySMSDetails);
            rs = pstmt.executeQuery();

            //
            int k = 0;
            while (rs.next()) {

                int unityOrNot = 1;//1 unity 2=others
                String updateData = "";

                String slink = rs.getString("link");
                String guid = rs.getString("guid");
                String fullname = rs.getString("full_name");
                String stuId = rs.getString("studentid");

                String account = rs.getString("studentemail");//email account

                System.out.println("guid:" + fullname + ",request time:" + new java.util.Date().toString());

                String message = "Dear " + fullname + ", \n Please click on the link or copy to your browser to create an account."
                        + "Link expires in 3days.  "
                        + slink
                        + "\n Student Registration Number:" + stuId;

                String subject = "Account Registration";
                String title_ = "<title>Account Details</title>";

                //StringBuffer body = new StringBuffer("<html>Dear Customer, Please find the attached with this mail a PDF file containing <br>");
                StringBuffer body = new StringBuffer("<html>");
                body.append("<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
                body.append(title_);
                body.append("<style type=\text/css\">");
                body.append(".t {font-size: 24px;font-family: Verdana, Geneva, sans-serif;}");
                body.append(".b {font-weight: bold;}");
                body.append(".sdads {color: #808080;}");
                body.append(".deepred {	color: #370000;}");
                body.append("</style></head>");

                body.append("<br>");
                body.append("<body>");
                body.append("<div align=\"center\">");
                body.append("<h4 align=\"center\"");
                body.append("class=\"sdads\">");
                body.append("<h3 align=\"center\" class=\"sdads\"><strong>Account Opening</strong></h3>");

                body.append("<table width=\"409\" border=\"0\" align=\"center\" cellpadding=\"1\" cellspacing=\"1\">");

                //
                body.append("<tr>");
                body.append("<td align=\"left\" bgcolor=\"#F4FFF8\"><strong class=\"sdads\"></strong></td>");
                body.append("<td bgcolor=\"#F4FFF8\"><strong class=\"deepred\">" + message + "</strong></td>");
                body.append("</tr>");
                //
                body.append("</table>");

                //body.append(message);
                //body.append("<img src=\"cid:image2\" width=\"30%\" height=\"30%\" /><br>");
                body.append("</div>");
                body.append("</body>");
                body.append("</html>");

                try {

                    if (embeddedImageEmailUtils.sendOut(account,
                            subject, body.toString())) {

                        updateData = "update studentstatus set status=1 "
                                + "where guid='" + guid + "' and "
                                + "link='" + slink + "'";
                        pstmt = con.prepareStatement(updateData);
                        pstmt.executeUpdate();

                    } else {

                        updateData = "update studentstatus set status=0 "
                                + "where guid='" + guid + "' and "
                                + "link='" + slink + "'";

                        pstmt = con.prepareStatement(updateData);
                        pstmt.executeUpdate();

                    }

                } catch (Exception ex) {

                    System.out.println("Could not send email.");
                    ex.printStackTrace();

                }

                //return true;
            }//end while

            return true;

        } catch (Exception e) {

            System.out.println("Exception from doTransaction method.....");
            e.printStackTrace();
            return false;

        } finally {

            //System.out.println("inside finally...");
            if (pstmt != null) {
                pstmt.close();
                //System.out.println("inside finally...close stmtUpdate");
            }

            if (pstmt != null) {
                pstmt.close();
                //System.out.println("inside finally...close stmt1");
            }

            if (rs != null) {
                rs.close();
                //System.out.println("inside finally...close rs1");
            }

        }

    }//end doTransact()
}
