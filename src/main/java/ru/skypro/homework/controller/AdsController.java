package ru.skypro.homework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.dto.CreateAds;
import ru.skypro.homework.model.dto.FullAds;
import ru.skypro.homework.model.dto.ResponseWrapperAds;
import ru.skypro.homework.model.entity.Ads;

import java.io.IOException;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("ads")
public class AdsController {

    @GetMapping()
    public ResponseEntity<ResponseWrapperAds> getAllAds() {
        return ResponseEntity.status(200).build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Ads> addAds(@RequestPart CreateAds createAds,
                                      @RequestPart MultipartFile image) {
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullAds> getAds(@PathVariable("id") Integer id) {
        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAds(@PathVariable("id") Integer id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Ads> updateAds(@PathVariable("id") Integer id,
                                         @RequestBody CreateAds createAds) {
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperAds> getAdsMe() {
        return ResponseEntity.status(200).build();
    }

    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> updateAdsImage(@PathVariable("id") Integer id,@RequestPart MultipartFile image) throws IOException {
        return ResponseEntity.status(200).build();
    }













}
