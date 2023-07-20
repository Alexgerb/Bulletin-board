package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
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

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    @Value("${path.to.images.folder}")
    private String imagesDir;

    @Value("${path.to.default.folder}")
    private String defaultDir;



    private final ImageRepository imageRepository;
    private final UserProfileRepository userProfileRepository;
    private final AdsRepository adsRepository;

    @Override
    public void uploadImage(Object o, MultipartFile imageFile) throws IOException {
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
        }
        Image image = new Image();
        image.setName(uuid);
        image.setFilePath(filePath.toString());
        imageRepository.save(image);
        addImage(o, image);

    }

    private void addImage(Object o, Image image) {
        if (o instanceof UserProfile) {
            ((UserProfile) o).setAvatar(image);
            userProfileRepository.save((UserProfile) o);
        } else {
            ((Ads) o).setImage(image);
            adsRepository.save((Ads) o);
        }
    }





    private String getDir(Object o) {
        String dir = o instanceof UserProfile ? avatarsDir : imagesDir;
        return dir;
    }


    private String getExtensions(String originalFilename) {
        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
    }

    @Override
    public Image findImage(Integer id) {
        return imageRepository.findImageById(id);

    }

    @Override
    public Image addDefaultAvatar() {
        Image image = imageRepository.findImageByFilePath(defaultDir);
        if (image == null) {
            return imageRepository.save(new Image("default", defaultDir));
        }
        return image;
    }

}
