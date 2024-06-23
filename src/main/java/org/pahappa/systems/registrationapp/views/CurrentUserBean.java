package org.pahappa.systems.registrationapp.views;

import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.DependantService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped
public class CurrentUserBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private User currentUser;
    private Dependant newDependant = new Dependant();
    private List<Dependant> currentUserDependants;
    private final DependantService dependantService = new DependantService();
    private Dependant.Gender[] genderValues;

    @PostConstruct
    public void init() {
        currentUser = getCurrentUser();
        currentUserDependants = currentUser.getDependants();
        genderValues = Dependant.Gender.values();
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
        return currentUserDependants;
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

    public void addDependant() {
        try {
            if (currentUser != null) {
                dependantService.addDependantToUser(currentUser, newDependant);
                newDependant = new Dependant(); // Reset the dependant object
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
}
