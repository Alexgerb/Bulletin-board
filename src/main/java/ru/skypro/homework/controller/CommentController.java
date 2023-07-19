package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.model.dto.CommentDto;
import ru.skypro.homework.model.dto.CreateComment;
import ru.skypro.homework.model.dto.ResponseWrapperComment;
import ru.skypro.homework.security.MyUserDetailsService;
import ru.skypro.homework.service.CommentService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{id}/comments")
    public ResponseEntity<ResponseWrapperComment> getAllComment (@PathVariable("id") Integer id) {
        return ResponseEntity.ok(commentService.getAllComment(id));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDto> addComment (@PathVariable("id") Integer id,
                                                     @RequestBody CreateComment comment) {
        return ResponseEntity.ok(commentService.addComment(id, comment));
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment (@PathVariable("adId") Integer adId,
                               @PathVariable("commentId") Integer commentId) {
        commentService.deleteComment(adId, commentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComments(@PathVariable("adId") Integer adId,
                                                     @PathVariable("commentId") Integer commentId,
                                                     @RequestBody CommentDto commentDto) {
        if (commentService.updateComment(adId, commentId, commentDto) == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
