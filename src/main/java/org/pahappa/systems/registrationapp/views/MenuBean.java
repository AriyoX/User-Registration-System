package org.pahappa.systems.registrationapp.views;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "menu")
@RequestScoped
public class MenuBean {

    public List<MenuItem> getMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Register User", "register"));
        menuItems.add(new MenuItem("Display all users", "display"));
        menuItems.add(new MenuItem("Update User Details", "update"));
        menuItems.add(new MenuItem("Search for Users", "search"));
        menuItems.add(new MenuItem("Delete All Users", "delete-all"));
        menuItems.add(new MenuItem("Delete a Specific User", "delete-specific"));
        return menuItems;
    }

    // Inner class to represent a menu item
    public static class MenuItem {
        private final String label;
        private final String outcome;

        public MenuItem(String label, String outcome) {
            this.label = label;
            this.outcome = outcome;
        }

        public String getOutcome() {
            return outcome;
        }

        public String getLabel() {
            return label;
        }

    }
}