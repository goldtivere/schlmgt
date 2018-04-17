/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.report;

import java.util.Date;

/**
 *
 * @author Gold
 */
public class Persona {

    private String firstTest;
    private String studentSubject;

    public Persona()
    {
        
    }
    public Persona(String firstTest, String studentSubject) {
        this.firstTest = firstTest;
        this.studentSubject = studentSubject;
    }

    public String getFirstTest() {
        return firstTest;
    }

    public void setFirstTest(String firstTest) {
        this.firstTest = firstTest;
    }

    public String getStudentSubject() {
        return studentSubject;
    }

    public void setStudentSubject(String studentSubject) {
        this.studentSubject = studentSubject;
    }

}
