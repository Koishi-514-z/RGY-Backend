package org.example.rgybackend.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.example.rgybackend.DTO.StringDTO;
import org.example.rgybackend.Model.EmotionModel;
import org.example.rgybackend.Model.ProfileModel;
import org.example.rgybackend.Model.TagModel;
import org.example.rgybackend.Model.UserModel;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PerformanceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

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
        final String baseUrl = "http://localhost:" + port;
        
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        AtomicLong totalResponseTime = new AtomicLong(0);
        
        for(int i = 1; i <= CONCURRENT_USERS; i++) {
            final int ID = i;
            executor.submit(() -> {
                try {
                    startLatch.await();                             // 控制所有子线程同时开始执行
                    
                    TestRestTemplate userRestTemplate = createRestTemplate();
                    long requestStart = System.nanoTime();
                    
                    String username = "New User " + ID;
                    Boolean result = userRestTemplate.getForObject(
                        baseUrl + "/api/user/login?username=" + username + "&password=" + TEST_PASSWORD, 
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

        long testStartTime = System.currentTimeMillis();
        
        startLatch.countDown();                                     // 唤醒所有子线程
        finishLatch.await(3, TimeUnit.MINUTES);             // 等待所有子线程执行完毕
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
    void concurrentSetEmotionStressTest() throws InterruptedException {
        System.out.println("=== 开始1000用户并发情绪打卡压力测试 ===");
        
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(CONCURRENT_USERS);
        final String baseUrl = "http://localhost:" + port;
        
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        AtomicLong totalResponseTime = new AtomicLong(0);
        
        for(int i = 1; i <= CONCURRENT_USERS; i++) {
            final int ID = i;
            executor.submit(() -> {
                try {                 
                    TestRestTemplate userRestTemplate = createRestTemplate();
                    
                    String userid = "NEW_USER_" + ID;
                    String username = "New User " + ID;

                    EmotionModel emotionModel = new EmotionModel();
                    emotionModel.setUserid(userid); 
                    emotionModel.setTimestamp(System.currentTimeMillis());
                    emotionModel.setScore(5L); 
                    emotionModel.setTag(new TagModel(1L, "喜悦"));
                    HttpEntity<EmotionModel> request = new HttpEntity<>(emotionModel);

                    userRestTemplate.getForObject(
                        baseUrl + "/api/user/login?username=" + username + "&password=" + TEST_PASSWORD, 
                        Boolean.class
                    );

                    startLatch.await();     
                    
                    long requestStart = System.nanoTime();

                    ResponseEntity<Boolean> response = userRestTemplate.exchange(
                        baseUrl + "/api/emotion/tag/update",
                        HttpMethod.PUT,
                        request,
                        Boolean.class
                    );
                    
                    long requestEnd = System.nanoTime();
                    long responseTime = (requestEnd - requestStart) / 1000000;

                    Boolean result = response.getBody();
                    if(result != null && result) {
                        successCount.incrementAndGet();
                        totalResponseTime.addAndGet(responseTime);
                    } 
                    else {
                        failureCount.incrementAndGet();
                    }
                    
                    if(ID % 100 == 0) {
                        System.out.printf("已完成 %d 个用户打卡测试%n", ID);
                    }

                } catch (Exception e) {
                    failureCount.incrementAndGet();
                } finally {
                    finishLatch.countDown();
                }
            });
        }

        try {
            Thread.sleep(20000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long testStartTime = System.currentTimeMillis();
        
        startLatch.countDown();                                  
        finishLatch.await(3, TimeUnit.MINUTES);           
        long testEndTime = System.currentTimeMillis();
        
        executor.shutdown();
        
        System.out.println("=== 打卡压力测试结果 ===");
        System.out.printf("总耗时: %.2f 秒%n", (testEndTime - testStartTime) / 1000.0);
        System.out.printf("成功打卡: %d%n", successCount.get());
        System.out.printf("失败打卡: %d%n", failureCount.get());
        System.out.printf("打卡成功率: %.2f%%%n", (successCount.get() * 100.0 / CONCURRENT_USERS));
        
        if (successCount.get() > 0) {
            long avgResponseTime = totalResponseTime.get() / successCount.get();
            System.out.printf("平均打卡响应时间: %d 毫秒%n", avgResponseTime);
            
            double throughput = successCount.get() / ((testEndTime - testStartTime) / 1000.0);
            System.out.printf("吞吐量: %.2f 操作/秒%n", throughput);
        }
        
        assertTrue(successCount.get() >= CONCURRENT_USERS * 0.9, "成功率应该至少达到90%");
    }

    @Test
    void concurrentSetDiaryStressTest() throws InterruptedException {
        System.out.println("=== 开始1000用户并发发表日记压力测试 ===");
        
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(CONCURRENT_USERS);
        final String baseUrl = "http://localhost:" + port;
        
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        AtomicLong totalResponseTime = new AtomicLong(0);
        
        for(int i = 1; i <= CONCURRENT_USERS; i++) {
            final int ID = i;
            executor.submit(() -> {
                try {                 
                    TestRestTemplate userRestTemplate = createRestTemplate();
                    
                    String username = "New User " + ID;

                    StringDTO diaryContent = new StringDTO("今天心情很好，学习了很多新知识，感觉很充实。");
                    HttpEntity<StringDTO> request = new HttpEntity<>(diaryContent);

                    userRestTemplate.getForObject(
                        baseUrl + "/api/user/login?username=" + username + "&password=" + TEST_PASSWORD, 
                        Boolean.class
                    );

                    startLatch.await();     
                    
                    long requestStart = System.nanoTime();

                    ResponseEntity<Boolean> response = userRestTemplate.exchange(
                        baseUrl + "/api/emotion/diary/update",
                        HttpMethod.PUT,
                        request,
                        Boolean.class
                    );
                    
                    long requestEnd = System.nanoTime();
                    long responseTime = (requestEnd - requestStart) / 1000000;

                    Boolean result = response.getBody();
                    if(result != null && result) {
                        successCount.incrementAndGet();
                        totalResponseTime.addAndGet(responseTime);
                    } 
                    else {
                        failureCount.incrementAndGet();
                    }
                    
                    if(ID % 100 == 0) {
                        System.out.printf("已完成 %d 个用户发表日记测试%n", ID);
                    }

                } catch (Exception e) {
                    failureCount.incrementAndGet();
                } finally {
                    finishLatch.countDown();
                }
            });
        }

        try {
            Thread.sleep(20000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long testStartTime = System.currentTimeMillis();
        
        startLatch.countDown();                                
        finishLatch.await(3, TimeUnit.MINUTES);           
        long testEndTime = System.currentTimeMillis();
        
        executor.shutdown();
        
        System.out.println("=== 发表日记压力测试结果 ===");
        System.out.printf("总耗时: %.2f 秒%n", (testEndTime - testStartTime) / 1000.0);
        System.out.printf("成功: %d%n", successCount.get());
        System.out.printf("失败: %d%n", failureCount.get());
        System.out.printf("成功率: %.2f%%%n", (successCount.get() * 100.0 / CONCURRENT_USERS));
        
        if (successCount.get() > 0) {
            long avgResponseTime = totalResponseTime.get() / successCount.get();
            System.out.printf("平均响应时间: %d 毫秒%n", avgResponseTime);
            
            double throughput = successCount.get() / ((testEndTime - testStartTime) / 1000.0);
            System.out.printf("吞吐量: %.2f 操作/秒%n", throughput);
        }
        
        assertTrue(successCount.get() >= CONCURRENT_USERS * 0.9, "成功率应该至少达到90%");
    }

    @Test
    void concurrentGetProfileStressTest() throws InterruptedException {
        System.out.println("=== 开始1000用户并发获取个人信息压力测试 ===");

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(CONCURRENT_USERS);
        final String baseUrl = "http://localhost:" + port;

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        AtomicLong totalResponseTime = new AtomicLong(0);

        for(int i = 1; i <= CONCURRENT_USERS; i++) {
            final int ID = i;
            executor.submit(() -> {
                try {
                    TestRestTemplate userRestTemplate = createRestTemplate();

                    String username = "New User " + ID;

                    userRestTemplate.getForObject(
                            baseUrl + "/api/user/login?username=" + username + "&password=" + TEST_PASSWORD,
                            Boolean.class
                    );

                    startLatch.await();

                    long requestStart = System.nanoTime();

                    ResponseEntity<ProfileModel> response = userRestTemplate.getForEntity(
                            baseUrl + "/api/user/get", 
                            ProfileModel.class
                    );

                    long requestEnd = System.nanoTime();
                    long responseTime = (requestEnd - requestStart) / 1000000;

                    ProfileModel profile = response.getBody();

                    if(profile != null && profile.getUsername().equals(username)) {
                        successCount.incrementAndGet();
                        totalResponseTime.addAndGet(responseTime);
                    }
                    else {
                        failureCount.incrementAndGet();
                    }

                    if(ID % 100 == 0) {
                        System.out.printf("已完成 %d 个用户获取个人信息测试%n", ID);
                    }

                } catch (Exception e) {
                    failureCount.incrementAndGet();
                } finally {
                    finishLatch.countDown();
                }
            });
        }

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long testStartTime = System.currentTimeMillis();

        startLatch.countDown();
        finishLatch.await(3, TimeUnit.MINUTES);
        long testEndTime = System.currentTimeMillis();

        executor.shutdown();

        System.out.println("=== 获取个人信息压力测试结果 ===");
        System.out.printf("总耗时: %.2f 秒%n", (testEndTime - testStartTime) / 1000.0);
        System.out.printf("成功: %d%n", successCount.get());
        System.out.printf("失败: %d%n", failureCount.get());
        System.out.printf("成功率: %.2f%%%n", (successCount.get() * 100.0 / CONCURRENT_USERS));

        if (successCount.get() > 0) {
            long avgResponseTime = totalResponseTime.get() / successCount.get();
            System.out.printf("平均响应时间: %d 毫秒%n", avgResponseTime);

            double throughput = successCount.get() / ((testEndTime - testStartTime) / 1000.0);
            System.out.printf("吞吐量: %.2f 操作/秒%n", throughput);
        }

        assertTrue(successCount.get() >= CONCURRENT_USERS * 0.9, "成功率应该至少达到90%");
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

    @Test
    void ModelResponseTest() {
        userLogin();

        StringDTO diaryContent = new StringDTO("今天心情很好，学习了很多新知识，感觉很充实。");
        HttpEntity<StringDTO> request = new HttpEntity<>(diaryContent);

        long testStartTime = System.currentTimeMillis();
        long totalResponseTime = 0;
        int successCount = 0;
        int failureCount = 0;

        for(int i = 1; i <= 100; i++) {
            long singleStart = System.nanoTime();
            ResponseEntity<Boolean> response = restTemplate.exchange(
                "/api/emotion/diary/update",
                HttpMethod.PUT,
                request,
                Boolean.class
            );
            long singleEnd = System.nanoTime();
            long singleResponseTime = (singleEnd - singleStart) / 1000000;
            totalResponseTime += singleResponseTime;

            Boolean result = response.getBody();
            if (response.getStatusCode() == HttpStatus.OK && result != null && result) {
                successCount++;
            } else {
                failureCount++;
            }

            if (i % 10 == 0) {
                System.out.printf("已完成 %d 次请求，当前耗时: %d 毫秒%n", i, singleResponseTime);
            }
        }

        long testEndTime = System.currentTimeMillis();
        long totalTime = testEndTime - testStartTime;
        double avgTime = totalResponseTime / 100.0;

        System.out.println("=== ModelResponseTest 统计结果 ===");
        System.out.printf("总耗时: %d 毫秒 (%.2f 秒)%n", totalTime, totalTime / 1000.0);
        System.out.printf("平均每次请求耗时: %.2f 毫秒%n", avgTime);
        System.out.printf("成功请求数: %d%n", successCount);
        System.out.printf("失败请求数: %d%n", failureCount);

        assertEquals(100, successCount, "所有请求都应该成功");
    }
}
