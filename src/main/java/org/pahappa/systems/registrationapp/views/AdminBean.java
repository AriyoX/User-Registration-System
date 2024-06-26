package org.pahappa.systems.registrationapp.views;

import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.DependantService;
import org.pahappa.systems.registrationapp.services.UserService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "adminBean")
@ViewScoped
public class AdminBean implements Serializable {
    private static final long serialVersionUID = 2L;
    private long user_id;
    private Dependant dependant = new Dependant();
    private final DependantService dependantService = new DependantService();
    private User selectedUser;
    private Dependant.Gender[] genderValues;
    private List<Dependant> dependants;
    private User currentUser;
    private String searchQuery;
    private final UserService userService = new UserService();
    private String username;


    @PostConstruct
    public void init() {
        currentUser = getCurrentUser();
        try {
            if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
                FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                externalContext.getSessionMap().put("currentUser", null);
                externalContext.redirect(externalContext.getRequestContextPath() + "/pages/login/login.xhtml?faces-redirect=true");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        genderValues = Dependant.Gender.values();
        dependants = dependantService.getAllDependants();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Dependant> getDependants() {
        return dependants;
    }

    public Dependant.Gender[] getGenderValues() {
        return genderValues;
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

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public void selectUser(User user) {
        this.selectedUser = user;
    }

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

    public void registerDependant(){
        User user = userService.getUser(username);
        try {
            if (user != null) {
                dependantService.addDependantToUser(user, dependant);
                dependants.add(dependant);
                dependants = dependantService.getAllDependants();
                dependant = new Dependant(); // Reset the dependant object
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Dependant added successfully!", null));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "User" + username + " does not exist.", null));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error adding dependant: " + e.getMessage(), null));
        }
    }

    public List<Dependant> getSelectedUserDependants() {
        return dependantService.getDependantsByUserId(user_id);
    }

    public void deleteDependant(Dependant dependant) {
        try {
            // dependant = dependantService.getDependant(username);
            dependants.remove(dependant);
            dependantService.deleteDependant(dependant);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Dependant deleted!"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Dependant could not be deleted."));
        }
    }

    public User getCurrentUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        return (User) externalContext.getSessionMap().get("currentUser");
    }

    public void searchDependent() {
        dependants = dependantService.searchDependants(searchQuery);
    }

    public void confirmDeleteAll(){
        try {
            dependantService.deleteAllDependants();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "All users deleted!"));
            dependants = dependantService.getAllDependants(); // Refresh the user list (now empty)
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete all users."));
        }
    }

//    public void filterDependant(){
//        dependants = dependantService.filterDependants();
//    }
}
