package ru.skypro.homework.model.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String login;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    @OneToOne
    @JoinColumn(name = "avatar_id")
    private Image avatar;


    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private Set<Ads> ads;


    public User() {
    }

    public User(String login, String email, String firstName, String lastName, String phone) {
        this.login = login;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public Set<Ads> getAds() {
        return ads;
    }

    public void setAds(Set<Ads> ads) {
        this.ads = ads;
    }

    public Image getAvatar() {
        return avatar;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(login, user.login) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(phone, user.phone) && Objects.equals(avatar, user.avatar) && Objects.equals(ads, user.ads);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, email, firstName, lastName, phone, avatar, ads);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", avatar=" + avatar +
                ", ads=" + ads +
                '}';
    }
}
