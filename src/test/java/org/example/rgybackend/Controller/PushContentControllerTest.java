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
import org.example.rgybackend.DTO.SimplifiedUrlData;
import org.example.rgybackend.Model.QuoteModel;
import org.example.rgybackend.Model.UrlDataModel;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PushContentControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String TEST_USERNAME = "TEST_USER";
    private static final String TEST_PASSWORD = "123456";
    private static final String TEST_PSY_USERNAME = "TEST_PSY";
    private static final String TEST_PSY_PASSWORD = "123456";
    private static final String BASE64_IMG = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAC0lEQVR42mP8//8/AwAI/wH+z4kAAAAASUVORK5CYII=";

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

    void psyLogin() {
        restTemplate.getForObject("/api/user/login?username=" + TEST_PSY_USERNAME + "&password=" + TEST_PSY_PASSWORD, Boolean.class);
    }

    @Test
    void getSimplifiedContent() {
        userLogin();

        ResponseEntity<SimplifiedUrlData[]> response = restTemplate.getForEntity("/api/pushcontent/getsim", SimplifiedUrlData[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get simplified content");
        SimplifiedUrlData[] simplifiedContent = response.getBody();
        assertNotNull(simplifiedContent, "Simplified content should not be null");
        assertTrue(simplifiedContent.length >= 0, "Simplified content should be an array");
    }

    @Test
    void getContentByTag() {
        userLogin();

        Long tagid = 1L;
        Integer pageIndex = 0;
        Integer pageSize = 10;
        
        ResponseEntity<UrlDataModel[]> response = restTemplate.getForEntity(
            "/api/pushcontent/getbytag?tagid=" + tagid + "&pageIndex=" + pageIndex + "&pageSize=" + pageSize, 
            UrlDataModel[].class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get content by tag");
        UrlDataModel[] content = response.getBody();
        assertNotNull(content, "Content by tag should not be null");
        assertTrue(content.length >= 0, "Content by tag should be an array");
        assertTrue(content.length <= pageSize, "Content size should not exceed page size");
    }

    @Test
    void getContent() {
        userLogin();

        Integer pageIndex = 0;
        Integer pageSize = 10;
        
        ResponseEntity<UrlDataModel[]> response = restTemplate.getForEntity(
            "/api/pushcontent/get?pageIndex=" + pageIndex + "&pageSize=" + pageSize, 
            UrlDataModel[].class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get personalized content");
        UrlDataModel[] content = response.getBody();
        assertNotNull(content, "Personalized content should not be null");
        assertTrue(content.length >= 0, "Personalized content should be an array");
        assertTrue(content.length <= pageSize, "Content size should not exceed page size");
    }

    @Test
    void getDataNum() {
        userLogin();

        Long tagid = 1L;
        
        ResponseEntity<Long> response = restTemplate.getForEntity("/api/pushcontent/getnum?tagid=" + tagid, Long.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get data number by tag");
        Long dataNum = response.getBody();
        assertNotNull(dataNum, "Data number should not be null");
        assertTrue(dataNum >= 0, "Data number should be non-negative");
    }

    @Test
    void getAllDataNum() {
        userLogin();

        ResponseEntity<Long> response = restTemplate.getForEntity("/api/pushcontent/getallnum", Long.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get all data number");
        Long allDataNum = response.getBody();
        assertNotNull(allDataNum, "All data number should not be null");
        assertTrue(allDataNum >= 0, "All data number should be non-negative");
    }

    @Test
    void getQuote() {
        userLogin();

        ResponseEntity<QuoteModel> response = restTemplate.getForEntity("/api/pushcontent/quote/get", QuoteModel.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get quote");
        QuoteModel quote = response.getBody();
        assertNotNull(quote, "Quote should not be null");
        assertNotNull(quote.getText(), "Quote content should not be null");
    }

    @Test
    void pushContent() {
        psyLogin();

        UrlDataModel urlDataModel = new UrlDataModel();
        urlDataModel.setTitle("测试推送内容");
        urlDataModel.setDescription("这是一个测试推送的内容描述");
        urlDataModel.setUrl("https://example.com/test");
        urlDataModel.setImg(BASE64_IMG);
        urlDataModel.setTags(List.of(urlDataModel.typeTags.get(0)));
        urlDataModel.setType("article");
        urlDataModel.setCreatedAt(System.currentTimeMillis());

        HttpEntity<UrlDataModel> request = new HttpEntity<>(urlDataModel);
        ResponseEntity<Boolean> response = restTemplate.exchange(
            "/api/pushcontent/push",
            HttpMethod.POST,
            request,
            Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to push content");
        Boolean result = response.getBody();
        assertNotNull(result, "Push content result should not be null");
        assertTrue(result, "Push content should be successful");

        urlDataModel.setImg(null); 
        request = new HttpEntity<>(urlDataModel);
        response = restTemplate.exchange(
            "/api/pushcontent/push",
            HttpMethod.POST,
            request,
            Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to push content without image");
        result = response.getBody();
        assertNotNull(result, "Push content without image result should not be null");
        assertTrue(result, "Push content without image should be successful");
    }

    @Test
    void addQuote() {
        psyLogin();

        QuoteModel quoteModel = new QuoteModel();
        quoteModel.setText("生活就像海洋，只有意志坚强的人，才能到达彼岸。");
        quoteModel.setAuthor("马克思");

        HttpEntity<QuoteModel> request = new HttpEntity<>(quoteModel);
        ResponseEntity<Boolean> response = restTemplate.exchange(
            "/api/pushcontent/quote/add",
            HttpMethod.POST,
            request,
            Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to add quote");
        Boolean result = response.getBody();
        assertNotNull(result, "Add quote result should not be null");
        assertTrue(result, "Add quote should be successful");
    }
}