package by.tovpenets.secure.api.controller;

import by.tovpenets.secure.api.entity.Blog;
import by.tovpenets.secure.api.service.BlogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/view")
public class ViewController {
    private final BlogService blogService;

    public ViewController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("")
    public List<Blog> getBlogs(){
        return blogService.getAll();
    }
}
