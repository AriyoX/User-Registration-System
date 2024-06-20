package org.pahappa.systems.registrationapp.views;

import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.DependantService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean(name = "dependantBean")
@ViewScoped
public class DependantBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private Dependant dependant = new Dependant();
    private final DependantService dependantService = new DependantService();
    private User selectedUser;
    private boolean showAddDependantDialog = false;
    private Dependant.Gender[] genderValues;

    @PostConstruct
    public void init() {
        genderValues = Dependant.Gender.values();
    }

    public void setDependant(Dependant dependant) {
        this.dependant = dependant;
    }

    public Dependant getDependant() {
        return dependant;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public void selectUser(User user) {
        this.selectedUser = user;
        this.showAddDependantDialog = true;
    }

    // Method to add a dependant to a user
    public void addDependant() {
        try {
            if (selectedUser != null) {
                dependantService.addDependantToUser(selectedUser, dependant);
                dependant = new Dependant(); // Reset the dependant object
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Dependant added successfully!", null));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "User not selected!", null));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error adding dependant: " + e.getMessage(), null));
        }
    }

    public Boolean getShowAddDependantDialog() {
        return showAddDependantDialog;
    }

    public Dependant.Gender[] getGenderValues() {
        return genderValues;
    }
}