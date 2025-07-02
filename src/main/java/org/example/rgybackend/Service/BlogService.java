package org.example.rgybackend.Service;

import org.example.rgybackend.Entity.Blog;
import org.example.rgybackend.Model.BlogModel;
import org.example.rgybackend.Model.SimplifiedProfileModel;

import java.util.List;

public interface BlogService {
    public void addBlog(String title, String content, List<String> tags, SimplifiedProfileModel author);


    void deleteBlog(Long blogid);

    public void addReply(Long blogid, String content, SimplifiedProfileModel author);

    public void deleteReply(Long replyid);


    List<BlogModel> getRequestedBlogs(int pageSize, int currentPage, String titleOrAuthor, List<String> tags, int emotion);

    BlogModel getBlogById(Long blogId);

    void likeBlog(Long blogid);

    void unlikeBlog(Long blogid);
}
