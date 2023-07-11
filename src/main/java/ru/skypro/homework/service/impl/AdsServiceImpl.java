package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.dto.AdsDto;
import ru.skypro.homework.model.dto.CreateAds;
import ru.skypro.homework.model.dto.FullAds;
import ru.skypro.homework.model.dto.ResponseWrapperAds;
import ru.skypro.homework.model.entity.Ads;
import ru.skypro.homework.model.entity.UserProfile;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {

    private final ImageService imageService;
    private final AdsRepository adsRepository;
    private final UserService userService;

    private final Integer COUNT_PAGE = 2;
    private Integer page=0;

    @Override
    public ResponseWrapperAds getAllAds() {
        if (checkCountAds()) {
            return responseWrapperAdsWithPadding();
        }
        return responseWrapperAdsAll();
    }

    private ResponseWrapperAds responseWrapperAdsWithPadding() {
        Integer countAds = adsRepository.findAll().size();
        page = page + 1 > countAds-COUNT_PAGE ? 1 : page + 1;
        PageRequest pageRequest = PageRequest.of(page - 1, COUNT_PAGE);
        return getAds(adsRepository.findAll(pageRequest).getContent());

    }

    private ResponseWrapperAds getAds(List<Ads> content) {
        ResponseWrapperAds responseWrapperAds = new ResponseWrapperAds();
        List<AdsDto> adsDtoList = new ArrayList<>();
        content.stream().sorted(Comparator.comparing(Ads::getId))
                .forEach(ads -> {
                    AdsDto adsDto = adsMapping(ads);
                    adsDtoList.add(adsDto);
                });
        responseWrapperAds.setCount(adsDtoList.size());
        responseWrapperAds.setResults(adsDtoList);
        return responseWrapperAds;
    }

    private ResponseWrapperAds responseWrapperAdsAll() {
        return getAds(adsRepository.findAll());
    }

    private boolean checkCountAds() {
        if (adsRepository.findAll().stream().count() > COUNT_PAGE) {
            return true;
        }
        return false;
    }

    @Override
    public AdsDto addAds(CreateAds createAds, MultipartFile image, String username) {
        UserProfile userProfile = userService.getUserProfile(username);
        Ads ads = createNewAds(createAds, image, userProfile);
        userProfile.setAds(ads);
        return adsMapping(ads);
    }

    private Ads createNewAds(CreateAds createAds, MultipartFile image, UserProfile userProfile) {
        Ads ads = new Ads();
        ads.setPrice(createAds.getPrice());
        ads.setTitle(createAds.getTitle());
        ads.setDescription(createAds.getDescription());
        ads.setUserProfile(userProfile);
        try {
            imageService.uploadImage(ads, image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ads;
    }

    private AdsDto adsMapping(Ads ads) {
        AdsDto adsDto = new AdsDto();
        adsDto.setPrice(ads.getPrice());
        adsDto.setTitle(ads.getTitle());
        adsDto.setImage("/ads/image/" + ads.getImage().getId());
        adsDto.setPk(ads.getId());
        adsDto.setAuthor(ads.getUserProfile().getId());
        saveAds(ads);
        return adsDto;
    }


    private void saveAds(Ads ads) {
        adsRepository.save(ads);
    }

    @Override
    public FullAds getFullAds(Integer id) {
        Ads ads = adsRepository.findById(id).orElse(new Ads());
        FullAds fullAds = new FullAds();
        fullAds.setPk(ads.getId());
        fullAds.setEmail(ads.getUserProfile().getEmail());
        fullAds.setImage("/ads/image/" + ads.getImage().getId());
        fullAds.setPhone(ads.getUserProfile().getPhone());
        fullAds.setDescription(ads.getDescription());
        fullAds.setPrice(ads.getPrice());
        fullAds.setTitle(ads.getTitle());
        fullAds.setAuthorFirstName(ads.getUserProfile().getFirstName());
        fullAds.setAuthorLastName(ads.getUserProfile().getLastName());
        return fullAds;
    }

    @Override
    public void deleteAds(Integer id) {
        Ads ads = adsRepository.findById(id).orElseThrow();
        File image = new File(ads.getImage().getFilePath());
        image.delete();
        adsRepository.delete(ads);
    }

    @Override
    public AdsDto updateAds(Integer id, CreateAds createAds) {
        Ads ads = adsRepository.findById(id).orElseThrow();
        ads.setDescription(createAds.getDescription());
        ads.setTitle(createAds.getTitle());
        ads.setPrice(createAds.getPrice());
        saveAds(ads);
        return adsMapping(ads);
    }

    @Override
    public ResponseWrapperAds getAdsMe(String username) {
        UserProfile userProfile = userService.getUserProfile(username);
        Set<AdsDto> adsDtoSet = new HashSet<>();
        userProfile.getAds().stream()
                .forEach(ads -> {
                    AdsDto adsDto = adsMapping(ads);
                    adsDtoSet.add(adsDto);
                });

        ResponseWrapperAds responseWrapperAds = new ResponseWrapperAds();

        responseWrapperAds.setCount(adsDtoSet.size());
        responseWrapperAds.setResults(adsDtoSet);
        return responseWrapperAds;
    }

    @Override
    public void getAdsImage(Integer id, MultipartFile image) {
        Ads ads = adsRepository.findById(id).orElseThrow();
        try {
            imageService.uploadImage(ads, image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Ads getAdsById(Integer id) {
        return adsRepository.findById(id).orElseThrow();
    }
}
