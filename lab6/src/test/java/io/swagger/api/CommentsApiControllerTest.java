package io.swagger.api;

import io.swagger.model.Comment;
import io.swagger.service.CommentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CommentsApiController.class)
public class CommentsApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    public void testCreateComment() throws Exception {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test content");
        comment.setAuthor("Test author");

        when(commentService.createComment(any(Comment.class))).thenReturn(comment);

        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"Test content\",\"author\":\"Test author\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Test content"))
                .andExpect(jsonPath("$.author").value("Test author"));
    }

    @Test
    public void testGetCommentById() throws Exception {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test content");
        comment.setAuthor("Test author");

        when(commentService.getCommentById(1L)).thenReturn(comment);

        mockMvc.perform(get("/comments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Test content"))
                .andExpect(jsonPath("$.author").value("Test author"));
    }

    @Test
    public void testUpdateComment() throws Exception {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Updated content");
        comment.setAuthor("Updated author");

        when(commentService.updateComment(eq(1L), any(Comment.class))).thenReturn(comment);

        mockMvc.perform(put("/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"Updated content\",\"author\":\"Updated author\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Updated content"))
                .andExpect(jsonPath("$.author").value("Updated author"));
    }

    @Test
    public void testDeleteComment() throws Exception {
        doNothing().when(commentService).deleteComment(1L);

        mockMvc.perform(delete("/comments/1"))
                .andExpect(status().isNoContent());
    }
}