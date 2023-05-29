package ru.skypro.homework.model.dto;

import lombok.Data;
import ru.skypro.homework.model.entity.Comment;

import java.util.Collection;

@Data
public class ResponseWrapperCommentDto {
    private Integer count;
    private Collection<Comment> results;
}
