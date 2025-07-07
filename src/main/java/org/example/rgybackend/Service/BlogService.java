package org.example.rgybackend.Service;

import org.example.rgybackend.Entity.Like;
import org.example.rgybackend.Model.*;

import java.util.List;

public interface BlogService {
    public void addBlog(String title, String content, List<String> tags, SimplifiedProfileModel author);

    void deleteBlog(Long blogid);

    public ReplyModel addReply(Long blogid, String content, SimplifiedProfileModel author);

    public void deleteReply(Long replyid);

    BlogsRet getRequestedBlogs(int pageSize, int currentPage, String titleOrAuthor, List<String> tags, int emotion);

    BlogModel getBlogById(Long blogId);

    void likeBlog(Long blogid,String userid);

    void unlikeBlog(Long blogid, String userid);

    List<BlogModel> getBlogsByUserid(String userid);

    List<BlogModel> getBlogRepliedByUserid(String userid);

    List<Like> getBlogLikedByUserid(String userid);

    BlogsRet getLatestBlogs(int pageSize, int currentPage, String titleOrAuthor, List<String> tags);

    void addBrowsenum(Long blogid);

    void reportBlog(Long blogid, String reason);

    List<IllegalModel> getIllegalBlogs();

    List<IllegalModel> getIllegalReplies();

    void setIllegalStatus(int illegalid, int i);

    void deleteIllegal(int illegalid);
}
