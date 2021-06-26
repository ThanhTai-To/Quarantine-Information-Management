package com.thanhtai.quarantineinformationmanagement.repository;

import com.thanhtai.quarantineinformationmanagement.model.QuarantineInformation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface QuarantineInformationRepository extends MongoRepository<QuarantineInformation, String> {

    @Query(sort = "{ _id : -1 }")
    Page<QuarantineInformation> findAllBy(Pageable pageable);

}
