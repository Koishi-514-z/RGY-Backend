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
import org.example.rgybackend.DTO.StringDTO;
import org.example.rgybackend.Model.ProfileModel;
import org.example.rgybackend.Model.SessionModel;
import org.example.rgybackend.Model.SessionTagModel;
import org.example.rgybackend.Model.UserModel;
import org.example.rgybackend.Utils.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String TEST_USERNAME = "TEST_USER";
    private static final String TEST_PASSWORD = "123456";
    private static final String TEST_USER_ID = "TEST_USER_1752566178965";
    private static final String TEST_PSY_ID = "TEST_PSY_1752567914700";
    private static final String TEST_EMAIL = "zsb_sjtu@sjtu.edu.cn";
    private static final String TEST_STUID = "523031910080";

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

    @Test
    void getSession() {
        userLogin();

        // Test for get myself
        ResponseEntity<SessionModel> response = restTemplate.getForEntity("/api/chat/getsession?sessionid=6", SessionModel.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get session");
        SessionModel session = response.getBody();
        assertNotNull(session, "Session should not be null");
        assertEquals(6L, session.getSessionid(), "Session ID does not match");
        assertEquals(TEST_USER_ID, session.getMyself().getUserid(), "User ID does not match");
        assertNotNull(session.getMessages(), "Messages should not be null");

        // Test for get other user
        response = restTemplate.getForEntity("/api/chat/getsession?sessionid=7", SessionModel.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get session");
        session = response.getBody();
        assertNotNull(session, "Session should not be null");
        assertEquals(7L, session.getSessionid(), "Session ID does not match");
        assertEquals(TEST_USER_ID, session.getMyself().getUserid(), "User ID should not match");
        assertNotNull(session.getMessages(), "Messages should not be null");

        // Test for non-existent session
        response = restTemplate.getForEntity("/api/chat/getsession?sessionid=999", SessionModel.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Expected NOT_FOUND for non-existent session");

        // Test for invalid user
        response = restTemplate.getForEntity("/api/chat/getsession?sessionid=1", SessionModel.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "Expected ERROR for invalid user session");
    }

    @Test
    void getSessionTags() {
        userLogin();

        // Test for getting session tags
        ResponseEntity<SessionTagModel[]> response = restTemplate.getForEntity("/api/chat/gettags", SessionTagModel[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get session tags");
        SessionTagModel[] sessionTags = response.getBody();
        assertNotNull(sessionTags, "Session tags should not be null");
        assertTrue(sessionTags.length > 0, "Session tags should not be empty");

        // Check if the first session tag matches the expected user ID
        assertEquals(TEST_USER_ID, sessionTags[0].getMyself().getUserid(), "User ID in session tag does not match");
    }

    @Test
    void getSessionid() {
        userLogin();

        // Test for getting session ID with valid user
        ResponseEntity<Long> response = restTemplate.getForEntity("/api/chat/getid?userid=" + TEST_PSY_ID, Long.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get session ID");
        Long sessionId = response.getBody();
        assertNotNull(sessionId, "Session ID should not be null");

        // Test for getting session ID with non-existent user
        response = restTemplate.getForEntity("/api/chat/getid?userid=nonexistent", Long.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get session ID");
        sessionId = response.getBody();
        assertNull(sessionId, "Session ID should be null for non-existent user");
    }

    @Test
    void postMessage() {
        userLogin();

        // Test for posting a message
        String messageContent = "Hello, this is a test message.";
        ResponseEntity<Boolean> response = restTemplate.exchange(
            "/api/chat/post?sessionid=6",
            HttpMethod.PUT,
            new HttpEntity<>(new StringDTO(messageContent)),
            Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to post message");
        Boolean success = response.getBody();
        assertNotNull(success, "Response body should not be null");
        assertTrue(success, "Message posting should be successful");

        // Test for posting a message to another session
        messageContent = "Hello, this is a test message.";
        response = restTemplate.exchange(
            "/api/chat/post?sessionid=7",
            HttpMethod.PUT,
            new HttpEntity<>(new StringDTO(messageContent)),
            Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to post message");
        success = response.getBody();
        assertNotNull(success, "Response body should not be null");
        assertTrue(success, "Message posting should be successful");

        // Test for posting a message to an invalid session
        ResponseEntity<ErrorResponse> errorResponse = restTemplate.exchange(
            "/api/chat/post?sessionid=1",
            HttpMethod.PUT,
            new HttpEntity<>(new StringDTO(messageContent)),
            ErrorResponse.class
        );
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, errorResponse.getStatusCode(), "Expected ERROR for invalid session");

        // Test for posting a message to a non-existent session
        errorResponse = restTemplate.exchange(
            "/api/chat/post?sessionid=999",
            HttpMethod.PUT,
            new HttpEntity<>(new StringDTO(messageContent)),
            ErrorResponse.class
        );
        assertEquals(HttpStatus.NOT_FOUND, errorResponse.getStatusCode(), "Expected NOT_FOUND for non-existent session");
    }

    @Test
    void updateRead() {
        userLogin();

        // Test for updating read status of a session
        ResponseEntity<Boolean> response = restTemplate.exchange(
            "/api/chat/read?sessionid=6",
            HttpMethod.PUT,
            null,
            Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to update read status");
        Boolean success = response.getBody();
        assertNotNull(success, "Response body should not be null");
        assertTrue(success, "Read status update should be successful");

        // Test for updating read status of another session
        response = restTemplate.exchange(
            "/api/chat/read?sessionid=7",
            HttpMethod.PUT,
            null,
            Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to update read status");
        success = response.getBody();
        assertNotNull(success, "Response body should not be null");
        assertTrue(success, "Read status update should be successful");

        // Test for updating read status of a non-existent session
        ResponseEntity<ErrorResponse> errorResponse = restTemplate.exchange(
            "/api/chat/read?sessionid=999",
            HttpMethod.PUT,
            null,
            ErrorResponse.class
        );
        assertEquals(HttpStatus.NOT_FOUND, errorResponse.getStatusCode(), "Expected NOT_FOUND for non-existent session");

        // Test for updating read status of an invalid session
        errorResponse = restTemplate.exchange(
            "/api/chat/read?sessionid=1",
            HttpMethod.PUT,
            null,
            ErrorResponse.class
        );
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, errorResponse.getStatusCode(), "Expected ERROR for invalid session");
    }

    @Test
    void createSession() {
        try {
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Boolean result = restTemplate.getForObject("/api/user/secure/code?email=" + TEST_EMAIL, Boolean.class);
        assertTrue(result, "验证码发送应该成功");

        ProfileModel newProfile = new ProfileModel();
        String newUserid = "NEW_USER_" + System.currentTimeMillis();
        newProfile.setUserid(newUserid);
        newProfile.setUsername("New User" + System.currentTimeMillis());
        newProfile.setEmail(TEST_EMAIL);
        newProfile.setAvatar(null);
        newProfile.setNote("This is a new user");
        newProfile.setRole(0L);
        newProfile.setJointime(System.currentTimeMillis());
        newProfile.setLevel(50L);
        restTemplate.postForEntity("/api/user/add", new UserModel(TEST_PASSWORD, TEST_STUID, newProfile), Boolean.class);

        userLogin();

        // Test for creating a session with a valid user
        ResponseEntity<Long> response = restTemplate.postForEntity(
            "/api/chat/create?userid=" + newUserid,
            null,
            Long.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to create session");
        Long sessionId = response.getBody();
        assertNotNull(sessionId, "Session ID should not be null");

        // Test for duplicate session creation
        ResponseEntity<ErrorResponse> errorResponse = restTemplate.postForEntity(
            "/api/chat/create?userid=" + newUserid,
            null,
            ErrorResponse.class
        );
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, errorResponse.getStatusCode(), "Expected ERROR for duplicate session creation");
    }
}