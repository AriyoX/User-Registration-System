package org.pahappa.systems.registrationapp.views;

import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.DependantService;
import org.pahappa.systems.registrationapp.services.UserService;
import org.pahappa.systems.registrationapp.util.MailService;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@ManagedBean
@ViewScoped
public class CurrentUserBean implements Serializable {
    private static final long serialVersionUID = 3L;
    private User currentUser;
    private Dependant newDependant = new Dependant();
    private List<Dependant> currentUserDependants;
    private final UserService userService = new UserService();
    private final DependantService dependantService = new DependantService();
    private Dependant.Gender[] genderValues;
    private Dependant selectedDependant;
    private String searchQuery;
    private Dependant.Gender selectedGender;

    @PostConstruct
    public void init() {
        currentUser = getCurrentUser();
        currentUserDependants = dependantService.getUserDependants(currentUser);
        genderValues = Dependant.Gender.values();
        selectedDependant = new Dependant();
        try {
            if (currentUser == null || !"USER".equals(currentUser.getRole())) {
                FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                externalContext.getSessionMap().put("currentUser", null);
                externalContext.redirect(externalContext.getRequestContextPath() + "/pages/login/login.xhtml?faces-redirect=true");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Dependant.Gender getSelectedGender() {
        return selectedGender;
    }

    public void setSelectedGender(Dependant.Gender selectedGender) {
        this.selectedGender = selectedGender;
    }

    public Dependant.Gender[] getGenderValues() {
        return genderValues;
    }

    public User getCurrentUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        return (User) externalContext.getSessionMap().get("currentUser");
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public List<Dependant> getCurrentUserDependants() {
        return currentUserDependants;
    }

    public void setCurrentUserDependants(List<Dependant> currentUserDependants) {
        this.currentUserDependants = currentUserDependants;
    }

    public Dependant getNewDependant() {
        return newDependant;
    }

    public void setNewDependant(Dependant newDependant) {
        this.newDependant = newDependant;
    }

    public Dependant getSelectedDependant() {
        return selectedDependant;
    }

    public void setSelectedDependant(Dependant selectedDependant) {
        this.selectedDependant = selectedDependant;
    }

    public void selectDependant(Dependant dependant) {
        this.selectedDependant = dependant;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public void addDependant() {
        try {
            if (currentUser != null) {
                dependantService.addDependantToUser(currentUser, newDependant);
                currentUserDependants.add(newDependant);
                newDependant = new Dependant(); // Reset the dependant object
                PrimeFaces.current().ajax().update("form:dataTable");
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Dependant added successfully!", null));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "User not selected!", null));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error adding dependant: " + e.getMessage(), null));
        }
    }

    public void deleteDependant(Dependant dependant) {
        try {
            // newDependant = dependantService.getDependant(username);
            currentUserDependants.remove(dependant);
            dependantService.deleteDependant(dependant);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Dependant deleted!"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Dependant could not be deleted."));
        }
    }

    public void updateUser(){
        try {
            userService.updateUser(currentUser);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "User updated!"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
        }
    }

    public void updateDependant() {
        try {
            dependantService.updateDependant(selectedDependant);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User updated!"));
            currentUserDependants =  dependantService.getUserDependants(currentUser);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public void searchDependent() {
        currentUserDependants = dependantService.searchDependants(searchQuery, currentUser);
    }

    public void confirmDeleteAllDependants(){
        try {
            dependantService.deleteAllUserDependants(currentUser);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "All users deleted!"));
            currentUserDependants = dependantService.getUserDependants(currentUser); // Refresh the user list (now empty)
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete all users."));
        }
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public int dependantCount() {
        List<Dependant> dependants = dependantService.getUserDependants(currentUser);
        if (dependants == null) {
            return 0;
        }
        return dependants.size();
    }

    public int femaleDependantCount() {
        List<Dependant> dependants = dependantService.getFemaleDependants(currentUser);
        if (dependants == null) {
            return 0;
        }
        return dependants.size();
    }

    public int maleDependantCount() {
        List<Dependant> dependants = dependantService.getMaleDependants(currentUser);
        if (dependants == null) {
            return 0;
        }
        return dependants.size();
    }

    public String ratioOfFemaleToMaleDependants() {
        List<Dependant> dependantsFemale = dependantService.getFemaleDependants(currentUser);
        int a = dependantsFemale.size();
        List<Dependant> dependantsMale = dependantService.getMaleDependants(currentUser);
        int b = dependantsMale.size();

        if (b == 0 || a == 0) {
            return "0";
        }

        int gcd = gcd(a, b);
        int simplifiedA = a / gcd;
        int simplifiedB = b / gcd;

        return simplifiedA + ":" + simplifiedB;
    }

    public void filterDependantsByGender() {
        if (selectedGender == null) {
            currentUserDependants = dependantService.getUserDependants(currentUser);
        } else {
            currentUserDependants = dependantService.getDependantsByGender(currentUser,selectedGender);
        }
    }

}
