package org.example.rgybackend.DAO.Impl;

import org.example.rgybackend.DAO.BlogDAO;
import org.example.rgybackend.Entity.Blog;
import org.example.rgybackend.Entity.Like;
import org.example.rgybackend.Entity.Reply;
import org.example.rgybackend.Model.BlogModel;
import org.example.rgybackend.Model.ReplyModel;
import org.example.rgybackend.Repository.BlogRepository;
import org.example.rgybackend.Repository.LikeRepository;
import org.example.rgybackend.Repository.ReplyRepository;
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

    @Override
    public void addBlog(BlogModel blogModel) {
        blogRepository.save(new Blog(blogModel));
    }

    @Override
    public void addReply(ReplyModel replyModel) {
        replyRepository.save(new Reply(replyModel));
    }

    @Override
    public void deleteBlog(Long blogId) {
        Blog blog = blogRepository.findById(blogId).get();
        blog.setValid(0);
        blogRepository.save(blog);
    }

    @Override
    public void deleteReply(Long replyId) {
        Reply reply = replyRepository.findById(replyId).get();
        reply.setValid(0);
        replyRepository.save(reply);
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
    }

    @Override
    public void unlikeBlog(Long blogid,String userid) {
        Blog blog = blogRepository.findById(blogid).get();
        blog.setLikeNum(blog.getLikeNum() - 1);
        blogRepository.save(blog);
        userLikeRepository.deleteByFromuseridAndBlogid(userid,blogid);
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

}
