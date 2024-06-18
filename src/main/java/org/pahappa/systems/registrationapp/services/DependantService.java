package org.pahappa.systems.registrationapp.services;

import org.pahappa.systems.registrationapp.dao.DependantDAO;
import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;

import java.util.List;

public class DependantService {
    private final DependantDAO dependantDAO;

    public DependantService(){
        this.dependantDAO = new DependantDAO();
    }

    public boolean isDatabaseConnected(){
        return dependantDAO.isDatabaseConnected();
    }

}
