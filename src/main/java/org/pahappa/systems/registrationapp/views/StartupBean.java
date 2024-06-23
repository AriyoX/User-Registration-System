package org.pahappa.systems.registrationapp.views;

import org.pahappa.systems.registrationapp.exception.WrongValidationException;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.Date;

@Singleton
@Startup
public class StartupBean {

    private static final Logger log = LoggerFactory.getLogger(StartupBean.class);
    @Inject
    private UserService userService;

    @PostConstruct
    public void init() {
        // Check if admin user exists
        Date date = new Date();
        try {
            User admin = userService.getUser("admin");

            // Create admin user if it doesn't exist
            if (admin == null) {
                admin = new User();
                admin.setUsername("admin");
                admin.setFirstname("admin");
                admin.setLastname("admin");
                admin.setDateOfBirth(new Date()); // Set current date
                admin.setPassword("admin"); // Consider using a more secure password hashing method
                admin.setRole("ADMIN");
                userService.registerUser(admin);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}