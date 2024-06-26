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
        DependantService dependantService = new DependantService();
        List<Dependant> dependants = dependantService.getDependantsByGender(Dependant.Gender.MALE);
        for (Dependant dependant : dependants) {
            System.out.println(dependant);
        }
    }
}
