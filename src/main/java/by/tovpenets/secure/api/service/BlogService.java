package by.tovpenets.secure.api.service;

import by.tovpenets.secure.api.entity.Blog;
import by.tovpenets.secure.api.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogService {
    private final BlogRepository blogRepository;

    @Autowired
    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public List<Blog> getAll(){
        return blogRepository.findAll();
    }

    public Optional<Blog> getBlog(Integer id){
        return blogRepository.findById(id);
    }

    public Blog createBlog(Blog blog){
        return blogRepository.save(blog);
    }

    public void updateBlog(Blog updatedBlog){
        blogRepository.save(updatedBlog);
    }

    public void deleteBlog(Blog blog){
        blogRepository.delete(blog);
    }
}
