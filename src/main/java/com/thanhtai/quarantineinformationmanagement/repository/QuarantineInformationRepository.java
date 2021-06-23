package com.thanhtai.quarantineinformationmanagement.repository;

import com.thanhtai.quarantineinformationmanagement.model.QuarantineInformation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuarantineInformationRepository extends MongoRepository<QuarantineInformation, String> {

}
