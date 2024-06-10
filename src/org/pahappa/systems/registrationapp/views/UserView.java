package org.pahappa.systems.registrationapp.views;

import org.pahappa.systems.registrationapp.services.UserService;
import org.pahappa.systems.registrationapp.models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
        String username;
        String firstName;
        String lastName;
        String dobInput;
        List<User> users = userService.getAllUsers();

        while(true){
            System.out.println("Please enter your username: ");
            username = scanner.nextLine();
            String finalUsername = username;
            Optional<User> optionalUser = users.stream().
                    filter(user -> user.getUsername().equals(finalUsername)).
                    findFirst();
            if(cancelled(username)){
                System.out.println("Operation cancelled.");
                return;
            }
            if (username.length() <= 3){
                System.out.println("Username cannot be less than 4 characters. Please try again.");
            }
            else if (!username.matches("[a-zA-Z0-9_]*") || username.matches("\\.+") || username.matches("_+") || username.matches("\\d+")){
                System.out.println("Invalid username. Please try again.");
            }
            else if (username.length() > 16){
                System.out.println("Username cannot be more than 16 characters. Please try again.");
            }
            else if (username.isBlank()){
                System.out.println("Username cannot be blank. Please try again.");
            } else if (optionalUser.isPresent()) {
                System.out.println("Username is already in use. Please try again.");
            } else
                break;
        }

        while(true){
            while (true){
                System.out.println("Please enter your first name: ");
                firstName = scanner.nextLine();
                if(cancelled(firstName)){
                    System.out.println("Operation cancelled.");
                    return;
                }
                else if (isNumeric(firstName)){
                    System.out.println("First name cannot be numeric. Please try again.");
                }
                else if (!firstName.matches("[a-zA-Z ]*")){
                    System.out.println("Invalid first name. Only alphabetical characters are allowed. Please try again.");
                }else if (firstName.length() > 32){
                    System.out.println("First name cannot be more than 32 characters. Please try again.");
                } else if (firstName.length() < 2) {
                    System.out.println("Last name cannot be less than 2 characters. Please try again.");
                } else
                    break;
            }

            while (true){
                System.out.println("Please enter your last name: ");
                lastName = scanner.nextLine();
                if(cancelled(lastName)){
                    System.out.println("Operation cancelled.");
                    return;
                }
                else if (isNumeric(lastName)){
                    System.out.println("Last name cannot be numeric. Please try again.");
                }
                else if (!lastName.matches("[a-zA-Z ]*")){
                    System.out.println("Invalid last name. Only alphabetical characters are allowed. Please try again.");
                } else if (lastName.length() > 32){
                    System.out.println("Last name cannot be more than 32 characters. Please try again.");
                } else if (lastName.length() < 2) {
                    System.out.println("Last name cannot be less than 2 characters. Please try again.");
                } else
                    break;
            }
            if (firstName.isBlank() && lastName.isBlank() ) {
                System.out.println("Both names cannot be blank. Please try again.");
            } else
                break;
        }

        while(true){
            System.out.println("Please enter your date of birth (DD-MM-YYYY): ");
            dobInput = scanner.nextLine();
            if(cancelled(dobInput)){
                System.out.println("Operation cancelled.");
                return;
            }
            try {
                Date dateOfBirth = simpleDateFormat.parse(dobInput);
                Date currentDate = new Date();
                if (dateOfBirth.after(currentDate)){
                    System.out.println("Date of birth cannot be after current date. Please try again.");
                }
                else {
                    boolean success = userService.registerUser(username, firstName, lastName, dateOfBirth);
                    if (success) {
                        System.out.println("User registered successfully.");
                        break;
                    } else {
                        System.out.println("Username already exists. Registration failed.");
                    }
                }
            } catch (ParseException e) {
                System.out.println("Invalid date format.");
            }
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
        String username;

        while(true){
            System.out.println("Please enter username: ");
            username = scanner.nextLine();
            if (username.isBlank()){
                System.out.println("Username cannot be blank. Please try again.");
            }
            else if (cancelled(username)) {
                System.out.println("Operation cancelled.");
                return;
            } else
                break;
        }

        User user = userService.getUser(username);
        if(!(user == null)) {
            System.out.println("User: " + user + "\nDetails " + "\nName: " + user.getFirstname() + " " + user.getLastname() + "\nDate of Birth: " + simpleDateFormat.format(user.getDateOfBirth()));
        } else
            System.out.println("No user found.");
    }

    private void updateUserOfUsername() {
        String username;
        String firstName;
        String lastName;
        String dobInput;
        List<User> users = userService.getAllUsers();

        while(true){
            System.out.println("Please enter username: ");
            username = scanner.nextLine();
            String finalUsername = username;
            Optional<User> optionalUser = users.stream().
                    filter(user -> user.getUsername().equals(finalUsername)).
                    findFirst();
            if(cancelled(username)){
                System.out.println("Operation cancelled.");
                return;
            }
            else if (username.isBlank()){
                System.out.println("Username cannot be blank. Please try again.");
            } else if (optionalUser.isEmpty()) {
                System.out.println("User does not exist. Please try again.");
            } else
                break;
        }

        while(true){
            while (true){
                System.out.println("Please enter new first name: ");
                String finalUsername = username;
                Optional<User> optionalUser = users.stream().
                        filter(user -> user.getUsername().equals(finalUsername)).
                        findFirst();
                String oldFirstname = optionalUser.get().getFirstname();
                System.out.println("Old first name: " + oldFirstname);
                firstName = scanner.nextLine();
                if(cancelled(firstName)){
                    System.out.println("Operation cancelled.");
                    return;
                }
                else if (isNumeric(firstName)){
                    System.out.println("First name cannot be numeric. Please try again.");
                }
                else if (!firstName.matches("[a-zA-Z ]*")){
                    System.out.println("Invalid first name. Only alphabetical characters are allowed. Please try again.");
                } else if (firstName.length() > 32){
                    System.out.println("First name cannot be more than 32 characters. Please try again.");
                } else if (firstName.length() < 2) {
                    System.out.println("First name cannot be less than 2 characters. Please try again.");
                } else
                    break;
            }

            while (true){
                System.out.println("Please enter new last name: ");
                String finalUsername = username;
                Optional<User> optionalUser = users.stream().
                        filter(user -> user.getUsername().equals(finalUsername)).
                        findFirst();
                String oldLastname = optionalUser.get().getLastname();
                System.out.println("Old first name: " + oldLastname);
                lastName = scanner.nextLine();
                if(cancelled(lastName)){
                    System.out.println("Operation cancelled.");
                    return;
                }
                else if (isNumeric(lastName)){
                    System.out.println("Last name cannot be numeric. Please try again.");
                }
                else if (!lastName.matches("[a-zA-Z ]*")){
                    System.out.println("Invalid last name. Only alphabetical characters are allowed. Please try again.");
                } else if (lastName.length() > 32){
                    System.out.println("Last name cannot be more than 32 characters. Please try again.");
                } else if (lastName.length() < 2) {
                    System.out.println("Last name cannot be less than 2 characters. Please try again.");
                } else
                    break;
            }
            if (firstName.isBlank() && lastName.isBlank() ) {
                System.out.println("Both names cannot be blank. Please try again.");
            } else
                break;
        }

        while(true){
            System.out.println("Please enter new date of birth (DD-MM-YYYY): ");
            String finalUsername = username;
            Optional<User> optionalUser = users.stream().
                    filter(user -> user.getUsername().equals(finalUsername)).
                    findFirst();
            String oldDateOfBirth = simpleDateFormat.format(optionalUser.get().getDateOfBirth());
            System.out.println("Old date of birth: " + oldDateOfBirth);
            dobInput = scanner.nextLine();
            if(cancelled(dobInput)){
                System.out.println("Operation cancelled.");
                return;
            }
            try {
                Date dateOfBirth = simpleDateFormat.parse(dobInput);
                Date currentDate = new Date();
                if (dateOfBirth.after(currentDate)){
                    System.out.println("Date of birth cannot be after current date. Please try again.");
                }
                else {
                    boolean success = userService.updateUser(username, firstName, lastName, dateOfBirth);
                    if (success) {
                        System.out.println("User updated successfully.");
                    } else {
                        System.out.println("Username does not exist. Update failed.");
                    }
                    break;
                }
            } catch (ParseException e) {
                System.out.println("Invalid date format.");
            }
        }
    }

    private void deleteUserOfUsername() {
        String username;

        while (true){
            System.out.println("Please enter username: ");
            username = scanner.nextLine();
            if (username.isBlank()){
                System.out.println("Username cannot be blank. Please try again.");
            } else
                break;
        }

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
            } else if (cancelled(flag)) {
                System.out.println("Operation cancelled.");
            } else {
                System.out.println("Not a valid input. Delete all users function cancelled.");
            }
        } else
            System.out.println("No users have been registered.");
    }

    private static boolean isNumeric(String str){
        try {
            double d = Double.parseDouble(str);
            int i = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private static boolean cancelled(String str){
        str = str.toLowerCase();
        return str.equals("q");
    }

}
