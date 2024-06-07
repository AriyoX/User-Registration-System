package org.pahappa.systems.registrationapp.views;

import org.pahappa.systems.registrationapp.services.UserService;
import org.pahappa.systems.registrationapp.models.User;

import java.text.ParseException;
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
        System.out.println("Please enter your username: ");
        String username = scanner.nextLine();
        if (username.isBlank()){
            System.out.println("Username cannot be blank. Please try again.");
            return;
        }
        System.out.println("Please enter your first name: ");
        String firstName = scanner.nextLine();
        System.out.println("Please enter your last name: ");
        String lastName = scanner.nextLine();
        if (firstName.isBlank() && lastName.isBlank() ) {
            System.out.println("Both names cannot be blank. Please try again.");
            return;
        }
        System.out.println("Please enter your date of birth (DD-MM-YYYY): ");
        String dobInput = scanner.nextLine();

        try {
            Date dateOfBirth = simpleDateFormat.parse(dobInput);
            boolean success = userService.registerUser(username, firstName, lastName, dateOfBirth);
            if (success) {
                System.out.println("User registered successfully.");
            } else {
                System.out.println("Username already exists. Registration failed.");
            }
        } catch (ParseException e) {
            System.out.println("Invalid date format.");
        } catch (Exception e) {
            System.out.println("Something went wrong. Please try again.");
        }
    }

    private void displayAllUsers() {
        List<User> users =  userService.getAllUsers();
        if (!users.isEmpty()) {
            System.out.println("All Users:");
            System.out.println("************************************************************");
            for (User user : users) {
                System.out.println("Username: " + user + "\nDetails " + "\nName: " + user.getFirstname() + " " + user.getLastname() + "\nDate of Birth: " + simpleDateFormat.format(user.getDateOfBirth()));
                System.out.println("************************************************************");
            }
        } else
            System.out.println("No users found.");
    }

    private void getUserOfUsername() {
        System.out.println("Please enter username: ");
        String username = scanner.nextLine();
        if (username.isBlank()){
            System.out.println("Username cannot be blank. Please try again.");
            return;
        }
        User user = userService.getUser(username);
        if(!(user == null)) {
            System.out.println("User: " + user + "\nDetails " + "\nName: " + user.getFirstname() + " " + user.getLastname() + "\nDate of Birth: " + simpleDateFormat.format(user.getDateOfBirth()));
        } else
            System.out.println("No user found.");
    }

    private void updateUserOfUsername() {
        System.out.println("Please enter username: ");
        String username = scanner.nextLine();
        if (username.isBlank()){
            System.out.println("Username cannot be blank. Please try again.");
            return;
        }
        System.out.println("Please enter new first name: ");
        String firstName = scanner.nextLine();
        System.out.println("Please enter new last name: ");
        String lastName = scanner.nextLine();
        if (firstName.isBlank() && lastName.isBlank() ) {
            System.out.println("Both names cannot be blank. Please try again.");
            return;
        }
        System.out.println("Please enter new date of birth (DD-MM-YYYY): ");
        String dobInput = scanner.nextLine();

        try {
            Date dateOfBirth = simpleDateFormat.parse(dobInput);
            boolean success = userService.updateUser(username, firstName, lastName, dateOfBirth);
            if (success) {
                System.out.println("User updated successfully.");
            } else {
                System.out.println("User does not exist. Update failed.");
            }
        } catch (ParseException e) {
            System.out.println("Invalid date format.");
        } catch (Exception e) {
            System.out.println("Something went wrong. Please try again.");
        }
    }

    private void deleteUserOfUsername() {
        System.out.println("Please enter username: ");
        String username = scanner.nextLine();
        boolean success = userService.deleteUser(username);
        if (success) {
            System.out.println("User deleted successfully.");
        } else
            System.out.println("Cannot delete a user that does not exist.");
    }

    private void deleteAllUsers() {
        List<User> users = userService.getAllUsers();
        if (!users.isEmpty()) {
            System.out.println("Are you sure you want to delete all users? Enter 'Y' to continue");
            String flag = scanner.nextLine();
            flag = flag.toLowerCase();
            if (flag.equals("y")){
                userService.deleteAllUsers();
                System.out.println("All users deleted successfully.");
            } else {
                System.out.println("Not a valid input. Delete all users function cancelled.");
            }
        } else
            System.out.println("No users have been registered.");
    }
}
