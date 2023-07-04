package ru.skypro.homework.model.entity;


import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "ads")
public class Ads {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserProfile userProfile;

    private String title;
    private String description;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    private Image image;

    @OneToMany(mappedBy = "ads",fetch = FetchType.LAZY)
    private Set<Comment> comments;


    private Integer price;

    public Ads() {
    }

    public Ads(Integer id, UserProfile userProfile, String title, int price) {
        this.id = id;
        this.userProfile = userProfile;
        this.title = title;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public String getTitle() {
        return title;
    }

    public int getPrice() {
        return price;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setComments(Comment comments) {
        this.comments.add(comments);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ads ads = (Ads) o;
        return price == ads.price && Objects.equals(id, ads.id) && Objects.equals(title, ads.title) && Objects.equals(image, ads.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, image, price);
    }

    @Override
    public String toString() {
        return "Ads{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", image=" + image +
                ", price=" + price +
                '}';
    }
}
