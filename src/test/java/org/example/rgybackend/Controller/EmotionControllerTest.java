package org.example.rgybackend.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.example.rgybackend.DTO.EmotionData;
import org.example.rgybackend.DTO.EmotionRecord;
import org.example.rgybackend.DTO.StringDTO;
import org.example.rgybackend.Model.DiaryModel;
import org.example.rgybackend.Model.EmotionDataModel;
import org.example.rgybackend.Model.EmotionModel;
import org.example.rgybackend.Model.TagModel;
import org.example.rgybackend.Utils.TimeUtil;
import org.junit.jupiter.api.BeforeEach;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmotionControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String TEST_USERNAME = "TEST_USER";
    private static final String TEST_PASSWORD = "123456";
    private static final String TEST_USER_ID = "TEST_USER_1752566178965";
    private static final String TEST_ADMIN_USERNAME = "TEST_ADMIN";
    private static final String TEST_ADMIN_PASSWORD = "123456";
    private static final String TEST_PSY_USERNAME = "TEST_PSY";
    private static final String TEST_PSY_PASSWORD = "123456";

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
    void getEmotion() {
        userLogin();

        ResponseEntity<EmotionModel> response = restTemplate.getForEntity("/api/emotion/tag/get", EmotionModel.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get emotion");
        EmotionModel emotionModel = response.getBody();
        assertNotNull(emotionModel, "Emotion model should not be null");
        assertEquals(TEST_USER_ID, emotionModel.getUserid(), "User ID should match the logged-in user");
    }

    @Test
    void getRecordNum() {
        userLogin();

        ResponseEntity<Long> response = restTemplate.getForEntity("/api/emotion/record/getnum", Long.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get record number");
        Long recordNum = response.getBody();
        assertNotNull(recordNum, "Record number should not be null");
        assertTrue(recordNum >= 0, "Record number should be non-negative");
    }

    @Test
    void getHistoryRecords() {
        userLogin();

        Long pageIndex = 0L;
        Long pageSize = 10L;
        
        ResponseEntity<EmotionRecord[]> response = restTemplate.getForEntity(
            "/api/emotion/record/get?pageIndex=" + pageIndex + "&pageSize=" + pageSize, 
            EmotionRecord[].class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get history records");
        EmotionRecord[] records = response.getBody();
        assertNotNull(records, "History records should not be null");
        assertTrue(records.length >= 0, "History records should be an array");
        assertTrue(records.length <= pageSize, "Records size should not exceed page size");
    }

    @Test
    void getTags() {
        userLogin();

        ResponseEntity<TagModel[]> response = restTemplate.getForEntity("/api/emotion/tag/getall", TagModel[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get emotion tags");
        TagModel[] tags = response.getBody();
        assertNotNull(tags, "Emotion tags should not be null");
        assertTrue(tags.length > 0, "Should have at least one emotion tag");
    }

    @Test
    void getUrlTags() {
        userLogin();

        ResponseEntity<TagModel[]> response = restTemplate.getForEntity("/api/emotion/tag/geturl", TagModel[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get URL tags");
        TagModel[] urlTags = response.getBody();
        assertNotNull(urlTags, "URL tags should not be null");
        assertTrue(urlTags.length >= 0, "URL tags should be an array");
    }

    @Test
    void checkNegative() {
        userLogin();

        ResponseEntity<Boolean> response = restTemplate.getForEntity("/api/emotion/negative", Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to check negative emotion");
        Boolean isNegative = response.getBody();
        assertNotNull(isNegative, "Negative check result should not be null");
    }

    @Test
    void getDiary() {
        userLogin();

        ResponseEntity<DiaryModel> response = restTemplate.getForEntity("/api/emotion/diary/get", DiaryModel.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get diary");
        DiaryModel diary = response.getBody();
        assertNotNull(diary, "Diary should not be null");
        assertEquals(TEST_USER_ID, diary.getUserid(), "Diary user ID should match logged-in user");
    }

    @Test
    void getWeekData() {
        userLogin();

        ResponseEntity<EmotionDataModel[]> response = restTemplate.getForEntity("/api/emotion/data/getweek", EmotionDataModel[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get week data");
        EmotionDataModel[] weekData = response.getBody();
        assertNotNull(weekData, "Week data should not be null");
        assertTrue(weekData.length >= 0, "Week data should be an array");
        assertTrue(weekData.length <= 7, "Week should have at most 7 days of data");
    }

    @Test
    void getMonthData() {
        userLogin();

        ResponseEntity<EmotionDataModel[]> response = restTemplate.getForEntity("/api/emotion/data/getmonth", EmotionDataModel[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get month data");
        EmotionDataModel[] monthData = response.getBody();
        assertNotNull(monthData, "Month data should not be null");
        assertTrue(monthData.length >= 0, "Month data should be an array");
        assertTrue(monthData.length <= 31, "Month should have at most 31 days of data");
    }

    @Test
    void scanEmotionData() {
        Long start = TimeUtil.getStartOfDayTimestamp(TimeUtil.today().minusDays(7));
        Long end = TimeUtil.getStartOfDayTimestamp(TimeUtil.today());
        Long interval = 1L; 

        userLogin();
        ResponseEntity<EmotionData> response = restTemplate.getForEntity(
            "/api/emotion/data/scan?start=" + start + "&end=" + end + "&interval=" + interval, 
            EmotionData.class
        );
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Should return forbidden for non-admin user");

        adminLogin(); 

        response = restTemplate.getForEntity(
            "/api/emotion/data/scan?start=" + start + "&end=" + end + "&interval=" + interval, 
            EmotionData.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to scan emotion data");
        EmotionData emotionData = response.getBody();
        assertNotNull(emotionData, "Emotion data should not be null");
        assertNotNull(emotionData.getTotalNum(), "Total number should not be null");
        assertTrue(emotionData.getTotalNum() >= 0, "Total number should be non-negative");
    }

    @Test
    void setEmotion() {
        userLogin();

        EmotionModel emotionModel = new EmotionModel();
        emotionModel.setUserid(TEST_USER_ID); 
        emotionModel.setTimestamp(System.currentTimeMillis());
        emotionModel.setScore(4L); 
        emotionModel.setTag(new TagModel(1L, "喜悦"));

        HttpEntity<EmotionModel> request = new HttpEntity<>(emotionModel);
        ResponseEntity<Boolean> response = restTemplate.exchange(
            "/api/emotion/tag/update",
            HttpMethod.PUT,
            request,
            Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to set emotion");
        Boolean result = response.getBody();
        assertNotNull(result, "Set emotion result should not be null");
        assertTrue(result, "Set emotion should be successful");
    }

    @Test
    void updateDiary() {
        userLogin();

        StringDTO diaryContent = new StringDTO("今天心情很好，学习了很多新知识，感觉很充实。");

        HttpEntity<StringDTO> request = new HttpEntity<>(diaryContent);
        ResponseEntity<Boolean> response = restTemplate.exchange(
            "/api/emotion/diary/update",
            HttpMethod.PUT,
            request,
            Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to update diary");
        Boolean result = response.getBody();
        assertNotNull(result, "Update diary result should not be null");
        assertTrue(result, "Update diary should be successful");

        diaryContent = new StringDTO("今天感觉很糟糕，什么都不想做，感觉人生没有意义。");

        request = new HttpEntity<>(diaryContent);
        response = restTemplate.exchange(
            "/api/emotion/diary/update",
            HttpMethod.PUT,
            request,
            Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to update negative diary");
        result = response.getBody();
        assertNotNull(result, "Update diary result should not be null");
        assertTrue(result, "Update negative diary should be successful");

        diaryContent = new StringDTO("每天都觉得活着没意义，黑暗和孤独让我窒息，准备明天晚上跳楼，希望能从痛苦中解脱。");

        request = new HttpEntity<>(diaryContent);
        response = restTemplate.exchange(
                "/api/emotion/diary/update",
                HttpMethod.PUT,
                request,
                Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to update negative diary");
        result = response.getBody();
        assertNotNull(result, "Update diary result should not be null");
        assertTrue(result, "Update negative diary should be successful");

        diaryContent = new StringDTO("每天都觉得活着没意义，黑暗和孤独让我窒息。");

        request = new HttpEntity<>(diaryContent);
        response = restTemplate.exchange(
                "/api/emotion/diary/update",
                HttpMethod.PUT,
                request,
                Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to update negative diary");
        result = response.getBody();
        assertNotNull(result, "Update diary result should not be null");
        assertTrue(result, "Update negative diary should be successful");
    }
}