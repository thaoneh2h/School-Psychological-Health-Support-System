package com.be.custom.dto.request;

import lombok.Data;

@Data
public class BlogPostDto {
    private Long id;
    private String title;
    private String content;
    private Long authorId;
}
