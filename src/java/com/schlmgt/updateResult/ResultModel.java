/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.updateResult;

import java.io.Serializable;

/**
 *
 * @author Gold
 */
public class ResultModel implements Serializable {

    private int id;
    private String studentName;
    private String studentId;
    private String subject;
    private double firstTest;
    private double secondTest;
    private double exam;
    private String term;
    private String Studentclass;
    private String year;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public double getFirstTest() {
        return firstTest;
    }

    public void setFirstTest(double firstTest) {
        this.firstTest = firstTest;
    }

    public double getSecondTest() {
        return secondTest;
    }

    public void setSecondTest(double secondTest) {
        this.secondTest = secondTest;
    }

    public double getExam() {
        return exam;
    }

    public void setExam(double exam) {
        this.exam = exam;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getStudentclass() {
        return Studentclass;
    }

    public void setStudentclass(String Studentclass) {
        this.Studentclass = Studentclass;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

}
