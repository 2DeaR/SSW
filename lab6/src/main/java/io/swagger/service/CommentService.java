package io.swagger.service;

import io.swagger.model.Comment;
import io.swagger.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment createComment(Comment comment) {
        logger.info("Создание нового комментария: content={}, author={}", comment.getContent(), comment.getAuthor());
        Comment savedComment = commentRepository.save(comment);
        logger.debug("Комментарий успешно создан с ID: {}", savedComment.getId());
        return savedComment;
    }

    public Comment getCommentById(Long id) {
        logger.info("Получение комментария по ID: {}", id);
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment == null) {
            logger.error("Комментарий с ID {} не найден", id);
            throw new NoSuchElementException("Комментарий с ID " + id + " не найден");
        }
        logger.debug("Комментарий найден: content={}, author={}", comment.getContent(), comment.getAuthor());
        return comment;
    }

    public Comment updateComment(Long id, Comment updatedComment) {
        logger.info("Обновление комментария с ID: {}", id);
        Comment existingComment = getCommentById(id);
        existingComment.setContent(updatedComment.getContent());
        existingComment.setAuthor(updatedComment.getAuthor());
        Comment savedComment = commentRepository.save(existingComment);
        logger.debug("Комментарий успешно обновлен: content={}, author={}", savedComment.getContent(), savedComment.getAuthor());
        return savedComment;
    }

    public void deleteComment(Long id) {
        logger.info("Удаление комментария с ID: {}", id);
        if (!commentRepository.existsById(id)) {
            logger.error("Комментарий с ID {} не найден для удаления", id);
            throw new NoSuchElementException("Комментарий с ID " + id + " не найден");
        }
        commentRepository.deleteById(id);
        logger.debug("Комментарий с ID {} успешно удален", id);
    }
}