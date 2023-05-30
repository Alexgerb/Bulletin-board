package ru.skypro.homework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.dto.NewPassword;
import ru.skypro.homework.model.dto.UserDto;

import java.io.IOException;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("users")
public class UserController {

    @PostMapping("/set_password")
    public ResponseEntity<NewPassword> setPassword() {
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMe() {
        return ResponseEntity.status(200).build();
    }

    @PatchMapping("/me")
    public ResponseEntity<UserDto> patchMe(@RequestBody UserDto userDto) {
        return ResponseEntity.status(200).build();
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUserImage(@RequestParam MultipartFile image) throws IOException {
        return ResponseEntity.status(200).build();
    }


}
