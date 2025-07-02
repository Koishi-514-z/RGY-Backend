package org.example.rgybackend.Service.Impl;

import org.example.rgybackend.DAO.BlogDAO;
import org.example.rgybackend.DAO.EmotionDAO;
import org.example.rgybackend.DAO.UserDAO;
import org.example.rgybackend.Entity.Blog;
import org.example.rgybackend.Entity.Reply;
import org.example.rgybackend.Model.BlogModel;
import org.example.rgybackend.Model.EmotionSimilarity;
import org.example.rgybackend.Model.ReplyModel;
import org.example.rgybackend.Model.SimplifiedProfileModel;
import org.example.rgybackend.Service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {
    // TODO: Implement Blog Service
    @Autowired
    private BlogDAO blogDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private EmotionDAO emotionDAO;

    @Override
    public void addBlog(String title, String content, List<String> tags, SimplifiedProfileModel author) {
        // TODO: Implement addBlog method

        Long timestamp = System.currentTimeMillis();
        Long likeNum = 0L;
        int emotion = emotionDAO.getEmotion(author.getUserid(), LocalDate.now()).getTag().getId().intValue();
        BlogModel blogModel = new BlogModel(null, author, timestamp,likeNum, title, content, tags, new ArrayList<>(), emotion);
        blogDAO.addBlog(blogModel);
    }

    @Override
    public void deleteBlog(Long blogid) {
        // TODO: Implement deleteBlog method
        blogDAO.deleteBlog(blogid);
    }

    @Override
    public void addReply(Long blogid, String content, SimplifiedProfileModel author) {
        // TODO: Implement addReply method
        Long timestamp = System.currentTimeMillis();
        ReplyModel replyModel = new ReplyModel(null, blogid, author, timestamp, content);
        blogDAO.addReply(replyModel);
    }

    @Override
    public void deleteReply(Long replyid) {
        // TODO: Implement deleteReply method
        blogDAO.deleteReply(replyid);
    }

    @Override
    public List<BlogModel> getRequestedBlogs(int pageSize, int currentPage, String titleOrAuthor, List<String> tags, int emotion) {
        List<Blog> blogss = blogDAO.getAllBlogs();
        List<BlogModel> blogs = new ArrayList<>();
        for (Blog blog : blogss) {
            BlogModel blogModel = new BlogModel();
            blogModel.setBlogid(blog.getBlogid());
            blogModel.setUser(userDAO.getSimplified(blog.getUserid()));
            blogModel.setTimestamp(blog.getTimestamp());
            blogModel.setLikeNum(blog.getLikeNum());
            blogModel.setTitle(blog.getTitle());

            blogModel.setContent(blog.getContent());
            blogModel.setTags(Arrays.asList(blog.getTags().split(",")));
            //获得blog的reply
            List<Reply> replies = blogDAO.getRepliesByBlogid(blog.getBlogid());
            List<ReplyModel> replyModels = new ArrayList<>();
            for (Reply reply : replies) {
                ReplyModel replyModel = new ReplyModel();
                replyModel.setReplyid(reply.getReplyid());
                replyModel.setBlogid(reply.getBlogid());
                replyModel.setUser(userDAO.getSimplified(reply.getUserid()));
                replyModel.setTimestamp(reply.getTimestamp());
                replyModel.setContent(reply.getContent());
                replyModels.add(replyModel);
            }
            blogModel.setReplies(replyModels);
            blogModel.setEmotion(blog.getEmotion());
            blogs.add(blogModel);
        }
        EmotionSimilarity emotionSimilarity = new EmotionSimilarity();
        //对所有blogs进行筛选，只选择blog的标题或作者包含titleOrAuthor的博客，且标签包含tags中的所有标签的博客
        List<BlogModel> filteredBlogs = filterBlogs(blogs, titleOrAuthor, tags);
        //对筛选后的blogs进行排序
        List<BlogModel> sortedBlogs = sortBlogs(filteredBlogs, emotionSimilarity, emotion);
        //分页
        int start = (currentPage - 1) * pageSize;
        int end = start + pageSize;
        if (end > sortedBlogs.size()) {
            end = sortedBlogs.size();
        }
        return sortedBlogs.subList(start, end);
    }

    private List<BlogModel> sortBlogs(List<BlogModel> filteredBlogs, EmotionSimilarity emotionSimilarity, int emotion) {
        List<BlogModel> result = new ArrayList<>();
        Map<Long, Long> similarityMap = new HashMap<>();
        for (BlogModel blog : filteredBlogs) {
            int similarity = 0;
//            if (emotion == 0) {
//                similarity = emotionSimilarity.getEmotion1().get(0).get(blog.getEmotion());
//            } else if (emotion == 1) {
//                similarity = emotionSimilarity.getEmotion1().get(1).get(blog.getEmotion());
//            } else if (emotion == 2) {
//                similarity = emotionSimilarity.getEmotion1().get(2).get(blog.getEmotion());
//            } else if (emotion == 3) {
//                similarity = emotionSimilarity.getEmotion1().get(3).get(blog.getEmotion());
//            } else if (emotion == 4) {
//                similarity = emotionSimilarity.getEmotion1().get(4).get(blog.getEmotion());
//            }
            similarity = emotionSimilarity.getEmotion1().get(emotion-1).get(blog.getEmotion()-1);
            similarityMap.put(blog.getBlogid(), (long) similarity * blog.getLikeNum());
        }

        List<Map.Entry<Long, Long>> sortedSimilarityMap = new ArrayList<>(similarityMap.entrySet());
        sortedSimilarityMap.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        for (Map.Entry<Long, Long> entry : sortedSimilarityMap) {
            for (BlogModel blog : filteredBlogs) {
                if (blog.getBlogid() == entry.getKey()) {
                    result.add(blog);
                    break;
                }
            }
        }
        return result;

    }

    private List<BlogModel> filterBlogs(List<BlogModel> blogs, String titleOrAuthor, List<String> tags) {
        List<BlogModel> result = new ArrayList<>();
        for (BlogModel blog : blogs) {
            if (blog.getTitle().contains(titleOrAuthor) || blog.getUser().getUsername().contains(titleOrAuthor)) {
               if(tags.isEmpty()|| new HashSet<>(blog.getTags()).containsAll(tags))
                   result.add(blog);
            }
        }
        return result;
    }

    @Override
    public BlogModel getBlogById(Long blogid) {
        Blog blog = blogDAO.getBlogById(blogid);
        BlogModel blogModel = new BlogModel();
        blogModel.setBlogid(blog.getBlogid());
        blogModel.setUser(userDAO.getSimplified(blog.getUserid()));
        blogModel.setTimestamp(blog.getTimestamp());
        blogModel.setLikeNum(blog.getLikeNum());
        blogModel.setTitle(blog.getTitle());
        blogModel.setContent(blog.getContent());
        blogModel.setTags(Arrays.asList(blog.getTags().split(",")));
        //获得blog的reply
        List<Reply> replies = blogDAO.getRepliesByBlogid(blog.getBlogid());
        List<ReplyModel> replyModels = new ArrayList<>();
        for (Reply reply : replies) {
            ReplyModel replyModel = new ReplyModel();
            replyModel.setReplyid(reply.getReplyid());
            replyModel.setBlogid(reply.getBlogid());
            replyModel.setUser(userDAO.getSimplified(reply.getUserid()));
            replyModel.setTimestamp(reply.getTimestamp());
            replyModel.setContent(reply.getContent());
            replyModels.add(replyModel);
        }
        blogModel.setReplies(replyModels);
        blogModel.setEmotion(blog.getEmotion());
        return blogModel;

    }
    @Override
    public void likeBlog(Long blogid) {
        // TODO: Implement likeBlog method
            blogDAO.likeBlog(blogid);
    }

    @Override
    public void unlikeBlog(Long blogid) {
        // TODO: Implement unlikeBlog method
            blogDAO.unlikeBlog(blogid);

    }

}
