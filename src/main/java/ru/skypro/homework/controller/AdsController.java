package ru.skypro.homework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.dto.AdsDto;
import ru.skypro.homework.model.dto.CreateAds;
import ru.skypro.homework.model.dto.FullAds;
import ru.skypro.homework.model.dto.ResponseWrapperAds;
import ru.skypro.homework.model.entity.Ads;
import ru.skypro.homework.service.AdsService;

import java.io.IOException;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("ads")
public class AdsController {

    private final AdsService adsService;

    public AdsController(AdsService adsService) {
        this.adsService = adsService;
    }

    @GetMapping()
    public ResponseEntity<ResponseWrapperAds> getAllAds() {
        return ResponseEntity.ok(adsService.getAllAds());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdsDto> addAds(@RequestPart CreateAds properties,
                                         @RequestPart MultipartFile image) {
        return ResponseEntity.ok(adsService.addAds(properties, image));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullAds> getAds(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(adsService.getAds(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAds(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(adsService.deleteAds(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AdsDto> updateAds(@PathVariable("id") Integer id,
                                         @RequestBody CreateAds createAds) {
        return ResponseEntity.ok(adsService.updateAds(id, createAds));
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperAds> getAdsMe() {
        return ResponseEntity.ok(adsService.getAdsMe());
    }

    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateAdsImage(@PathVariable("id") Integer id,@RequestPart MultipartFile image) throws IOException {
        return ResponseEntity.ok(adsService.updateAdsImage(id, image));
    }













}
