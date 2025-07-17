package org.example.rgybackend.Controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.example.rgybackend.Model.NotificationPrivateModel;
import org.example.rgybackend.Model.NotificationSentModel;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory; 

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NotificationControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private NotificationController notificationController;

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
    @NoArgsConstructor
    @AllArgsConstructor
    public class NotificationMultiple {
        private Long type;
        private String title;
        private String content;
        private String priority;

        private List<String> users;
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
    }

    @Test
    @Order(1)
    void getPrivateNotification() {
        userLogin();
        //向/api/notification/get发送get请求，获取通知列表
        List<NotificationPrivateModel> notificationList = restTemplate.getForObject("/api/notification/get", List.class);
        ResponseEntity<List<NotificationPrivateModel>> response = restTemplate.exchange("/api/notification/get", HttpMethod.GET,null,new ParameterizedTypeReference<List<NotificationPrivateModel>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        //验证返回的通知列表是否为空
        assertNotNull(notificationList);
    }

    @Test
    @Order(4)
    void getPrivateNotificationMine() {
        adminLogin();
        //向/api/notification/getmine发送get请求，获取通知列表
        ResponseEntity<List<NotificationSentModel>> response = restTemplate.exchange("/api/notification/getmine", HttpMethod.GET,null,new ParameterizedTypeReference<List<NotificationSentModel>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<NotificationSentModel> notificationList = response.getBody();
        //验证返回的通知列表是否为空
        assertFalse(notificationList.isEmpty());
        assertNotNull(notificationList);
    }

    @Test
    @Order(3)
    void addPrivateNotification() {
        adminLogin();
        NotificationPrivateModel notification = new NotificationPrivateModel(null,300L,TEST_ADMIN_ID, TEST_USER_ID, "测试通知", "测试内容",null, 1L,"low");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NotificationPrivateModel> requestEntity = new HttpEntity<>(notification, headers);
        //向"/api/notification/private/add"发送请求，添加通知
//        ResponseEntity<Boolean> response = restTemplate.postForEntity("/api/notification/private/add", notification, Boolean.class);
        ResponseEntity<Boolean> response = restTemplate.exchange("/api/notification/private/add", HttpMethod.PUT, requestEntity,new ParameterizedTypeReference<Boolean>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    @Order(2)
    void addMultiplePrivateNotification() {
        adminLogin();
        NotificationMultiple notificationMultiple = new NotificationMultiple(1000L, "测试通知", "测试内容", "low", List.of(TEST_USER_ID, TEST_ADMIN_ID,TEST_PSY_ID));
        //添加消息头并组装为请求
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NotificationMultiple> requestEntity = new HttpEntity<>(notificationMultiple, headers);
        //向"/api/notification/multiple/add"发送请求，添加多个通知
        ResponseEntity<Boolean> response = restTemplate.exchange("/api/notification/multiple/add", HttpMethod.PUT, requestEntity,new ParameterizedTypeReference<Boolean>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    @Order(5)
    void markRead() {
        //向"/api/notification/markread"发送请求，标记通知为已读
        userLogin();
        AtomicReference<ResponseEntity<Boolean>> response = new AtomicReference<>(restTemplate.postForEntity("/api/notification/markread?notificationid=10", null, Boolean.class));

        assertEquals(HttpStatus.OK, response.get().getStatusCode());
        assertTrue(response.get().getBody());
        assertThrows(Exception.class, () -> {
            response.set(restTemplate.postForEntity("/api/notification/markread?notificationid=1", null, Boolean.class));
        });



    }

    @Test
    @Order(6)
    void markAllPrivateRead() {
        userLogin();
        //向"/api/notification/private/markallread"发送请求，标记所有私人通知为已读
        ResponseEntity<Boolean> response = restTemplate.postForEntity("/api/notification/private/markallread", null, Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    @Order(7)
    void markAllPublicRead() {
        userLogin();
        //向"/api/notification/public/markallread"发送请求，标记所有公共通知为已读
        ResponseEntity<Boolean> response = restTemplate.postForEntity("/api/notification/public/markallread", null, Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());

    }

    @Test
    @Order(8)
    void deleteNotification() {
        userLogin();
        //向"/api/notification/private/del"发送请求，删除通知
        ResponseEntity<Boolean> response = restTemplate.exchange("/api/notification/private/del?notificationid=2", HttpMethod.DELETE,null,Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }
}