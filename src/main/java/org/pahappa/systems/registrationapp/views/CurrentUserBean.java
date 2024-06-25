package org.pahappa.systems.registrationapp.views;

import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.DependantService;
import org.pahappa.systems.registrationapp.services.UserService;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ManagedBean
@ViewScoped
public class CurrentUserBean implements Serializable {
    private static final long serialVersionUID = 3L;
    private User currentUser;
    private Dependant newDependant = new Dependant();
    private List<Dependant> currentUserDependants;
    private final UserService userService = new UserService();
    private final DependantService dependantService = new DependantService();
    private Dependant.Gender[] genderValues;
    private Dependant selectedDependant;

    @PostConstruct
    public void init() {
        currentUser = getCurrentUser();
        currentUserDependants = dependantService.getUserDependants(currentUser);
        genderValues = Dependant.Gender.values();
        selectedDependant = new Dependant();
        try {
            if (currentUser == null) {
                FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                externalContext.getSessionMap().put("currentUser", null);
                externalContext.redirect(externalContext.getRequestContextPath() + "/pages/login/login.xhtml?faces-redirect=true");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Dependant.Gender[] getGenderValues() {
        return genderValues;
    }

    public User getCurrentUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        return (User) externalContext.getSessionMap().get("currentUser");
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public List<Dependant> getCurrentUserDependants() {
        return dependantService.getUserDependants(currentUser);
    }

    public void setCurrentUserDependants(List<Dependant> currentUserDependants) {
        this.currentUserDependants = currentUserDependants;
    }

    public Dependant getNewDependant() {
        return newDependant;
    }

    public void setNewDependant(Dependant newDependant) {
        this.newDependant = newDependant;
    }

    public Dependant getSelectedDependant() {
        return selectedDependant;
    }

    public void setSelectedDependant(Dependant selectedDependant) {
        this.selectedDependant = selectedDependant;
    }

    public void selectDependant(Dependant dependant) {
        this.selectedDependant = dependant;
    }


    public void addDependant() {
        try {
            if (currentUser != null) {
                dependantService.addDependantToUser(currentUser, newDependant);
                currentUserDependants.add(newDependant);
                newDependant = new Dependant(); // Reset the dependant object
                PrimeFaces.current().ajax().update("form:dataTable");
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

    public void deleteDependant(Dependant dependant) {
        try {
            // newDependant = dependantService.getDependant(username);
            currentUserDependants.remove(dependant);
            dependantService.deleteDependant(dependant);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Dependant deleted!"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Dependant could not be deleted."));
        }
    }

    public void updateUser(){
        try {
            userService.updateUser(currentUser);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "User updated!"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public void updateDependant() {
        try {
            dependantService.updateDependant(selectedDependant);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User updated!"));
            currentUserDependants =  dependantService.getUserDependants(currentUser);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }
}
