package org.pahappa.systems.registrationapp.util;

import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.UserService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Date;

@WebListener
public class AdminUserInitializer implements ServletContextListener {

    private UserService userService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Get an instance of your UserService (adjust if needed)
        userService = new UserService();

        // Create the admin user if it doesn't exist
        createAdminUser();
    }

    private void createAdminUser() {
        try {
            User admin = userService.getUser("admin");

            if (admin == null) {
                admin = new User();
                admin.setUsername("admin");
                admin.setFirstname("admin");
                admin.setLastname("admin");
                admin.setDateOfBirth(new Date());
                admin.setEmail("ahumuzaariyo@outlook.com");
                admin.setPassword("@dmin"); // Use a strong hashing method in production!
                admin.setRole("ADMIN");
                userService.registerUser(admin);

                System.out.println("Admin user created successfully!"); // Or use a logger
            } else {
                System.out.println("Admin user already exists.");
            }
        } catch (Exception e) {
            System.err.println("Error creating admin user: " + e.getMessage());
            // Use a proper logging framework (e.g., Log4j, SLF4j) in a real app
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Clean up if needed
    }
}