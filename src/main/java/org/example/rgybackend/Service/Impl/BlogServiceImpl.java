package org.example.rgybackend.Service.Impl;

import org.example.rgybackend.DAO.BlogDAO;
import org.example.rgybackend.DAO.CrisisAuditingDAO;
import org.example.rgybackend.DAO.EmotionDAO;
import org.example.rgybackend.DAO.NotificationPrivateDAO;
import org.example.rgybackend.DAO.UserDAO;
import org.example.rgybackend.Entity.Blog;
import org.example.rgybackend.Entity.Illegal;
import org.example.rgybackend.Entity.Like;
import org.example.rgybackend.Entity.Reply;
import org.example.rgybackend.Model.*;
import org.example.rgybackend.Service.BlogService;
import org.example.rgybackend.Service.UserService;
import org.example.rgybackend.Service.MilestoneServive;
import org.example.rgybackend.Utils.BERTModel;
import org.example.rgybackend.Utils.CacheUtil;
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
    private UserService userService;

    @Autowired
    private EmotionDAO emotionDAO;

    @Autowired
    private BERTModel bertModel;

    @Autowired
    private NotificationPrivateDAO notificationPrivateDAO;

    @Autowired
    private CrisisAuditingDAO crisisAuditingDAO;

    @Autowired
    private CacheUtil cacheUtil;

    @Autowired
    private MilestoneServive milestoneServive;

    @Override
    public void addBlog(String title, String content, List<String> tags, String author) {
        milestoneServive.addMilestone(author, 2L);
        
        Long justify = bertModel.justify(content);

        if(justify == 1) {
            NotificationPrivateModel notification = new NotificationPrivateModel(NotificationUtil.psyAssist);
            notification.setAdminid("System");
            notification.setUserid(author);
            notificationPrivateDAO.addNotification(notification);
        }

        else if(justify >= 2) {
            if(justify >= 4) {
                NotificationPrivateModel notification = new NotificationPrivateModel(NotificationUtil.crisis);
                notification.setAdminid("System");
                notification.setUserid(author);
                notificationPrivateDAO.addNotification(notification);
            }
            Long timestamp = System.currentTimeMillis();
            Long likeNum = 0L;
            int emotion = 0;
            if (emotionDAO.getEmotion(author, LocalDate.now()).getTag() == null)
                emotion = 0;
            else emotion = emotionDAO.getEmotion(author, LocalDate.now()).getTag().getId().intValue();
            BlogModel blogModel = new BlogModel(null, author,null,null, timestamp, likeNum, title, content, tags, new ArrayList<>(), emotion, timestamp, 0L, 0);
            Blog blog = blogDAO.addBlog(blogModel, 1);
            CrisisAuditingModel crisisAuditingModel = new CrisisAuditingModel(null, author, title + '\n' + content, TimeUtil.now(), blog.getBlogid(), 0L, justify - 2);
            crisisAuditingDAO.addCrisis(crisisAuditingModel);
            return;
        }

        Long timestamp = System.currentTimeMillis();
        Long likeNum = 0L;
        int emotion = 0;
        if (emotionDAO.getEmotion(author, LocalDate.now()).getTag() == null)
            emotion = 0;
        else emotion = emotionDAO.getEmotion(author, LocalDate.now()).getTag().getId().intValue();
        BlogModel blogModel = new BlogModel(null, author,null,null, timestamp, likeNum, title, content, tags, new ArrayList<>(), emotion, timestamp, 0L, 1);
        blogDAO.addBlog(blogModel, 1);
    }

    @Override
    public void deleteBlog(Long blogid, int type) {
        blogDAO.deleteBlog(blogid);
        if(type == 0) {
            addBlockingRecord(blogid,0);
        }
    }

    @Override
    public void addBlockingRecord(Long contentid,int type) {
        blogDAO.addBlockingRecord(contentid,"管理员屏蔽", type);
    }

    @Override
    public ReplyModel addReply(Long blogid, String content, String author) {
        String fromuserid = author;
        String touserid = getBlogById(blogid).getUser().getUserid();

        Long justify = bertModel.justify(content);

        if(justify == 1) {
            NotificationPrivateModel notification = new NotificationPrivateModel(NotificationUtil.psyAssist);
            notification.setAdminid("System");
            notification.setUserid(author);
            notificationPrivateDAO.addNotification(notification);
        }

        else if(justify >= 2) {
            if(justify >= 4) {
                NotificationPrivateModel notification = new NotificationPrivateModel(NotificationUtil.crisis);
                notification.setAdminid("System");
                notification.setUserid(author);
                notificationPrivateDAO.addNotification(notification);
            }
            Long timestamp = System.currentTimeMillis();
            ReplyModel replyModel = new ReplyModel(null, blogid, fromuserid, touserid, timestamp, content,null);
            Reply reply = blogDAO.addReply(replyModel,1);
            CrisisAuditingModel crisisAuditingModel = new CrisisAuditingModel(null, author, content, TimeUtil.now(), reply.getReplyid(), 1L, justify - 2);
            crisisAuditingDAO.addCrisis(crisisAuditingModel);
            return new ReplyModel();
        }

        cacheUtil.evictIntimateUsersCache(fromuserid);
        cacheUtil.evictIntimateUsersCache(touserid);

        Long timestamp = System.currentTimeMillis();
        ReplyModel replyModel = new ReplyModel(null, blogid, fromuserid, touserid, timestamp, content, null);
        blogDAO.addReply(replyModel,1);
        return replyModel;
    }

    @Override
    public void deleteReply(Long replyid, int type) {
        Reply reply = blogDAO.getReplyById(replyid);

        cacheUtil.evictIntimateUsersCache(reply.getFromuserid());
        cacheUtil.evictIntimateUsersCache(reply.getTouserid());

        blogDAO.deleteReply(replyid);
        if(type == 0) {
            addBlockingRecord(replyid,1);
        } 
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
            blogModel.setUserid(blog.getUserid());
            String username = userService.getProfileTag(blog.getUserid()).getUsername();
            blogModel.setUsername(username);
            blogModel.setTimestamp(blog.getTimestamp());
            blogModel.setLikeNum(blog.getLikeNum());
            blogModel.setTitle(blog.getTitle());
            blogModel.setContent(blog.getContent());
            blogModel.setTags(Arrays.asList(blog.getTags().split(",")));
            blogModel.setEmotion(blog.getEmotion());
            blogModel.setBrowsenum(blog.getBrowsenum());
            blogModel.setLastreply(blog.getLastreply());
            blogModel.setValid(blog.getValid());
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
        for(BlogModel blogModel : blogsRet.getBlogs())
        {
            blogModel.setUser(userDAO.getSimplified(blogModel.getUserid()));
        }
        return blogsRet;
    }

    public BlogsRet getLatestBlogs(int pageSize, int currentPage, String titleOrAuthor, List<String> tags){
        List<Blog> blogss = blogDAO.getAllBlogs();
        BlogsRet blogsRet = new BlogsRet();
        List<BlogModel> blogs = new ArrayList<>();
        for (Blog blog : blogss) {
            if(blog.getValid() == 0) continue;
            BlogModel blogModel = new BlogModel();
            blogModel.setBlogid(blog.getBlogid());
            blogModel.setUserid(blog.getUserid());
            blogModel.setUsername(userService.getProfileTag(blog.getUserid()).getUsername());
            blogModel.setTimestamp(blog.getTimestamp());
            blogModel.setLikeNum(blog.getLikeNum());
            blogModel.setTitle(blog.getTitle());
            blogModel.setContent(blog.getContent());
            blogModel.setTags(Arrays.asList(blog.getTags().split(",")));
            blogModel.setEmotion(blog.getEmotion());
            blogModel.setBrowsenum(blog.getBrowsenum());
            blogModel.setLastreply(blog.getLastreply());
            blogModel.setValid(blog.getValid());
            blogs.add(blogModel);
        }
        //对所有blogs进行筛选，只选择blog的标题或作者包含titleOrAuthor的博客，且标签包含tags中的所有标签的博客
        List<BlogModel> filteredBlogs = filterBlogs(blogs, titleOrAuthor, tags);
        int total = filteredBlogs.size();
        blogsRet.setTotal(total);
        filteredBlogs.sort(Comparator.comparing(BlogModel::getLastreply).reversed());
        int start = (currentPage - 1) * pageSize;
        int end = start + pageSize;
        if (start >= filteredBlogs.size()) {
            blogsRet.setBlogs(new ArrayList<>());
            return blogsRet;
        }
        if (end > filteredBlogs.size()) {
            end = filteredBlogs.size();
        }

        blogsRet.setBlogs(filteredBlogs.subList(start, end));
        for(BlogModel blogModel : blogsRet.getBlogs())
        {
            blogModel.setUser(userDAO.getSimplified(blogModel.getUserid()));
        }
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
            similarityMap.put(blog.getBlogid(), (long) similarity * (10L + blog.getLikeNum() + blog.getBrowsenum()/10));
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
            if (blog.getTitle().contains(titleOrAuthor) || blog.getUsername().contains(titleOrAuthor)) {
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
        blogModel.setValid(blog.getValid());
        blogModel.setBrowsenum(blog.getBrowsenum());
        blogModel.setTags(Arrays.asList(blog.getTags().split(",")));
        //获得blog的reply
        List<Reply> replies = blogDAO.getRepliesByBlogid(blog.getBlogid());
        List<ReplyModel> replyModels = new ArrayList<>(replies.size());
        for (Reply reply : replies) {
            if (reply.getValid() == 0) continue;
            replyModels.add(null);

        }
        blogModel.setReplies(replyModels);
        blogModel.setEmotion(blog.getEmotion());
        return blogModel;

    }

    @Override
    public void likeBlog(Long blogid, String userid) {
        String touserid = getBlogById(blogid).getUser().getUserid();

        milestoneServive.addMilestone(touserid, 3L);

        cacheUtil.evictIntimateUsersCache(userid);
        cacheUtil.evictIntimateUsersCache(touserid);

        blogDAO.likeBlog(blogid, userid);
    }

    @Override
    public void unlikeBlog(Long blogid, String userid) {
        String touserid = getBlogById(blogid).getUser().getUserid();

        cacheUtil.evictIntimateUsersCache(userid);
        cacheUtil.evictIntimateUsersCache(touserid);
        
        blogDAO.unlikeBlog(blogid,userid);

    }

    @Override
    public List<SimplifiedBlogModel> getBlogsByUserid(String userid){
        List<Blog> blogss = blogDAO.getBlogsByUserid(userid);
        List<SimplifiedBlogModel> blogs = new ArrayList<>();
        for (Blog blog : blogss) {
            if(blog.getValid() == 0) continue;
            SimplifiedBlogModel blogModel = new SimplifiedBlogModel();
            blogModel.setBlogid(blog.getBlogid());
            blogModel.setTimestamp(blog.getTimestamp());
            blogModel.setLikeNum(blog.getLikeNum());
            blogModel.setTitle(blog.getTitle());
            blogModel.setContent(blog.getContent());
            blogModel.setTags(Arrays.asList(blog.getTags().split(",")));
            blogModel.setUser(userService.getProfileTag(blog.getUserid()));
            blogModel.setEmotion(blog.getEmotion());
            blogModel.setValid(blog.getValid());
            blogModel.setBrowsenum(blog.getBrowsenum());
            List<Reply> replies = blogDAO.getRepliesByBlogid(blog.getBlogid());
            List<SimplifiedReplyModel> replyModels = new ArrayList<>(replies.size());
            for (Reply reply : replies) {
                if (reply.getValid() == 0) continue;
                replyModels.add(null);
            }
            blogModel.setReplies(replyModels);
            blogs.add(blogModel);
        }
        return blogs;
    }

    @Override
    public List<SimplifiedBlogModel> getBlogRepliedByUserid(String userid){
        List<Reply> replies = blogDAO.getRepliesByUserid(userid);
        List<SimplifiedBlogModel> blogs = new ArrayList<>();
        Map<Long, SimplifiedBlogModel> blogMap = new HashMap<>();
        for (Reply reply : replies){
            if (reply.getValid() == 0) continue;
            SimplifiedBlogModel blogModel = getSimplifiedBlogById(reply.getBlogid());
            blogMap.put(reply.getBlogid(), blogModel);
        }
        for (Map.Entry<Long, SimplifiedBlogModel> entry : blogMap.entrySet()) {
            blogs.add(entry.getValue());
        }
        return blogs;
    }

    @Override
    public SimplifiedBlogModel getSimplifiedBlogById(Long blogid) {
        Blog blog = blogDAO.getBlogById(blogid);
        SimplifiedBlogModel blogModel = new SimplifiedBlogModel();
        blogModel.setBlogid(blog.getBlogid());
        blogModel.setUser(userService.getProfileTag(blog.getUserid()));
        blogModel.setTimestamp(blog.getTimestamp());
        blogModel.setLikeNum(blog.getLikeNum());
        blogModel.setTitle(blog.getTitle());
        blogModel.setContent(blog.getContent());
        blogModel.setValid(blog.getValid());
        blogModel.setBrowsenum(blog.getBrowsenum());
        blogModel.setTags(Arrays.asList(blog.getTags().split(",")));
        List<Reply> replies = blogDAO.getRepliesByBlogid(blog.getBlogid());
        List<SimplifiedReplyModel> replyModels = new ArrayList<>(replies.size());
        for (Reply reply : replies) {
            if (reply.getValid() == 0) continue;
            replyModels.add(null);
        }
        blogModel.setReplies(replyModels);
        blogModel.setEmotion(blog.getEmotion());
        return blogModel;

    }

    @Override
    public List<Like> getBlogLikedByUserid(String userid) {
        return blogDAO.getBlogLikedByUserid(userid);
    }

    @Override
    public void addBrowsenum(Long blogid){
        blogDAO.addBrowsenum(blogid);
    }

    @Override
    public void reportBlog(Long blogid, String reason){
        blogDAO.reportBlog(blogid, reason);
    }


    @Override
    public void reportReply(Long replyid, String reason){
        blogDAO.reportReply(replyid, reason);
    }
    @Override
    public List<IllegalModel> getIllegalBlogs(){
        List<Illegal> illegals = blogDAO.getByType(0);
        List<IllegalModel> illegalBlogs = new ArrayList<>();
        for (Illegal illegal : illegals) {
            IllegalModel illegalModel = new IllegalModel();
            illegalModel.setIllegalid(illegal.getIllegalid());
            illegalModel.setType(illegal.getType());
            illegalModel.setContentid(illegal.getContentid());
            illegalModel.setUser(userService.getProfileTag(illegal.getUserid()));
            illegalModel.setTimestamp(illegal.getTimestamp());
            illegalModel.setReason(illegal.getReason());
            illegalModel.setStatus(illegal.getStatus());
            illegalModel.setBlogid(illegal.getContentid());
            illegalModel.setContent(getBlogById(illegal.getContentid()).getContent());
            illegalModel.setTitle(getBlogById(illegal.getContentid()).getTitle());
            illegalBlogs.add(illegalModel);
        }
        //将illegalBlogs按时间排序
        illegalBlogs.sort(Comparator.comparing(IllegalModel::getTimestamp).reversed());
        return illegalBlogs;
    }

    @Override
    public List<IllegalModel> getIllegalReplies(){
        List<Illegal> illegals = blogDAO.getByType(1);
        List<IllegalModel> illegalReplies = new ArrayList<>();
        for (Illegal illegal : illegals) {
            IllegalModel illegalModel = new IllegalModel();
            illegalModel.setIllegalid(illegal.getIllegalid());
            illegalModel.setType(illegal.getType());
            illegalModel.setContentid(illegal.getContentid());
            illegalModel.setUser(userService.getProfileTag(illegal.getUserid()));
            illegalModel.setTimestamp(illegal.getTimestamp());
            illegalModel.setReason(illegal.getReason());
            illegalModel.setStatus(illegal.getStatus());
            illegalModel.setTitle("");
            illegalModel.setBlogid(blogDAO.getReplyById(illegal.getContentid()).getBlogid());
            illegalModel.setContent(blogDAO.getReplyContentById(illegal.getContentid()));
            illegalReplies.add(illegalModel);
        }
        //将illegalReplies按时间排序
        illegalReplies.sort(Comparator.comparing(IllegalModel::getTimestamp).reversed());
        return illegalReplies;
    }


    @Override
    public void setIllegalStatus(int illegalid, int i){
        blogDAO.setIllegalStatus(illegalid, i);
    }

    @Override
    public void deleteIllegal(int illegalid){
        blogDAO.deleteIllegal(illegalid);
    }

    @Override
    public List<SimplifiedReplyModel> getRepliesByUserid(String userid){
        List<Reply> replies = blogDAO.getRepliesByUserid(userid);
        List<SimplifiedReplyModel> replyModels = new ArrayList<>();
        for (Reply reply : replies) {
            if (reply.getValid() == 0) continue;
            SimplifiedReplyModel replyModel = new SimplifiedReplyModel();
            replyModel.setReplyid(reply.getReplyid());
            replyModel.setBlogid(reply.getBlogid());
            replyModel.setUser(userService.getProfileTag(reply.getFromuserid()));
            replyModel.setFromuserid(reply.getFromuserid());
            replyModel.setTouserid(reply.getTouserid());
            replyModel.setTimestamp(reply.getTimestamp());
            replyModel.setContent(reply.getContent());
            replyModels.add(replyModel);
        }
        return replyModels;
    }
    @Override
    public List<ReplyModel> getRepliesByBlogid(Long blogid, int pageSize,int currentPage){
        List<Reply> replies = blogDAO.getRepliesByBlogid(blogid);
        List<ReplyModel> replyModels = new ArrayList<>();
        List<ReplyModel> result = new ArrayList<>();
        for (Reply reply : replies) {
            if (reply.getValid() == 0) continue;
            ReplyModel replyModel = new ReplyModel();
            replyModel.setReplyid(reply.getReplyid());
            replyModel.setBlogid(reply.getBlogid());
            replyModel.setFromuserid(reply.getFromuserid());
            replyModel.setTouserid(reply.getTouserid());
            replyModel.setTimestamp(reply.getTimestamp());
            replyModel.setContent(reply.getContent());
            replyModels.add(replyModel);
        }
        replyModels.sort(Comparator.comparing(ReplyModel::getTimestamp).reversed());
        int start = (currentPage - 1) * pageSize;
        int end = start + pageSize;
        if (start >= replyModels.size()) {
            replyModels = new ArrayList<>();
            return replyModels;
        }
        if (end > replyModels.size()) {
            end = replyModels.size();
        }
        result = replyModels.subList(start, end);
        for(ReplyModel replyModel : result)
        {
            replyModel.setUser(userService.getSimplifiedProfile(replyModel.getFromuserid()));
        }
        return result;
    }

}
