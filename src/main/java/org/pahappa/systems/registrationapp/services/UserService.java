package org.pahappa.systems.registrationapp.services;

import org.pahappa.systems.registrationapp.dao.UserDAO;
import org.pahappa.systems.registrationapp.exception.ExitException;
import org.pahappa.systems.registrationapp.exception.WrongValidationException;
import org.pahappa.systems.registrationapp.models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserDAO userDAO;
    private final SimpleDateFormat simpleDateFormat;


    public UserService() {
        this.userDAO = new UserDAO();
        this.simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        this.simpleDateFormat.setLenient(false);
    }

    public void registerUser(User user) {
        User existingUser = userDAO.getUserByUsername(user.getUsername());
        if (existingUser == null) {
            try{
                validateNewUser(user);
                userDAO.add(user);
            } catch (WrongValidationException e) {
                e.printStackTrace();
            }
        }
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public User getUser(String username) {
        return userDAO.getUserByUsername(username);
    }

    public void updateUser(User user) {
        User existingUser = userDAO.getUserByUsername(user.getUsername());
        try{
            validateUser(user);
            userDAO.update(existingUser);
        } catch (WrongValidationException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(User user) {
        User existingUser = userDAO.getUserByUsername(user.getUsername());
        try{
            validateUser(user);
            userDAO.delete(existingUser);
        } catch (WrongValidationException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllUsers(){
       userDAO.deleteAllUsers();
    }

    // for new username
    public void validateNewUsername(String username) throws WrongValidationException {
        List<User> users = getAllUsers();
        Optional<User> optionalUser = users.stream().filter(user -> user.getUsername().equals(username)).findFirst();
        if (optionalUser.isPresent()) {
            throw new WrongValidationException("Username is already taken. Please use a different username.");
        }
        if (username.length() < 3){
            throw new WrongValidationException("Username must be at least 3 characters. Please try again.");
        }
        if (username.length() > 16){
            throw new WrongValidationException("Username must be at most 16 characters. Please try again.");
        }
        if (!username.matches("[a-zA-Z0-9_]*") || username.matches("\\.+") || username.matches("_+") || username.matches("\\d+")){
            throw new WrongValidationException("Invalid username. Please try again.");
        }
        if (username.isBlank()){
            throw new WrongValidationException("Username cannot be blank. Please try again.");
        }
    }

    // for existing username
    public void validateUsername(String username) throws WrongValidationException {
        List<User> users = getAllUsers();
        if (users.isEmpty()){
            throw new WrongValidationException("No users registered. Please 'Q' to quit.");
        }
        Optional<User> optionalUser = users.stream().filter(user -> user.getUsername().equals(username)).findFirst();
        if (username.isBlank()){
            throw new WrongValidationException("Username cannot be blank. Please try again.");
        }
        if (optionalUser.isEmpty()) {
            throw new WrongValidationException("User " + username + " does not exist. Please try again.");
        }
    }

    public void validateFirstName(String firstName) throws WrongValidationException {
        if (firstName.length() < 2 && !firstName.isBlank()){
            throw new WrongValidationException("First name must be at least 2 characters. Please try again.");
        }
        if (firstName.length() > 32){
            throw new WrongValidationException("First name must be at most 32 characters. Please try again.");
        }
        if (!firstName.matches("[a-zA-Z ]*")){
            throw new WrongValidationException("Invalid first name. Only alphabetical letters are allowed. Please try again.");
        }
    }

    public void validateLastName(String lastName) throws WrongValidationException {
        if (lastName.length() < 2 && !lastName.isBlank()){
            throw new WrongValidationException("Last name must be at least 2 characters. Please try again.");
        }
        if (lastName.length() > 32){
            throw new WrongValidationException("Last name must be at most 32 characters. Please try again.");
        }
        if (!lastName.matches("[a-zA-Z ]*")){
            throw new WrongValidationException("Invalid last name. Only alphabetical letters are allowed. Please try again.");
        }
    }

    public void validateBothNames(String firstName, String lastName) throws WrongValidationException {
        if (firstName.isBlank() && lastName.isBlank() ){
            throw new WrongValidationException("Both names cannot be blank. Please try again.");
        }
    }

    public void validateDateOfBirth(String dobInput) throws WrongValidationException {
        Date dateOfBirth;
        try {
            dateOfBirth = simpleDateFormat.parse(dobInput);
            Date currentDate = new Date();
            if (dateOfBirth.after(currentDate)) {
                throw new WrongValidationException("Date of Birth cannot be after current date. Please try again.");
            }
        } catch (ParseException e) {
            throw new WrongValidationException("Invalid date: " + dobInput + ". It should be in the format DD-MM-YYYY. Please try again.");
        }
    }

    public void anyUsersRegistered() throws WrongValidationException {
        List<User> users = getAllUsers();
        if (users.isEmpty()){
            throw new WrongValidationException("No users registered.");
        }
    }

    public boolean isDatabaseConnected(){
        return userDAO.isDatabaseConnected();
    }

    public void validateNewUser(User user) throws WrongValidationException {
        try{
            validateNewUsername(user.getUsername());
            validateFirstName(user.getFirstname());
            validateLastName(user.getLastname());
            validateBothNames(user.getFirstname(), user.getLastname());
            validateDateOfBirth(simpleDateFormat.format(user.getDateOfBirth()));
        } catch (WrongValidationException e){
            System.out.println(e.getMessage());
        }
    }

    public void validateUser(User user) throws WrongValidationException {
        try{
            validateUsername(user.getUsername());
            validateFirstName(user.getFirstname());
            validateLastName(user.getLastname());
            validateBothNames(user.getFirstname(), user.getLastname());
            validateDateOfBirth(simpleDateFormat.format(user.getDateOfBirth()));
        } catch (WrongValidationException e){
            System.out.println(e.getMessage());
        }
    }

    public void isFunctionExited(String str) throws ExitException {
        if(str.equalsIgnoreCase("q")){
            throw new ExitException("Operation Cancelled.");
        }
    }

}
