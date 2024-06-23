package org.pahappa.systems.registrationapp.views;

import org.apache.commons.lang3.RandomStringUtils;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.UserService;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ManagedBean(name = "userBean")
@ViewScoped
public class UserBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private User user = new User();
    private final UserService userService = new UserService();
    private List<User> users;
    private String searchQuery;
    private User selectedUser;
    private long id;
    private User currentUser;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
            user.setPassword(generateCommonLangPassword());
            user.setRole("USER");
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

    public String generateCommonLangPassword() {
        return "12345678";
    }

    public User getCurrentUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        return (User) externalContext.getSessionMap().get("currentUser");
    }

}
