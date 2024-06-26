package org.pahappa.systems.registrationapp.views;

import org.pahappa.systems.registrationapp.exception.ExitException;
import org.pahappa.systems.registrationapp.services.DependantService;
import org.pahappa.systems.registrationapp.services.UserService;
import org.pahappa.systems.registrationapp.models.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class UserView {

    private final Scanner scanner;
    private final UserService userService;
    private final DependantService dependantService;
    private final SimpleDateFormat simpleDateFormat;

    public UserView() {
        this.scanner = new Scanner(System.in);
        this.userService = new UserService();
        this.dependantService = new DependantService();
        this.simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        this.simpleDateFormat.setLenient(false);
    }

    public void displayMenu() {
        System.out.println("********* User Registration System *********");
        boolean running = true;
        if (!userService.isDatabaseConnected() || !dependantService.isDatabaseConnected()) {
            System.out.println("Database is not connected. Please connect to database and restart the program.");
            running = false;
        }

        while (running) {
            System.out.println("\nChoose an operation:");
            System.out.println("1. Register a user");
            System.out.println("2. Display all users");
            System.out.println("3. Get a user of username");
            System.out.println("4. Update user details of username");
            System.out.println("5. Delete User of username");
            System.out.println("6. Delete all users");
            System.out.println("7. Exit");
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character
                switch (choice) {
                    case 1:
                        registerUser();
                        break;
                    case 2:
                        displayAllUsers();
                        break;
                    case 3:
                        getUserOfUsername();
                        break;
                    case 4:
                        updateUserOfUsername();
                        break;
                    case 5:
                        deleteUserOfUsername();
                        break;
                    case 6:
                        deleteAllUsers();
                        break;
                    case 7:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid choice. Please try again.");
                scanner.nextLine(); // Consume the newline character
            }
        }
    }

    // 1
    private void registerUser() {
        User newUser = new User();
        if (!promptToValidateAndSetNewDetails(newUser)) return;
        try {
            userService.registerUser(newUser);
            System.out.println("User: " + newUser.getUsername() + " created successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String newUsernamePrompt() {
        String username;
        while (true) {
            System.out.println("Please enter your username: (Press 'Q' to cancel operation)");
            username = scanner.nextLine();
            try {
                userService.isFunctionExited(username);
                userService.validateNewUsername(username);
                return username;
            } catch (ExitException e) {
                System.out.println(e.getMessage());
                return null;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private String firstNamePrompt() {
        String firstName;
        while (true) {
            System.out.println("Please enter your first name: (Press 'Q' to cancel operation)");
            firstName = scanner.nextLine();
            try {
                userService.isFunctionExited(firstName);
                userService.validateFirstName(firstName);
                return firstName;
            } catch (ExitException e) {
                System.out.println(e.getMessage());
                return null;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private String lastNamePrompt() {
        String lastName;
        while (true) {
            System.out.println("Please enter your last name: (Press 'Q' to cancel operation)");
            lastName = scanner.nextLine();
            try {
                userService.isFunctionExited(lastName);
                userService.validateLastName(lastName);
                return lastName;
            } catch (ExitException e) {
                System.out.println(e.getMessage());
                return null;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private Date dateOfBirthPrompt() {
        while (true) {
            System.out.println("Please enter your date of birth (DD-MM-YYYY): (Press 'Q' to cancel operation)");
            String dobInput = scanner.nextLine();
            try {
                userService.isFunctionExited(dobInput);
                userService.validateDateOfBirth(dobInput);
                return simpleDateFormat.parse(dobInput);
            } catch (ExitException e) {
                System.out.println(e.getMessage());
                return null;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private boolean promptToValidateAndSetNewDetails(User user) {
        while (true) {
            try {
                String username = newUsernamePrompt();
                if (username == null) return false;
                user.setUsername(username);
                while (true) {
                    try {
                        String firstName = firstNamePrompt();
                        if (firstName == null) return false;
                        user.setFirstname(firstName);
                        String lastName = lastNamePrompt();
                        if (lastName == null) return false;
                        user.setLastname(lastName);
                        userService.validateBothNames(firstName, lastName);
                        Date dateOfBirth = dateOfBirthPrompt();
                        if (dateOfBirth == null) return false;
                        user.setDateOfBirth(dateOfBirth);
                        return true;
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // 2
    private void displayAllUsers() {
        try {
            userService.anyUsersRegistered();
            listAllUsers();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void listAllUsers() {
        List<User> users = userService.getAllUsers();
        if (!users.isEmpty()) {
            System.out.println("All Users:");
            System.out.println("************************************************************");
            for (User user : users) {
                System.out.println("Username: " + user + "\nDetails " + "\nName: " + user.getFirstname() + " " + user.getLastname() + "\nDate of Birth: " + simpleDateFormat.format(user.getDateOfBirth()));
                System.out.println("************************************************************");
            }
        }
    }

    // 3
    private void getUserOfUsername() {
        try {
            userService.anyUsersRegistered();
            String username = existingUsernamePrompt();
            User user = userService.getUser(username);
            listUserDetails(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String existingUsernamePrompt() {
        while (true) {
            System.out.println("Please enter username: (Press 'Q' to cancel operation)");
            String username = scanner.nextLine();
            try {
                userService.isFunctionExited(username);
                userService.validateExistingUsername(username);
                return username;
            } catch (ExitException e) {
                System.out.println(e.getMessage());
                return null;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void listUserDetails(User user) {
        if (user != null) {
            System.out.println("User: " + user + "\nDetails " +
                    "\nName: " + user.getFirstname() + " " + user.getLastname() +
                    "\nDate of Birth: " + simpleDateFormat.format(user.getDateOfBirth()));
        }
    }

    // 4
    private void updateUserOfUsername(){
        User existingUser = getUserToUpdate();
        if (existingUser == null) return;
        if (!promptToValidateAndUpdateDetails(existingUser)) return;
        try {
            userService.updateUser(existingUser);
            System.out.println("User: " + existingUser.getUsername() + " updated successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private User getUserToUpdate(){
        try{
            userService.anyUsersRegistered();
            return userService.getUser(existingUsernamePrompt());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private Date updateDateOfBirthPrompt(User user) {
        while (true) {
            System.out.println("Please enter your date of birth (DD-MM-YYYY): (Press 'Q' to cancel operation)");
            String dobInput = scanner.nextLine();
            try {
                if (dobInput.isEmpty()){
                    return user.getDateOfBirth();
                }
                userService.isFunctionExited(dobInput);
                userService.validateDateOfBirth(dobInput);
                return simpleDateFormat.parse(dobInput);
            } catch (ExitException e) {
                System.out.println(e.getMessage());
                return null;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private boolean promptToValidateAndUpdateDetails(User user) {
        String oldFirstName = user.getFirstname();
        String oldLastName = user.getLastname();
        while (true) {
            try {
                System.out.println("Old first name: " + oldFirstName + ". Press Enter to skip.");
                String firstName = firstNamePrompt();
                if (firstName == null) return false;
                if (firstName.isEmpty()){
                    user.setFirstname(oldFirstName);
                } else
                    user.setFirstname(firstName);
                System.out.println("Old last name: " + oldLastName + ". Press Enter to skip.");
                String lastName = lastNamePrompt();
                if (lastName == null) return false;
                if (lastName.isEmpty()){
                    user.setLastname(oldLastName);
                } else
                    user.setLastname(lastName);
                userService.validateBothNames(user.getFirstname(), user.getLastname());
                System.out.println("Old date of birth: " + simpleDateFormat.format(user.getDateOfBirth()) + ". Press Enter to skip.");
                Date dateOfBirth = updateDateOfBirthPrompt(user);
                if (dateOfBirth == null) return false;
                user.setDateOfBirth(dateOfBirth);
                return true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // 5
    private void deleteUserOfUsername() {
        try {
            userService.anyUsersRegistered();
            String username = deleteUserPrompt();
            if (username == null) return;
            User deletedUser = userService.getUser(username);
            userService.deleteUser(deletedUser);
            System.out.println("User " + deletedUser.getUsername() + " deleted successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String deleteUserPrompt() {
        while (true) {
            System.out.println("Please enter username of user to delete: (Press 'Q' to cancel operation)");
            String username = scanner.nextLine();
            try {
                userService.isFunctionExited(username);
                userService.validateExistingUsername(username);
                return username;
            } catch (ExitException e) {
                System.out.println(e.getMessage());
                return null;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // 6
    private void deleteAllUsers() {
        try {
            userService.anyUsersRegistered();
            deleteAllUsersPrompt();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteAllUsersPrompt() {
        System.out.println("Are you sure you want to delete all users?. Press 'Y' to confirm deletion.");
        if (scanner.nextLine().equalsIgnoreCase("Y")) {
            userService.deleteAllUsers();
            System.out.println("All users deleted successfully.");
        } else
            System.out.println("Operation cancelled.");
    }

}