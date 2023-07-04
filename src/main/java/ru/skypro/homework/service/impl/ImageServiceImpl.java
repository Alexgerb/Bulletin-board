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

    private final ImageRepository imageRepository;
    private final UserProfileRepository userProfileRepository;
    private final AdsRepository adsRepository;

    @Override
    public void uploadImage(Object o, MultipartFile imageFile) throws IOException {
        String dir = getDir(o);
        Path filePath =  Path.of(dir, UUID.randomUUID().toString()+"." + getExtensions(imageFile.getOriginalFilename()));

        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = imageFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 2048);
                BufferedOutputStream bos = new BufferedOutputStream(os, 2048);
        ) {
            bis.transferTo(bos);
        }
        Image image = new Image();
        image.setName(imageFile.getName());
        image.setFilePath(filePath.toString());
        image.setFileSize(imageFile.getSize());
        image.setMediaType(imageFile.getContentType());
        image.setData(imageFile.getBytes());
        imageRepository.save(image);
        addImage(o, image);

    }

    private void addImage(Object o, Image image) {
        if (o instanceof UserProfile) {
            UserProfile userProfile = userProfileRepository.findByUsername(((UserProfile) o).getUsername());
            ((UserProfile) o).setAvatar(image);
            userProfileRepository.save(userProfile);
        } else {
            ((Ads) o).setImage(image);
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
    public List<Object> findAll() {
        return null;
    }

}
