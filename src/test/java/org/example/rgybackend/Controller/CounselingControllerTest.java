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
import org.example.rgybackend.DTO.PsyCommentData;
import org.example.rgybackend.Model.AvailableTimeModel;
import org.example.rgybackend.Model.CounselingModel;
import org.example.rgybackend.Model.PsyProfileModel;
import org.example.rgybackend.Model.TagModel;
import org.example.rgybackend.Utils.ErrorResponse;
import org.example.rgybackend.Utils.TimeUtil;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CounselingControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String TEST_USERNAME = "TEST_USER";
    private static final String TEST_PASSWORD = "123456";
    private static final String TEST_PSY_USERNAME = "TEST_PSY";
    private static final String TEST_PSY_PASSWORD = "123456";
    private static final String TEST_PSY_ID = "TEST_PSY_1752567914700";

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
    void getCounseling() {
        psyLogin();

        ResponseEntity<CounselingModel[]> response = restTemplate.getForEntity("/api/counseling/get?psyid=" + TEST_PSY_ID, CounselingModel[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get counseling data");
        CounselingModel[] counselingModels = response.getBody();
        assertNotNull(counselingModels, "Counseling data should not be null");
    }

    @Test
    void getUserCounseling() {
        userLogin();

        ResponseEntity<CounselingModel[]> response = restTemplate.getForEntity("/api/counseling/getuser", CounselingModel[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get user counseling data");
        CounselingModel[] counselingModels = response.getBody();
        assertNotNull(counselingModels, "User counseling data should not be null");
    }

    @Test
    void getDateCounseling() {
        psyLogin();

        Long timestamp = TimeUtil.now() + TimeUtil.DAY;
        ResponseEntity<CounselingModel[]> response = restTemplate.getForEntity("/api/counseling/getdate?psyid=" + TEST_PSY_ID + "&timestamp=" + timestamp, CounselingModel[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get date counseling data");
        CounselingModel[] counselingModels = response.getBody();
        assertNotNull(counselingModels, "Date counseling data should not be null");
    }

    @Test
    void getPsyProfile() {
        userLogin();

        ResponseEntity<PsyProfileModel> response = restTemplate.getForEntity("/api/counseling/getpsy?psyid=" + TEST_PSY_ID, PsyProfileModel.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get psy profile");
        PsyProfileModel psyProfile = response.getBody();
        assertNotNull(psyProfile, "Psy profile should not be null");
        assertEquals(TEST_PSY_ID, psyProfile.getUserid(), "Psy ID should match");
        assertEquals(TEST_PSY_USERNAME, psyProfile.getUsername(), "Psy username should match");
    }

    @Test
    void getPsyProfiles() {
        userLogin();

        ResponseEntity<PsyProfileModel[]> response = restTemplate.getForEntity("/api/counseling/getpsys", PsyProfileModel[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get psy profiles");
        PsyProfileModel[] psyProfiles = response.getBody();
        assertNotNull(psyProfiles, "Psy profiles should not be null");
        assertTrue(psyProfiles.length >= 0, "Psy profiles should be array");
    }

    @Test
    void getTypeTags() {
        userLogin();

        ResponseEntity<TagModel[]> response = restTemplate.getForEntity("/api/counseling/gettags", TagModel[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get type tags");
        TagModel[] typeTags = response.getBody();
        assertNotNull(typeTags, "Type tags should not be null");
        assertTrue(typeTags.length > 0, "Type tags should not be empty");
    }

    @Test
    void addCounseling() {
        userLogin();

        CounselingModel counselingModel = new CounselingModel();
        counselingModel.setPsyid(TEST_PSY_ID);
        counselingModel.setTimestamp(TimeUtil.now() + TimeUtil.DAY);
        counselingModel.setType(counselingModel.typeTags.get(0)); // 选择第一个类型标签
        counselingModel.setStatus(0L); // pending

        HttpEntity<CounselingModel> request = new HttpEntity<>(counselingModel);
        ResponseEntity<Boolean> response = restTemplate.exchange(
            "/api/counseling/add",
            HttpMethod.POST,
            request,
            Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to add counseling");
        Boolean result = response.getBody();
        assertNotNull(result, "Add counseling result should not be null");
        assertTrue(result, "Add counseling should be successful");
    }

    @Test
    void addComment() {
        userLogin();

        PsyCommentData commentData = new PsyCommentData();

        commentData.setPsyid(TEST_PSY_ID);
        commentData.setSuccess(true);
        commentData.setScore(5L);

        HttpEntity<PsyCommentData> request = new HttpEntity<>(commentData);
        ResponseEntity<Boolean> response = restTemplate.exchange(
            "/api/counseling/addcomm",
            HttpMethod.POST,
            request,
            Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to add comment");
        Boolean result = response.getBody();
        assertNotNull(result, "Add comment result should not be null");
        assertTrue(result, "Add comment should be successful");
    }

    @Test
    void setStatus() {
        psyLogin();

        // 获取一个咨询预约的ID
        ResponseEntity<CounselingModel[]> counselings = restTemplate.getForEntity("/api/counseling/get?psyid=" + TEST_PSY_ID, CounselingModel[].class);
        assertEquals(HttpStatus.OK, counselings.getStatusCode(), "Failed to get counseling data");
        CounselingModel[] counselingModels = counselings.getBody();
        assertNotNull(counselingModels, "Counseling data should not be null");
        Long counselingid = counselingModels[0].getCounselingid();

        // 设置咨询状态为已接受（1）
        ResponseEntity<Boolean> response = restTemplate.exchange(
            "/api/counseling/status?counselingid=" + counselingid + "&status=1",
            HttpMethod.PUT,
            null,
            Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to set status");
        Boolean result = response.getBody();
        assertNotNull(result, "Set status result should not be null");
        assertTrue(result, "Set status should be successful");

        // 设置咨询状态为已完成（2）
        response = restTemplate.exchange(
            "/api/counseling/status?counselingid=" + counselingid + "&status=2",
            HttpMethod.PUT,
            null,
            Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to set status to finished");
        result = response.getBody();
        assertNotNull(result, "Set status to finished result should not be null");
        assertTrue(result, "Set status to finished should be successful");

        // 设置咨询状态为已拒绝（3）
        response = restTemplate.exchange(
            "/api/counseling/status?counselingid=" + counselingid + "&status=3",
            HttpMethod.PUT,
            null,
            Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to set status to rejected");
        result = response.getBody();
        assertNotNull(result, "Set status to rejected result should not be null");
        assertTrue(result, "Set status to rejected should be successful");

        // 测试无效的Counseling ID
        ResponseEntity<ErrorResponse> errorResponse = restTemplate.exchange(
            "/api/counseling/status?counselingid=999&status=1",
            HttpMethod.PUT,
            null,
            ErrorResponse.class
        );
        assertEquals(HttpStatus.NOT_FOUND, errorResponse.getStatusCode(), "Should return NOT_FOUND for invalid counseling ID");


        userLogin();
        response = restTemplate.exchange(
            "/api/counseling/status?counselingid=" + counselingid + "&status=3",
            HttpMethod.PUT,
            null,
            Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to set status to rejected");
        result = response.getBody();
        assertNotNull(result, "Set status to rejected result should not be null");
        assertTrue(result, "Set status to rejected should be successful");
    }

    @Test
    void removeCounseling() {
        psyLogin();

        // 获取一个咨询预约的ID
        ResponseEntity<CounselingModel[]> counselings = restTemplate.getForEntity("/api/counseling/get?psyid=" + TEST_PSY_ID, CounselingModel[].class);
        assertEquals(HttpStatus.OK, counselings.getStatusCode(), "Failed to get counseling data");
        CounselingModel[] counselingModels = counselings.getBody();
        assertNotNull(counselingModels, "Counseling data should not be null");
        Long counselingid = counselingModels[0].getCounselingid();

        ResponseEntity<Boolean> response = restTemplate.exchange(
            "/api/counseling/del?counselingid=" + counselingid,
            HttpMethod.DELETE,
            null,
            Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to remove counseling");
        Boolean result = response.getBody();
        assertNotNull(result, "Remove counseling result should not be null");
    }

    @Test
    void getAvailableTime() {
        userLogin();

        ResponseEntity<AvailableTimeModel> response = restTemplate.getForEntity("/api/counseling/available/get?psyid=" + TEST_PSY_ID, AvailableTimeModel.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get available time");
        AvailableTimeModel availableTime = response.getBody();
        assertNotNull(availableTime, "Available time should not be null");
        assertEquals(TEST_PSY_ID, availableTime.getPsyid(), "Psy ID should match");
    }

    @Test
    void getDateAvailables() {
        userLogin();

        LocalDate monday = TimeUtil.firstDayOfWeek().plusDays(7);
        LocalDate tuesday = monday.plusDays(1);
        LocalDate wednesday = monday.plusDays(2);
        LocalDate thursday = monday.plusDays(3);
        LocalDate friday = monday.plusDays(4);
        LocalDate saturday = monday.plusDays(5);
        LocalDate sunday = monday.plusDays(6);

        ResponseEntity<Long[]> mondayResponse = restTemplate.getForEntity("/api/counseling/available/getdate?psyid=" + TEST_PSY_ID + "&timestamp=" + TimeUtil.getStartOfDayTimestamp(monday), Long[].class);
        assertEquals(HttpStatus.OK, mondayResponse.getStatusCode(), "Failed to get Monday availables");
        Long[] mondayAvailables = mondayResponse.getBody();
        assertNotNull(mondayAvailables, "Monday availables should not be null");
        assertTrue(mondayAvailables.length > 0, "Monday availables should not be empty");

        ResponseEntity<Long[]> tuesdayResponse = restTemplate.getForEntity("/api/counseling/available/getdate?psyid=" + TEST_PSY_ID + "&timestamp=" + TimeUtil.getStartOfDayTimestamp(tuesday), Long[].class);
        assertEquals(HttpStatus.OK, tuesdayResponse.getStatusCode(), "Failed to get Tuesday availables");
        Long[] tuesdayAvailables = tuesdayResponse.getBody();
        assertNotNull(tuesdayAvailables, "Tuesday availables should not be null");
        assertTrue(tuesdayAvailables.length > 0, "Tuesday availables should not be empty");

        ResponseEntity<Long[]> wednesdayResponse = restTemplate.getForEntity("/api/counseling/available/getdate?psyid=" + TEST_PSY_ID + "&timestamp=" + TimeUtil.getStartOfDayTimestamp(wednesday), Long[].class);
        assertEquals(HttpStatus.OK, wednesdayResponse.getStatusCode(), "Failed to get Wednesday availables");
        Long[] wednesdayAvailables = wednesdayResponse.getBody();
        assertNotNull(wednesdayAvailables, "Wednesday availables should not be null");
        assertTrue(wednesdayAvailables.length > 0, "Wednesday availables should not be empty");

        ResponseEntity<Long[]> thursdayResponse = restTemplate.getForEntity("/api/counseling/available/getdate?psyid=" + TEST_PSY_ID + "&timestamp=" + TimeUtil.getStartOfDayTimestamp(thursday), Long[].class);
        assertEquals(HttpStatus.OK, thursdayResponse.getStatusCode(), "Failed to get Thursday availables");
        Long[] thursdayAvailables = thursdayResponse.getBody();
        assertNotNull(thursdayAvailables, "Thursday availables should not be null");
        assertTrue(thursdayAvailables.length > 0, "Thursday availables should not be empty");

        ResponseEntity<Long[]> fridayResponse = restTemplate.getForEntity("/api/counseling/available/getdate?psyid=" + TEST_PSY_ID + "&timestamp=" + TimeUtil.getStartOfDayTimestamp(friday), Long[].class);
        assertEquals(HttpStatus.OK, fridayResponse.getStatusCode(), "Failed to get Friday availables");
        Long[] fridayAvailables = fridayResponse.getBody();
        assertNotNull(fridayAvailables, "Friday availables should not be null");
        assertTrue(fridayAvailables.length > 0, "Friday availables should not be empty");

        ResponseEntity<Long[]> saturdayResponse = restTemplate.getForEntity("/api/counseling/available/getdate?psyid=" + TEST_PSY_ID + "&timestamp=" + TimeUtil.getStartOfDayTimestamp(saturday), Long[].class);
        assertEquals(HttpStatus.OK, saturdayResponse.getStatusCode(), "Failed to get Saturday availables");
        Long[] saturdayAvailables = saturdayResponse.getBody();
        assertNotNull(saturdayAvailables, "Saturday availables should not be null");
        assertTrue(saturdayAvailables.length > 0, "Saturday availables should not be empty");

        ResponseEntity<Long[]> sundayResponse = restTemplate.getForEntity("/api/counseling/available/getdate?psyid=" + TEST_PSY_ID + "&timestamp=" + TimeUtil.getStartOfDayTimestamp(sunday), Long[].class);
        assertEquals(HttpStatus.OK, sundayResponse.getStatusCode(), "Failed to get Sunday availables");
        Long[] sundayAvailables = sundayResponse.getBody();
        assertNotNull(sundayAvailables, "Sunday availables should not be null");
        assertTrue(sundayAvailables.length > 0, "Sunday availables should not be empty");
    }

    @Test
    void setAvailableTimes() {
        psyLogin();

        AvailableTimeModel availableTimeModel = new AvailableTimeModel();
        availableTimeModel.setPsyid(TEST_PSY_ID);
        availableTimeModel.setMonday(List.of(9L, 10L, 11L)); 
        availableTimeModel.setTuesday(List.of(14L, 15L, 16L)); 
        availableTimeModel.setWednesday(List.of(9L, 10L));
        availableTimeModel.setThursday(List.of(14L, 15L));
        availableTimeModel.setFriday(List.of(9L, 10L, 14L));
        availableTimeModel.setSaturday(List.of(10L));
        availableTimeModel.setSunday(List.of(10L));

        HttpEntity<AvailableTimeModel> request = new HttpEntity<>(availableTimeModel);
        ResponseEntity<Boolean> response = restTemplate.exchange(
            "/api/counseling/available/set",
            HttpMethod.POST,
            request,
            Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to set available times");
        Boolean result = response.getBody();
        assertNotNull(result, "Set available times result should not be null");
        assertTrue(result, "Set available times should be successful");
    }

    @Test
    void placeCallBackRequest() {
        userLogin();

        ResponseEntity<Boolean> response = restTemplate.exchange(
            "/api/counseling/callback",
            HttpMethod.POST,
            null,
            Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to place callback request");
        Boolean result = response.getBody();
        assertNotNull(result, "Place callback request result should not be null");
        assertTrue(result, "Place callback request should be successful");
    }
}