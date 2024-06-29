package org.pahappa.systems.registrationapp.views;

import org.pahappa.systems.registrationapp.dao.UserDAO;
import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.DependantService;
import org.pahappa.systems.registrationapp.services.UserService;
import org.primefaces.PrimeFaces;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.legend.LegendLabel;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean(name = "adminBean")
@ViewScoped
public class AdminBean implements Serializable {
    private static final long serialVersionUID = 2L;
    private long user_id;
    private Dependant dependant = new Dependant();
    private final DependantService dependantService = new DependantService();
    private User selectedUser;
    private Dependant.Gender[] genderValues;
    private List<Dependant> dependants;
    private User currentUser;
    private String searchQuery;
    private final UserService userService = new UserService();
    private String username;
    private final UserDAO userDAO = new UserDAO();
    private User user = new User();
    private Dependant.Gender selectedGender;
    private Dependant selectedDependant;
    private PieChartModel pieModel;
    private BarChartModel barModel;
    private User admin = new User();
    private List<User> admins;

    @PostConstruct
    public void init() {
        currentUser = getCurrentUser();
        createPieModel();
        createBarModel();
        try {
            if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
                FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                externalContext.getSessionMap().put("currentUser", null);
                externalContext.redirect(externalContext.getRequestContextPath() + "/pages/login/login.xhtml?faces-redirect=true");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        genderValues = Dependant.Gender.values();
        dependants = dependantService.getAllDependants();
        selectedDependant = new Dependant();
        admins = userService.getAdminUsers();
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
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

    public Dependant.Gender getSelectedGender() {
        return selectedGender;
    }

    public void setSelectedGender(Dependant.Gender selectedGender) {
        this.selectedGender = selectedGender;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Dependant> getDependants() {
        return dependants;
    }

    public Dependant.Gender[] getGenderValues() {
        return genderValues;
    }

    public void setDependant(Dependant dependant) {
        this.dependant = dependant;
    }

    public Dependant getDependant() {
        return dependant;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public void selectUser(User user) {
        this.selectedUser = user;
    }

    public void addDependant() {
        try {
            if (selectedUser != null) {
                dependantService.addDependantToUser(selectedUser, dependant);
                dependant = new Dependant(); // Reset the dependant object
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

    public void registerDependant(){
        User user = userService.getUser(username);
        try {
            if (user != null) {
                if (dependant.getFirstname().isBlank() || dependant.getLastname().isBlank()) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Name cannot be blank!", null));
                } else {
                    dependantService.addDependantToUser(user, dependant);
                    dependants.add(dependant);
                    dependants = dependantService.getAllDependants();
                    dependant = new Dependant(); // Reset the dependant object
                    PrimeFaces.current().ajax().update("form:dataTable");
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Dependant added successfully!", null));

                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "User: " + username + " does not exist.", null));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error adding dependant: " + e.getMessage(), null));
        }
    }

    public List<Dependant> getSelectedUserDependants() {
        return dependantService.getDependantsByUserId(user_id);
    }

    public void deleteDependant(Dependant dependant) {
        try {
            // dependant = dependantService.getDependant(username);
            dependants.remove(dependant);
            dependantService.deleteDependant(dependant);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Dependant deleted!"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Dependant could not be deleted."));
        }
    }

    public User getCurrentUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        return (User) externalContext.getSessionMap().get("currentUser");
    }

    public void searchDependent() {
        dependants = dependantService.searchDependants(searchQuery);
    }

    public void updateDependant() {
        try {
            if (selectedDependant.getFirstname().isBlank() || selectedDependant.getLastname().isBlank()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Name cannot be blank.", null));
            } else {
                dependantService.updateDependant(selectedDependant);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User updated!"));
                dependants =  dependantService.getAllDependants();
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public void confirmDeleteAllDependants(){
        try {
            dependantService.deleteAllDependants();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "All users deleted!"));
            dependants = dependantService.getAllDependants(); // Refresh the user list (now empty)
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete all users."));
        }
    }

    public int userCount(){
        List<User> users = userService.getAllUsers();
        if (users == null){
            return 0;
        }
        return users.size();
    }

    public int dependantCount(){
        List<Dependant> dependants = dependantService.getAllDependants();
        if (dependants == null){
            return 0;
        }
        return dependants.size();
    }

    public int usersWithDependantsCount(){
        List<User> users = userDAO.getUsersWithActiveDependants();
        if (users == null){
            return 0;
        }
        return users.size();
    }

    public int usersWithoutDependantsCount(){
        List<User> users = userDAO.getUsersWithoutDependants();
        if (users == null){
            return 0;
        }
        return users.size();
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public String ratioOfUsersWithDependantsToUsersWithout() {
        List<User> usersWithActiveDependants = userDAO.getUsersWithActiveDependants();
        int a = usersWithActiveDependants.size();

        List<User> usersWithoutActiveDependants = userDAO.getUsersWithoutDependants();
        int b =  usersWithoutActiveDependants.size();
        if (b == 0 || a == 0) {
            return "0";
        }

        int gcd = gcd(a, b);
        int simplifiedA = a / gcd;
        int simplifiedB = b / gcd;

        return simplifiedA + ":" + simplifiedB;
    }

    public int femaleDependantsCount(){
        List<Dependant> dependantsFemale = dependantService.getFemaleDependants();
        if (dependantsFemale == null){
            return 0;
        }
        return dependantsFemale.size();
    }

    public int maleDependantsCount(){
        List<Dependant> dependantsMale = dependantService.getMaleDependants();
        if (dependantsMale == null){
            return 0;
        }
        return dependantsMale.size();
    }

    public String ratioOfFemaleToMaleDependants() {
        List<Dependant> dependantsFemale = dependantService.getFemaleDependants();
        int a = dependantsFemale.size();
        List<Dependant> dependantsMale = dependantService.getMaleDependants();
        int b = dependantsMale.size();

        if (b == 0 || a == 0) {
            return "0";
        }

        int gcd = gcd(a, b);
        int simplifiedA = a / gcd;
        int simplifiedB = b / gcd;

        return simplifiedA + ":" + simplifiedB;

    }

    public int activeDependantsCount(User user){
        List<Dependant> dependants = dependantService.getUserDependants(user);
        if (dependants == null){
            return 0;
        }
        return dependants.size();
    }

    public void filterDependantsByGender() {
        if (selectedGender == null) {
            dependants = dependantService.getAllDependants();
        } else {
            dependants = dependantService.getDependantsByGender(selectedGender);
        }
    }

    public void createPieModel(){
        pieModel = new PieChartModel();
        ChartData data = new ChartData();

        PieChartDataSet dataSet = new PieChartDataSet();
        List<Number> values = new ArrayList<>();
        values.add(femaleDependantsCount());
        values.add(maleDependantsCount());
        dataSet.setData(values);

        List<String> bgColors = new ArrayList<>();
        bgColors.add("rgb(255, 99, 132)");
        bgColors.add("rgb(54, 162, 235)");
        dataSet.setBackgroundColor(bgColors);

        data.addChartDataSet(dataSet);
        List<String> labels = new ArrayList<>();
        labels.add("Female");
        labels.add("Male");
        data.setLabels(labels);

        pieModel.setData(data);
    }

    public PieChartModel getPieModel() {
        return pieModel;
    }

    public void createBarModel(){
        barModel = new BarChartModel();
        ChartData data = new ChartData();

        BarChartDataSet barDataSet = new BarChartDataSet();
        barDataSet.setLabel("Users");

        List<Number> values = new ArrayList<>();
        values.add(usersWithDependantsCount());
        values.add(usersWithoutDependantsCount());
        barDataSet.setData(values);

        List<String> bgColor = new ArrayList<>();
        bgColor.add("rgba(255, 99, 132, 0.2)");
        bgColor.add("rgba(255, 159, 64, 0.2)");
        barDataSet.setBackgroundColor(bgColor);

        List<String> borderColor = new ArrayList<>();
        borderColor.add("rgb(255, 99, 132)");
        borderColor.add("rgb(255, 159, 64)");
        barDataSet.setBorderColor(borderColor);
        barDataSet.setBorderWidth(1);

        data.addChartDataSet(barDataSet);

        List<String> labels = new ArrayList<>();
        labels.add("Users with dependants");
        labels.add("Users without dependants");
        data.setLabels(labels);

        //Data
        barModel.setData(data);

        //Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setStepSize(1);
        linearAxes.setTicks(ticks);
        linearAxes.setBeginAtZero(true);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Users With and Without Dependants");
        options.setTitle(title);

        Legend legend = new Legend();
        legend.setDisplay(true);
        legend.setPosition("top");
        LegendLabel legendLabels = new LegendLabel();
        legendLabels.setFontStyle("bold");
        legendLabels.setFontColor("#2980B9");
        legendLabels.setFontSize(24);
        legend.setLabels(legendLabels);
        options.setLegend(legend);

        barModel.setOptions(options);
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public List<User> userList(String query){
        return userService.searchUsers(query);
    }

    public void registerAdmin(){
        try {
            if (admin.getFirstname().isBlank() || admin.getLastname().isBlank() || admin.getUsername().isBlank()){
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Names cannot be blank", null));
                return;
            }
            if (admins.size() > 5){
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "There cannot be more than 5 admins in the system.", null));
                return;
            }
            admin.setFirstname(admin.getFirstname().trim());
            admin.setLastname(admin.getLastname().trim());
            admin.setUsername(admin.getUsername().trim());
            admin.setDateOfBirth(new Date());
            admin.setPassword("@dmin");
            admin.setRole("ADMIN");
            admin.setEmail(admin.getEmail().trim());
            userService.registerUser(admin);
            admins.add(admin);
            admin = new User();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", null));
        } catch (Exception e){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
    }

    public int adminsCount(){
        return admins.size();
    }

}
