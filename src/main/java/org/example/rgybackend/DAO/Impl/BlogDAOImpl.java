package org.example.rgybackend.DAO.Impl;

import org.example.rgybackend.DAO.BlogDAO;
import org.example.rgybackend.Entity.Blog;
import org.example.rgybackend.Entity.Illegal;
import org.example.rgybackend.Entity.Like;
import org.example.rgybackend.Entity.Reply;
import org.example.rgybackend.Model.BlogModel;
import org.example.rgybackend.Model.ReplyModel;
import org.example.rgybackend.Repository.BlogRepository;
import org.example.rgybackend.Repository.IllegalRepository;
import org.example.rgybackend.Repository.LikeRepository;
import org.example.rgybackend.Repository.ReplyRepository;
import org.example.rgybackend.Utils.Notification;
import org.example.rgybackend.Utils.SocketMessage;
import org.example.rgybackend.Utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BlogDAOImpl implements BlogDAO {
    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private LikeRepository userLikeRepository;

    @Autowired
    private IllegalRepository illegalRepository;

    @Autowired
    private Notification socket;

    @Override
    public Blog addBlog(BlogModel blogModel , int valid) {
        blogRepository.save(new Blog(blogModel,valid));
        SocketMessage sockMessage = new SocketMessage("System", blogModel.getBlogid(), "System", null, blogModel.getTimestamp(), blogModel.getContent());
        socket.pushBlogToAllClients(sockMessage);
        return blogRepository.findByTimestampAndUserid(blogModel.getTimestamp(),blogModel.getUser().getUserid());
    }

    @Override
    public Reply addReply(ReplyModel replyModel,int valid) {
        replyRepository.save(new Reply(replyModel,valid));
        Blog blog = blogRepository.findById(replyModel.getBlogid()).get();
        blog.setLastreply(replyModel.getTimestamp());
        blogRepository.save(blog);
        SocketMessage sockMessage = new SocketMessage("System", replyModel.getReplyid(), "System", null, replyModel.getTimestamp(), replyModel.getContent());
        socket.pushBlogToAllClients(sockMessage);
        return replyRepository.findByTimestampAndFromuserid(replyModel.getTimestamp(),replyModel.getFromuserid());
    }

    @Override
    public void deleteBlog(Long blogId) {
        Blog blog = blogRepository.findById(blogId).get();
        blog.setValid(0);
        blogRepository.save(blog);
        SocketMessage sockMessage = new SocketMessage("System", blogId, "System", null, blog.getTimestamp(), blog.getContent());
        socket.pushBlogToAllClients(sockMessage);
    }

    @Override
    public void deleteReply(Long replyId) {
        Reply reply = replyRepository.findById(replyId).get();
        reply.setValid(0);
        replyRepository.save(reply);
        SocketMessage sockMessage = new SocketMessage("System", replyId, "System", null, reply.getTimestamp(), reply.getContent());
        socket.pushBlogToAllClients(sockMessage);
    }

    @Override
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }
    
    @Override
    public Blog getBlogById(Long blogid) {
        if (blogRepository.findById(blogid).isPresent()) {
            return blogRepository.findById(blogid).get();
        } else {
            return null;
        }
    }

    @Override
    public List<Reply> getRepliesByBlogid(Long blogid) {
        return replyRepository.findAllByBlogid(blogid);
    }

    @Override
    public void likeBlog(Long blogid, String userid) {
        Blog blog = blogRepository.findById(blogid).get();
        blog.setLikeNum(blog.getLikeNum() + 1);
        Long timestamp = System.currentTimeMillis();
        Like userLike = new Like(userid,blog.getUserid(),blogid,timestamp);
        userLikeRepository.save(userLike);
        blogRepository.save(blog);
        SocketMessage sockMessage = new SocketMessage("System", blogid, "System", null, TimeUtil.now(), "like");
        socket.pushBlogToAllClients(sockMessage);
    }

    @Override
    public void unlikeBlog(Long blogid, String userid) {
        Blog blog = blogRepository.findById(blogid).get();
        blog.setLikeNum(blog.getLikeNum() - 1);
        blogRepository.save(blog);
        userLikeRepository.deleteByFromuseridAndBlogid(userid,blogid);
        SocketMessage sockMessage = new SocketMessage("System", blogid, "System", null, TimeUtil.now(), "unlike");
        socket.pushBlogToAllClients(sockMessage);
    }

    @Override
    public List<Blog> getBlogsByUserid(String userid) {
        List<Blog> blogs = blogRepository.findAllByUserid(userid);
        return blogs;
    }

    @Override
    public List<Reply> getRepliesByUserid(String userid) {
        List<Reply> replies = replyRepository.findAllByFromuserid(userid);
        return replies;
    }

    @Override
    public List<Like> getBlogLikedByUserid(String userid){
        List<Like> userLikes = userLikeRepository.findAllByFromuserid(userid);
        return userLikes;
    }
    @Override
    public void addBrowsenum(Long blogid){
        Blog blog = blogRepository.findById(blogid).get();
        blog.setBrowsenum(blog.getBrowsenum() + 1);
        blogRepository.save(blog);
    }
    @Override
    public void reportBlog(Long blogid, String reason){
        Blog blog = blogRepository.findById(blogid).get();
        Illegal illegal = new Illegal(0,blogid,blog.getUserid(),System.currentTimeMillis(),reason,0);
        illegalRepository.save(illegal);
    }

    @Override
    public void reportReply(Long replyid, String reason){
        Reply reply = replyRepository.findById(replyid).get();
        Illegal illegal = new Illegal(1,replyid,reply.getFromuserid(),System.currentTimeMillis(),reason,0);
        illegalRepository.save(illegal);
    }

    @Override
    public List<Illegal> getByType(int type){
        return illegalRepository.findAllByType(type);
    }

    @Override
    public String getReplyContentById(Long contentid){
        return replyRepository.findById(contentid).get().getContent();
    }

    @Override
    public void setIllegalStatus(int illegalid, int i){
        Illegal illegal = illegalRepository.findByIllegalid(illegalid);
        illegal.setStatus(i);
        illegalRepository.save(illegal);
    }

    @Override
    public void deleteIllegal(int illegalid){
        illegalRepository.deleteById(illegalid);
    }

    @Override
    public Reply getReplyById(Long replyid) {
        if (replyRepository.findById(replyid).isPresent()) {
            return replyRepository.findById(replyid).get();
        } else {
            return null;
        }
    }
//    @Override
//    public void (){
//        return illegalRepository.findAll();
}
