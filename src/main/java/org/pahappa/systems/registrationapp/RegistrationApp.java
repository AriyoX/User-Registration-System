package org.pahappa.systems.registrationapp;

import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.DependantService;
import org.pahappa.systems.registrationapp.services.UserService;
import org.pahappa.systems.registrationapp.views.UserView;

import java.util.Date;
import java.util.List;

public class RegistrationApp {

    public static void main(String[] args) {
        // UserView userView = new UserView();
        // userView.displayMenu();
        Date date = new Date();
        UserService userService = new UserService();
        User admin = userService.getUser("admin");
        if (admin == null) {
            admin = new User();
            admin.setUsername("admin");
            admin.setFirstname("admin");
            admin.setLastname("admin");
            admin.setDateOfBirth(date);
            admin.setPassword("admin");
            admin.setRole("ADMIN");
            // admin.setDeleted(false);
            userService.registerUser(admin);
        }
    }
}
