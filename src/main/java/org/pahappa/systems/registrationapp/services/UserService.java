package org.pahappa.systems.registrationapp.services;

import org.pahappa.systems.registrationapp.models.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class UserService {
    List<User> users = new ArrayList<>();

    public boolean registerUser(String username, String firstName, String lastName, Date dateOfBirth) {
        Optional<User> optionalUser = users.stream().
                filter(user -> user.getUsername().equals(username)).
                findAny();
        if (optionalUser.isPresent()) {
            return false;
        } else {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setFirstname(firstName);
            newUser.setLastname(lastName);
            newUser.setDateOfBirth(dateOfBirth);
            users.add(newUser);
            return true;
        }
    }

    public List<User> getAllUsers() {
        return users;
    }

    public User getUser(String username) {
        Optional<User> optionalUser = users.stream().
                filter(user -> user.getUsername().equals(username)).
                findFirst();
        return optionalUser.orElse(null);
    }

    public boolean updateUser(String username, String firstName, String lastName, Date dateOfBirth) {
        Optional<User> optionalUser = users.stream().
                filter(user -> user.getUsername().equals(username)).
                findFirst();
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setFirstname(firstName);
            user.setLastname(lastName);
            user.setDateOfBirth(dateOfBirth);
            return true;
        } else
            return false;
    }

    public boolean deleteUser(String username) {
        Optional<User> optionalUser = users.stream().
                filter(user -> user.getUsername().equals(username)).
                findFirst();

        if (optionalUser.isPresent()) {
            users.remove(optionalUser.get());
            return true;
        } else
            return false;
    }

    public void deleteAllUsers(){
        users.clear();
    }

}
