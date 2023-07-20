package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.model.dto.CommentDto;
import ru.skypro.homework.model.dto.CreateComment;
import ru.skypro.homework.model.dto.ResponseWrapperComment;
import ru.skypro.homework.model.dto.RoleEnum;
import ru.skypro.homework.model.entity.Ads;
import ru.skypro.homework.model.entity.Comment;
import ru.skypro.homework.model.entity.Role;
import ru.skypro.homework.model.entity.UserProfile;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.security.MyUserDetailsService;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final AdsService adsService;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final MyUserDetailsService myUserDetailsService;


    @Override
    public ResponseWrapperComment getAllComment(Integer id) {
        Ads ads = adsService.getAdsById(id);
        List<CommentDto> commentList = new ArrayList<>();
        ads.getComments().stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt))
                .forEach(comment -> {
                    commentList.add(mappingComment(comment));
                });

        ResponseWrapperComment responseWrapperComment = new ResponseWrapperComment();
        responseWrapperComment.setCount(commentList.size());
        responseWrapperComment.setResults(commentList);
        return responseWrapperComment;
    }

    @Override
    public CommentDto addComment(Integer id, CreateComment createComment) {
        UserProfile userProfile = userService.getUserProfile();

        Ads ads = adsService.getAdsById(id);
        Comment comment = new Comment();
        comment.setText(createComment.getText());
        comment.setAds(ads);
        comment.setUserProfile(userProfile);
        saveComment(comment);
        CommentDto commentDto = mappingComment(comment);
        return commentDto;
    }

    private void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Integer adId, Integer commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        if (checkAccess(comment)) {
            commentRepository.delete(comment);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public CommentDto updateComment(Integer adId, Integer commentId, CommentDto commentDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        if (checkAccess(comment)) {
            comment.setText(commentDto.getText());
            commentRepository.save(comment);
            return mappingComment(comment);
        }
        return null;
    }

    private CommentDto mappingComment(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setText(comment.getText());
        commentDto.setPk(comment.getId());

        ZoneId zoneId = ZoneId.systemDefault();
        long epoch = comment.getCreatedAt().atZone(zoneId).toInstant().toEpochMilli();

        commentDto.setCreatedAt(epoch); //дата время
        commentDto.setAuthor(comment.getUserProfile().getId());
        commentDto.setAuthorImage("/users/"+comment.getUserProfile().getId()+"/getAvatar");
        commentDto.setAuthorFirstName(comment.getUserProfile().getFirstName());

        return commentDto;
    }

    private boolean checkAccess(Comment comment) {
        UserProfile userProfile = userService.getUserProfile();
        Set<Role> roles = userProfile.getRoles();
       // if (roles.stream().anyMatch(role -> role.getName().equals(RoleEnum.ADMIN.toString())) || userProfile.getComments().contains(comment)) {
        if (roles.stream().anyMatch(role -> role.getName().equals(RoleEnum.ADMIN.toString())) || userProfile.getId() == comment.getUserProfile().getId()) {
            return true;
        }
        return false;
    }
}
