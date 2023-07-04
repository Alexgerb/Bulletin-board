package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.dto.AdsDto;
import ru.skypro.homework.model.dto.CreateAds;
import ru.skypro.homework.model.dto.FullAds;
import ru.skypro.homework.model.dto.ResponseWrapperAds;
import ru.skypro.homework.model.entity.Ads;

public interface AdsService {

    ResponseWrapperAds getAllAds();

    AdsDto addAds(CreateAds createAds, MultipartFile image, String username);

    FullAds getAds(Integer id);

    Void deleteAds(Integer id);

    AdsDto updateAds(Integer id, CreateAds createAds);

    ResponseWrapperAds getAdsMe(String username);

    Void getAdsImage(Integer id, MultipartFile image);

    Ads getAdsById(Integer id);
}
