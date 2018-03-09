/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.profile;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "views")
@ViewScoped
public class ClassView implements Serializable {

    private EditStudent edits = new EditStudent();

    @PostConstruct
    public void init() {
        testMic();
    }

    public void testMic() {

       
    }

    public EditStudent getEdits() {
        return edits;
    }

    public void setEdits(EditStudent edits) {
        this.edits = edits;
    }

}
