package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import org.openapitools.jackson.nullable.JsonNullable;
import io.swagger.configuration.NotUndefined;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import javax.validation.Valid;
import javax.validation.constraints.*;
import javax.persistence.*;

/**
 * Comment
 */
@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-03-25T12:58:12.376654904Z[GMT]")
@Entity
@Table(name = "comments")
public class Comment {
  @JsonProperty("id")
  @JsonInclude(JsonInclude.Include.NON_ABSENT)
  @JsonSetter(nulls = Nulls.FAIL)
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id = null;

  @JsonProperty("content")
  @Column(nullable = false)
  private String content = null;

  @JsonProperty("author")
  @Column(nullable = false)
  private String author = null;

  public Comment id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   **/
  @Schema(description = "")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Comment content(String content) {
    this.content = content;
    return this;
  }

  /**
   * Содержимое комментария
   * @return content
   **/
  @Schema(required = true, description = "Содержимое комментария")
  @NotNull
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Comment author(String author) {
    this.author = author;
    return this;
  }

  /**
   * Автор комментария
   * @return author
   **/
  @Schema(required = true, description = "Автор комментария")
  @NotNull
  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Comment comment = (Comment) o;
    return Objects.equals(this.id, comment.id) &&
            Objects.equals(this.content, comment.content) &&
            Objects.equals(this.author, comment.author);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, content, author);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Comment {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    author: ").append(toIndentedString(author)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}