package ru.skypro.homework.model.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class ResponseWrapperAds {
    private Integer count;
    private Collection<AdsDto> results;
}
