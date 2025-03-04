package com.be.custom.service.blogpost;

import com.be.base.core.BaseService;
import com.be.custom.dto.request.BlogPostDto;
import com.be.custom.entity.BlogPostEntity;
import com.be.custom.entity.UserEntity;
import com.be.custom.repository.BlogPostRepository;
import com.be.custom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlogPostService extends BaseService<BlogPostEntity, BlogPostRepository> {

    private final UserRepository userRepository;

    public List<BlogPostEntity> getAllPosts() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving blog posts", e);
        }
    }

    public List<BlogPostEntity> getAllPostsByUser() {
        try {
            return repository.findAllByIsDeletedFalse();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving blog posts", e);
        }
    }

    public BlogPostEntity getPostById(Long id) {
        try {
            return repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Blog post not found"));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving blog post", e);
        }
    }

    public BlogPostEntity savePost( BlogPostDto blogPostDTO) {
        try{
            BlogPostEntity post;
            Long id = blogPostDTO.getId();
            if (id != null) {
                post = getPostById(id);
            } else {
                post = new BlogPostEntity();
                UserEntity author = userRepository.findById(blogPostDTO.getAuthorId())
                        .orElseThrow(() -> new EntityNotFoundException("Author not found"));
                post.setAuthor(author);
            }

            post.setTitle(blogPostDTO.getTitle());
            post.setContent(blogPostDTO.getContent());
            return repository.save(post);
        } catch (Exception e) {
            throw new RuntimeException("Error saving blog post", e);
        }
    }

    public void deletePost(Long id) {
        try {
            BlogPostEntity post = getPostById(id);
            post.setIsDeleted(true);
            repository.save(post);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting blog post", e);
        }
    }

}
