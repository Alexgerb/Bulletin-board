package ru.skypro.homework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.model.dto.CommentDto;
import ru.skypro.homework.model.dto.CreateComment;
import ru.skypro.homework.model.dto.ResponseWrapperComment;
import ru.skypro.homework.model.entity.Comment;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("ads")
public class CommentController {

    @GetMapping("/{id}/comments")
    public ResponseEntity<ResponseWrapperComment> getAllComment (@PathVariable("id") Integer id) {
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CreateComment> addComment (@PathVariable("id") Integer id,
                                                     @RequestBody CreateComment comment) {
        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment (@PathVariable("adId") Integer adId,
                               @PathVariable("commentId") Integer commentId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComments(@PathVariable("adId") Integer adId,
                                                     @PathVariable("commentId") Integer commentId,
                                                     @RequestBody CommentDto commentDto) {
        return ResponseEntity.status(200).build();
    }

}
