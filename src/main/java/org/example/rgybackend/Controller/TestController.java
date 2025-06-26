package org.example.rgybackend.Controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {
   @RequestMapping("/blogs/getById/{id}")
    public String getBlog(@PathVariable("id") int id) {
        JSONObject response = new JSONObject();
        response.put("blogid", id);
        response.put("title", "My Blog");
        response.put("userid", "John Doe");
        //likeNum
        response.put("likeNum", 100);
        response.put("content", "这是我的博客文章。它与技术和编程有关。我希望你喜欢它。我是 John Doe.In 这个博客，我将分享我关于编程的知识和经验。此外，我会写下我的爱好和兴趣。希望你喜欢它。这是我的博客文章。它与技术和编程有关。我希望你喜欢它。我是 John Doe.In 这个博客，我将分享我关于编程的知识和经验。此外，我会写下我的爱好和兴趣。希望你喜欢它。这是我的博客文章。它与技术和编程有关。我希望你喜欢它。这是我的博客文章。它与技术和编程有关。我希望你喜欢它。我是 John Doe.In 这个博客，我将分享我关于编程的知识和经验。此外，我会写下我的爱好和兴趣。希望你喜欢它。这是我的博客文章。它与技术和编程有关。我希望你喜欢它。我是 John Doe.In 这个博客，我将分享我关于编程的知识和经验。此外，我会写下我的爱好和兴趣。希望你喜欢它。这是我的博客文章。它与技术和编程有关。我希望你喜欢它。这是我的博客文章。它与技术和编程有关。我希望你喜欢它。我是 John Doe.In 这个博客，我将分享我关于编程的知识和经验。此外，我会写下我的爱好和兴趣。希望你喜欢它。这是我的博客文章。它与技术和编程有关。我希望你喜欢它。我是 John Doe.In 这个博客，我将分享我关于编程的知识和经验。此外，我会写下我的爱好和兴趣。希望你喜欢它。这是我的博客文章。它与技术和编程有关。我希望你喜欢它。这是我的博客文章。它与技术和编程有关。我希望你喜欢它。我是 John Doe.In 这个博客，我将分享我关于编程的知识和经验。此外，我会写下我的爱好和兴趣。希望你喜欢它。这是我的博客文章。它与技术和编程有关。我希望你喜欢它。我是 John Doe.In 这个博客，我将分享我关于编程的知识和经验。此外，我会写下我的爱好和兴趣。希望你喜欢它。这是我的博客文章。它与技术和编程有关。我希望你喜欢它。");
        response.put("timestamp", System.currentTimeMillis());
        //response.put("cover", "http://localhost:8080/api/readImg/20250624212957.jpg");
        response.put("tag", new String[] {"Technology", "Programming"});
        JSONArray reply = new JSONArray();
        for (int i = 0; i < 5; i++) {
            /*
            * reply: {
                replyid:
                blogid:
                userid:
                timestamp:
                content:
            }
            * */
            JSONObject replyObj = new JSONObject();
            replyObj.put("replyid", i);
            replyObj.put("blogid", id);
            replyObj.put("userid", "Jane Doe");
            replyObj.put("timestamp", System.currentTimeMillis());
            replyObj.put("content", "This is my reply.");
            reply.add(replyObj);
        }

        response.put("reply", reply);
        return response.toJSONString();
    }

    @RequestMapping("/blogs/get")
    public String getBlogs() {
        JSONArray response = new JSONArray();
        for (int i = 0; i < 10; i++) {
            JSONObject blog = new JSONObject();
            blog.put("blogid", i);
            blog.put("title", "My Blog ");
            blog.put("likeNum", 100);
            blog.put("userid", "John Doe");
            blog.put("content", "这是我的博客文章。它与技术和编程有关。我希望你喜欢它。我是 John Doe.In 这个博客，我将分享我关于编程的知识和经验。此外，我会写下我的爱好和兴趣。希望你喜欢它。这是我的博客文章。它与技术和编程有关。我希望你喜欢它。我是 John Doe.In 这个博客，我将分享我关于编程的知识和经验。此外，我会写下我的爱好和兴趣。希望你喜欢它。这是我的博客文章。它与技术和编程有关。我希望你喜欢它。这是我的博客文章。它与技术和编程有关。我希望你喜欢它。我是 John Doe.In 这个博客，我将分享我关于编程的知识和经验。此外，我会写下我的爱好和兴趣。希望你喜欢它。这是我的博客文章。它与技术和编程有关。我希望你喜欢它。我是 John Doe.In 这个博客，我将分享我关于编程的知识和经验。此外，我会写下我的爱好和兴趣。希望你喜欢它。这是我的博客文章。它与技术和编程有关。我希望你喜欢它。这是我的博客文章。它与技术和编程有关。我希望你喜欢它。我是 John Doe.In 这个博客，我将分享我关于编程的知识和经验。此外，我会写下我的爱好和兴趣。希望你喜欢它。这是我的博客文章。它与技术和编程有关。我希望你喜欢它。我是 John Doe.In 这个博客，我将分享我关于编程的知识和经验。此外，我会写下我的爱好和兴趣。希望你喜欢它。这是我的博客文章。它与技术和编程有关。我希望你喜欢它。这是我的博客文章。它与技术和编程有关。我希望你喜欢它。我是 John Doe.In 这个博客，我将分享我关于编程的知识和经验。此外，我会写下我的爱好和兴趣。希望你喜欢它。这是我的博客文章。它与技术和编程有关。我希望你喜欢它。我是 John Doe.In 这个博客，我将分享我关于编程的知识和经验。此外，我会写下我的爱好和兴趣。希望你喜欢它。这是我的博客文章。它与技术和编程有关。我希望你喜欢它。");
            blog.put("timestamp", System.currentTimeMillis());
            blog.put("tag", new String[] {"Technology", "Programming"});
            //cover
            blog.put("cover", "http://localhost:8080/api/readImg/DSC_4728-已增强-降噪-5.jpg");
            response.add(blog);
        }
        return response.toJSONString();
    }
    @RequestMapping("/replies/add")
    public String addReply() {
        JSONObject response = new JSONObject();
        JSONObject reply = new JSONObject();
        reply.put("replyid", 1);
        reply.put("blogid", 1);
        reply.put("userid", "Jane Doe");
        reply.put("timestamp", System.currentTimeMillis());
        reply.put("content", "This is my reply.");
        response.put("reply", reply);
        response.put("success", true);
        return response.toJSONString();
    }

    @RequestMapping("/user/getAvatar/{userid}")
    public String getAvatar(@PathVariable("userid") String userid) {
        JSONObject response = new JSONObject();
        response.put("avatar", "http://localhost:8080/api/readImg/cat.JPG");
        return response.toJSONString();
    }

}
