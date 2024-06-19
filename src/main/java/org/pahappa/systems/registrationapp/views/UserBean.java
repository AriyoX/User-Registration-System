package org.pahappa.systems.registrationapp.views;

import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.UserService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.util.List;

@ManagedBean(name = "userBean")
@RequestScoped
public class UserBean {
    private User user = new User();
    private final UserService userService = new UserService();
    private List<User> users;

    @PostConstruct
    public void init() {
        users = userService.getAllUsers();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

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

    public List<User> getUsers() {
        return users;
    }

}
