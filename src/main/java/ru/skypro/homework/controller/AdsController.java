package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.dto.AdsDto;
import ru.skypro.homework.model.dto.CreateAds;
import ru.skypro.homework.model.dto.FullAds;
import ru.skypro.homework.model.dto.ResponseWrapperAds;
import ru.skypro.homework.model.entity.Ads;
import ru.skypro.homework.model.entity.Image;
import ru.skypro.homework.model.entity.UserProfile;
import ru.skypro.homework.security.MyUserDetailsService;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdsController {

    private final AdsService adsService;
    private final MyUserDetailsService myUserDetailsService;
    private final ImageService imageService;


    @GetMapping()
    public ResponseEntity<ResponseWrapperAds> getAllAds() {
        return ResponseEntity.ok(adsService.getAllAds());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdsDto> addAds(@RequestPart CreateAds properties,
                                         @RequestPart MultipartFile image) {
        return ResponseEntity.ok(adsService.addAds(properties, image, myUserDetailsService.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullAds> getAds(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(adsService.getAds(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAds(@PathVariable("id") Integer id) {
        adsService.deleteAds(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AdsDto> updateAds(@PathVariable("id") Integer id,
                                         @RequestBody CreateAds createAds) {
        return ResponseEntity.ok(adsService.updateAds(id, createAds));
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperAds> getAdsMe() {
        return ResponseEntity.ok(adsService.getAdsMe(myUserDetailsService.getUsername()));
    }

    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateAdsImage(@PathVariable("id") Integer id,@RequestPart MultipartFile image) throws IOException {
        adsService.getAdsImage(id, image);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/image/{id}")
    public void getMeImage(@PathVariable("id") Integer id,
                           HttpServletResponse response)throws IOException {
        Image image = imageService.findImage(id);
        Path path = Path.of(image.getFilePath());
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream()) {
            response.setStatus(200);
            is.transferTo(os);
        }

    }















}
