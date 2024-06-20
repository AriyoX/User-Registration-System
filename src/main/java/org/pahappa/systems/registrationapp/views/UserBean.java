package org.pahappa.systems.registrationapp.views;

import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.UserService;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "userBean")
@ViewScoped
public class UserBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private User user = new User();
    private final UserService userService = new UserService();
    private List<User> users;
    private String searchQuery;
    private User selectedUser;

    @PostConstruct
    public void init() {
        users = userService.getAllUsers();
    }

    // search query
    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    // user
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUsers() {
        return users;
    }

    // selected user
    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    // methods
    // register method
    public String registerUser() {
        try {
            userService.registerUser(user);
            user = new User();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Registration Successful!", null));
            return "register";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
        return null;
    }

    // search method
    public void searchUser() {
        users = userService.searchUsers(searchQuery);
    }

    // update method
    public void updateUser() {
        try {
            userService.updateUser(selectedUser);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User updated!"));
            users = userService.getAllUsers();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    // select method
    public void selectUser(User user) {
        this.selectedUser = user;
    }

    // delete method
    public void confirmDelete(User user) {
        try {
            userService.deleteUser(user);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User deleted!"));
            users.remove(user);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete user."));
        }
    }

    // delete all method
    public void confirmDeleteAll() {
        try {
            userService.deleteAllUsers();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "All users deleted!"));
            users = userService.getAllUsers(); // Refresh the user list (now empty)
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete all users."));
        }
    }

    public void viewDependants() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ConfigurableNavigationHandler navigationHandler = (ConfigurableNavigationHandler) facesContext.getApplication().getNavigationHandler();
        navigationHandler.performNavigation("/pages/dependants/view_dependants.xhtml?id=" + selectedUser.getId());
    }
}
