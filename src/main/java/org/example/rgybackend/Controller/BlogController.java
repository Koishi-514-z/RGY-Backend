package org.example.rgybackend.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import org.example.rgybackend.DAO.EmotionDAO;
import org.example.rgybackend.Entity.Like;
import org.example.rgybackend.Model.BlogModel;
import org.example.rgybackend.Model.BlogsRet;
import org.example.rgybackend.Model.ReplyModel;
import org.example.rgybackend.Model.SimplifiedProfileModel;
import org.example.rgybackend.Service.BlogService;
import org.example.rgybackend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private UserService userService;

    @Autowired
    private EmotionDAO emotionDAO;
    
    @GetMapping("/getmine")
    public List<BlogModel> getMyBlogs(HttpSession session) {
        List<BlogModel> result = new ArrayList<>();
        String userid = (String)session.getAttribute("user");
        result = blogService.getBlogsByUserid(userid);
        return result;
    }

    @GetMapping("/getlike")
    public List<BlogModel> getLikeBlogs(HttpSession session) {
        List<BlogModel> result = new ArrayList<>();
        String userid = (String)session.getAttribute("user");
        List<Like> userLikes = blogService.getBlogLikedByUserid(userid);
        Map<Long, BlogModel> blogMap = new HashMap<>();
        for (Like userLike : userLikes) {
            BlogModel blogModel = blogService.getBlogById(userLike.getBlogid());
            blogMap.put(blogModel.getBlogid(), blogModel);
        }
        result = new ArrayList<>(blogMap.values());
        return result;
    }

    @GetMapping("/getcomment")
    public List<BlogModel> getCommentBlogs(HttpSession session) {
        List<BlogModel> result = new ArrayList<>();
        String userid = (String)session.getAttribute("user");
        result = blogService.getBlogRepliedByUserid(userid);
        return result;
    }

    @PostMapping ("/get")
    public BlogsRet getAllBlogs(@RequestBody String params, HttpSession session) {
        BlogsRet result = new BlogsRet();
        JSONObject json = new JSONObject();
        json = JSON.parseObject(params);
        int pageSize = json.getIntValue("pageSize");
        int currentPage = json.getIntValue("currentPage");
        String titleOrAuthor = json.getString("searchText");
        List<String> tags = json.getJSONArray("tags").toJavaList(String.class);
        int emotion = 0;
        if (emotionDAO.getEmotion(session.getAttribute("user").toString(), LocalDate.now()).getTag() == null)
            emotion = 0;
        else
            emotion = emotionDAO.getEmotion(session.getAttribute("user").toString(), LocalDate.now()).getTag().getId().intValue();
        result =  blogService.getRequestedBlogs(pageSize, currentPage, titleOrAuthor, tags, emotion);
        return result;
    }

    @GetMapping ("/getById/{id}")
    public BlogModel getBlogById(@PathVariable String id) {
        Long idLong = Long.parseLong(id);
        BlogModel blogModel = blogService.getBlogById(idLong);
        return blogModel;
    }


    @PostMapping ("/add")
    public boolean addBlog(@RequestBody String params, HttpSession session) {
        JSONObject json = new JSONObject();
        json = JSON.parseObject(params);
        String title = json.getString("title");
        String content = json.getString("content");
        List<String> tags = json.getJSONArray("tags").toJavaList(String.class);
        //输出tags内容
        SimplifiedProfileModel author = userService.getSimplifiedProfile(session.getAttribute("user").toString());
        blogService.addBlog(title, content, tags, author);
        return true;
    }

    @PostMapping ("/delete")
    public boolean deleteBlog(@RequestBody String params, HttpSession session) {
        JSONObject json = new JSONObject();
        json = JSON.parseObject(params);
        Long blogid = json.getLongValue("blogid");
        blogService.deleteBlog(blogid);
        return true;
    }

    @PostMapping ("/addReply")
    public ReplyModel addReply(@RequestBody String params, HttpSession session) {
        JSONObject json = new JSONObject();
        json = JSON.parseObject(params);
        Long blogid = json.getLongValue("blogid");
        String content = json.getString("content");
        SimplifiedProfileModel author = userService.getSimplifiedProfile(session.getAttribute("user").toString());
        return blogService.addReply(blogid, content, author);

    }

    @PostMapping ("/deleteReply")
    public boolean deleteReply(@RequestBody String params, HttpSession session) {
        JSONObject json = new JSONObject();
        json = JSON.parseObject(params);
        Long replyid = json.getLongValue("replyid");
        blogService.deleteReply(replyid);
        return true;
    }
    @PostMapping ("/getIfLiked")
    public boolean getIfLiked(@RequestBody String params, HttpSession session) {
        JSONObject json = new JSONObject();
        json = JSON.parseObject(params);
        Long blogid = json.getLongValue("blogid");
        String userid = (String)session.getAttribute("user");
        //判断是否已经点过赞
        if (blogService.getBlogLikedByUserid(userid).stream().anyMatch(like -> like.getBlogid().equals(blogid))) {
            return true;
        } else {
            return false;
        }
    }

    @PostMapping ("/like")
    public boolean likeBlog(@RequestBody String params, HttpSession session) {
        JSONObject json = new JSONObject();
        json = JSON.parseObject(params);
        Long blogid = json.getLongValue("blogid");
        String userid = (String)session.getAttribute("user");
        //判断是否已经点过赞
        if (blogService.getBlogLikedByUserid(userid).stream().anyMatch(like -> like.getBlogid().equals(blogid))) {
            blogService.unlikeBlog(blogid, userid);
        } else {
        blogService.likeBlog(blogid, userid);
        }
        return true;
    }

    @PostMapping ("/cancellike")
    public boolean unlikeBlog(@RequestBody String params, HttpSession session) {
        JSONObject json = new JSONObject();
        json = JSON.parseObject(params);
        Long blogid = json.getLongValue("blogid");
        String userid = (String)session.getAttribute("user");
        blogService.unlikeBlog(blogid,userid);
        return true;
    }

}
