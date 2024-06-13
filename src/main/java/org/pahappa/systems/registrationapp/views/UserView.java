package org.pahappa.systems.registrationapp.views;

import org.pahappa.systems.registrationapp.exception.ExitException;
import org.pahappa.systems.registrationapp.services.UserService;
import org.pahappa.systems.registrationapp.models.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class UserView {

    private final Scanner scanner;
    private final UserService userService;
    private final SimpleDateFormat simpleDateFormat;

    public UserView(){
        this.scanner = new Scanner(System.in);
        this.userService = new UserService();
        this.simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        this.simpleDateFormat.setLenient(false);
    }

    public void displayMenu() {
        System.out.println("********* User Registration System *********");
        boolean running = true;
        if (!userService.isDatabaseConnected()){
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
            try{
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
            }catch (Exception e){
                System.out.println("Invalid choice. Please try again.");
                scanner.nextLine(); // Consume the newline character
            }
        }
    }

    private void registerUser() {
        String username;
        String firstName;
        String lastName;
        String dobInput;
        Date dateOfBirth;
        User newUser = new User();
        while(true){
            System.out.println("Please enter your username: ");
            username = scanner.nextLine();
            try {
                userService.isFunctionExited(username);
                userService.validateNewUsername(username);
                newUser.setUsername(username);
                break;
            } catch (ExitException e) {
                System.out.println(e.getMessage());
                return;
            } catch (Exception e){
                System.out.println(e.getMessage());
            }

        }
        while(true){
            while (true){
                System.out.println("Please enter your first name: ");
                firstName = scanner.nextLine();
                try {
                    userService.isFunctionExited(firstName);
                    userService.validateFirstName(firstName);
                    newUser.setFirstname(firstName);
                    break;
                } catch (ExitException e) {
                    System.out.println(e.getMessage());
                    return;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            while (true){
                System.out.println("Please enter your last name: ");
                lastName = scanner.nextLine();
                try {
                    userService.isFunctionExited(lastName);
                    userService.validateLastName(lastName);
                    newUser.setLastname(lastName);
                    break;
                } catch (ExitException e) {
                    System.out.println(e.getMessage());
                    return;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            try {
                userService.validateBothNames(firstName, lastName);
                break;
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        while(true){
            System.out.println("Please enter your date of birth (DD-MM-YYYY): ");
            dobInput = scanner.nextLine();
            try {
                userService.isFunctionExited(dobInput);
                userService.validateDateOfBirth(dobInput);
                dateOfBirth = simpleDateFormat.parse(dobInput);
                newUser.setDateOfBirth(dateOfBirth);
                break;
            } catch (ExitException e) {
                System.out.println(e.getMessage());
                return;
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        try{
            userService.registerUser(newUser);
            System.out.println("User: " + newUser.getUsername() + " created successfully.");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void displayAllUsers() {
        try {
            userService.anyUsersRegistered();
            List<User> users = userService.getAllUsers();
            System.out.println("All Users:");
            System.out.println("************************************************************");
            for (User user : users) {
                System.out.println("Username: " + user + "\nDetails " + "\nName: " + user.getFirstname() + " " + user.getLastname() + "\nDate of Birth: " + simpleDateFormat.format(user.getDateOfBirth()));
                System.out.println("************************************************************");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void getUserOfUsername() {
        String username;
        User user;
        try {
            userService.anyUsersRegistered();
            while(true){
                System.out.println("Please enter username: ");
                username = scanner.nextLine();
                try {
                    userService.isFunctionExited(username);
                    userService.validateUsername(username);
                    break;
                } catch (ExitException e) {
                    System.out.println(e.getMessage());
                    return;
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            user = userService.getUser(username);
            System.out.println("User: " + user + "\nDetails " + "\nName: " + user.getFirstname() + " " + user.getLastname() + "\nDate of Birth: " + simpleDateFormat.format(user.getDateOfBirth()));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void updateUserOfUsername() {
        String username;
        String firstName;
        String lastName;
        String dobInput;
        Date dateOfBirth;
        User updatedUser;
        try {
            userService.anyUsersRegistered();
            while(true){
                System.out.println("Please enter username: ");
                username = scanner.nextLine();
                try {
                    userService.isFunctionExited(username);
                    userService.validateUsername(username);
                    updatedUser = userService.getUser(username);
                    break;
                } catch (ExitException e) {
                    System.out.println(e.getMessage());
                    return;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            while(true){
                while (true){
                    System.out.println("Please enter new first name: ");
                    String oldFirstname = updatedUser.getFirstname();
                    System.out.println("Old first name: " + oldFirstname);
                    firstName = scanner.nextLine();
                    try{
                        userService.isFunctionExited(firstName);
                        userService.validateFirstName(firstName);
                        break;
                    } catch (ExitException e) {
                        System.out.println(e.getMessage());
                        return;
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                while (true){
                    System.out.println("Please enter new last name: ");
                    String oldLastname = updatedUser.getLastname();
                    System.out.println("Old last name: " + oldLastname);
                    lastName = scanner.nextLine();
                    try{
                        userService.isFunctionExited(lastName);
                        userService.validateLastName(lastName);
                        break;
                    } catch (ExitException e) {
                        System.out.println(e.getMessage());
                        return;
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                try {
                    userService.validateBothNames(firstName, lastName);
                    updatedUser.setFirstname(firstName);
                    updatedUser.setLastname(lastName);
                    break;
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            while(true){
                System.out.println("Please enter new date of birth (DD-MM-YYYY): ");
                String oldDateOfBirth = simpleDateFormat.format(updatedUser.getDateOfBirth());
                System.out.println("Old date of birth: " + oldDateOfBirth);
                dobInput = scanner.nextLine();
                try {
                    userService.isFunctionExited(dobInput);
                    userService.validateDateOfBirth(dobInput);
                    dateOfBirth = simpleDateFormat.parse(dobInput);
                    updatedUser.setDateOfBirth(dateOfBirth);
                    break;
                } catch (ExitException e) {
                    System.out.println(e.getMessage());
                    return;
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            try {
                userService.updateUser(updatedUser);
                System.out.println("User: " + updatedUser.getUsername() + " updated successfully.");
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteUserOfUsername() {
        String username;
        User deletedUser;
        try {
            userService.anyUsersRegistered();
            while(true){
                System.out.println("Please enter username of user to delete: ");
                username = scanner.nextLine();
                try {
                    userService.isFunctionExited(username);
                    userService.validateUsername(username);
                    deletedUser = userService.getUser(username);
                    userService.deleteUser(deletedUser);
                    System.out.println("User " + deletedUser.getUsername() + " deleted successfully.");
                    break;
                } catch (ExitException e) {
                    System.out.println(e.getMessage());
                    return;
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void deleteAllUsers() {
        try {
            userService.anyUsersRegistered();
            System.out.println("Are you sure you want to delete all users?. Press 'Y' to confirm deletion.");
            if (scanner.nextLine().equalsIgnoreCase("Y")){
                userService.deleteAllUsers();
                System.out.println("All users deleted successfully.");
            } else
                System.out.println("Operation Cancelled");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
