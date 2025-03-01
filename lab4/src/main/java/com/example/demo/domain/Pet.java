package com.example.demo.domain;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class Pet {
    private Long id;
    @NotBlank(message = "Name cannot be empty")
    private String name;
    private Category category;
    private List<Tag> tags;
    private String status;

    public Pet() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public List<Tag> getTags() { return tags; }
    public void setTags(List<Tag> tags) { this.tags = tags; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}