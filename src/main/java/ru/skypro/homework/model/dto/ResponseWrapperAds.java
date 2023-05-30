package ru.skypro.homework.model.dto;

import lombok.Data;
import ru.skypro.homework.model.entity.Ads;

import java.util.Collection;

@Data
public class ResponseWrapperAds {
    private Integer count;
    private Collection<Ads> results;
}
