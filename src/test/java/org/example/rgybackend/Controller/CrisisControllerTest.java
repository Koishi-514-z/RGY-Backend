package org.example.rgybackend.Controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.example.rgybackend.Model.CrisisAuditingModel;
import org.example.rgybackend.Model.CrisisModel;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory; 

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CrisisControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CrisisController crisisController;

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
    @NoArgsConstructor
    public class ConfirmCrisisRequest {
        private int crisisid;
        private Long urgencyLevel;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class DeleteCrisisRequest {
        private int crisisid;
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
    void listCrisisAuditing() {
        adminLogin();
        List<CrisisAuditingModel> crisisModels = restTemplate.getForObject("/api/crisis/listAuditing", List.class);
        assertNotNull(crisisModels);
        Logout();
        userLogin();
        // 只有管理员可以看到所有待审核的危机信息,判断用户查看时是否会抛出异常
        assertThrows(Exception.class, () -> restTemplate.getForObject("/api/crisis/listAuditing", List.class));
        Logout();
    }

    @Test
    @Order(3)
    void listCrisisByUser() {
        adminLogin();
        List<CrisisAuditingModel> crisisModels = restTemplate.getForObject("/api/crisis/listUser/" + TEST_USER_ID, List.class);
        assertNotNull(crisisModels);
        Logout();
        userLogin();
        // 只有管理员可以看到所有待审核的危机信息,判断用户查看时是否会抛出异常
        assertThrows(Exception.class, () -> restTemplate.getForObject("/api/crisis/listUser/" + TEST_USER_ID, List.class));
        Logout();

    }

    @Test
    @Order(2)
    void confirmCrisis() {
        adminLogin();
        ConfirmCrisisRequest request = new ConfirmCrisisRequest(1, 2L);
        boolean result = restTemplate.postForObject("/api/crisis/confirm", request, Boolean.class);
        assertTrue(result);
        Logout();
        userLogin();
        // 只有管理员可以审核危机信息,判断用户审核时是否会抛出异常
        ConfirmCrisisRequest request2 = new ConfirmCrisisRequest(2, 1L);
        assertThrows(Exception.class, () -> restTemplate.postForObject("/api/crisis/confirm", request2, Boolean.class));
        Logout();

    }

    @Test
    @Order(5)
    void deleteCrisisAuditing() {
        adminLogin();
        DeleteCrisisRequest request = new DeleteCrisisRequest(2);
        boolean result = restTemplate.postForObject("/api/crisis/delete", request, Boolean.class);
        assertTrue(result);
        Logout();
        userLogin();
        // 只有管理员可以删除危机信息,判断用户删除时是否会抛出异常
        DeleteCrisisRequest request2 = new DeleteCrisisRequest(2);
        assertThrows(Exception.class, () -> restTemplate.postForObject("/api/crisis/delete", request2, Boolean.class));
        Logout();

    }

    @Test
    @Order(4)
    void getAllCrisis() {
        adminLogin();
        List<CrisisModel> crisisModels = restTemplate.getForObject("/api/crisis/getall", List.class);
        assertNotNull(crisisModels);

    }

    @Test
    @Order(6)
    void updateCrisisStatus() {
        adminLogin();
        //boolean result = restTemplate.put("/api/crisis/update?crisisid=1&status=2", null);
        ResponseEntity<Boolean> response = restTemplate.exchange("/api/crisis/update?crisisid=5&status=2", HttpMethod.PUT,null,Boolean.class);
        assertTrue(response.getBody());
        Logout();
        userLogin();
        Logout();
    }
}