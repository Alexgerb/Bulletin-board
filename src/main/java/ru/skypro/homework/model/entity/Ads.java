package ru.skypro.homework.model.entity;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ads")
public class Ads {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer user;

    private String title;

    private String image;

    private int price;

    public Ads() {
    }

    public Ads(Integer id, Integer user, String title, String image, int price) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.image = image;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getPrice() {
        return price;
    }

    public Integer getUser() {
        return user;
    }

    public String getImage() {
        return image;
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
                ", image='" + image + '\'' +
                ", price=" + price +
                '}';
    }
}
