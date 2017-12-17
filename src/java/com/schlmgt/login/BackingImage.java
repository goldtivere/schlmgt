/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.login;

import com.schlmgt.dbconn.DbConnectionX;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Gold
 */
public class BackingImage {

    private StreamedContent dbImage;

    public StreamedContent getDbImage() {
        return dbImage;
    }

    public void setDbImage(StreamedContent dbImage) {
        this.dbImage = dbImage;
    }

    public BackingImage() throws SQLException {
        
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

       
            con = dbConnections.mySqlDBconnection();
            InputStream dbStream = null;
            String id = "qwe";
            System.out.println("inside servlet–>" + id);

            con = dbConnections.mySqlDBconnection();

            String queryProfile = "select image from user_details "
                    + "where image_name=?";
            pstmt = con.prepareStatement(queryProfile);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                byte[] bytearray = new byte[1048576];
                int size = 0;
                dbStream = rs.getBinaryStream(1);

            }
            dbImage = new DefaultStreamedContent(dbStream, "image/jpeg");

       

    }

//getters and setters
}
