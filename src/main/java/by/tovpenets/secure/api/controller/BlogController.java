package by.tovpenets.secure.api.controller;


import by.tovpenets.secure.api.dto.BlogDto;
import by.tovpenets.secure.api.entity.Blog;
import by.tovpenets.secure.api.entity.User;
import by.tovpenets.secure.api.service.BlogService;
import by.tovpenets.secure.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("author")
public class BlogController {
    private final BlogService blogService;
    private final UserService userService;

    @Autowired
    public BlogController(BlogService blogService, UserService userService) {
        this.blogService = blogService;
        this.userService = userService;
    }


    @PostMapping("/create_blog")
    public void createBlog(@RequestBody BlogDto blogDto){
        User user = getAuthUser();
        List<Blog> blogs = user.getBlogs();
            Blog newBlog = new Blog();
            newBlog.setUserName(user.getLogin());
            newBlog.setContent(blogDto.getContent());
            blogs.add(blogService.createBlog(newBlog));
            user.setBlogs(blogs);
            userService.updateUser(user);

    }

    @PutMapping("/update_blog/{id}")
    public ResponseEntity<Blog> updateBlog(@PathVariable Integer id, @RequestBody BlogDto newBlog){
        User user = getAuthUser();
        List<Blog> blogsOfUser = getListOfBlogsFromAuthUser(user);
        if(blogsOfUser.get(id) != null){
            Blog updatedBlog = blogsOfUser.get(id-1);
            updatedBlog.setContent(newBlog.getContent());
            blogService.updateBlog(updatedBlog);
            return new ResponseEntity<>(updatedBlog, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete_blog/{id}")
    public ResponseEntity<String> deleteBlog(@PathVariable Integer id){
        User user = getAuthUser();
        List<Blog> blogs = getListOfBlogsFromAuthUser(user);
        if(!blogs.isEmpty()){
            Blog blogForDelete = blogs.get(id-1);
            if(blogs.contains(blogForDelete)){
                blogs.remove(blogForDelete);
                blogService.deleteBlog(blogForDelete);
                userService.updateUser(user);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Blogs is empty", HttpStatus.NO_CONTENT);
    }

    public User getAuthUser(){
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        return userService.findByLogin(name);
    }

    public List<Blog> getListOfBlogsFromAuthUser(User user){
        return user.getBlogs();
    }
}

