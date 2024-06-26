package org.pahappa.systems.registrationapp.views;

import org.pahappa.systems.registrationapp.models.User;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class UserMenu {

    @PostConstruct
    public void init() {
        User currentUser = getCurrentUser();
        try {
            if (currentUser == null ) {
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

    public List<MenuItem> getMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Dependants", "/pages/user/dependants.xhtml"));
        menuItems.add(new MenuItem("Settings", "/pages/user/update.xhtml"));
        return menuItems;
    }

    public static class MenuItem {
        private final String label;
        private final String outcome;

        public MenuItem(String label, String outcome) {
            this.label = label;
            this.outcome = outcome;
        }

        public String getOutcome() {
            return outcome;
        }

        public String getLabel() {
            return label;
        }
    }
}