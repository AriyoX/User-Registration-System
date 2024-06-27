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
        UserService userService = new UserService();
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            System.out.println(user.getPassword());
        }
    }
}
