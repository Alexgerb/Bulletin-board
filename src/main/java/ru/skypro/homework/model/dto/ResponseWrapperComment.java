package ru.skypro.homework.model.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class ResponseWrapperComment {
    private Integer count;
    private Collection<CommentDto> results;
}
