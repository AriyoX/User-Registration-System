package org.pahappa.systems.registrationapp.services;

import org.pahappa.systems.registrationapp.dao.DependantDAO;
import org.pahappa.systems.registrationapp.dao.UserDAO;
import org.pahappa.systems.registrationapp.exception.WrongValidationException;
import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.Dependant.Gender;
import org.pahappa.systems.registrationapp.models.User;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DependantService {
    private final DependantDAO dependantDAO;
    private final UserDAO userDAO;
    private final UserService userService;
    private final SimpleDateFormat simpleDateFormat;

    public DependantService(){
        this.dependantDAO = new DependantDAO();
        this.userDAO = new UserDAO();
        this.userService = new UserService();
        this.simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        this.simpleDateFormat.setLenient(false);
    }

    public boolean isDatabaseConnected(){
        return dependantDAO.isDatabaseConnected();
    }

    public List<Dependant> getAllDependants(){
        return dependantDAO.getAllDependants();
    }

    public List<Dependant> getUserDependants(User user){
        return dependantDAO.getUserDependants(user);
    }

    public Dependant getDependant(String username){
        return dependantDAO.getDependantByUsername(username);
    }

    public Dependant getDependantByFirstName(String firstName){
        return dependantDAO.getDependantByFirstName(firstName);
    }

    public Dependant getDependantByLastName(String lastName){
        return dependantDAO.getDependantByLastName(lastName);
    }

    public Dependant getDependantByGender(Gender gender){
        return dependantDAO.getDependantByGender(gender);
    }

    public List<Dependant> searchDependants(String query, User user){
        List<Dependant> dependants = getUserDependants(user);
        if (query == null || query.isEmpty()){
            return dependants;
        }
        return dependants.stream()
                .filter(dependent -> dependent.getFirstname().toLowerCase().contains(query.toLowerCase()) ||
                        dependent.getUsername().toLowerCase().contains(query.toLowerCase()) ||
                        dependent.getLastname().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());

    }

    public List<Dependant> searchDependants(String query){
        List<Dependant> dependants = getAllDependants();
        if (query == null || query.isEmpty()){
            return dependants;
        }
        return dependants.stream()
                .filter(dependent -> dependent.getFirstname().toLowerCase().contains(query.toLowerCase()) ||
                        dependent.getUsername().toLowerCase().contains(query.toLowerCase()) ||
                        dependent.getLastname().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

//    public List<Dependant> filterDependants(String query, User user){
//        List<Dependant> dependants = getUserDependants(user);
//        if (query == null || query.isEmpty()){
//            return dependants;
//        }
//        return dependants = dependants.stream()
//                .filter(dependent -> dependent.getGender().name().equalsIgnoreCase(gender));
//    }

    public void deleteAllDependants(){
        dependantDAO.deleteAllDependants();
    }

    public List<Dependant> getDependantsByUserId(long userId){
        return dependantDAO.getDependantsByUserId(userId);
    }

    public void addDependantToUser(User user, Dependant dependant) throws WrongValidationException {
        Dependant existingDependant = dependantDAO.getDependantByUsername(dependant.getUsername());
        User existingUser = userDAO.getUserByUsername(user.getUsername());
        if(existingDependant == null && existingUser != null){
            try {
                validateNewDependant(dependant);
                dependantDAO.addDependantToUser(user, dependant);
            } catch (WrongValidationException e) {
                throw new WrongValidationException("Error registering dependant.");
            }
        } else {
            throw new WrongValidationException("Dependant with that username already exists.");
        }
    }

    public void updateDependant(Dependant dependant) throws WrongValidationException {
        Dependant dependantToUpdate = dependantDAO.getDependantByUsername(dependant.getUsername());
        if (dependantToUpdate == null) {
            throw new WrongValidationException("Dependant does not exist.");
        }
        try {
            validateExistingDependant(dependantToUpdate);
            dependantToUpdate.setFirstname(dependant.getFirstname());
            dependantToUpdate.setLastname(dependant.getLastname());
            dependantToUpdate.setGender(dependant.getGender());
            dependantToUpdate.setDateOfBirth(dependant.getDateOfBirth());
            dependantDAO.update(dependantToUpdate);
        } catch (WrongValidationException e) {
            throw new WrongValidationException(e.getMessage());
        }
    }

    public void deleteDependant(Dependant dependant){
        dependantDAO.delete(dependant);
    }

    public void validateDependantUsername(String username) throws WrongValidationException {
        List<Dependant> dependants = getAllDependants();
        Optional<Dependant> optionalDependant = dependants.stream()
                .filter(dependant -> dependant.getUsername().equals(username)).findFirst();
        if (optionalDependant.isPresent()){
            throw new WrongValidationException("Dependant with username '" + username + "' already exists.");
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

    public void validateNewDependant(Dependant dependant) throws WrongValidationException {
        validateDependantUsername(dependant.getUsername());
        userService.validateFirstName(dependant.getFirstname());
        userService.validateLastName(dependant.getLastname());
        userService.validateDateOfBirth(simpleDateFormat.format(dependant.getDateOfBirth()));
    }

    public void validateExistingDependant(Dependant dependant) throws WrongValidationException {
        userService.validateFirstName(dependant.getFirstname());
        userService.validateLastName(dependant.getLastname());
        userService.validateDateOfBirth(simpleDateFormat.format(dependant.getDateOfBirth()));
    }

}
