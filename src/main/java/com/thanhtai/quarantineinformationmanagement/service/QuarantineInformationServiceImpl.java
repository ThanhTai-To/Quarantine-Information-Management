package com.thanhtai.quarantineinformationmanagement.service;

import com.thanhtai.quarantineinformationmanagement.api.model.CreateQIRequestModel;
import com.thanhtai.quarantineinformationmanagement.model.QuarantineInformation;
import com.thanhtai.quarantineinformationmanagement.repository.QuarantineInformationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuarantineInformationServiceImpl implements QuarantineInformationService {

    @Autowired
    private QuarantineInformationRepository quarantineInformationRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public QuarantineInformation createQuarantineInformation(CreateQIRequestModel createQIRequestModel) {
        QuarantineInformation quarantineInformation =
                modelMapper.map(createQIRequestModel, QuarantineInformation.class);
        return quarantineInformationRepository.save(quarantineInformation);
    }
}
