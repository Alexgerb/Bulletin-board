package ru.skypro.homework.service;

import ru.skypro.homework.model.dto.CommentDto;
import ru.skypro.homework.model.dto.CreateComment;
import ru.skypro.homework.model.dto.ResponseWrapperComment;

public interface CommentService {
    ResponseWrapperComment getAllComment(Integer id);

    CommentDto addComment(Integer id, CreateComment comment, String username);

    void deleteComment(Integer adId, Integer commentId);

    CommentDto updateComment(Integer adId, Integer commentId, CommentDto commentDto);
}
