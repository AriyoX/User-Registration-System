package org.pahappa.systems.registrationapp.views;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.Date;

@ManagedBean(name = "calendarBean")
@RequestScoped
public class CalenderBean {

    public Date getCurrentDate(){
        return new Date();
    }
}
