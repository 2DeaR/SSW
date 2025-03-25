package io.swagger.api;

import io.swagger.model.Comment;
import io.swagger.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-03-25T12:58:12.376654904Z[GMT]")
@RestController
public class CommentsApiController implements CommentsApi {

    private static final Logger logger = LoggerFactory.getLogger(CommentsApiController.class);

    private final CommentService commentService;

    @org.springframework.beans.factory.annotation.Autowired
    public CommentsApiController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Override
    public ResponseEntity<Comment> commentsPost(@Valid Comment body) {
        logger.info("Получен запрос на создание комментария: content={}, author={}", body.getContent(), body.getAuthor());
        try {
            Comment createdComment = commentService.createComment(body);
            logger.debug("Запрос на создание комментария успешно обработан, ID: {}", createdComment.getId());
            return new ResponseEntity<Comment>(createdComment, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Ошибка при создании комментария: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<Comment> commentsIdGet(Long id) {
        logger.info("Получен запрос на получение комментария по ID: {}", id);
        try {
            Comment comment = commentService.getCommentById(id);
            logger.debug("Запрос на получение комментария успешно обработан: content={}, author={}", comment.getContent(), comment.getAuthor());
            return new ResponseEntity<Comment>(comment, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Ошибка при получении комментария с ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<Comment> commentsIdPut(Long id, @Valid Comment body) {
        logger.info("Получен запрос на обновление комментария с ID: {}", id);
        try {
            Comment updatedComment = commentService.updateComment(id, body);
            logger.debug("Запрос на обновление комментария успешно обработан: content={}, author={}", updatedComment.getContent(), updatedComment.getAuthor());
            return new ResponseEntity<Comment>(updatedComment, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Ошибка при обновлении комментария с ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<Void> commentsIdDelete(Long id) {
        logger.info("Получен запрос на удаление комментария с ID: {}", id);
        try {
            commentService.deleteComment(id);
            logger.debug("Запрос на удаление комментария с ID {} успешно обработан", id);
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Ошибка при удалении комментария с ID {}: {}", id, e.getMessage());
            throw e;
        }
    }
}