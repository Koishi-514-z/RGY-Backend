package org.example.rgybackend.DAO.Impl;

import org.example.rgybackend.DAO.BlogDAO;
import org.example.rgybackend.Entity.Blog;
import org.example.rgybackend.Entity.Reply;
import org.example.rgybackend.Model.BlogModel;
import org.example.rgybackend.Model.ReplyModel;
import org.example.rgybackend.Repository.BlogRepository;
import org.example.rgybackend.Repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BlogDAOImpl implements BlogDAO {
    // TODO: Implement BlogDAOImpl
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private ReplyRepository replyRepository;
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
        List<Blog> blogs = blogRepository.findAll();
//        List<BlogModel> blogModels = new ArrayList<>();
//        for (Blog blog : blogs) {
//            if (blog.getValid() == 1) {
//                BlogModel blogModel = new BlogModel();
//                blogModel.setBlogid(blog.getBlogid());
//                blogModel.setUser(new SimplifiedProfileModel(blog.getUserid()));
//                blogModel.setTimestamp(blog.getTimestamp());
//                blogModel.setLikeNum(blog.getLikeNum());
//                blogModel.setTitle(blog.getTitle());
//                blogModel.setCover(blog.getCover());
//                blogModel.setContent(blog.getContent());
//                blogModel.setTags(blog.getTags());
//                blogModel.setEmotion(blog.getEmotion());
//                blogModel.setReplies(new ArrayList<>());
//                List<Reply> replies = replyRepository.findAllByBlogid(blog.getBlogid());
//                for (Reply reply : replies) {
//                    if (reply.getValid() == 1) {
//                        ReplyModel replyModel = new ReplyModel();
//                        replyModel.setReplyid(reply.getReplyid());
//                        replyModel.setBlogid(reply.getBlogid());
//                        replyModel.setUser(new SimplifiedProfileModel(reply.getUserid()));
//                        replyModel.setTimestamp(reply.getTimestamp());
//                        replyModel.setContent(reply.getContent());
//                        blogModel.getReplies().add(replyModel);
//                    }
//                }
//                blogModels.add(blogModel);
//            }
//        }
        return blogs;
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
    public void likeBlog(Long blogid) {
        Blog blog = blogRepository.findById(blogid).get();
        blog.setLikeNum(blog.getLikeNum() + 1);
        blogRepository.save(blog);
    }
    @Override
    public void unlikeBlog(Long blogid) {
        Blog blog = blogRepository.findById(blogid).get();
        blog.setLikeNum(blog.getLikeNum() - 1);
        blogRepository.save(blog);
    }

}
