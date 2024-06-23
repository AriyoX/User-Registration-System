package org.pahappa.systems.registrationapp.views;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "mainmenu")
@ApplicationScoped
public class MainMenuBean{

    public List<MenuItem> getMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Register Dependant", "/pages/dependants/register-dependant"));
        menuItems.add(new MenuItem("View Your Dependants", "/pages/dependants/view-user-dependants.xhtml"));
        return menuItems;
    }

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