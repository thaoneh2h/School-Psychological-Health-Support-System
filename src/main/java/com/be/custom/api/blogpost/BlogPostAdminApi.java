package com.be.custom.api.blogpost;

import com.be.custom.common.security.CustomUserDetails;
import com.be.custom.dto.request.BlogPostDto;
import com.be.custom.service.blogpost.BlogPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.EntityNotFoundException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blog")
@PreAuthorize("@authorizationService.isSystemAdmin()")
public class BlogPostAdminApi {

    private final BlogPostService blogPostService;

    @GetMapping("/get-all-blogs")
    public ResponseEntity<?> getAllPosts() {
        try {
            return ResponseEntity.ok(blogPostService.getAllPosts());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch blog posts: " + e.getMessage());
        }
    }

    @GetMapping("/get-blogs-by-user")
    public ResponseEntity<?> getAllPostsByUser() {
        try {
            return ResponseEntity.ok(blogPostService.getAllPostsByUser());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch blog posts: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(blogPostService.getPostById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving blog post: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createPost(@ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody BlogPostDto blogPostDTO) {
        try {
            blogPostDTO.setAuthorId(userDetails.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(blogPostService.savePost(blogPostDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating blog post: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        try {
            blogPostService.deletePost(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting blog post: " + e.getMessage());
        }
    }
}
