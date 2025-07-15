package org.example.rgybackend.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

import org.example.rgybackend.DTO.IntimateDTO;
import org.example.rgybackend.Model.AdminProfileModel;
import org.example.rgybackend.Model.MilestoneModel;
import org.example.rgybackend.Model.ProfileModel;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    private static final String TEST_USERNAME = "TEST_USER";
    private static final String TEST_PASSWORD = "123456";
    private static final String TEST_USER_ID = "TEST_USER_1752566178965";
    private static final String TEST_ADMIN_USERNAME = "TEST_ADMIN";
    private static final String TEST_ADMIN_PASSWORD = "123456";
    private static final String TEST_ADMIN_ID = "TEST_ADMIN_1752566178965";
    private static final String TEST_PSY_USERNAME = "TEST_PSY";
    private static final String TEST_PSY_PASSWORD = "123456";
    private static final String TEST_PSY_ID = "TEST_PSY_1752566178965";
    private static final String TEST_DISABLED_USERNAME = "TEST_USER_DISABLED";
    private static final String TEST_DISABLED_PASSWORD = "123456";
    private static final String TEST_DISABLED_ID = "TEST_USER_DISABLED_1752566178965";
    private static final String CORRECT_KEY = "0xFFFFFFFF";

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
    void login() {
        // Test for valid login
        boolean result = restTemplate.getForObject("/api/user/login?username=" + TEST_USERNAME + "&password=" + TEST_PASSWORD, Boolean.class);
        assertTrue(result, "Login should be successful with correct credentials");
        
        // Test for user not existed
        String nonExistentUser = "NON_EXISTENT_USER";
        boolean nonExistentResult = restTemplate.getForObject("/api/user/login?username=" + nonExistentUser + "&password=" + TEST_PASSWORD, Boolean.class);
        assertFalse(nonExistentResult, "Login should fail for non-existent user");

        // Test for incorrect password
        String incorrectPassword = "wrongpassword";
        boolean incorrectResult = restTemplate.getForObject("/api/user/login?username=" + TEST_USERNAME + "&password=" + incorrectPassword, Boolean.class);
        assertFalse(incorrectResult, "Login should fail with incorrect password");
    }

    @Test
    void logout() {
        userLogin();

        boolean result = restTemplate.getForObject("/api/user/logout", Boolean.class);
        assertTrue(result, "Logout should be successful");
        
        ResponseEntity<ProfileModel> response = restTemplate.getForEntity("/profile/get", ProfileModel.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "Profile retrieval should fail after logout");
    }

    @Test
    void userExisted() {
        // Test for existing user
        boolean exists = restTemplate.getForObject("/api/user/existed?username=" + TEST_USERNAME, Boolean.class);
        assertTrue(exists, "User should exist");

        // Test for non-existing user
        String nonExistentUser = "NON_EXISTENT_USER";
        boolean nonExistentResult = restTemplate.getForObject("/api/user/existed?username=" + nonExistentUser, Boolean.class);
        assertFalse(nonExistentResult, "User should not exist");
    }

    @Test
    void getUserProfile() {
        ResponseEntity<ProfileModel> response = restTemplate.getForEntity("/profile/get", ProfileModel.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "Profile retrieval should fail without login");

        userLogin();

        response = restTemplate.getForEntity("/profile/get", ProfileModel.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Profile retrieval should be successful after login");
        ProfileModel profile = response.getBody();
        assertNotNull(profile, "Profile should not be null for an existing user");
        assertEquals(TEST_USER_ID, profile.getUserid(), "Profile ID should match the test user ID");
    }

    @Test
    void getAllProfile() {
        // Test for unauthorized access
        ResponseEntity<AdminProfileModel[]> response = restTemplate.getForEntity("/api/user/getall", AdminProfileModel[].class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "Admin profile retrieval should fail without admin login");

        // Login as a regular user
        userLogin();
        response = restTemplate.getForEntity("/api/user/getall", AdminProfileModel[].class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Admin profile retrieval should fail for regular user");

        // Admin login
        adminLogin();

        // Test for authorized access
        response = restTemplate.getForEntity("/api/user/getall", AdminProfileModel[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Admin profile retrieval should be successful after admin login");
        AdminProfileModel[] profiles = response.getBody();
        assertNotNull(profiles, "Profiles should not be null for an existing admin");
    }

    @Test
    void getPsyProfile() {
        // Test for unauthorized access
        ResponseEntity<ProfileModel> response = restTemplate.getForEntity("/api/user/getpsy", ProfileModel.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "Psychologist profile retrieval should fail without login");

        // Psychologist login
        psyLogin();

        // Test for authorized access
        response = restTemplate.getForEntity("/api/user/getpsy", ProfileModel.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Psychologist profile retrieval should be successful after psychologist login");
        ProfileModel psyProfile = response.getBody();
        assertNotNull(psyProfile, "Psychologist profile should not be null for an existing psychologist");
        assertEquals(TEST_PSY_ID, psyProfile.getUserid(), "Psychologist profile ID should match the test psychologist ID");
    }

    @Test
    void getSimplifiedProfile() {
        // Test for unauthorized access
        ResponseEntity<ProfileModel> response = restTemplate.getForEntity("/api/user/getsim?userid=" + TEST_USER_ID, ProfileModel.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "Simplified profile retrieval should fail without login");

        // User login
        userLogin();

        // Test for authorized access
        response = restTemplate.getForEntity("/api/user/getsim?userid=" + TEST_USER_ID, ProfileModel.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Simplified profile retrieval should be successful after user login");
        ProfileModel simplifiedProfile = response.getBody();
        assertNotNull(simplifiedProfile, "Simplified profile should not be null for an existing user");
        assertEquals(TEST_USER_ID, simplifiedProfile.getUserid(), "Simplified profile ID should match the test user ID");
    }

    @Test
    void getIntimateUsers() {
        // Test for unauthorized access
        ResponseEntity<IntimateDTO[]> response = restTemplate.getForEntity("/api/user/getintm", IntimateDTO[].class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "Intimate users retrieval should fail without login");

        // User login
        userLogin();

        // Test for authorized access
        response = restTemplate.getForEntity("/api/user/getintm", IntimateDTO[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Intimate users retrieval should be successful after user login");
        IntimateDTO[] intimateUsers = response.getBody();
        assertNotNull(intimateUsers, "Intimate users list should not be null for an existing user");
    }

    @Test
    void getMilestone() {
        // Test for unauthorized access
        ResponseEntity<MilestoneModel> response = restTemplate.getForEntity("/api/user/milestone", MilestoneModel.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "Milestone retrieval should fail without login");

        // User login
        userLogin();

        // Test for authorized access
        response = restTemplate.getForEntity("/api/user/milestone", MilestoneModel.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Milestone retrieval should be successful after user login");
        MilestoneModel milestone = response.getBody();
        assertNotNull(milestone, "Milestone should not be null for an existing user");
    }

    @Test
    void verifyPassword() {
        // Test for unauthorized access
        ResponseEntity<Boolean> response = restTemplate.getForEntity("/api/user/verify/pwd?password=" + TEST_PASSWORD, Boolean.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "Password verification should fail without login");

        // User login
        userLogin();

        // Test for authorized access with correct password
        response = restTemplate.getForEntity("/api/user/verify/pwd?password=" + TEST_PASSWORD, Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Password verification should be successful with correct password");
        Boolean body = response.getBody();
        assertNotNull(body, "Password verification response should not be null");
        assertTrue(body, "Password verification should return true for correct password");

        // Test for incorrect password
        String incorrectPassword = "wrongpassword";
        response = restTemplate.getForEntity("/api/user/verify/pwd?password=" + incorrectPassword, Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Password verification should be successful with incorrect password");
        body = response.getBody();
        assertNotNull(body, "Password verification response should not be null");
        assertFalse(body, "Password verification should return false for incorrect password");
    }

    @Test
    void verifyAdmin() {
        // Test for authorized access with correct key
        ResponseEntity<Boolean> response = restTemplate.getForEntity("/api/user/verify/admin?verifyKey=" + CORRECT_KEY, Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Admin verification should be successful with correct key");
        Boolean body = response.getBody();
        assertNotNull(body, "Admin verification response should not be null");
        assertTrue(body, "Admin verification should return true for correct key");

        // Test for incorrect key
        String incorrectKey = "wrongkey";
        response = restTemplate.getForEntity("/api/user/verify/admin?verifyKey=" + incorrectKey, Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Admin verification should be successful with incorrect key");
        body = response.getBody();
        assertNotNull(body, "Admin verification response should not be null");
        assertFalse(body, "Admin verification should return false for incorrect key");
    }

    @Test
    void isDisabled() {
        // Test for existing disabled user
        boolean isDisabled = restTemplate.getForObject("/api/user/disabled/get?username=" + TEST_DISABLED_USERNAME, Boolean.class);
        assertTrue(isDisabled, "User should be disabled");

        // Test for existing enabled user
        isDisabled = restTemplate.getForObject("/api/user/disabled/get?username=" + TEST_USERNAME, Boolean.class);
        assertFalse(isDisabled, "User should not be disabled");

        // Test for non-existing user
        String nonExistentUser = "NON_EXISTENT_USER";
        boolean nonExistentResult = restTemplate.getForObject("/api/user/disabled/get?username=" + nonExistentUser, Boolean.class);
        assertTrue(nonExistentResult, "User should not exist");
    }

    @Test
    void addUser() {
    }

    @Test
    void updateProfile() {
    }

    @Test
    void updatePsyProfile() {
    }

    @Test
    void updatePassword() {
    }

    @Test
    void setDisabled() {
    }

    @Test
    void postAuthCode() {
    }

    @Test
    void checkAuthCode() {
    }
}