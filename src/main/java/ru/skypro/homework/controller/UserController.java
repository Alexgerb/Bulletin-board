package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.dto.NewPassword;
import ru.skypro.homework.model.dto.UserDto;
import ru.skypro.homework.model.entity.UserProfile;
import ru.skypro.homework.service.UserService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/set_password")
    public ResponseEntity<NewPassword> setPassword(@RequestBody NewPassword newPassword) {
        if (userService.changePassword(newPassword)) {
            return ResponseEntity.ok(newPassword);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMe() throws IOException {
        return ResponseEntity.ok(userService.getMe());
    }

    @GetMapping("/{id}/getAvatar")
    public void getMeImage(@PathVariable("id") Integer id,
                           HttpServletResponse response)throws IOException {
        UserProfile userProfile = userService.getUserById(id);
        Path path = Path.of(userService.getAvatar(userProfile.getUsername()));
            try (InputStream is = Files.newInputStream(path);
                 OutputStream os = response.getOutputStream()) {
                response.setStatus(200);
                is.transferTo(os);
            }

    }

    @PatchMapping("/me")
    public ResponseEntity<UserDto> patchMe(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(userDto));

    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUserImage(@RequestPart MultipartFile image) throws IOException {
        return ResponseEntity.ok(userService.updateUserImage(image));
    }


}



