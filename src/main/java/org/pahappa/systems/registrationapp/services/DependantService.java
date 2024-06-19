package org.pahappa.systems.registrationapp.services;

import org.pahappa.systems.registrationapp.dao.DependantDAO;
import org.pahappa.systems.registrationapp.dao.UserDAO;
import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.Dependant.Gender;
import org.pahappa.systems.registrationapp.models.User;

import java.util.List;

public class DependantService {
    private final DependantDAO dependantDAO;
    private final UserDAO userDAO;

    public DependantService(){
        this.dependantDAO = new DependantDAO();
        this.userDAO = new UserDAO();
    }

    public boolean isDatabaseConnected(){
        return dependantDAO.isDatabaseConnected();
    }

    public List<Dependant> getAllDependants(){
        return dependantDAO.getAllDependants();
    }

    public List<Dependant> getUserDependants(User user){
        return dependantDAO.getUserDependants(user);
    }

    public Dependant getDependant(String username){
        return dependantDAO.getDependantByUsername(username);
    }

    public Dependant getDependantByFirstName(String firstName){
        return dependantDAO.getDependantByFirstName(firstName);
    }

    public Dependant getDependantByLastName(String lastName){
        return dependantDAO.getDependantByLastName(lastName);
    }

    public Dependant getDependantByGender(Gender gender){
        return dependantDAO.getDependantByGender(gender);
    }

    public void addDependantToUser(User user, Dependant dependant){
        Dependant existingDependant = dependantDAO.getDependantByUsername(dependant.getUsername());
        User existingUser = userDAO.getUserByUsername(user.getUsername());
        if(existingDependant == null && existingUser != null){
            dependantDAO.addDependantToUser(user, dependant);
        }
    }

    public void deleteDependant(Dependant dependant){
        dependantDAO.delete(dependant);
    }

}
