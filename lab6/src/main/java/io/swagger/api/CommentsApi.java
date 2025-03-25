package io.swagger.api;

import io.swagger.model.Comment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import javax.validation.constraints.*;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-03-25T12:58:12.376654904Z[GMT]")
@Validated
public interface CommentsApi {

    @Operation(summary = "Удалить комментарий по ID", description = "", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Комментарий успешно удален"),
            @ApiResponse(responseCode = "404", description = "Комментарий не найден") })
    @RequestMapping(value = "/comments/{id}",
            method = RequestMethod.DELETE)
    ResponseEntity<Void> commentsIdDelete(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("id") Long id);

    @Operation(summary = "Получить комментарий по ID", description = "", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "404", description = "Комментарий не найден") })
    @RequestMapping(value = "/comments/{id}",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<Comment> commentsIdGet(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("id") Long id);

    @Operation(summary = "Обновить комментарий по ID", description = "", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно обновлен", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "404", description = "Комментарий не найден") })
    @RequestMapping(value = "/comments/{id}",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.PUT)
    ResponseEntity<Comment> commentsIdPut(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("id") Long id,
                                          @Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody Comment body);

    @Operation(summary = "Создать новый комментарий", description = "", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Комментарий успешно создан", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class))) })
    @RequestMapping(value = "/comments",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Comment> commentsPost(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody Comment body);
}