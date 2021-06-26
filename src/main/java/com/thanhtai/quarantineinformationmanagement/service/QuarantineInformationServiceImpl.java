package com.thanhtai.quarantineinformationmanagement.service;

import com.thanhtai.quarantineinformationmanagement.api.model.CreateQIRequestModel;
import com.thanhtai.quarantineinformationmanagement.api.model.QuarantineInformationResponse;
import com.thanhtai.quarantineinformationmanagement.api.model.QuarantineInformationResponseModel;
import com.thanhtai.quarantineinformationmanagement.api.model.UpdateQIRequestModel;
import com.thanhtai.quarantineinformationmanagement.model.QuarantineInformation;
import com.thanhtai.quarantineinformationmanagement.repository.QuarantineInformationRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        validateSameOriginFromAndDestination(createQIRequestModel.getOriginFrom().toString()
                ,createQIRequestModel.getDestination().toString());
        Optional<QuarantineInformation> optionalQuarantineInformation = quarantineInformationRepository.findByOriginFromAndAndDestinationAndStartAt(
                createQIRequestModel.getOriginFrom().toString()
                , createQIRequestModel.getDestination().toString()
                , createQIRequestModel.getStartAt().toString());
        if (optionalQuarantineInformation.isPresent()) {
            throw new RuntimeException("Already existed, please update it");
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

    @Override
    public void deleteQuarantineInformationById(String quarantineInfoId) {
        validateNotExisted(quarantineInfoId);
        quarantineInformationRepository.deleteById(quarantineInfoId);
    }

    @Override
    public QuarantineInformationResponseModel updateQuarantineInformationById(String quarantineInfoId, UpdateQIRequestModel updateQIRequestModel) {
        validateSameOriginFromAndDestination(updateQIRequestModel.getOriginFrom().toString()
                ,updateQIRequestModel.getDestination().toString());
        QuarantineInformation quarantineInformation
                = quarantineInformationRepository.findQuarantineInformationById(quarantineInfoId);
        if (quarantineInformation==null) {
            throw new RuntimeException("quarantineInfoId=" + quarantineInfoId + " not existed");
        }
        QuarantineInformation updatedQuarantineInformation
                = quarantineInformationRepository.save(updateQuarantineInformation(quarantineInformation, updateQIRequestModel));
        QuarantineInformationResponseModel responseModel
                = modelMapper.map(updatedQuarantineInformation, QuarantineInformationResponseModel.class);
        return responseModel;
    }

    private QuarantineInformation updateQuarantineInformation(QuarantineInformation quarantineInformation, UpdateQIRequestModel updateQIRequestModel) {
        quarantineInformation.setOriginFrom(updateQIRequestModel.getOriginFrom().toString());
        quarantineInformation.setDestination(updateQIRequestModel.getDestination().toString());
        quarantineInformation.setEndAt(updateQIRequestModel.getEndAt());
        quarantineInformation.setReasonUpdated(updateQIRequestModel.getReasonUpdated());
        return quarantineInformation;
    }

    private void validateNotExisted(String quarantineInfoId) {
        Optional<QuarantineInformation> optionalQuarantineInformation = quarantineInformationRepository.findById(quarantineInfoId);
        if (optionalQuarantineInformation.isEmpty()) {
            throw new RuntimeException("quarantineInfoId=" + quarantineInfoId + " not existed");
        }
    }

    private void validateSameOriginFromAndDestination(String originFrom, String destination) {
        if (originFrom.equals(destination)) {
            throw new RuntimeException("origin must differ to destination");
        }
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
