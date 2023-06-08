package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.model.dto.CommentDto;
import ru.skypro.homework.model.dto.CreateComment;
import ru.skypro.homework.model.dto.ResponseWrapperComment;
import ru.skypro.homework.service.CommentService;
@Service
public class CommentServiceImpl implements CommentService {
    @Override
    public ResponseWrapperComment getAllComment() {
        return null;
    }

    @Override
    public CreateComment addComment(Integer id, CreateComment comment) {
        return null;
    }

    @Override
    public void deleteComment(Integer adId, Integer commentId) {

    }

    @Override
    public CommentDto updateComment(Integer adId, Integer commentId, CommentDto commentDto) {
        return null;
    }
}
