package org.example.rgybackend.Service;

import org.example.rgybackend.Entity.Like;
import org.example.rgybackend.Model.*;

import java.util.List;

public interface BlogService {
    public void addBlog(String title, String content, List<String> tags, String author);

    void deleteBlog(Long blogid,int type);

    void addBlockingRecord(Long blogid,int type);

    public ReplyModel addReply(Long blogid, String content, String author);

    public void deleteReply(Long replyid,int type);

    BlogsRet getRequestedBlogs(int pageSize, int currentPage, String titleOrAuthor, List<String> tags, int emotion);

    BlogModel getBlogById(Long blogId);

    void likeBlog(Long blogid,String userid);

    void unlikeBlog(Long blogid, String userid);

    List<SimplifiedBlogModel> getBlogsByUserid(String userid);

    List<SimplifiedBlogModel> getBlogRepliedByUserid(String userid);

    SimplifiedBlogModel getSimplifiedBlogById(Long blogid);

    List<Like> getBlogLikedByUserid(String userid);

    BlogsRet getLatestBlogs(int pageSize, int currentPage, String titleOrAuthor, List<String> tags);

    void addBrowsenum(Long blogid);

    void reportBlog(Long blogid, String reason);

    List<IllegalModel> getIllegalBlogs();

    List<IllegalModel> getIllegalReplies();

    void setIllegalStatus(int illegalid, int i);

    void deleteIllegal(int illegalid);

    void reportReply(Long replyid, String reason);



    List<SimplifiedReplyModel> getRepliesByUserid(String userid);

    List<ReplyModel> getRepliesByBlogid(Long blogid,int pageSize, int currentPage);
}
