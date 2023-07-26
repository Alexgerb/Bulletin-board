package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.model.dto.*;
import ru.skypro.homework.model.entity.Ads;
import ru.skypro.homework.model.entity.Role;
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

    private static final Logger logger = LoggerFactory.getLogger(AdsServiceImpl.class);

    private final ImageService imageService;
    private final AdsRepository adsRepository;
    private final UserService userService;

    /**
     * получить все объявления
     * */
    @Override
    public ResponseWrapperAds getAllAds() {
        logger.info("Method of getting all ads");
        return responseWrapperAdsAll();
    }

    /***
     * получить все объявления с пагинацией
     * */
    @Override
    public ResponseWrapperAds getAllAdsWithPagination(Integer page, Integer size) {
        logger.info("The method of getting all the ads from the pagination");
        return responseWrapperAdsWithPagination(page, size);
    }

    /**
     * получить wrapper с разбивкой по страницам
     * */
    private ResponseWrapperAds responseWrapperAdsWithPagination(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return getAds(adsRepository.findAll(pageRequest).getContent());

    }
    /**
     * метод возвращает wrapper с пагинацией объявления
     * */
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

    /**
     * метод возвращает wrapper всех объявлений
     * @return  ResponseWrapperAds
     * */
    private ResponseWrapperAds responseWrapperAdsAll() {
        return getAds(adsRepository.findAll());
    }

    /**
     * метод добавления объявления
     * @return AdsDto
     * */
    @Override
    public AdsDto addAds(CreateAds createAds, MultipartFile image) {
        logger.info("The method of adding an ad");
        UserProfile userProfile = userService.getUserProfile();
        Ads ads = createNewAds(createAds, image, userProfile);
        userProfile.setAds(ads);
        return adsMapping(ads);
    }

    /**
     * метод создает новое объявление
     * @return Ads
     * */
    private Ads createNewAds(CreateAds createAds, MultipartFile image, UserProfile userProfile) {
        logger.info("the method of adding a new ad");
        Ads ads = new Ads();
        ads.setPrice(createAds.getPrice());
        ads.setTitle(createAds.getTitle());
        ads.setDescription(createAds.getDescription());
        ads.setUserProfile(userProfile);
        try {
            imageService.uploadImage(ads, image);
            logger.debug("file {} uploaded successfully ", image.getName());
        } catch (IOException e) {
            logger.debug("file {} upload error", image.getName());
            throw new RuntimeException(e);
        }
        return ads;
    }

    /**
     * метод преобразования Ads в AdsDto
     * */
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


    /**
     * метод сохранения в БД нового объявления
     * */
    private void saveAds(Ads ads) {
        adsRepository.save(ads);
    }

    /**
     * метод возвращает подробную информацию об объявлении
     * @return FullAds
     * */
    @Override
    public FullAds getFullAds(Integer id) {
        logger.info("The method of getting detailed information about the ad");
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

    /**
     * метод удаления объявления
     * */
    @Override
    public void deleteAds(Integer id) {
        logger.info("the method of deleting the ad {}", id);
        Ads ads = adsRepository.findById(id).orElseThrow();
        UserProfile userProfile = userService.getUserProfile();
        if (checkAccess(ads, userProfile)) {
            File image = new File(ads.getImage().getFilePath());
            image.delete();
            adsRepository.delete(ads);
            logger.info("ad deleted successfully id{}", id);
        } else {
            logger.debug("Insufficient rights to delete id{}", id);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }


    /**
     * метод обновления объявления
     * @return AdsDto
     * */
    @Override
    public AdsDto updateAds(Integer id, CreateAds createAds) {
        logger.info("the method of updating the ad {}", id);
        Ads ads = adsRepository.findById(id).orElseThrow();
        UserProfile userProfile = userService.getUserProfile();
        if (checkAccess(ads, userProfile)) {
            ads.setDescription(createAds.getDescription());
            ads.setTitle(createAds.getTitle());
            ads.setPrice(createAds.getPrice());
            saveAds(ads);
            logger.info("ad update successfully id{}", id);
            return adsMapping(ads);
        } else {
            logger.debug("Insufficient rights to update id{}", id);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * метод получения всех объявлений пользователя
     * @return ResponseWrapperAds
     * */
    @Override
    public ResponseWrapperAds getAdsMe() {
        logger.info("the method of getting all the user's ads");
        UserProfile userProfile = userService.getUserProfile();
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

    /**
     * метод получения картинки объявления
     * */
    @Override
    public void getAdsImage(Integer id, MultipartFile image) {
        logger.info("the method of getting the ad image is called");
        Ads ads = adsRepository.findById(id).orElseThrow();
        UserProfile userProfile = userService.getUserProfile();
        if (checkAccess(ads, userProfile)) {
            try {
                imageService.uploadImage(ads, image);
                logger.info("picture uploaded");
            } catch (IOException e) {
                logger.debug("image loading error");
                throw new RuntimeException(e);
            }
        } else {
            logger.debug("Insufficient rights to update id{}", id);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

    }

    /**
     * метод получения объявления по id
     * @return Ads
     * */
    @Override
    public Ads getAdsById(Integer id) {
        return adsRepository.findById(id).orElseThrow();
    }

    /**
     * метод проверяет принадлежность объявления пользователю либо роль Администратора
     * @return  true or false
     * */
    private boolean checkAccess(Ads ads, UserProfile userProfile) {
        Set<Role> roles = userProfile.getRoles();
        if (roles.stream().anyMatch(role -> role.getName().equals(RoleEnum.ADMIN.toString())) || userProfile.getId() == ads.getUserProfile().getId()) {
            return true;
        }
        return false;
    }
}
