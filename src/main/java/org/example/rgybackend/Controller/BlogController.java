package org.example.rgybackend.Controller;

import java.util.ArrayList;
import java.util.List;

import org.example.rgybackend.Entity.Blog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {
    
    @GetMapping("/getmine")
    public List<Blog> getMyBlogs() {
        List<Blog> result = new ArrayList<>();
        result.add(new Blog(1L, "123456789", 651096849L, 1L, "AAAAAA", null, "aerghuskhbviuhb", new ArrayList<>(), new ArrayList<>()));
        result.add(new Blog(2L, "123456789", 651096849L, 1L, "BBBBBB", null, "aerghuskhbviuhb", new ArrayList<>(), new ArrayList<>()));
        result.add(new Blog(2L, "123456789", 651096849L, 1L, "CCCCCC", null, "aerghuskhbviuhb", new ArrayList<>(), new ArrayList<>()));
        return result;
    }

    @GetMapping("/getlike")
    public List<Blog> getLikeBlogs() {
        List<Blog> result = new ArrayList<>();
        result.add(new Blog(1L, "123456789", 651096849L, 1L, "AAAAAA", null, "aerghuskhbviuhb", new ArrayList<>(), new ArrayList<>()));
        result.add(new Blog(2L, "123456789", 651096849L, 1L, "BBBBBB", null, "aerghuskhbviuhb", new ArrayList<>(), new ArrayList<>()));
        result.add(new Blog(2L, "123456789", 651096849L, 1L, "CCCCCC", null, "aerghuskhbviuhb", new ArrayList<>(), new ArrayList<>()));
        return result;
    }

    @GetMapping("/getcomment")
    public List<Blog> getCommentBlogs() {
        List<Blog> result = new ArrayList<>();
        result.add(new Blog(1L, "123456789", 651096849L, 1L, "AAAAAA", null, "aerghuskhbviuhb", new ArrayList<>(), new ArrayList<>()));
        result.add(new Blog(2L, "123456789", 651096849L, 1L, "BBBBBB", null, "aerghuskhbviuhb", new ArrayList<>(), new ArrayList<>()));
        result.add(new Blog(2L, "123456789", 651096849L, 1L, "CCCCCC", null, "aerghuskhbviuhb", new ArrayList<>(), new ArrayList<>()));
        return result;
    }
}
