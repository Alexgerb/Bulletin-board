package ru.skypro.homework.model.entity;


import javax.persistence.*;
import java.util.Objects;

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

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

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

    public UserProfile getUser() {
        return userProfile;
    }

    public String getTitle() {
        return title;
    }

    public int getPrice() {
        return price;
    }

    public void setUser(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ads ads = (Ads) o;
        return price == ads.price && Objects.equals(id, ads.id) && Objects.equals(userProfile, ads.userProfile) && Objects.equals(title, ads.title) && Objects.equals(image, ads.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userProfile, title, image, price);
    }

    @Override
    public String toString() {
        return "Ads{" +
                "id=" + id +
                ", user=" + userProfile +
                ", title='" + title + '\'' +
                ", price=" + price +
                '}';
    }
}
