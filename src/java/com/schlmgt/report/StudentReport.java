/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author Gold
 */
@ManagedBean
@RequestScoped
public class StudentReport {

    private List<Persona> personas = new ArrayList<Persona>();

    public List<Persona> getPersonas() {

        Persona pers = new Persona();
        pers.setFirstTest("Jaime");
        pers.setStudentSubject("MD");

        personas.add(pers);
        
        pers = new Persona();
        pers.setFirstTest("Gold");
        pers.setStudentSubject("Tivere");

        personas.add(pers);
        return personas;
    }

    public void serPersonas(List<Persona> personas) {
        this.personas = personas;

    }

    public void generateReport(ActionEvent actionEvent) {
        try {
            File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/pages/port/studentReport.jasper"));

            byte[] bytes = JasperRunManager.runReportToPdf(jasper.getPath(), null, new JRBeanCollectionDataSource(getPersonas()));
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.setContentType("application/pdf");
            response.setContentLength(bytes.length);
            ServletOutputStream outStream = response.getOutputStream();
            outStream.write(bytes, 0, bytes.length);
            outStream.flush();
            outStream.close();
            FacesContext.getCurrentInstance().responseComplete();

            for (Persona kl : personas) {
                System.out.println(kl.getFirstTest());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void pdf(Map<String,Object> params,String jasperPath,List<?> dataSource, String fileName) throws JRException, IOException
    {
        String relativeWebPath= FacesContext.getCurrentInstance().getExternalContext().getRealPath(jasperPath);
        File file= new File(relativeWebPath);
        JRBeanCollectionDataSource source= new JRBeanCollectionDataSource(dataSource,false);
        JasperPrint print= JasperFillManager.fillReport(file.getPath(), params,source);
        HttpServletResponse response=(HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.addHeader("Content-disposition", "attachment;filename="+fileName);
        ServletOutputStream stream= response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(print,stream);
        
        FacesContext.getCurrentInstance().responseComplete();
        
    }
    public void printPDF() throws JRException, IOException
    {
        List<Persona> datasource= new ArrayList<>();
        datasource.add(new Persona("Gonzalo","Mendoza"));
        datasource.add(new Persona("Lionel","Mesi"));
        String filename="name.pdf";
        String jasperPath="/pages/port/studentReport.jasper";
        
        this.pdf(null, jasperPath, datasource, filename);
    }
        

}
