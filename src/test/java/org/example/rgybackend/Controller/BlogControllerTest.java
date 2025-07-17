package org.example.rgybackend.Controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.rgybackend.DTO.ProfileTag;
import org.example.rgybackend.Model.*;
import org.example.rgybackend.Utils.ForbiddenException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.example.rgybackend.Model.SimplifiedProfileModel;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BlogControllerTest {
    @Autowired
    private BlogController blogController;
    @Autowired
    private TestRestTemplate restTemplate;

    private String user_avatar;

    private static final String TEST_USERNAME = "guoxutao2";
    private static final String TEST_PASSWORD = "123456";
    private static final String TEST_USER_ID = "guoxutao2_1751976105521";
    private static final String TEST_ADMIN_USERNAME = "guoxutao";
    private static final String TEST_ADMIN_PASSWORD = "20050205gxt";
    private static final String TEST_ADMIN_ID = "guoxutao_1751548604691";
    private static final String TEST_PSY_USERNAME = "guoxutao3";
    private static final String TEST_PSY_PASSWORD = "123456";
    private static final String TEST_PSY_ID = "guoxutao3_1751984132973";
    private static final String TEST_DISABLED_USERNAME = "guoxutao4";
    private static final String TEST_DISABLED_PASSWORD = "123456";
    private static final String TEST_DISABLED_ID = "guoxutao4_1751984133974";

    private static final Long TEST_USER_JOINTIME = 1751976105612L;
    @Data
    @AllArgsConstructor
    public class BlogsRequest {
        private int pageSize;
        int currentPage;
        String searchText;
        List<String> tags;
        // 必须有无参构造函数
        public BlogsRequest() {}
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class GetRepliesForBlogRequest {
        private Long blogid;
        private int pageSize;
        private int currentPage;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class AddBlogRequest {
        private String title;
        private String content;
        private List<String> tags;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class BlogidRequest {
        private Long blogid;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class ReplyidRequest {
        private Long replyid;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class AddReplyRequest {
        private Long blogid;
        private String content;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class ReportRequest {
        private String blogid;
        private String reason;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class ReportReplyRequest {
        private String replyid;
        private String reason;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class SheldingBlogRequest {
        private String blogid;
        private String illegalid;


    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class SheldingReplyRequest {
        private Long replyid;
        private int illegalid;

    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class RecoverIllegalRequest {
        private int illegalid;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class DeleteIllegalRequest {
        private int illegalid;
    }
    @BeforeEach
    void setUp() throws IOException {
        BasicCookieStore cookieStore = new BasicCookieStore();
        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory(
                        HttpClients.custom()
                                .setDefaultCookieStore(cookieStore)
                                .build()
                );

        this.restTemplate.getRestTemplate().setRequestFactory(factory);
        user_avatar = null;
    }

    void userLogin() {
        restTemplate.getForObject("/api/user/login?username=" + TEST_USERNAME + "&password=" + TEST_PASSWORD, Boolean.class);
    }

    void adminLogin() {
        restTemplate.getForObject("/api/user/login?username=" + TEST_ADMIN_USERNAME + "&password=" + TEST_ADMIN_PASSWORD, Boolean.class);
    }

    void psyLogin() {
        restTemplate.getForObject("/api/user/login?username=" + TEST_PSY_USERNAME + "&password=" + TEST_PSY_PASSWORD, Boolean.class);
    }

    void Logout() {
        restTemplate.getForObject("/api/user/logout" , Boolean.class);
    }

    @Test
    @Order(1)
    //查找个人相关发帖，尽量保证不是空数组以覆盖全部代码
    void getMyBlogs() {
        userLogin();
        List<SimplifiedBlogModel> blogModels = new ArrayList<>();
        SimplifiedBlogModel blogModel = new SimplifiedBlogModel(2L,new ProfileTag("guoxutao2_1751976105521","guoxutao2"),1111L,1L,"1","1",new ArrayList<>(),new ArrayList<>(),1,null,2L,1);
        List <String> tags = new ArrayList<>();
        tags.add("学习");
        blogModel.setTags(tags);
        List<SimplifiedReplyModel> replyModels = new ArrayList<>();
        replyModels.add(null);
        blogModel.setReplies(replyModels);
        blogModels.add(blogModel);
        ResponseEntity<List<SimplifiedBlogModel>> response = restTemplate.exchange("/api/blogs/getmine?userid=guoxutao2_1751976105521", HttpMethod.GET,null,new ParameterizedTypeReference<List<SimplifiedBlogModel>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<SimplifiedBlogModel> result = response.getBody();
        assertNotNull(result);
    }

    @Test
    @Order(2)
    void getLikeBlogs() {
        userLogin();
        List<SimplifiedBlogModel> blogModels = new ArrayList<>();
        SimplifiedBlogModel blogModel = new SimplifiedBlogModel(2L,new ProfileTag("guoxutao2_1751976105521","guoxutao2"),1111L,1L,"1","1",new ArrayList<>(),new ArrayList<>(),1,null,2L,1);
        List <String> tags = new ArrayList<>();
        tags.add("学习");
        blogModel.setTags(tags);
        List<SimplifiedReplyModel> replyModels = new ArrayList<>();
        replyModels.add(null);
        blogModel.setReplies(replyModels);
        blogModels.add(blogModel);
        ResponseEntity<List<SimplifiedBlogModel>> response = restTemplate.exchange("/api/blogs/getlike?userid=guoxutao2_1751976105521", HttpMethod.GET,null,new ParameterizedTypeReference<List<SimplifiedBlogModel>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<SimplifiedBlogModel> result = response.getBody();
        assertNotNull(result);
    }

    @Test
    @Order(3)
    void getCommentBlogs() {
        userLogin();
        List<SimplifiedBlogModel> blogModels = new ArrayList<>();
        SimplifiedBlogModel blogModel = new SimplifiedBlogModel(2L,new ProfileTag("guoxutao2_1751976105521","guoxutao2"),1111L,1L,"1","1",new ArrayList<>(),new ArrayList<>(),1,null,2L,1);
        List <String> tags = new ArrayList<>();
        tags.add("学习");
        blogModel.setTags(tags);
        List<SimplifiedReplyModel> replyModels = new ArrayList<>();
        replyModels.add(null);
        blogModel.setReplies(replyModels);
        blogModels.add(blogModel);
        ResponseEntity<List<SimplifiedBlogModel>> response = restTemplate.exchange("/api/blogs/getcomment?userid=guoxutao2_1751976105521", HttpMethod.GET,null,new ParameterizedTypeReference<List<SimplifiedBlogModel>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<SimplifiedBlogModel> result = response.getBody();
        assertNotNull(result);
    }

    @Test
    @Order(4)
    void getReplies() {
        userLogin();
        List<SimplifiedReplyModel> replyModels = new ArrayList<SimplifiedReplyModel>();
        SimplifiedReplyModel replyModel = new SimplifiedReplyModel(1L,2L,"guoxutao2_1751976105521","guoxutao2_1751976105521",2222L,"1",new ProfileTag("guoxutao2_1751976105521","guoxutao2"));
        replyModels.add(replyModel);
        ResponseEntity<List<SimplifiedReplyModel>> response = restTemplate.exchange("/api/blogs/getreply?userid=guoxutao2_1751976105521", HttpMethod.GET,null,new ParameterizedTypeReference<List<SimplifiedReplyModel>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<SimplifiedReplyModel> result = response.getBody();
        assertNotNull(result);
    }

    //保证存在id为2的blog
    @Test
    @Order(5)
    void getRepliesForBlog() {
        userLogin();
        List<ReplyModel> replyModels = new ArrayList<ReplyModel>();
        ReplyModel replyModel = new ReplyModel(1L,2L,"guoxutao2_1751976105521","guoxutao2_1751976105521",2222L,"1",new SimplifiedProfileModel(TEST_USER_ID,TEST_USERNAME,user_avatar,"888",1751976105612L));
        replyModels.add(replyModel);
        GetRepliesForBlogRequest requestBody = new GetRepliesForBlogRequest(2L,10,1);
        // 3. 设置请求头 (JSON 格式)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 4. 封装请求体和请求头
        HttpEntity<GetRepliesForBlogRequest> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<List<ReplyModel>> response = restTemplate.exchange("/api/blogs/getrepliesforblog", HttpMethod.POST,requestEntity,new ParameterizedTypeReference<List<ReplyModel>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<ReplyModel> result = response.getBody();
        assertNotNull(result);
    }

    //保证存在tag包含"学习",标题包含“1”的blog（可更改检索条件）
    @Test
    @Order(6)
    void getAllBlogs() {
        userLogin();
        List<BlogModel> blogModels = new ArrayList<>();
        SimplifiedProfileModel simplifiedProfileModel = new SimplifiedProfileModel(TEST_USER_ID,TEST_USERNAME,user_avatar,"888",1751976105612L);
        List<String> tags = new ArrayList<>();
        tags.add("学习");
        BlogModel blogModel = new BlogModel(2L,TEST_USER_ID,simplifiedProfileModel,1111L,1L,"1","1",tags,null,1,1752724854282L,2L,1);
        blogModels.add(blogModel);
        BlogsRequest requestBody = new BlogsRequest();
        requestBody.setPageSize(10);
        requestBody.setCurrentPage(1);
        requestBody.setSearchText("1");
        requestBody.setTags(tags);

        // 3. 设置请求头 (JSON 格式)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 4. 封装请求体和请求头
        HttpEntity<BlogsRequest> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<BlogsRet> response = restTemplate.postForEntity("/api/blogs/get", requestEntity, BlogsRet.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        BlogsRet blogsRet = response.getBody();
        assertNotNull(blogsRet);
        List<BlogModel> result = blogsRet.getBlogs();
        assertNotNull(result);
    }


    @Test
    @Order(7)
    void getLatestBlogs() {
        userLogin();
        List<BlogModel> blogModels = new ArrayList<>();
        SimplifiedProfileModel simplifiedProfileModel = new SimplifiedProfileModel(TEST_USER_ID,TEST_USERNAME,user_avatar,"888",1751976105612L);
        List<String> tags = new ArrayList<>();
        tags.add("学习");
        BlogModel blogModel = new BlogModel(2L,TEST_USER_ID,simplifiedProfileModel,1111L,1L,"1","1",tags,null,1,1752724854282L,2L,1);
        blogModels.add(blogModel);
        BlogsRequest requestBody = new BlogsRequest();
        requestBody.setPageSize(10);
        requestBody.setCurrentPage(1);
        requestBody.setSearchText("1");
        requestBody.setTags(tags);

        // 3. 设置请求头 (JSON 格式)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 4. 封装请求体和请求头
        HttpEntity<BlogsRequest> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<BlogsRet> response = restTemplate.postForEntity("/api/blogs/getLatest", requestEntity,BlogsRet.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        BlogsRet blogsRet = response.getBody();
        assertNotNull(blogsRet);
        List<BlogModel> result = blogsRet.getBlogs();
        assertNotNull(result);

    }

    @Test
    @Order(8)
    void getBlogById() {
        userLogin();
        SimplifiedProfileModel simplifiedProfileModel = new SimplifiedProfileModel(TEST_USER_ID,TEST_USERNAME,user_avatar,"888",1751976105612L);
        BlogModel blogModel = new BlogModel(2L,null,new SimplifiedProfileModel(TEST_USER_ID,TEST_USERNAME,user_avatar,"888",1751976105612L),1111L,1L,"1","1",new ArrayList<>(),null,1,null,2L,1);
        List<String> tags = new ArrayList<>();
        tags.add("学习");
        blogModel.setTags(tags);
        List<ReplyModel> replyModels = new ArrayList<>();
        replyModels.add(null);
        blogModel.setReplies(replyModels);
        ResponseEntity<BlogModel> response = restTemplate.exchange("/api/blogs/getById/2", HttpMethod.GET,null,BlogModel.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        BlogModel result = response.getBody();
        assertNotNull(result);
    }


    @Test
    @Order(9)
    void addBlog() {
         userLogin();

         //测试普通内容
        List<String> tags = new ArrayList<>();
        tags.add("学习");
        AddBlogRequest requestBody = new AddBlogRequest("测试标题","心情不错",tags);
        // 3. 设置请求头 (JSON 格式)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 4. 封装请求体和请求头
        HttpEntity<AddBlogRequest> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Boolean> response = restTemplate.exchange("/api/blogs/add", HttpMethod.POST,requestEntity,Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Boolean result = response.getBody();
        assertEquals(true, result);
        
        //测试L1分级内容
        tags = new ArrayList<>();
        tags.add("学习");
        requestBody = new AddBlogRequest("测试标题2","今天的心情不太好",tags);
        // 3. 设置请求头 (JSON 格式)
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 4. 封装请求体和请求头
        requestEntity = new HttpEntity<>(requestBody, headers);
        response = restTemplate.exchange("/api/blogs/add", HttpMethod.POST,requestEntity,Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        result = response.getBody();
        assertEquals(true, result);
       
        //测试L2分级内容
        tags = new ArrayList<>();
        tags.add("学习");
        requestBody = new AddBlogRequest("测试标题3","想死",tags);
        // 3. 设置请求头 (JSON 格式)
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 4. 封装请求体和请求头
        requestEntity = new HttpEntity<>(requestBody, headers);
        response = restTemplate.exchange("/api/blogs/add", HttpMethod.POST,requestEntity,Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        result = response.getBody();
        assertEquals(true, result);
    }

    @Test
    @Order(24)
    void deleteBlog() {
        adminLogin();
        BlogidRequest requestBody = new BlogidRequest(2L);
        // 3. 设置请求头 (JSON 格式)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 4. 封装请求体和请求头
        HttpEntity<BlogidRequest> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Boolean> response = restTemplate.exchange("/api/blogs/delete", HttpMethod.POST, requestEntity, Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Boolean result = response.getBody();
        assertEquals(true, result);
        //从数据库中查询是否删除成功
        BlogModel blogModel = blogController.getBlogById("2");
        assertEquals(blogModel.getValid(), 0);
    }

    @Test
    @Order(10)
    void addReply() {
        userLogin();
        AddReplyRequest requestBody = new AddReplyRequest(2L,"普通回复");
        // 3. 设置请求头 (JSON 格式)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 4. 封装请求体和请求头
        HttpEntity<AddReplyRequest> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<ReplyModel> response = restTemplate.exchange("/api/blogs/addReply", HttpMethod.POST,requestEntity,ReplyModel.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ReplyModel result = response.getBody();
        assertEquals("普通回复", result.getContent());
        assertEquals(TEST_USER_ID, result.getFromuserid());
        assertEquals(2L, result.getBlogid());

        //测试L1分级回复
        requestBody = new AddReplyRequest(2L,"今天心情不太好");
        // 3. 设置请求头 (JSON 格式)
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 4. 封装请求体和请求头
        requestEntity = new HttpEntity<>(requestBody, headers);
        response = restTemplate.exchange("/api/blogs/addReply", HttpMethod.POST,requestEntity,ReplyModel.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        result = response.getBody();
        assertEquals("今天心情不太好", result.getContent());
        assertEquals(TEST_USER_ID, result.getFromuserid());

        assertEquals(2L, result.getBlogid());
        //测试L2分级回复
        requestBody = new AddReplyRequest(2L,"想死");
        // 3. 设置请求头 (JSON 格式)
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 4. 封装请求体和请求头
        requestEntity = new HttpEntity<>(requestBody, headers);
        response = restTemplate.exchange("/api/blogs/addReply", HttpMethod.POST,requestEntity,ReplyModel.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        result = response.getBody();
        assertEquals("想死", result.getContent());
        assertEquals(TEST_USER_ID, result.getFromuserid());

        assertEquals(2L, result.getBlogid());
    }

    @Test
    @Order(23)
    void deleteReply() {
        adminLogin();
        ReplyidRequest requestBody = new ReplyidRequest(1L);
        // 3. 设置请求头 (JSON 格式)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 4. 封装请求体和请求头
        HttpEntity<ReplyidRequest> requestEntity = new HttpEntity<>(requestBody, headers);
        AtomicReference<ResponseEntity<Boolean>> response = new AtomicReference<>(restTemplate.exchange("/api/blogs/deleteReply", HttpMethod.POST, requestEntity, Boolean.class));
        assertEquals(HttpStatus.OK, response.get().getStatusCode());
        Boolean result = response.get().getBody();
        assertEquals(true, result);
        Logout();
        //测试普通用户能否删除回复

        userLogin();
        requestBody = new ReplyidRequest(1L);
        // 3. 设置请求头 (JSON 格式)
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 4. 封装请求体和请求头
        requestEntity = new HttpEntity<>(requestBody, headers);
        //会抛出ForbiddenException

        HttpEntity<ReplyidRequest> finalRequestEntity = requestEntity;
        assertThrows(RestClientException.class, () -> {
            response.set(restTemplate.exchange("/api/blogs/deleteReply", HttpMethod.POST, finalRequestEntity, Boolean.class));
        });
    }

    @Test
    @Order(11)
    void getIfLiked() {
        userLogin();
        BlogidRequest requestBody = new BlogidRequest(2L);
        // 3. 设置请求头 (JSON 格式)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 4. 封装请求体和请求头
        HttpEntity<BlogidRequest> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Boolean> response = restTemplate.exchange("/api/blogs/getIfLiked", HttpMethod.POST,requestEntity,Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Boolean result = response.getBody();
        assertEquals(false, result);

    }

    @Test
    @Order(12)
    void likeBlog() {
        userLogin();
        BlogidRequest requestBody = new BlogidRequest(2L);
        // 3. 设置请求头 (JSON 格式)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 4. 封装请求体和请求头
        HttpEntity<BlogidRequest> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Boolean> response = restTemplate.exchange("/api/blogs/like", HttpMethod.POST,requestEntity,Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Boolean result = response.getBody();
        assertEquals(true, result);
    }

    @Test
    @Order(13)
    void unlikeBlog() {
        userLogin();
        BlogidRequest requestBody = new BlogidRequest(2L);
        // 3. 设置请求头 (JSON 格式)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 4. 封装请求体和请求头
        HttpEntity<BlogidRequest> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Boolean> response = restTemplate.exchange("/api/blogs/cancellike", HttpMethod.POST,requestEntity,Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Boolean result = response.getBody();
        assertEquals(true, result);
    }

    @Test
    @Order(14)
    void addBrowsenum() {
        userLogin();
        BlogidRequest requestBody = new BlogidRequest(2L);
        // 3. 设置请求头 (JSON 格式)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 4. 封装请求体和请求头
        HttpEntity<BlogidRequest> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Boolean> response = restTemplate.exchange("/api/blogs/addBrowsenum", HttpMethod.POST,requestEntity,Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Boolean result = response.getBody();
        assertEquals(true, result);
        //从数据库中查询是否添加成功
        BlogModel blogModel = blogController.getBlogById("2");
        //assertEquals(blogModel.getBrowsenum(), 3);
    }

    @Test
    @Order(15)
    void reportBlog() {
        userLogin();
        ReportRequest requestBody = new ReportRequest("27","垃圾广告");
        // 3. 设置请求头 (JSON 格式)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 4. 封装请求体和请求头
        HttpEntity<ReportRequest> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Boolean> response = restTemplate.exchange("/api/blogs/report", HttpMethod.POST,requestEntity,Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Boolean result = response.getBody();
        assertEquals(true, result);

    }

    @Test
    @Order(16)
    void reportReply() {
        userLogin();
        ReportReplyRequest requestBody = new ReportReplyRequest("28","垃圾广告");
        // 3. 设置请求头 (JSON 格式)
        // 3. 设置请求头 (JSON 格式)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 4. 封装请求体和请求头
        HttpEntity<ReportReplyRequest> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Boolean> response = restTemplate.exchange("/api/blogs/reportReply", HttpMethod.POST,requestEntity,Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Boolean result = response.getBody();
        assertEquals(true, result);
    }

    @Test
    @Order(17)
    void getIllegalBlogs() {
        adminLogin();

        ResponseEntity<List<IllegalModel>> response = restTemplate.exchange("/api/blogs/getIllegalBlogs", HttpMethod.GET, null, new ParameterizedTypeReference<List<IllegalModel>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<IllegalModel> result = response.getBody();
        assertNotNull(result);
    }

    @Test
    @Order(18)
    void getIllegalReplies() {
        adminLogin();

        ResponseEntity<List<IllegalModel>> response = restTemplate.exchange("/api/blogs/getIllegalReplies", HttpMethod.GET, null, new ParameterizedTypeReference<List<IllegalModel>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<IllegalModel> result = response.getBody();
        assertNotNull(result);
    }

    @Test
    @Order(19)
    void sheldingBlog() {
        adminLogin();

        SheldingBlogRequest requestBody = new SheldingBlogRequest("27","1");
        // 3. 设置请求头 (JSON 格式)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 4. 封装请求体和请求头
        HttpEntity<SheldingBlogRequest> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Boolean> response = restTemplate.exchange("/api/blogs/sheldingBlog", HttpMethod.POST,requestEntity,Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Boolean result = response.getBody();
        assertEquals(true, result);

    }

    @Test
    @Order(20)
    void sheldingReply() {
        adminLogin();
        //向"/api/blogs/sheldingReply"发送请求，屏蔽回复
        SheldingReplyRequest requestBody = new SheldingReplyRequest(28L,2);
        // 3. 设置请求头 (JSON 格式)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 4. 封装请求体和请求头
        HttpEntity<SheldingReplyRequest> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Boolean> response = restTemplate.exchange("/api/blogs/sheldingReply", HttpMethod.POST,requestEntity,Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Boolean result = response.getBody();
        assertEquals(true, result);
    }

    @Test
    @Order(21)
    void recoverIllegal() {
        adminLogin();
        //向"/api/blogs/recoverIllegal"发送请求，恢复违规内容
        RecoverIllegalRequest requestBody = new RecoverIllegalRequest(1);
        // 3. 设置请求头 (JSON 格式)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 4. 封装请求体和请求头
        HttpEntity<RecoverIllegalRequest> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Boolean> response = restTemplate.exchange("/api/blogs/recoverIllegal", HttpMethod.POST,requestEntity,Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Boolean result = response.getBody();
        assertEquals(true, result);
    }

    @Test
    @Order(22)
    void deleteIllegal() {
        adminLogin();
        //向"/api/blogs/deleteIllegal"发送请求，删除违规内容
        DeleteIllegalRequest requestBody = new DeleteIllegalRequest(2);
        // 3. 设置请求头 (JSON 格式)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 4. 封装请求体和请求头
        HttpEntity<DeleteIllegalRequest> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Boolean> response = restTemplate.exchange("/api/blogs/deleteIllegal", HttpMethod.POST,requestEntity,Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Boolean result = response.getBody();
        assertEquals(true, result);
    }
}