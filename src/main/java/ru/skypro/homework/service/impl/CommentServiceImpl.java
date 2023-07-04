package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.dto.CommentDto;
import ru.skypro.homework.model.dto.CreateComment;
import ru.skypro.homework.model.dto.ResponseWrapperComment;
import ru.skypro.homework.model.entity.Ads;
import ru.skypro.homework.model.entity.Comment;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserService userService;
    private final AdsService adsService;
    private final CommentRepository commentRepository;


    @Override
    public ResponseWrapperComment getAllComment(Integer id) {
        Ads ads = adsService.getAdsById(id);
        Set<CommentDto> commentSet = new HashSet<>();
        ads.getComments().stream()
                .forEach(comment -> {
                    commentSet.add(mappingComment(comment));
                });

        ResponseWrapperComment responseWrapperComment = new ResponseWrapperComment();
        responseWrapperComment.setCount(commentSet.size());
        responseWrapperComment.setResults(commentSet);
        return responseWrapperComment;
    }

    @Override
    public CommentDto addComment(Integer id, CreateComment createComment) {
        Ads ads = adsService.getAdsById(id);
        Comment comment = new Comment();
        comment.setText(createComment.getText());
        comment.setAds(ads);
        comment.setUserProfile(ads.getUserProfile());
        saveComment(comment);
        CommentDto commentDto = mappingComment(comment);
        return commentDto;
    }

    private void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Integer adId, Integer commentId) {

    }

    @Override
    public CommentDto updateComment(Integer adId, Integer commentId, CommentDto commentDto) {
        return null;
    }

    private CommentDto mappingComment(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setText(comment.getText());
        commentDto.setPk(comment.getId());

        ZoneId zoneId = ZoneId.systemDefault();
        long epoch = comment.getCreatedAt().atZone(zoneId).toEpochSecond();

        commentDto.setCreatedAt(epoch); //дата время
        commentDto.setAuthor(comment.getUserProfile().getId());
        commentDto.setAuthorImage("/users/me/getAvatar");
        commentDto.setAuthorFirstName(comment.getUserProfile().getFirstName());

        return commentDto;
    }
}
