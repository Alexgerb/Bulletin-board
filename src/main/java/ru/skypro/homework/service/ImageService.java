package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.entity.Image;

import java.io.IOException;
import java.util.List;

public interface ImageService {

    void uploadImage(Object o, MultipartFile imageFile) throws IOException;

    Image findImage(Integer id);


}
