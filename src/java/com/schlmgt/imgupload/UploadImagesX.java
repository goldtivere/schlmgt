/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.imgupload;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;
import com.schlmgt.logic.LoadPPTfile;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author abobademichael
 */
public class UploadImagesX {

    private String messangerOfTruth;

    private String pst_url;
    LoadPPTfile loadPPTfile = new LoadPPTfile();  

    public boolean uploadImg(UploadedFile file, String account) throws Exception {

        InputStream finp = null;
        FileOutputStream fos = null;

        try {

            byte fileNameByte[] = file.getContents();
            long fileNameSize = file.getSize();

            //peg this size of uploaded file
            //2097152
            //347825
            if (fileNameSize > 2097152 || fileNameSize < 10) {

                setMessangerOfTruth("File Upload Failed. Invalid file format or image file greater than 335KB");
                return false;

            }

            //
            if (!(loadPPTfile.isLoadPPtFile())) {
                setMessangerOfTruth("Cannot load configuration file...");
                return false;
            }

            Properties ppt = loadPPTfile.getPptFile();

            String pst_location = ppt.getProperty("pst_location");
            String pst_url_location = ppt.getProperty("pst_url_location");

            System.out.println("pst_location:" + pst_location + ",,,  pst_url_location :" + pst_url_location);

            fos = new FileOutputStream(pst_location + account + ".jpg");
            fos.write(fileNameByte);

            fos.close();

            setPst_url(pst_url_location + account + ".jpg");

            System.out.println("Did you get here PIX ?" + getPst_url());

            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            if (fos != null) {
                fos.close();
            }

            if (finp != null) {
                finp.close();
            }

        }

    }

    /**
     * @return the messangerOfTruth
     */
    public String getMessangerOfTruth() {
        return messangerOfTruth;
    }

    /**
     * @param messangerOfTruth the messangerOfTruth to set
     */
    public void setMessangerOfTruth(String messangerOfTruth) {
        this.messangerOfTruth = messangerOfTruth;
    }

    /**
     * @return the pst_url
     */
    public String getPst_url() {
        return pst_url;
    }

    /**
     * @param pst_url the pst_url to set
     */
    public void setPst_url(String pst_url) {
        this.pst_url = pst_url;
    }

}
