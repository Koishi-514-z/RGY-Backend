package org.example.rgybackend.Controller;

import org.junit.jupiter.api.BeforeEach;
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
import org.example.rgybackend.DTO.IntimateDTO;
import org.example.rgybackend.Entity.PsyProfileExtra;
import org.example.rgybackend.Model.AdminProfileModel;
import org.example.rgybackend.Model.MilestoneModel;
import org.example.rgybackend.Model.ProfileModel;
import org.example.rgybackend.Model.PsyProfileModel;
import org.example.rgybackend.Model.SimplifiedProfileModel;
import org.example.rgybackend.Model.UserModel;
import org.example.rgybackend.Utils.ErrorResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String TEST_USERNAME = "TEST_USER";
    private static final String TEST_PASSWORD = "123456";
    private static final String TEST_USER_ID = "TEST_USER_1752566178965";
    private static final String TEST_ADMIN_USERNAME = "TEST_ADMIN";
    private static final String TEST_ADMIN_PASSWORD = "123456";
    private static final String TEST_PSY_USERNAME = "TEST_PSY";
    private static final String TEST_PSY_PASSWORD = "123456";
    private static final String TEST_PSY_ID = "TEST_PSY_1752567914700";
    private static final String TEST_DISABLED_USERNAME = "TEST_USER_DISABLED";
    private static final String TEST_DISABLED_ID = "TEST_USER_DISABLED_1752566178965";
    private static final String CORRECT_KEY = "0xFFFFFFFF";
    private static final String TEST_STUID = "523031910080";
    private static final String TEST_EMAIL = "zsb_sjtu@sjtu.edu.cn";
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
        
        ResponseEntity<ProfileModel> response = restTemplate.getForEntity("/api/user/get", ProfileModel.class);
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
        ResponseEntity<ErrorResponse> errorResponse = restTemplate.getForEntity("/api/user/get", ErrorResponse.class);
        assertEquals(HttpStatus.UNAUTHORIZED, errorResponse.getStatusCode(), "Profile retrieval should fail without login");

        userLogin();

        ResponseEntity<ProfileModel> response = restTemplate.getForEntity("/api/user/get", ProfileModel.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Profile retrieval should be successful after login");
        ProfileModel profile = response.getBody();
        assertNotNull(profile, "Profile should not be null for an existing user");
        assertEquals(TEST_USER_ID, profile.getUserid(), "Profile ID should match the test user ID");
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
    void getPsyProfile() {
        // Test for unauthorized access
        ResponseEntity<ErrorResponse> errorResponse = restTemplate.getForEntity("/api/user/getpsy", ErrorResponse.class);
        assertEquals(HttpStatus.UNAUTHORIZED, errorResponse.getStatusCode(), "Psychologist profile retrieval should fail without login");

        // Psychologist login
        psyLogin();

        // Test for authorized access
        ResponseEntity<PsyProfileModel> response = restTemplate.getForEntity("/api/user/getpsy", PsyProfileModel.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Psychologist profile retrieval should be successful after psychologist login");
        PsyProfileModel psyProfile = response.getBody();
        assertNotNull(psyProfile, "Psychologist profile should not be null for an existing psychologist");
        assertEquals(TEST_PSY_ID, psyProfile.getUserid(), "Psychologist profile ID should match the test psychologist ID");
    }

    @Test
    void getSimplifiedProfile() {
        // Test for unauthorized access
        ResponseEntity<ErrorResponse> errorResponse = restTemplate.getForEntity("/api/user/getsim?userid=" + TEST_USER_ID, ErrorResponse.class);
        assertEquals(HttpStatus.UNAUTHORIZED, errorResponse.getStatusCode(), "Simplified profile retrieval should fail without login");
    
        // User login
        userLogin();

        // Test for authorized access
        ResponseEntity<SimplifiedProfileModel> response = restTemplate.getForEntity("/api/user/getsim?userid=" + TEST_USER_ID, SimplifiedProfileModel.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Simplified profile retrieval should be successful after user login");
        SimplifiedProfileModel simplifiedProfile = response.getBody();
        assertNotNull(simplifiedProfile, "Simplified profile should not be null for an existing user");
        assertEquals(TEST_USER_ID, simplifiedProfile.getUserid(), "Simplified profile ID should match the test user ID");
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
    void getMilestone() {
        // Test for unauthorized access
        ResponseEntity<ErrorResponse> errorResponse = restTemplate.getForEntity("/api/user/milestone", ErrorResponse.class);
        assertEquals(HttpStatus.UNAUTHORIZED, errorResponse.getStatusCode(), "Milestone retrieval should fail without login");

        // User login
        userLogin();

        // Test for authorized access
        ResponseEntity<MilestoneModel> response = restTemplate.getForEntity("/api/user/milestone", MilestoneModel.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Milestone retrieval should be successful after user login");
        MilestoneModel milestone = response.getBody();
        assertNotNull(milestone, "Milestone should not be null for an existing user");
    }

    @Test
    void verifyPassword() {
        // Test for unauthorized access
        ResponseEntity<ErrorResponse> errorResponse = restTemplate.getForEntity("/api/user/verify/pwd?password=" + TEST_PASSWORD, ErrorResponse.class);
        assertEquals(HttpStatus.UNAUTHORIZED, errorResponse.getStatusCode(), "Password verification should fail without login");

        // User login
        userLogin();

        // Test for correct password
        ResponseEntity<Boolean> response = restTemplate.getForEntity("/api/user/verify/pwd?password=" + TEST_PASSWORD, Boolean.class);
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
        try {
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Boolean result = restTemplate.getForObject("/api/user/secure/code?email=" + TEST_EMAIL, Boolean.class);
        assertTrue(result, "验证码发送应该成功");

        // Test for adding a new user with valid data
        ProfileModel newProfile = new ProfileModel();
        newProfile.setUserid("NEW_USER_" + System.currentTimeMillis());
        newProfile.setUsername("New User" + System.currentTimeMillis());
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

        // Test for adding a psychologist user
        ProfileModel psyProfile = new ProfileModel();
        psyProfile.setUserid("NEW_PSY_" + System.currentTimeMillis());
        psyProfile.setUsername("New PSY" + System.currentTimeMillis());
        psyProfile.setEmail(TEST_EMAIL);
        psyProfile.setAvatar(null);
        psyProfile.setNote("This is a new psychologist user");
        psyProfile.setRole(2L);
        psyProfile.setJointime(System.currentTimeMillis());
        psyProfile.setLevel(0L);
        ResponseEntity<Boolean> psyResponse = restTemplate.postForEntity("/api/user/add", new UserModel(TEST_PSY_PASSWORD, TEST_STUID, psyProfile), Boolean.class);
        assertEquals(HttpStatus.OK, psyResponse.getStatusCode(), "Adding a new psychologist user should be successful");
        body = psyResponse.getBody();
        assertNotNull(body, "Response body should not be null for adding psychologist user");
        assertTrue(body, "Adding a new psychologist user should return true");

        // Test for adding a user with missing username
        ProfileModel invalidUser = new ProfileModel();
        invalidUser.setUserid("INVALID_USER_1752566178965");
        invalidUser.setUsername(null);
        invalidUser.setEmail(TEST_EMAIL); 
        invalidUser.setAvatar(null);
        invalidUser.setNote("This user has no username");
        invalidUser.setRole(0L);
        invalidUser.setJointime(System.currentTimeMillis());
        invalidUser.setLevel(0L);
        ResponseEntity<ErrorResponse> invalidResponse = restTemplate.postForEntity("/api/user/add", new UserModel(TEST_PASSWORD, TEST_STUID, invalidUser), ErrorResponse.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, invalidResponse.getStatusCode(), "Adding a user with missing username should return an error");

        // Test for adding a user with mismatched email
        ProfileModel mismatchedEmailUser = new ProfileModel();
        mismatchedEmailUser.setUserid("MISMATCHED_EMAIL_USER_1752566178965");
        mismatchedEmailUser.setUsername("Mismatched Email User");
        mismatchedEmailUser.setEmail("xxx@qq.com"); // Mismatched email
        mismatchedEmailUser.setAvatar(null);
        mismatchedEmailUser.setNote("This user has a mismatched email");
        mismatchedEmailUser.setRole(0L);
        mismatchedEmailUser.setJointime(System.currentTimeMillis());
        mismatchedEmailUser.setLevel(0L);
        ResponseEntity<Boolean> mismatchedResponse = restTemplate.postForEntity("/api/user/add", new UserModel(TEST_PASSWORD, TEST_STUID, mismatchedEmailUser), Boolean.class);
        body = mismatchedResponse.getBody();
        assertNotNull(body, "Response body should not be null for mismatched email user");
        assertFalse(body, "Adding a user with mismatched email should return false");

        // Test for adding a user with duplicate ID
        ProfileModel duplicateUser = new ProfileModel();
        duplicateUser.setUserid(TEST_USER_ID); // Using existing user ID
        duplicateUser.setUsername("Duplicate User");
        duplicateUser.setEmail(TEST_EMAIL);
        duplicateUser.setAvatar(null);
        duplicateUser.setNote("This is a new user");
        duplicateUser.setRole(0L);
        duplicateUser.setJointime(System.currentTimeMillis());
        duplicateUser.setLevel(0L);
        ResponseEntity<ErrorResponse> duplicateResponse = restTemplate.postForEntity("/api/user/add", new UserModel(TEST_PASSWORD, TEST_STUID, duplicateUser), ErrorResponse.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, duplicateResponse.getStatusCode(), "Adding a user with duplicate ID should return an error");
    }

    @Test
    void updateProfile() {
        // Test for unauthorized access
        ResponseEntity<ErrorResponse> errorResponse = restTemplate.exchange(
            "/api/user/profile/update",
            HttpMethod.PUT,
            new HttpEntity<>(new ProfileModel()),
            ErrorResponse.class
        );
        assertEquals(HttpStatus.UNAUTHORIZED, errorResponse.getStatusCode(), "Profile update should fail without login");

        // User login
        userLogin();

        // Test for updating profile with valid data
        ProfileModel updatedProfile = new ProfileModel();
        updatedProfile.setUserid(TEST_USER_ID);
        updatedProfile.setUsername(TEST_USERNAME);
        updatedProfile.setEmail(TEST_EMAIL);
        updatedProfile.setAvatar(BASE64_IMG);
        updatedProfile.setNote("This is an updated user");
        updatedProfile.setRole(0L);
        updatedProfile.setJointime(System.currentTimeMillis());
        updatedProfile.setLevel(0L);
        ResponseEntity<Boolean> response = restTemplate.exchange(
            "/api/user/profile/update",
            HttpMethod.PUT,
            new HttpEntity<>(updatedProfile),
            Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Updating profile should be successful with valid data");
        Boolean body = response.getBody();
        assertNotNull(body, "Response body should not be null");
        assertTrue(body, "Updating profile should return true for valid data");

        // Test for updating profile with invalid data
        ProfileModel invalidProfile = new ProfileModel();
        invalidProfile.setUserid(TEST_USER_ID);
        invalidProfile.setUsername(null); // Missing username
        invalidProfile.setEmail(TEST_EMAIL);
        invalidProfile.setAvatar(null);
        invalidProfile.setNote("This user has no username");
        invalidProfile.setRole(0L);
        invalidProfile.setJointime(System.currentTimeMillis());
        invalidProfile.setLevel(0L);
        ResponseEntity<ErrorResponse> invalidResponse = restTemplate.exchange(
            "/api/user/profile/update",
            HttpMethod.PUT,
            new HttpEntity<>(invalidProfile),
            ErrorResponse.class
        );
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, invalidResponse.getStatusCode(), "Updating profile with invalid data should return an error");

        // Test for updating profile with invalid avatar
        ProfileModel invalidAvatarProfile = new ProfileModel();
        invalidAvatarProfile.setUserid(TEST_USER_ID);
        invalidAvatarProfile.setUsername("Invalid Avatar User");
        invalidAvatarProfile.setEmail(TEST_EMAIL);
        invalidAvatarProfile.setAvatar("invalid_avatar_data"); // Invalid avatar data
        invalidAvatarProfile.setNote("This user has an invalid avatar");
        invalidAvatarProfile.setRole(0L);
        invalidAvatarProfile.setJointime(System.currentTimeMillis());
        invalidAvatarProfile.setLevel(0L);
        ResponseEntity<ErrorResponse> invalidAvatarResponse = restTemplate.exchange(
            "/api/user/profile/update",
            HttpMethod.PUT,
            new HttpEntity<>(invalidAvatarProfile),
            ErrorResponse.class
        );
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, invalidAvatarResponse.getStatusCode(), "Updating profile with invalid avatar should return an error");

        // Test for updating profile with mismatched userid
        ProfileModel mismatchedUseridProfile = new ProfileModel();
        mismatchedUseridProfile.setUserid("MISMATCHED_USER_ID_1752566178965"); // Different ID
        mismatchedUseridProfile.setUsername("Mismatched User");
        mismatchedUseridProfile.setEmail(TEST_EMAIL);
        mismatchedUseridProfile.setAvatar(null);
        mismatchedUseridProfile.setNote("This user has a mismatched ID");
        mismatchedUseridProfile.setRole(0L);
        mismatchedUseridProfile.setJointime(System.currentTimeMillis());
        mismatchedUseridProfile.setLevel(0L);
        ResponseEntity<ErrorResponse> mismatchedResponse = restTemplate.exchange(
            "/api/user/profile/update",
            HttpMethod.PUT,
            new HttpEntity<>(mismatchedUseridProfile),
            ErrorResponse.class
        );
        assertEquals(HttpStatus.FORBIDDEN, mismatchedResponse.getStatusCode(), "Updating profile with mismatched userid should return an error");
    }

    @Test
    void updatePsyProfile() {
        // Test for unauthorized access
        ResponseEntity<ErrorResponse> errorResponse = restTemplate.exchange(
            "/api/user/profile/updatepsy",
            HttpMethod.PUT,
            new HttpEntity<>(new PsyProfileModel()),
            ErrorResponse.class
        );
        assertEquals(HttpStatus.UNAUTHORIZED, errorResponse.getStatusCode(), "Psychologist profile update should fail without login");

        // Psychologist login
        psyLogin();

        // Test for updating psychologist profile with valid data
        PsyProfileExtra psyProfileExtra = new PsyProfileExtra(TEST_PSY_ID);
        PsyProfileModel updatedPsyProfile = new PsyProfileModel(psyProfileExtra);
        updatedPsyProfile.setUsername(TEST_PSY_USERNAME);
        updatedPsyProfile.setEmail(TEST_EMAIL);
        updatedPsyProfile.setAvatar(BASE64_IMG);
        updatedPsyProfile.setJointime(System.currentTimeMillis());
        ResponseEntity<Boolean> response = restTemplate.exchange(
            "/api/user/profile/updatepsy",
            HttpMethod.PUT,
            new HttpEntity<>(updatedPsyProfile),
            Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Updating psychologist profile should be successful with valid data");
        Boolean body = response.getBody();
        assertNotNull(body, "Response body should not be null");
        assertTrue(body, "Updating psychologist profile should return true for valid data");

        // Test for updating psychologist profile with invalid data
        PsyProfileModel invalidPsyProfile = new PsyProfileModel(psyProfileExtra);
        invalidPsyProfile.setUsername(null); // Missing username
        invalidPsyProfile.setEmail(TEST_EMAIL);
        invalidPsyProfile.setAvatar(null);
        invalidPsyProfile.setJointime(System.currentTimeMillis());
        ResponseEntity<ErrorResponse> invalidResponse = restTemplate.exchange(
            "/api/user/profile/updatepsy",
            HttpMethod.PUT,
            new HttpEntity<>(invalidPsyProfile),
            ErrorResponse.class
        );
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, invalidResponse.getStatusCode(), "Updating psychologist profile with invalid data should return an error");

        // Test for updating psychologist profile with invalid avatar
        PsyProfileModel invalidAvatarPsyProfile = new PsyProfileModel(psyProfileExtra);
        invalidAvatarPsyProfile.setUsername("Invalid Avatar Psychologist");
        invalidAvatarPsyProfile.setEmail(TEST_EMAIL);
        invalidAvatarPsyProfile.setAvatar("invalid_avatar_data"); // Invalid avatar data
        invalidAvatarPsyProfile.setJointime(System.currentTimeMillis());
        ResponseEntity<ErrorResponse> invalidAvatarResponse = restTemplate.exchange(
            "/api/user/profile/updatepsy",
            HttpMethod.PUT,
            new HttpEntity<>(invalidAvatarPsyProfile),
            ErrorResponse.class
        );
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, invalidAvatarResponse.getStatusCode(), "Updating psychologist profile with invalid avatar should return an error");

        // Test for updating psychologist profile with mismatched userid
        psyProfileExtra = new PsyProfileExtra("MISMATCHED_PSY_ID_1752566178965");
        PsyProfileModel mismatchedUseridPsyProfile = new PsyProfileModel(psyProfileExtra);
        mismatchedUseridPsyProfile.setUsername("Mismatched Psychologist");
        mismatchedUseridPsyProfile.setEmail(TEST_EMAIL);
        mismatchedUseridPsyProfile.setAvatar(null);
        mismatchedUseridPsyProfile.setJointime(System.currentTimeMillis());
        ResponseEntity<ErrorResponse> mismatchedResponse = restTemplate.exchange(
            "/api/user/profile/updatepsy",
            HttpMethod.PUT,
            new HttpEntity<>(mismatchedUseridPsyProfile),
            ErrorResponse.class
        );
        assertEquals(HttpStatus.FORBIDDEN, mismatchedResponse.getStatusCode(), "Updating psychologist profile with mismatched userid should return an error");
    }

    @Test
    void updatePassword() {
        // Test for unauthorized access
        ResponseEntity<ErrorResponse> errorResponse = restTemplate.exchange(
            "/api/user/pwd?password=" + TEST_PASSWORD,
            HttpMethod.PUT,
            null,
            ErrorResponse.class
        );
        assertEquals(HttpStatus.UNAUTHORIZED, errorResponse.getStatusCode(), "Password update should fail without login");

        // User login
        userLogin();

        // Test for updating password with valid data
        ResponseEntity<Boolean> response = restTemplate.exchange(
            "/api/user/pwd?password=" + TEST_PASSWORD,
            HttpMethod.PUT,
            null,
            Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Password update should be successful with valid data");
        Boolean body = response.getBody();
        assertNotNull(body, "Response body should not be null");
        assertTrue(body, "Password update should return true for valid data");

        // Test for updating password with invalid data (e.g., empty password)
        ResponseEntity<ErrorResponse> invalidResponse = restTemplate.exchange(
            "/api/user/pwd?password=",
            HttpMethod.PUT,
            null,
            ErrorResponse.class
        );
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, invalidResponse.getStatusCode(), "Updating password with invalid data should return an error");
    }

    @Test
    void setDisabled() {
        // Test for unauthorized access
        ResponseEntity<ErrorResponse> errorResponse = restTemplate.exchange(
            "/api/user/disabled/set?userid=" + TEST_DISABLED_ID + "&disabled=true",
            HttpMethod.PUT,
            null,
            ErrorResponse.class
        );
        assertEquals(HttpStatus.UNAUTHORIZED, errorResponse.getStatusCode(), "Setting disabled status should fail without admin login");

        // User login
        userLogin();
        errorResponse = restTemplate.exchange(
            "/api/user/disabled/set?userid=" + TEST_DISABLED_ID + "&disabled=true",
            HttpMethod.PUT,
            null,
            ErrorResponse.class
        );
        assertEquals(HttpStatus.FORBIDDEN, errorResponse.getStatusCode(), "Setting disabled status should fail for regular user");

        // Admin login
        adminLogin();

        // Test for setting disabled status with valid data
        ResponseEntity<Boolean> response = restTemplate.exchange(
            "/api/user/disabled/set?userid=" + TEST_DISABLED_ID + "&disabled=true",
            HttpMethod.PUT,
            null,
            Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Setting disabled status should be successful with valid data");
        Boolean body = response.getBody();
        assertNotNull(body, "Response body should not be null");
        assertTrue(body, "Setting disabled status should return true for valid data");

        // Test for setting disabled status with invalid userid
        ResponseEntity<ErrorResponse> invalidResponse = restTemplate.exchange(
            "/api/user/disabled/set?userid=INVALID_USER_ID&disabled=true",
            HttpMethod.PUT,
            null,
            ErrorResponse.class
        );
        assertEquals(HttpStatus.NOT_FOUND, invalidResponse.getStatusCode(), "Setting disabled status with invalid userid should return an error");
    }

    @Test
    void postAuthCode() {
        // Test for sending auth code with valid email
        try {
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Boolean result = restTemplate.getForObject("/api/user/secure/code?email=" + TEST_EMAIL, Boolean.class);
        assertTrue(result, "Auth code should be sent successfully for valid email");

        // Test for sending within a short time frame
        result = restTemplate.getForObject("/api/user/secure/code?email=" + TEST_EMAIL, Boolean.class);
        assertFalse(result, "Auth code should not be sent again within a short time frame");
    }

    @Test
    void checkAuthCode() {
        // Test for checking auth code before sending it
        Boolean result = restTemplate.getForObject("/api/user/secure/check?authCode=123456", Boolean.class);
        assertFalse(result, "Auth code check should fail before sending the code");

        // Send auth code first
        try {
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        result = restTemplate.getForObject("/api/user/secure/code?email=" + TEST_EMAIL, Boolean.class);
        assertTrue(result, "Auth code should be sent successfully for valid email");

        // Test for checking auth code with correct code
        result = restTemplate.getForObject("/api/user/secure/check?authCode=123456", Boolean.class);
        assertTrue(result, "Auth code check should be successful with correct code");

        // Test for checking auth code with incorrect code
        result = restTemplate.getForObject("/api/user/secure/check?authCode=654321", Boolean.class);
        assertFalse(result, "Auth code check should fail with incorrect code");

        // Test for checking auth code with expired code
        try {
            Thread.sleep(10000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        result = restTemplate.getForObject("/api/user/secure/check?authCode=123456", Boolean.class);
        assertFalse(result, "Auth code check should fail with expired code");
    }
}