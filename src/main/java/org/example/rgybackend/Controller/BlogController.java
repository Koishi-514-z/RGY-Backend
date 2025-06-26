package org.example.rgybackend.Controller;

import java.util.ArrayList;
import java.util.List;

import org.example.rgybackend.Model.BlogModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {
    
    @GetMapping("/getmine")
    public List<BlogModel> getMyBlogs() {
        List<BlogModel> result = new ArrayList<>();
        result.add(new BlogModel(1L, "123456789", 651096849L, 1L, "AAAAAA", null, "aerghuskhbviuhb", new ArrayList<>(), new ArrayList<>()));
        result.add(new BlogModel(2L, "123456789", 651096849L, 1L, "BBBBBB", null, "aerghuskhbviuhb", new ArrayList<>(), new ArrayList<>()));
        result.add(new BlogModel(2L, "123456789", 651096849L, 1L, "CCCCCC", null, "aerghuskhbviuhb", new ArrayList<>(), new ArrayList<>()));
        return result;
    }

    @GetMapping("/getlike")
    public List<BlogModel> getLikeBlogs() {
        List<BlogModel> result = new ArrayList<>();
        result.add(new BlogModel(1L, "123456789", 651096849L, 1L, "AAAAAA", null, "aerghuskhbviuhb", new ArrayList<>(), new ArrayList<>()));
        result.add(new BlogModel(2L, "123456789", 651096849L, 1L, "BBBBBB", null, "aerghuskhbviuhb", new ArrayList<>(), new ArrayList<>()));
        result.add(new BlogModel(2L, "123456789", 651096849L, 1L, "CCCCCC", null, "aerghuskhbviuhb", new ArrayList<>(), new ArrayList<>()));
        return result;
    }

    @GetMapping("/getcomment")
    public List<BlogModel> getCommentBlogs() {
        List<BlogModel> result = new ArrayList<>();
        result.add(new BlogModel(1L, "123456789", 651096849L, 1L, "AAAAAA", null, "aerghuskhbviuhb", new ArrayList<>(), new ArrayList<>()));
        result.add(new BlogModel(2L, "123456789", 651096849L, 1L, "BBBBBB", null, "aerghuskhbviuhb", new ArrayList<>(), new ArrayList<>()));
        result.add(new BlogModel(2L, "123456789", 651096849L, 1L, "CCCCCC", null, "aerghuskhbviuhb", new ArrayList<>(), new ArrayList<>()));
        return result;
    }
}
