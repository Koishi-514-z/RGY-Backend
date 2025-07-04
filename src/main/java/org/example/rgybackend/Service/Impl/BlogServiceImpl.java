package org.example.rgybackend.Service.Impl;

import org.example.rgybackend.DAO.BlogDAO;
import org.example.rgybackend.DAO.CrisisAuditingDAO;
import org.example.rgybackend.DAO.EmotionDAO;
import org.example.rgybackend.DAO.NotificationPrivateDAO;
import org.example.rgybackend.DAO.UserDAO;
import org.example.rgybackend.Entity.Blog;
import org.example.rgybackend.Entity.Like;
import org.example.rgybackend.Entity.Reply;
import org.example.rgybackend.Model.*;
import org.example.rgybackend.Service.BlogService;
import org.example.rgybackend.Utils.BERTModel;
import org.example.rgybackend.Utils.ModelResponse;
import org.example.rgybackend.Utils.NotificationUtil;
import org.example.rgybackend.Utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogDAO blogDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private EmotionDAO emotionDAO;

    @Autowired
    private BERTModel bertModel;

    @Autowired
    private NotificationPrivateDAO notificationPrivateDAO;

    @Autowired
    private CrisisAuditingDAO crisisAuditingDAO;

    @Override
    public void addBlog(String title, String content, List<String> tags, SimplifiedProfileModel author) {
        ModelResponse crisisResponse = bertModel.checkCrisis(content);
        if(crisisResponse.getPredicted_class() == 1) {
            NotificationPrivateModel notification = new NotificationPrivateModel(NotificationUtil.psyAssist);
            notification.setAdminid("System");
            notification.setUserid(author.getUserid());
            notificationPrivateDAO.addNotification(notification);
        }
        else if(crisisResponse.getPredicted_class() == 2) {
            NotificationPrivateModel notification = new NotificationPrivateModel(NotificationUtil.crisis);
            notification.setAdminid("System");
            notification.setUserid(author.getUserid());
            notificationPrivateDAO.addNotification(notification);
            CrisisAuditingModel crisisAuditingModel = new CrisisAuditingModel(null, author.getUserid(), content, TimeUtil.now());
            crisisAuditingDAO.addCrisis(crisisAuditingModel);
            return;
        }

        Long timestamp = System.currentTimeMillis();
        Long likeNum = 0L;
        int emotion = 0;
        if (emotionDAO.getEmotion(author.getUserid(), LocalDate.now()).getTag() == null)
            emotion = 0;
        else emotion = emotionDAO.getEmotion(author.getUserid(), LocalDate.now()).getTag().getId().intValue();
        BlogModel blogModel = new BlogModel(null, author, timestamp,likeNum, title, content, tags, new ArrayList<>(), emotion);
        blogDAO.addBlog(blogModel);
    }

    @Override
    public void deleteBlog(Long blogid) {
        blogDAO.deleteBlog(blogid);
    }

    @Override
    public ReplyModel addReply(Long blogid, String content, SimplifiedProfileModel author) {
        ModelResponse crisisResponse = bertModel.checkCrisis(content);
        if(crisisResponse.getPredicted_class() == 1) {
            NotificationPrivateModel notification = new NotificationPrivateModel(NotificationUtil.psyAssist);
            notification.setAdminid("System");
            notification.setUserid(author.getUserid());
            notificationPrivateDAO.addNotification(notification);
        }
        else if(crisisResponse.getPredicted_class() == 2) {
            NotificationPrivateModel notification = new NotificationPrivateModel(NotificationUtil.crisis);
            notification.setAdminid("System");
            notification.setUserid(author.getUserid());
            notificationPrivateDAO.addNotification(notification);
            CrisisAuditingModel crisisAuditingModel = new CrisisAuditingModel(null, author.getUserid(), content, TimeUtil.now());
            crisisAuditingDAO.addCrisis(crisisAuditingModel);
            return new ReplyModel();
        }

        Long timestamp = System.currentTimeMillis();
        ReplyModel replyModel = new ReplyModel(null, blogid, author.getUserid(), getBlogById(blogid).getUser().getUserid(),timestamp, content,author);
        blogDAO.addReply(replyModel);
        return replyModel;
    }

    @Override
    public void deleteReply(Long replyid) {
        blogDAO.deleteReply(replyid);
    }

    @Override
    public BlogsRet getRequestedBlogs(int pageSize, int currentPage, String titleOrAuthor, List<String> tags, int emotion) {
        List<Blog> blogss = blogDAO.getAllBlogs();
        BlogsRet blogsRet = new BlogsRet();
        List<BlogModel> blogs = new ArrayList<>();
        for (Blog blog : blogss) {
            if(blog.getValid() == 0) continue;
            BlogModel blogModel = new BlogModel();
            blogModel.setBlogid(blog.getBlogid());
            blogModel.setUser(userDAO.getSimplified(blog.getUserid()));
            blogModel.setTimestamp(blog.getTimestamp());
            blogModel.setLikeNum(blog.getLikeNum());
            blogModel.setTitle(blog.getTitle());
            blogModel.setContent(blog.getContent());
            blogModel.setTags(Arrays.asList(blog.getTags().split(",")));
            blogModel.setEmotion(blog.getEmotion());
            blogs.add(blogModel);
        }
        EmotionSimilarity emotionSimilarity = new EmotionSimilarity();
        //对所有blogs进行筛选，只选择blog的标题或作者包含titleOrAuthor的博客，且标签包含tags中的所有标签的博客
        List<BlogModel> filteredBlogs = filterBlogs(blogs, titleOrAuthor, tags);
        int total = filteredBlogs.size();
        blogsRet.setTotal(total);
        //对筛选后的blogs进行排序
        List<BlogModel> sortedBlogs = sortBlogs(filteredBlogs, emotionSimilarity, emotion);
        //分页
        int start = (currentPage - 1) * pageSize;
        int end = start + pageSize;
        if (start >= sortedBlogs.size()) {
            blogsRet.setBlogs(new ArrayList<>());
            return blogsRet;
        }
        if (end > sortedBlogs.size()) {
            end = sortedBlogs.size();
        }

        blogsRet.setBlogs(sortedBlogs.subList(start, end));
        return blogsRet;
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
            similarity = emotionSimilarity.getEmotion1().get(emotion).get(blog.getEmotion());
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
            if (reply.getValid() == 0) continue;
            ReplyModel replyModel = new ReplyModel();
            replyModel.setReplyid(reply.getReplyid());
            replyModel.setBlogid(reply.getBlogid());
            replyModel.setUser(userDAO.getSimplified(reply.getFromuserid()));
            replyModel.setFromuserid(reply.getFromuserid());
            replyModel.setTouserid(reply.getTouserid());
            replyModel.setTimestamp(reply.getTimestamp());
            replyModel.setContent(reply.getContent());
            replyModels.add(replyModel);
        }
        blogModel.setReplies(replyModels);
        blogModel.setEmotion(blog.getEmotion());
        return blogModel;

    }

    @Override
    public void likeBlog(Long blogid,String userid) {
        // TODO: Implement likeBlog method
            blogDAO.likeBlog(blogid,userid);
    }

    @Override
    public void unlikeBlog(Long blogid, String userid) {
        // TODO: Implement unlikeBlog method
            blogDAO.unlikeBlog(blogid,userid);

    }

    @Override
    public List<BlogModel> getBlogsByUserid(String userid){
        List<Blog> blogss = blogDAO.getBlogsByUserid(userid);
        List<BlogModel> blogs = new ArrayList<>();
        for (Blog blog : blogss) {
            if(blog.getValid() == 0) continue;
            BlogModel blogModel = new BlogModel();
            blogModel.setBlogid(blog.getBlogid());
            blogModel.setUser(userDAO.getSimplified(blog.getUserid()));
            blogModel.setTimestamp(blog.getTimestamp());
            blogModel.setLikeNum(blog.getLikeNum());
            blogModel.setTitle(blog.getTitle());
            blogModel.setContent(blog.getContent());
            blogModel.setTags(Arrays.asList(blog.getTags().split(",")));
            List<Reply> replies = blogDAO.getRepliesByBlogid(blog.getBlogid());
            List<ReplyModel> replyModels = new ArrayList<>();
            for (Reply reply : replies) {
                if (reply.getValid() == 0) continue;
                ReplyModel replyModel = new ReplyModel();
                replyModel.setReplyid(reply.getReplyid());
                replyModel.setBlogid(reply.getBlogid());
                replyModel.setUser(userDAO.getSimplified(reply.getFromuserid()));
                replyModel.setFromuserid(reply.getFromuserid());
                replyModel.setTouserid(reply.getTouserid());
                replyModel.setTimestamp(reply.getTimestamp());
                replyModel.setContent(reply.getContent());
                replyModels.add(replyModel);
            }
            blogModel.setReplies(replyModels);
            blogModel.setEmotion(blog.getEmotion());
            blogs.add(blogModel);
        }
        return blogs;
    }

    @Override
    public List<BlogModel> getBlogRepliedByUserid(String userid){
        List<Reply> replies = blogDAO.getRepliesByUserid(userid);
        List<BlogModel> blogs = new ArrayList<>();
        Map<Long, BlogModel> blogMap = new HashMap<>();
        for (Reply reply : replies){
            if (reply.getValid() == 0) continue;
            BlogModel blogModel = getBlogById(reply.getBlogid());
            blogMap.put(reply.getBlogid(), blogModel);
        }
        for (Map.Entry<Long, BlogModel> entry : blogMap.entrySet()) {
            blogs.add(entry.getValue());
        }
        return blogs;
    }
    @Override
    public List<Like> getBlogLikedByUserid(String userid){
        return blogDAO.getBlogLikedByUserid(userid);
    }
}
