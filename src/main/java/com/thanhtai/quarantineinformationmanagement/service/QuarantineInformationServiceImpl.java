package com.thanhtai.quarantineinformationmanagement.service;

import com.thanhtai.quarantineinformationmanagement.api.model.CreateQIRequestModel;
import com.thanhtai.quarantineinformationmanagement.api.model.QuarantineInformationResponse;
import com.thanhtai.quarantineinformationmanagement.api.model.QuarantineInformationResponseModel;
import com.thanhtai.quarantineinformationmanagement.model.QuarantineInformation;
import com.thanhtai.quarantineinformationmanagement.repository.QuarantineInformationRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class QuarantineInformationServiceImpl implements QuarantineInformationService {

    private static final Logger logger = LoggerFactory.getLogger(QuarantineInformationServiceImpl.class);


    @Autowired
    private QuarantineInformationRepository quarantineInformationRepository;
    @Autowired
    private ModelMapper modelMapper;

    private final int PAGE_SIZE = 20;

    @Override
    public QuarantineInformation createQuarantineInformation(CreateQIRequestModel createQIRequestModel) {
        // TODO: catch origin=destination
        // already existed same start date and place
        if (createQIRequestModel.getOriginFrom().equals(createQIRequestModel.getDestination())) {
            throw new RuntimeException("origin must differ to destination");
        }
        QuarantineInformation quarantineInformation =
                modelMapper.map(createQIRequestModel, QuarantineInformation.class);
        return quarantineInformationRepository.save(quarantineInformation);
    }

    @Override
    public QuarantineInformationResponse getListQuarantineInformation(Integer page) {
        Page<QuarantineInformation> quarantineInformationList =
                quarantineInformationRepository.findAllBy(PageRequest.of(page, PAGE_SIZE));
        return buildQuarantineInformationResponseModel(quarantineInformationList);
    }

    private QuarantineInformationResponse buildQuarantineInformationResponseModel(Page<QuarantineInformation> quarantineInformationList) {
        QuarantineInformationResponse quarantineInformationResponse = new QuarantineInformationResponse();
        quarantineInformationResponse.setCurrentPage(quarantineInformationList.getNumber());
        quarantineInformationResponse.setTotalPage(quarantineInformationList.getTotalPages());
        quarantineInformationResponse.setTotalElements(quarantineInformationList.getNumberOfElements());
        quarantineInformationList.stream().forEach(quarantineInformation -> {
            QuarantineInformationResponseModel qiResponseModel =
                    modelMapper.map(quarantineInformation, QuarantineInformationResponseModel.class);
            quarantineInformationResponse.addQuarantineInformationListItem(qiResponseModel);
        });
        return quarantineInformationResponse;
    }
}
