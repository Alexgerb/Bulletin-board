package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.dto.AdsDto;
import ru.skypro.homework.model.dto.CreateAds;
import ru.skypro.homework.model.dto.FullAds;
import ru.skypro.homework.model.dto.ResponseWrapperAds;
import ru.skypro.homework.model.entity.Ads;

import javax.persistence.criteria.CriteriaBuilder;

public interface AdsService {

    ResponseWrapperAds getAllAds();

    ResponseWrapperAds getAllAdsWithPagination(Integer page, Integer size);

    AdsDto addAds(CreateAds createAds, MultipartFile image);

    FullAds getFullAds(Integer id);

    void deleteAds(Integer id);

    AdsDto updateAds(Integer id, CreateAds createAds);

    ResponseWrapperAds getAdsMe();

    void getAdsImage(Integer id, MultipartFile image);

    Ads getAdsById(Integer id);
}
