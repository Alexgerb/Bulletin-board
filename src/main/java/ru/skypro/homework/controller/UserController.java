package ru.skypro.homework.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.model.dto.UserDto;

@RestController
@RequestMapping("/users")
public class UserController {



    @PostMapping("/set_password")
    public ResponseEntity<String> setPassword() {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMe() {
        return ResponseEntity.ok(null);
    }

    @PatchMapping("/me")
    public ResponseEntity<UserDto> patchMe(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userDto);
    }




}
