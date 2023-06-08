package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.dto.AdsDto;
import ru.skypro.homework.model.dto.CreateAds;
import ru.skypro.homework.model.dto.FullAds;
import ru.skypro.homework.model.dto.ResponseWrapperAds;

public interface AdsService {

    ResponseWrapperAds getAllAds();

    AdsDto addAds(CreateAds createAds, MultipartFile image);

    FullAds getAds(Integer id);

    Void deleteAds(Integer id);

    AdsDto updateAds(Integer id, CreateAds createAds);

    ResponseWrapperAds getAdsMe();

    Void updateAdsImage(Integer id, MultipartFile image);
}
