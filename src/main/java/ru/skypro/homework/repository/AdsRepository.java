package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.entity.Ads;
import ru.skypro.homework.model.entity.UserProfile;

import java.util.Set;

@Repository
public interface AdsRepository extends JpaRepository<Ads, Integer> {
    @Query(value = "select a from Ads a where a.userProfile = :user", nativeQuery = true)
    Set<Ads> findAllByUserId(@Param("user") UserProfile user);

    //Ads findAdsById(Integer id);
}
