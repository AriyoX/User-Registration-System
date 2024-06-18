package org.pahappa.systems.registrationapp.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "dependants")
public class Dependant extends Person{

    public enum Gender {
        MALE, FEMALE
    }

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Dependant(String username, String firstname, String lastname, Date dateOfBirth, Gender gender) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    public Dependant() {

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dependant dependant = (Dependant) o;
        return Objects.equals(gender, dependant.gender) && Objects.equals(user, dependant.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gender, user);
    }

    @Override
    public String toString() {
        return "Dependant{" +
                "gender='" + gender + '\'' +
                ", user=" + user +
                '}';
    }
}
