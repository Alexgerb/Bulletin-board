package ru.skypro.homework.model.dto;

import lombok.Data;
import ru.skypro.homework.model.entity.Comment;

import java.util.Collection;

@Data
public class ResponseWrapperComment {
    private Integer count;
    private Collection<Comment> results;
}
