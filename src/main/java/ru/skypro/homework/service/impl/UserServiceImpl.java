package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.model.dto.NewPassword;
import ru.skypro.homework.model.dto.UserDto;
import ru.skypro.homework.repository.UserProfileRepository;
import ru.skypro.homework.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserProfileRepository userProfileRepository;

    public UserServiceImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }


    @Override
    public void setPassword(NewPassword newPassword) {

    }

    @Override
    public UserDto getMe() {
        return null;
    }

    @Override
    public UserDto updateUser() {
        return null;
    }

    @Override
    public Void updateUserImage() {
        return null;
    }
}
