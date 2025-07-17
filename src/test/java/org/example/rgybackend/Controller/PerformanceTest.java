package org.example.rgybackend.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.example.rgybackend.DTO.IntimateDTO;
import org.example.rgybackend.Model.AdminProfileModel;
import org.example.rgybackend.Model.ProfileModel;
import org.example.rgybackend.Model.UserModel;
import org.example.rgybackend.Utils.ErrorResponse;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PerformanceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String TEST_USERNAME = "TEST_USER";
    private static final String TEST_PASSWORD = "123456";
    private static final String TEST_ADMIN_USERNAME = "TEST_ADMIN";
    private static final String TEST_ADMIN_PASSWORD = "123456";
    private static final String TEST_PSY_USERNAME = "TEST_PSY";
    private static final String TEST_PSY_PASSWORD = "123456";
    private static final String TEST_EMAIL = "zsb_sjtu@sjtu.edu.cn";
    private static final String TEST_STUID = "523031910080";

    private static final int CONCURRENT_USERS = 1000;
    private static final int THREAD_POOL_SIZE = 50;

    @BeforeEach
    void setUp() {
        BasicCookieStore cookieStore = new BasicCookieStore();
        HttpComponentsClientHttpRequestFactory factory =
            new HttpComponentsClientHttpRequestFactory(
                HttpClients.custom()
                    .setDefaultCookieStore(cookieStore)
                    .build()
            );

        this.restTemplate.getRestTemplate().setRequestFactory(factory);
    }

    private TestRestTemplate createRestTemplate() {
        BasicCookieStore cookieStore = new BasicCookieStore();
        HttpComponentsClientHttpRequestFactory factory =
            new HttpComponentsClientHttpRequestFactory(
                HttpClients.custom()
                    .setDefaultCookieStore(cookieStore)
                    .build()
            );
        
        TestRestTemplate restTemplate = new TestRestTemplate();
        restTemplate.getRestTemplate().setRequestFactory(factory);
        return restTemplate;
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

    @Test
    void concurrentLoginStressTest() throws InterruptedException {
        System.out.println("=== 开始1000用户并发登录压力测试 ===");
        
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(CONCURRENT_USERS);
        
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        AtomicLong totalResponseTime = new AtomicLong(0);
        
        long testStartTime = System.currentTimeMillis();
        
        for(int i = 1; i <= CONCURRENT_USERS; i++) {
            final int ID = i;
            executor.submit(() -> {
                try {
                    startLatch.await();
                    
                    TestRestTemplate userRestTemplate = createRestTemplate();
                    long requestStart = System.nanoTime();
                    
                    String username = "New User " + ID;
                    Boolean result = userRestTemplate.getForObject(
                        "/api/user/login?username=" + username + "&password=" + TEST_PASSWORD, 
                        Boolean.class
                    );
                    
                    long requestEnd = System.nanoTime();
                    long responseTime = (requestEnd - requestStart) / 1000000;
                    
                    if(result != null && result) {
                        successCount.incrementAndGet();
                        totalResponseTime.addAndGet(responseTime);
                    } 
                    else {
                        failureCount.incrementAndGet();
                    }
                    
                    if(ID % 100 == 0) {
                        System.out.printf("已完成 %d 个用户登录测试%n", ID);
                    }

                } catch (Exception e) {
                    failureCount.incrementAndGet();
                } finally {
                    finishLatch.countDown();
                }
            });
        }
        
        startLatch.countDown();
        finishLatch.await(3, TimeUnit.MINUTES);
        long testEndTime = System.currentTimeMillis();
        
        executor.shutdown();
        
        System.out.println("=== 登录压力测试结果 ===");
        System.out.printf("总耗时: %.2f 秒%n", (testEndTime - testStartTime) / 1000.0);
        System.out.printf("成功登录: %d%n", successCount.get());
        System.out.printf("失败登录: %d%n", failureCount.get());
        System.out.printf("登录成功率: %.2f%%%n", (successCount.get() * 100.0 / CONCURRENT_USERS));
        
        if (successCount.get() > 0) {
            long avgResponseTime = totalResponseTime.get() / successCount.get();
            System.out.printf("平均登录响应时间: %d 毫秒%n", avgResponseTime);
            
            double throughput = successCount.get() / ((testEndTime - testStartTime) / 1000.0);
            System.out.printf("登录吞吐量: %.2f 登录/秒%n", throughput);
        }
        
        assertTrue(successCount.get() >= CONCURRENT_USERS * 0.9, "登录成功率应该至少达到90%");
    }

    @Test
    void getUserProfile() {
        String username = "New User 2000";
        restTemplate.getForObject("/api/user/login?username=" + username + "&password=" + TEST_PASSWORD, Boolean.class);

        ResponseEntity<ProfileModel> response = restTemplate.getForEntity("/api/user/get", ProfileModel.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Profile retrieval should be successful after login");
        ProfileModel profile = response.getBody();
        assertNotNull(profile, "Profile should not be null for an existing user");
    }

    @Test
    void getAllProfile() {
        // Test for unauthorized access
        ResponseEntity<ErrorResponse> errorResponse = restTemplate.getForEntity("/api/user/getall", ErrorResponse.class);
        assertEquals(HttpStatus.UNAUTHORIZED, errorResponse.getStatusCode(), "Admin profile retrieval should fail without admin login");

        // Login as a regular user
        userLogin();
        errorResponse = restTemplate.getForEntity("/api/user/getall", ErrorResponse.class);
        assertEquals(HttpStatus.FORBIDDEN, errorResponse.getStatusCode(), "Admin profile retrieval should fail for regular user");

        // Admin login
        adminLogin();

        // Test for authorized access
        ResponseEntity<AdminProfileModel[]> response = restTemplate.getForEntity("/api/user/getall", AdminProfileModel[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Admin profile retrieval should be successful after admin login");
        AdminProfileModel[] profiles = response.getBody();
        assertNotNull(profiles, "Profiles should not be null for an existing admin");
    }

    @Test
    void getIntimateUsers() {
        // Test for unauthorized access
        ResponseEntity<ErrorResponse> errorResponse = restTemplate.getForEntity("/api/user/getintm", ErrorResponse.class);
        assertEquals(HttpStatus.UNAUTHORIZED, errorResponse.getStatusCode(), "Intimate users retrieval should fail without login");

        // User login
        userLogin();

        // Test for authorized access
        ResponseEntity<IntimateDTO[]> response = restTemplate.getForEntity("/api/user/getintm", IntimateDTO[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Intimate users retrieval should be successful after user login");
        IntimateDTO[] intimateUsers = response.getBody();
        assertNotNull(intimateUsers, "Intimate users list should not be null for an existing user");
    }

    @Test
    void addUser() {
        System.out.println("=== 开始批量创建用户测试 ===");
        
        try {
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Boolean result = restTemplate.getForObject("/api/user/secure/code?email=" + TEST_EMAIL, Boolean.class);
        assertTrue(result, "验证码发送应该成功");

        long batchStart = System.currentTimeMillis();
        for(int ID = 1; ID <= 2000; ++ID) {
            ProfileModel newProfile = new ProfileModel();
            newProfile.setUserid("NEW_USER_" + ID);
            newProfile.setUsername("New User " + ID);
            newProfile.setEmail(TEST_EMAIL);
            newProfile.setAvatar(null);
            newProfile.setNote("This is a new user");
            newProfile.setRole(0L);
            newProfile.setJointime(System.currentTimeMillis());
            newProfile.setLevel(50L);
            
            ResponseEntity<Boolean> response = restTemplate.postForEntity("/api/user/add", new UserModel(TEST_PASSWORD, TEST_STUID, newProfile), Boolean.class);
            
            assertEquals(HttpStatus.OK, response.getStatusCode(), "Adding a new user should be successful");
            Boolean body = response.getBody();
            assertNotNull(body, "Response body should not be null");
            assertTrue(body, "Adding a new user should return true");
            
            // 每1000个用户输出统计信息
            if (ID % 1000 == 0) {
                long currentTime = System.currentTimeMillis();
                long elapsed = currentTime - batchStart;
                double avgTime = elapsed / (double) ID;
                double usersPerSecond = ID / (elapsed / 1000.0);
                System.out.printf("进度: %d/2000, 耗时: %.2f秒, 平均: %.2fms/用户, 速度: %.2f用户/秒%n", ID, elapsed / 1000.0, avgTime, usersPerSecond);
            }
        }
        
        long overallEnd = System.currentTimeMillis();
        long batchTime = overallEnd - batchStart;
        
        System.out.println("=== 测试完成统计 ===");
        System.out.printf("批量创建耗时: %.2f 秒%n", batchTime / 1000.0);
        System.out.printf("平均每个用户: %.2f 毫秒%n", batchTime / 2000.0);
        System.out.printf("处理速度: %.2f 用户/秒%n", 2000.0 / (batchTime / 1000.0));
    }
}