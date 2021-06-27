package com.thanhtai.quarantineinformationmanagement.service;

import com.thanhtai.quarantineinformationmanagement.api.model.CreateQIRequestModel;
import com.thanhtai.quarantineinformationmanagement.api.model.QuarantineInformationResponse;
import com.thanhtai.quarantineinformationmanagement.api.model.QuarantineInformationResponseModel;
import com.thanhtai.quarantineinformationmanagement.api.model.UpdateQIRequestModel;
import com.thanhtai.quarantineinformationmanagement.model.QuarantineInformation;

public interface QuarantineInformationService {

    QuarantineInformation createQuarantineInformation(CreateQIRequestModel createQIRequestModel);

    QuarantineInformationResponse getListQuarantineInformation(Integer page, String originFrom, String destination);

    void deleteQuarantineInformationById(String quarantineInfoId);

    QuarantineInformationResponseModel updateQuarantineInformationById(String quarantineInfoId
            , UpdateQIRequestModel updateQIRequestModel);
}
