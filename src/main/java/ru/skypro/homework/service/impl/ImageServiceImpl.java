package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.entity.Ads;
import ru.skypro.homework.model.entity.Image;
import ru.skypro.homework.model.entity.UserProfile;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserProfileRepository;
import ru.skypro.homework.service.ImageService;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    @Value("${path.to.images.folder}")
    private String imagesDir;

    @Value("${path.to.default.folder}")
    private String defaultDir;



    private final ImageRepository imageRepository;
    private final UserProfileRepository userProfileRepository;
    private final AdsRepository adsRepository;

    /**
     * метод загрузки картинки
     * */
    @Override
    public void uploadImage(Object o, MultipartFile imageFile) throws IOException {
        logger.info("the image loading method");
        String dir = getDir(o);
        String uuid = UUID.randomUUID().toString();
        Path filePath =  Path.of(dir, uuid+"." + getExtensions(imageFile.getOriginalFilename()));

        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (
                InputStream is = imageFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is);
                BufferedOutputStream bos = new BufferedOutputStream(os);
        ) {
            bis.transferTo(bos);
            logger.info("the download was successful");
        }
        Image image = new Image();
        image.setName(uuid);
        image.setFilePath(filePath.toString());
        imageRepository.save(image);
        addImage(o, image);

    }

    /**
     * метод сохранения картинки в зависимости от сущности объекта UserProfile или Ads
     * */
    private void addImage(Object o, Image image) {
        if (o instanceof UserProfile) {
            ((UserProfile) o).setAvatar(image);
            userProfileRepository.save((UserProfile) o);
        } else {
            ((Ads) o).setImage(image);
            adsRepository.save((Ads) o);
        }
    }





    /**
     * метод возвращает путь к файлу в зависимости от сущности объекта UserProfile или Ads
     * */
    private String getDir(Object o) {
        String dir = o instanceof UserProfile ? avatarsDir : imagesDir;
        return dir;
    }


    /**
     * метод возвращает расширение файла
     * */
    private String getExtensions(String originalFilename) {
        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
    }

    /**
     * метод поиска картинки в БД по id
     * */
    @Override
    public Image findImage(Integer id) {
        logger.info("the method of image search by id {}", id);
        return imageRepository.findImageById(id);

    }

    /**
     * метод возвращает дефолтную аватарку пользователю, если нет загруженной фотографии
     * */
    @Override
    public Image addDefaultAvatar() {
        Image image = imageRepository.findImageByFilePath(defaultDir);
        if (image == null) {
            return imageRepository.save(new Image("default", defaultDir));
        }
        return image;
    }

}
