package ru.skypro.homework.service;

import ru.skypro.homework.model.dto.CommentDto;
import ru.skypro.homework.model.dto.CreateComment;
import ru.skypro.homework.model.dto.ResponseWrapperComment;

public interface CommentService {
    ResponseWrapperComment getAllComment();

    CreateComment addComment(Integer id, CreateComment comment);

    void deleteComment(Integer adId, Integer commentId);

    CommentDto updateComment(Integer adId, Integer commentId, CommentDto commentDto);
}
