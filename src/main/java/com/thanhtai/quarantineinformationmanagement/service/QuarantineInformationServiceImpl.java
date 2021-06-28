package com.thanhtai.quarantineinformationmanagement.service;

import com.thanhtai.quarantineinformationmanagement.api.model.*;
import com.thanhtai.quarantineinformationmanagement.error.AlreadyExistedException;
import com.thanhtai.quarantineinformationmanagement.error.DuplicateException;
import com.thanhtai.quarantineinformationmanagement.error.InvalidProvinceName;
import com.thanhtai.quarantineinformationmanagement.error.ResourceNotFoundException;
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
        // TODO: catch
        // already existed same start date and place
        validateSameOriginFromAndDestination(createQIRequestModel.getOriginFrom().toString()
                ,createQIRequestModel.getDestination().toString());
        Optional<QuarantineInformation> optionalQuarantineInformation = quarantineInformationRepository.findByOriginFromAndDestinationAndStartAt(
                createQIRequestModel.getOriginFrom().toString()
                , createQIRequestModel.getDestination().toString()
                , createQIRequestModel.getStartAt());
        if (optionalQuarantineInformation.isPresent()) {
            throw new AlreadyExistedException("This quarantine information");
        }
        QuarantineInformation quarantineInformation =
                modelMapper.map(createQIRequestModel, QuarantineInformation.class);
        return quarantineInformationRepository.save(quarantineInformation);
    }

    @Override
    public QuarantineInformationResponse getListQuarantineInformation(Integer page, String originFrom, String destination) {
        if (originFrom == null) {
            originFrom = "ALL";
        }
        if (destination == null) {
            destination = "ALL";
        }

        String originFromUpperCase = originFrom.toUpperCase();
        String destinationUpperCase = destination.toUpperCase();
        // check if originFrom and destination is in province enum
        if (!validateProvinceAPIModelEnum(originFromUpperCase) ||
            !validateProvinceAPIModelEnum(destinationUpperCase)) {
            throw new InvalidProvinceName(originFrom + "/" + destination);
        }

        logger.info("originFrom " + originFromUpperCase + " destination " + destinationUpperCase);
        Page<QuarantineInformation> quarantineInformationList;

        if (originFromUpperCase.equals(destinationUpperCase)) {
            logger.info("find by ALL");
            quarantineInformationList =
                    quarantineInformationRepository.findAllBy(PageRequest.of(page, PAGE_SIZE));
        } else if (originFromUpperCase.equals("ALL")) {
            logger.info("find by destination");
            quarantineInformationList =
                    quarantineInformationRepository
                            .findAllByDestination(PageRequest.of(page, PAGE_SIZE), destinationUpperCase);
        } else if (destinationUpperCase.equals("ALL")) {
            logger.info("find by origin");
            quarantineInformationList =
                    quarantineInformationRepository
                            .findAllByOriginFrom(PageRequest.of(page, PAGE_SIZE),originFromUpperCase);
        } else {
            logger.info("find by origin and destination");
            quarantineInformationList =
                    quarantineInformationRepository.findAllByOriginFromAndDestination(
                            PageRequest.of(page, PAGE_SIZE)
                            , originFromUpperCase
                            , destinationUpperCase);
        }
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
            throw new ResourceNotFoundException("quarantineInfoId= " + quarantineInfoId);
        }
        QuarantineInformation updatedQuarantineInformation
                = quarantineInformationRepository.save(updateQuarantineInformation(quarantineInformation, updateQIRequestModel));
        return modelMapper.map(updatedQuarantineInformation, QuarantineInformationResponseModel.class);
    }

    private QuarantineInformation updateQuarantineInformation(QuarantineInformation quarantineInformation, UpdateQIRequestModel updateQIRequestModel) {
        quarantineInformation.setOriginFrom(updateQIRequestModel.getOriginFrom().toString());
        quarantineInformation.setDestination(updateQIRequestModel.getDestination().toString());
        quarantineInformation.setEndAt(updateQIRequestModel.getEndAt());
        quarantineInformation.setReasonUpdated(updateQIRequestModel.getReasonUpdated());
        quarantineInformation.setStatus("UPDATED");
        return quarantineInformation;
    }

    private void validateNotExisted(String quarantineInfoId) {
        Optional<QuarantineInformation> optionalQuarantineInformation = quarantineInformationRepository.findById(quarantineInfoId);
        if (optionalQuarantineInformation.isEmpty()) {
            throw new ResourceNotFoundException("quarantineInfoId=" + quarantineInfoId);
        }
    }

    private void validateSameOriginFromAndDestination(String originFrom, String destination) {
        if (originFrom.equals(destination)) {
            throw new DuplicateException(originFrom + "/" + destination);
        }
    }

    private boolean validateProvinceAPIModelEnum(String province) {
        if (Province.fromValue(province)!=null || province.equals("ALL")) {
            logger.info("Input province " + province);
            return true;
        }
        return false;
    }

    private QuarantineInformationResponse buildQuarantineInformationResponseModel(Page<QuarantineInformation> quarantineInformationList) {
        QuarantineInformationResponse quarantineInformationResponse = new QuarantineInformationResponse();
        quarantineInformationResponse.setCurrentPage(quarantineInformationList.getNumber());
        quarantineInformationResponse.setTotalPage(quarantineInformationList.getTotalPages());
        quarantineInformationResponse.setTotalElements(quarantineInformationList.getNumberOfElements());
        quarantineInformationList.stream().forEach(quarantineInformation -> {
            logger.info("quarantineInformation" + quarantineInformation.getDestination());

            QuarantineInformationResponseModel qiResponseModel =
                    modelMapper.map(quarantineInformation, QuarantineInformationResponseModel.class);
            quarantineInformationResponse.addQuarantineInformationListItem(qiResponseModel);
        });
        return quarantineInformationResponse;
    }
}
