package org.pahappa.systems.registrationapp.views;

import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;

import javax.annotation.PostConstruct;
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
    private Dependant newDependant;
    private List<Dependant> currentUserDependants;

    @PostConstruct
    public void init() {
        currentUser = getCurrentUser();
        currentUserDependants = currentUser.getDependants();
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


}
