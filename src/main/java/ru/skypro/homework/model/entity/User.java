package ru.skypro.homework.model.entity;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    @Lob
    private byte[] avatar;  //image

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private Set<Ads> ads;


    public User() {
    }

    public User(String email, String firstName, String lastName, String phone) {
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

    public byte[] getAvatar() {
        return avatar;
    }

    public Set<Ads> getAds() {
        return ads;
    }



    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public void setAds(Set<Ads> ads) {
        this.ads = ads;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(phone, user.phone) && Arrays.equals(avatar, user.avatar) && Objects.equals(ads, user.ads);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, email, firstName, lastName, phone, ads);
        result = 31 * result + Arrays.hashCode(avatar);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", avatar=" + Arrays.toString(avatar) +
                '}';
    }
}
