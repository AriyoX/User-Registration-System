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
        User user = new User();
        Dependant dependant = new Dependant();

        user.setUsername("Ariyo123");
        user.setPassword("123456");
        user.setFirstname("Ariyo");
        user.setLastname("Ariyo");
        user.setDateOfBirth(date);

        dependant.setUsername("Ahumuza123");
        dependant.setFirstname("Ahumuza");
        dependant.setLastname("Ahumuza");
        dependant.setDateOfBirth(date);
        dependant.setGender(Dependant.Gender.MALE);

        DependantService dependantService = new DependantService();
        UserService userService = new UserService();
        userService.registerUser(user);
        dependantService.addDependantToUser(user, dependant);
    }
}
