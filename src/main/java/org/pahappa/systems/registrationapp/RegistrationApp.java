package org.pahappa.systems.registrationapp;

import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.DependantService;
import org.pahappa.systems.registrationapp.services.UserService;
import org.pahappa.systems.registrationapp.views.UserView;

import java.util.Date;

public class RegistrationApp {

    public static void main(String[] args) {
        // UserView userView = new UserView();
        // userView.displayMenu();

        Date date = new Date();
        User user = new User();
        Dependant dependant = new Dependant();

        user.setUsername("Test1");
        user.setFirstname("Test");
        user.setLastname("Test");
        user.setDateOfBirth(date);

        dependant.setUsername("Test2");
        dependant.setFirstname("Test");
        dependant.setLastname("Test");
        dependant.setDateOfBirth(date);
        dependant.setGender(Dependant.Gender.MALE);

        DependantService dependantService = new DependantService();
        UserService userService = new UserService();
        userService.registerUser(user);
        dependantService.addDependantToUser(user, dependant);

    }
}
