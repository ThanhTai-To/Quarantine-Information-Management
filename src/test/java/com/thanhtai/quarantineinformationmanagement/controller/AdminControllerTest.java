package com.thanhtai.quarantineinformationmanagement.controller;

import com.thanhtai.quarantineinformationmanagement.api.model.*;
import com.thanhtai.quarantineinformationmanagement.error.AlreadyExistedException;
import com.thanhtai.quarantineinformationmanagement.error.DuplicateException;
import com.thanhtai.quarantineinformationmanagement.error.InvalidProvinceName;
import com.thanhtai.quarantineinformationmanagement.error.ResourceNotFoundException;
import com.thanhtai.quarantineinformationmanagement.model.QuarantineInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminControllerTest {


    @Autowired
    private AdminController adminController;
    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    public void setUp() {
        LoginRequestModel requestModel = new LoginRequestModel();
        requestModel.setUsername("admin");
        requestModel.setPassword("12345678");
        this.adminController.login(requestModel);
    }

    @Test
    void testLoginWithCorrectUsernameAndPassword() {
        assertNotNull(adminController);
    }

    @Test
    void testLoginWithWrongUsername() {
        LoginRequestModel request = new LoginRequestModel();
        request.setUsername("username");
        request.setPassword("dfghjklghj");
        assertThrows(InternalAuthenticationServiceException.class, () -> {adminController.login(request);});
    }


    @Test
    void testCreateQuarantineInformationSuccess() {
        CreateQIRequestModel requestModel = new CreateQIRequestModel();
        requestModel.setOriginFrom(Province.AN_GIANG);
        requestModel.setDestination(Province.BA_RIA_VUNG_TAU);
        requestModel.setStartAt("21-06-2021");
        requestModel.setEndAt("10-07-2021");
        ResponseEntity<ObjectSuccessResponse> responseEntity = adminController.createQuarantineInformation(requestModel);
        assertEquals(HttpStatus.CREATED.value(), responseEntity.getStatusCodeValue());
        assertEquals("Created successfully", Objects.requireNonNull(responseEntity.getBody()).getMessage());

        assertThrows(AlreadyExistedException.class, () -> {adminController.createQuarantineInformation(requestModel);});
    }

    @Test
    void testGetListQuarantineInformation() {
        createQuarantineInformation("PHU_YEN", "KHANH_HOA", "21-03-2021", "15-04-2021");
        createQuarantineInformation("NINH_BINH", "QUANG_NINH", "22-03-2021", "16-04-2021");
        createQuarantineInformation("HA_NOI", "HO_CHI_MINH", "23-03-2021", "17-04-2021");
        ResponseEntity<QuarantineInformationResponse> responseEntity =
                adminController.getListQuarantineInformation(0, "PHU_YEN", "KHANH_HOA");
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());

        assertNotNull(adminController.getListQuarantineInformation(0, "ALL", "KHANH_HOA"));
        assertNotNull(adminController.getListQuarantineInformation(0, "NINH_BINH", "ALL"));
        assertNotNull(adminController.getListQuarantineInformation(0, "ALL", "ALL"));

        assertThrows(InvalidProvinceName.class, () -> {adminController.getListQuarantineInformation(0, "PHU YEN", "NHA TRANG");});
    }

    @Test
    void testDeleteQuarantineInformationById() {
        QuarantineInformation qi = createQuarantineInformation("PHU_YEN", "KHANH_HOA", "21-03-2021", "15-04-2021");
        ResponseEntity<ObjectSuccessResponse> responseEntity = adminController.deleteQuarantineInformationById(qi.getId());
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        assertEquals("Deleted successfully", Objects.requireNonNull(responseEntity.getBody()).getMessage());
        // delete with id not found
        assertThrows(ResourceNotFoundException.class, () -> {adminController.deleteQuarantineInformationById(qi.getId() + "1");});

    }

    @Test
    void testUpdateQuarantineInformationById() {
        QuarantineInformation qi = createQuarantineInformation("PHU_YEN", "KHANH_HOA", "21-03-2021", "15-04-2021");
        UpdateQIRequestModel updateQIRequestModel = new UpdateQIRequestModel();
        updateQIRequestModel.setOriginFrom(Province.HA_NOI);
        updateQIRequestModel.setDestination(Province.KHANH_HOA);
        updateQIRequestModel.setEndAt("29-04-2021");
        updateQIRequestModel.setReasonUpdated("extend end date and update originFrom");
        ResponseEntity<QuarantineInformationResponseModel> updated = adminController.updateQuarantineInformationById(qi.getId(), updateQIRequestModel);
        assertNotNull(updated);
        assertEquals(HttpStatus.OK.value(), updated.getStatusCodeValue());
    }

    @Test
    void testUpdateQuarantineInformationWithExceptions() {
        QuarantineInformation qi = createQuarantineInformation("PHU_YEN", "KHANH_HOA", "21-03-2021", "15-04-2021");
        UpdateQIRequestModel updateQIRequestModel = new UpdateQIRequestModel();
        updateQIRequestModel.setOriginFrom(Province.HA_NOI);
        updateQIRequestModel.setDestination(Province.KHANH_HOA);
        updateQIRequestModel.setEndAt("29-04-2021");
        updateQIRequestModel.setReasonUpdated("extend end date and update originFrom");

        assertThrows(ResourceNotFoundException.class, () -> {adminController.updateQuarantineInformationById(qi.getId() + "1", updateQIRequestModel);});

        updateQIRequestModel.setDestination(Province.HA_NOI);
        assertThrows(DuplicateException.class, () -> {adminController.updateQuarantineInformationById(qi.getId(), updateQIRequestModel);});
    }

    private QuarantineInformation createQuarantineInformation(String origin, String destination, String startDate, String endDate) {
        return mongoTemplate.save(QuarantineInformation.builder()
                .originFrom(origin)
                .destination(destination)
                .startAt(startDate)
                .endAt(endDate).build());
    }

}
