package org.pahappa.systems.registrationapp.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User extends Person{

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dependant> dependants;

    public User(){

    }

    private User(String username, String firstname, String lastname, Date dateOfBirth){
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
    }

    public List<Dependant> getDependants() {
        return dependants;
    }

    public void setDependants(List<Dependant> dependants) {
        this.dependants = dependants;
    }

    @Override
    public String toString() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(firstname, user.firstname) &&
                Objects.equals(lastname, user.lastname) &&
                Objects.equals(dateOfBirth, user.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, firstname, lastname, dateOfBirth);
    }

}
