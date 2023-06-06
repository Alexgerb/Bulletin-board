package ru.skypro.homework.model.dto;

import lombok.Data;

@Data
public class UserDto {
    private Integer id;
    private String email;
    private String FirstName;
    private String LastName;
    private String phone;
    private String image;

}
