package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.dto.NewPassword;
import ru.skypro.homework.model.dto.UserDto;
import ru.skypro.homework.model.entity.UserProfile;
import ru.skypro.homework.repository.UserProfileRepository;
import ru.skypro.homework.security.MyUserDetailsService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder encoder;
    private final ImageServiceImpl imageService;
    private final MyUserDetailsService myUserDetailsService;

    /**
     * метод получения информации о пользователе
     * @return UserDto
     * */
    @Override
    public UserDto getMe() {
        logger.info("the method of getting user information");
        UserDto userDto = new UserDto();
        UserProfile userProfile = userProfileRepository.findByUsername(myUserDetailsService.getUsername());
        userDto.setEmail(myUserDetailsService.getUsername());
        userDto.setPhone(userProfile.getPhone());
        userDto.setFirstName(userProfile.getFirstName());
        userDto.setLastName(userProfile.getLastName());
        userDto.setImage("/users/"+userProfile.getId()+"/getAvatar");
        return userDto;
    }

    /**
     * метод обновления информации о пользователе
     * */
    @Override
    public UserDto updateUser(UserDto userDto) {
        logger.info("the method of updating user information");
        UserProfile userProfile = userProfileRepository.findByUsername(myUserDetailsService.getUsername());
        userProfile.setFirstName(userDto.getFirstName());
        userProfile.setLastName(userDto.getLastName());
        userProfile.setPhone(userDto.getPhone());
        userProfileRepository.save(userProfile);
        return userDto;

    }

    /**
     * метод обновления аватарки пользователя
     * */
    @Override
    public Void updateUserImage(MultipartFile image ){
        logger.info("the method of updating the user's avatar");
        UserProfile userProfile = userProfileRepository.findByUsername(myUserDetailsService.getUsername());
        try {
            imageService.uploadImage(userProfile, image);
            logger.info("avatar updated successfully");
        } catch (IOException e) {
            logger.debug("avatar update error");
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * метод изменения пароля пользователя
     * */
    @Override
    public boolean changePassword(NewPassword newPassword) {
        logger.info("the method of changing the user's password");
        UserProfile userProfile = userProfileRepository.findByUsername(myUserDetailsService.getUsername());
        if (!encoder.matches(newPassword.getNewPassword(), userProfile.getPassword())) {
            userProfile.setPassword(encoder.encode(newPassword.getNewPassword()));
            userProfileRepository.save(userProfile);
            logger.info("password changed successfully");
            return true;
        }
        logger.info("the password has NOT been changed");
        return false;

    }

    /**
     * метод получения аватарки пользователя по username
     * @return String Path
     * */
    @Override
    public String getAvatar(String username) {
        logger.info("the method of getting the user's avatar is called");
        UserProfile userProfile = userProfileRepository.findByUsername(username);
        String path = userProfile.getAvatar().getFilePath();;
        return path;
    }

    /**
     * метод возвращает текущего авторизованного пользователя
     * @return  UserProfile
     * */
    @Override
    public UserProfile getUserProfile( ) {
        logger.info("the method of getting the current user");
        return userProfileRepository.findByUsername(myUserDetailsService.getUsername());
    }


    /**
     * метод поиска пользователя по id
     * @return  UserProfile
     * */
    @Override
    public UserProfile getUserById(Integer id) {
        logger.info("the method of searching for a user by id{}", id);
        return userProfileRepository.findById(id).orElseThrow();
    }


}
