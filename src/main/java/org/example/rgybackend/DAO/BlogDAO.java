package org.example.rgybackend.DAO;

import org.example.rgybackend.Entity.Blog;
import org.example.rgybackend.Entity.Illegal;
import org.example.rgybackend.Entity.Like;
import org.example.rgybackend.Entity.Reply;
import org.example.rgybackend.Model.BlogModel;
import org.example.rgybackend.Model.ReplyModel;

import java.util.List;

public interface BlogDAO {
    Blog addBlog(BlogModel blogModel,int valid);

    Reply addReply(ReplyModel replyModel, int valid);

    void deleteBlog(Long blogId);

    void recoverBlog(Long blogId);

    void deleteReply(Long replyId);

    void recoverReply(Long replyId);

    List<Blog> getAllBlogs();

    Blog getBlogById(Long blogId);

    List<Reply> getRepliesByBlogid(Long blogid);

    void likeBlog(Long blogid, String userid);

    void unlikeBlog(Long blogid, String userid);

    List<Blog> getBlogsByUserid(String userid);

    List<Reply> getRepliesByUserid(String userid);

    List<Like> getBlogLikedByUserid(String userid);

    void addBrowsenum(Long blogid);

    void reportBlog(Long blogid, String reason);

    void addBlockingRecord(Long contentid, String reason,int type);

    void reportReply(Long replyid, String reason);

    List<Illegal> getByType(int type);

    String getReplyContentById(Long contentid);

    void setIllegalStatus(int illegalid, int i);

    void deleteIllegal(int illegalid);



    Reply getReplyById(Long replyid);
}
