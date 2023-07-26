package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final AdsService adsService;
    private final CommentRepository commentRepository;
    private final UserService userService;

    /**
     * метод возвращает wrapper все комментарий
     * @return ResponseWrapperComment
     * */
    @Override
    public ResponseWrapperComment getAllComment(Integer id) {
        logger.info("the method of getting all comments");
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

    /**
     * метод добавления комментария
     * @return CommentDto
     * */
    @Override
    public CommentDto addComment(Integer id, CreateComment createComment) {
        logger.info("the method of adding a comment");
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
    /**
     * метод сохранения комментария
     * */
    private void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    /**
     * метод удаления комментария
     * */
    @Override
    public void deleteComment(Integer adId, Integer commentId) {
        logger.info("the method of deleting a comment");
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        if (checkAccess(comment)) {
            commentRepository.delete(comment);
            logger.info("comment deleted successfully");
        } else {
            logger.debug("Insufficient rights to delete id{}", adId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * метод обновления комментария
     * @return CommentDto
     * */
    @Override
    public CommentDto updateComment(Integer adId, Integer commentId, CommentDto commentDto) {
        logger.info("the comment update method");
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        if (checkAccess(comment)) {
            comment.setText(commentDto.getText());
            commentRepository.save(comment);
            logger.info("comment updated  successfully");
            return mappingComment(comment);
        }
        logger.debug("Insufficient rights to update id{}", adId);
        return null;
    }

    /**
     * преобразование Comment to CommentDto
     * */
    private CommentDto mappingComment(Comment comment) {
        logger.info("the method of mapping Comment to CommentDto");
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

    /**
     * метод проверяет принадлежность комментарий пользователю либо роль Администратора
     * @return  true or false
     * */
    private boolean checkAccess(Comment comment) {
        UserProfile userProfile = userService.getUserProfile();
        Set<Role> roles = userProfile.getRoles();
        if (roles.stream().anyMatch(role -> role.getName().equals(RoleEnum.ADMIN.toString())) || userProfile.getId() == comment.getUserProfile().getId()) {
            return true;
        }
        return false;
    }
}
