package ru.skypro.homework.model.entity;


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
    private User user;

    private String title;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    private Integer price;

    public Ads() {
    }

    public Ads(Integer id, User user, String title, int price) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public int getPrice() {
        return price;
    }

    public void setUser(User user) {
        this.user = user;
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
        return price == ads.price && Objects.equals(id, ads.id) && Objects.equals(user, ads.user) && Objects.equals(title, ads.title) && Objects.equals(image, ads.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, title, image, price);
    }

    @Override
    public String toString() {
        return "Ads{" +
                "id=" + id +
                ", user=" + user +
                ", title='" + title + '\'' +
                ", price=" + price +
                '}';
    }
}
