package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.dto.AdsDto;
import ru.skypro.homework.model.dto.CreateAds;
import ru.skypro.homework.model.dto.FullAds;
import ru.skypro.homework.model.dto.ResponseWrapperAds;
import ru.skypro.homework.service.AdsService;

@Service
public class AdsServiceImpl implements AdsService {
    @Override
    public ResponseWrapperAds getAllAds() {
        return null;
    }

    @Override
    public AdsDto addAds(CreateAds createAds, MultipartFile image) {
        return null;
    }

    @Override
    public FullAds getAds(Integer id) {
        return null;
    }

    @Override
    public Void deleteAds(Integer id) {
        return null;
    }

    @Override
    public AdsDto updateAds(Integer id, CreateAds createAds) {
        return null;
    }

    @Override
    public ResponseWrapperAds getAdsMe() {
        return null;
    }

    @Override
    public Void updateAdsImage(Integer id, MultipartFile image) {
        return null;
    }
}
