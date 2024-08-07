package org.pahappa.systems.registrationapp.services;

import org.pahappa.systems.registrationapp.dao.UserDAO;
import org.pahappa.systems.registrationapp.exception.ExitException;
import org.pahappa.systems.registrationapp.exception.WrongValidationException;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.util.MailService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Base64;

public class UserService {
    private final UserDAO userDAO;
    private final SimpleDateFormat simpleDateFormat;


    public UserService() {
        this.userDAO = new UserDAO();
        this.simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        this.simpleDateFormat.setLenient(false);
    }

    public void registerUser(User user) throws WrongValidationException{
        User existingUser = userDAO.getUserByUsername(user.getUsername());
        if (existingUser != null) {
            throw new WrongValidationException("Username already exists, please choose another one.");
        }
        try {
            validateNewUser(user);
            String encodedPassword = Base64.getEncoder().encodeToString((user.getPassword()).getBytes());
            user.setPassword(encodedPassword);
            userDAO.add(user);
        } catch (Exception e) {
            throw new WrongValidationException(e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public List<User> getAdminUsers() {
        return userDAO.getAdminUsers();
    }

    public User getUser(String username) {
        return userDAO.getUserByUsername(username);
    }

    public User getUserByFirstName(String firstName) {
        return userDAO.getUserByFirstName(firstName);
    }

    public User getUserByLastName(String lastName) {
        return userDAO.getUserByLastName(lastName);
    }

    public void updateUser(User user) throws WrongValidationException {
        User existingUser = userDAO.getUserById(user.getId());
        if (existingUser == null) {
            throw new WrongValidationException("User does not exist. Please try again.");
        }
        try{
            validateExistingUser(existingUser);
            existingUser.setFirstname(user.getFirstname());
            existingUser.setLastname(user.getLastname());
            existingUser.setDateOfBirth(user.getDateOfBirth());
            String encodedPassword = Base64.getEncoder().encodeToString((user.getPassword()).getBytes());
            existingUser.setPassword(encodedPassword);
            existingUser.setEmail(user.getEmail());
            userDAO.update(existingUser);
            MailService.send("ahumuzaariyo@gmail.com", "tiadbqtshilfdprn", existingUser.getEmail(), "Updated Details",user.getUsername() + "\nYour details have been updated. Your new password is: " + user.getPassword());
        } catch (Exception e) {
            throw new WrongValidationException(e.getMessage());
        }
    }

    public void deleteUser(User user) {
        User existingUser = userDAO.getUserById(user.getId());
        try{
            // validateExistingUser(user);
            userDAO.delete(existingUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAllUsers(){
        userDAO.deleteAllUsers();
    }

    public User getUserById(long userId) {
        return userDAO.getUserById(userId);
    }

    public List<User> searchUsers(String query) {
        List<User> users = userDAO.getAllUsers();
        if (query == null || query.trim().isEmpty()) {
            return users;
        }
        return users.stream()
                .filter(user -> user.getUsername().toLowerCase().contains(query.toLowerCase()) ||
                        user.getFirstname().toLowerCase().contains(query.toLowerCase()) ||
                        user.getLastname().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<User> searchDeletedUsers(String query) {
        List<User> users = userDAO.getDeletedUsers();
        if (query == null || query.trim().isEmpty()) {
            return users;
        }
        return users.stream()
                .filter(user -> user.getUsername().toLowerCase().contains(query.toLowerCase()) ||
                        user.getFirstname().toLowerCase().contains(query.toLowerCase()) ||
                        user.getLastname().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public User loginUser(String identifier, String password) throws WrongValidationException {
        User existingUserWithUsername = userDAO.getUserByUsername(identifier);
        User existingUserWithEmail = userDAO.getUserByEmail(identifier);
        if (existingUserWithUsername != null) {
            String encodedPasswordFromDatabase = existingUserWithUsername.getPassword();
            String actualPassword = new String(Base64.getDecoder().decode(encodedPasswordFromDatabase));
            if (actualPassword.equals(password)) {
                return existingUserWithUsername;
            }
        }
        if (existingUserWithEmail != null) {
            String encodedPasswordFromDatabase = existingUserWithEmail.getPassword();
            String actualPassword = new String(Base64.getDecoder().decode(encodedPasswordFromDatabase));
            if (actualPassword.equals(password)) {
                return existingUserWithEmail;
            }
        }
        return null;
    }

    // for new username
    public void validateNewUsername(String username) throws WrongValidationException {
        List<User> users = getAllUsers();
        Optional<User> optionalUser = users.stream().filter(user -> user.getUsername().equals(username)).findFirst();
        if (optionalUser.isPresent()) {
            throw new WrongValidationException("Username is already taken. Please use a different username.");
        }
        if (username.length() < 3 || username == null){
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
    public void validateExistingUsername(String username) throws WrongValidationException {
        List<User> users = getAllUsers();
        if (users.isEmpty()){
            throw new WrongValidationException("No users registered.");
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
        validateNewUsername(user.getUsername());
        validateFirstName(user.getFirstname());
        validateLastName(user.getLastname());
        validateBothNames(user.getFirstname(), user.getLastname());
        validateDateOfBirth(simpleDateFormat.format(user.getDateOfBirth()));
        validateNewEmail(user.getEmail());
    }

    public void validateExistingUser(User user) throws WrongValidationException {
        validateExistingUsername(user.getUsername());
        validateFirstName(user.getFirstname());
        validateLastName(user.getLastname());
        validateBothNames(user.getFirstname(), user.getLastname());
        validateDateOfBirth(simpleDateFormat.format(user.getDateOfBirth()));
        // validateExistingEmail(user.getEmail());
    }

    public void isFunctionExited(String str) throws ExitException {
        if(str.equalsIgnoreCase("q")){
            throw new ExitException("Operation Cancelled.");
        }
    }

    public void validateNewEmail(String email) throws WrongValidationException{
        List<User> users = getAllUsers();
        Optional<User> optionalUser = users.stream().filter(user -> user.getEmail().equals(email)).findFirst();
        if (optionalUser.isPresent()){
            throw new WrongValidationException("Email is already in use. Please try again.");
        }
//        if(!email.matches("^(.+)@(\\\\S+)$")){
//            throw new WrongValidationException("Invalid email address. Please try again.");
//        }
    }

    public void validateExistingEmail(String email) throws WrongValidationException{
        if(!email.matches("^(.+)@(\\\\S+)$")){
            throw new WrongValidationException("Invalid email address. Please try again.");
        }
    }

    public void restoreDeletedUser(String username) throws WrongValidationException {
        User user = userDAO.getDeletedUserByUsername(username);
        if (user == null){
            throw new WrongValidationException("User not found. Please try again.");
        }
        userDAO.restoreDeletedUser(username);
    }

}
