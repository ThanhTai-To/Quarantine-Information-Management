package com.thanhtai.quarantineinformationmanagement.service;

import com.thanhtai.quarantineinformationmanagement.api.model.CreateQIRequestModel;
import com.thanhtai.quarantineinformationmanagement.model.QuarantineInformation;

public interface QuarantineInformationService {

    QuarantineInformation createQuarantineInformation(CreateQIRequestModel createQIRequestModel);
}
