package ru.skypro.homework.model.dto;

import lombok.Data;
import ru.skypro.homework.model.entity.Ads;
import ru.skypro.homework.model.entity.UserProfile;

import java.util.Collection;

@Data
public class ResponseWrapperAds {
    private Integer count;
    private Collection<AdsDto> results;
}
