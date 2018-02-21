/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.updateSubject;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Gold
 */
@ManagedBean
public class SubjectUpdate implements Serializable{
    
    private String studentClass;
    private String studentGrade;

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public String getStudentGrade() {
        return studentGrade;
    }

    public void setStudentGrade(String studentGrade) {
        this.studentGrade = studentGrade;
    }
    
    
    
}
