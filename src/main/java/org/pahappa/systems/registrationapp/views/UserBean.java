package org.pahappa.systems.registrationapp.views;

import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.UserService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "userBean")
@SessionScoped
public class UserBean {
    private User user = new User();
    private final UserService userService = new UserService();

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String registerUser() {
        try {
            // Validation and registration logic
            userService.registerUser(user);

            // Reset the form fields
            user = new User();

            // Add a success message
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Registration Successful!", null));

            return "index";

        } catch (Exception e) {
            // Handle other validation or registration errors
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
        return null;
    }
}
