package com.thanhtai.quarantineinformationmanagement.repository;

import com.thanhtai.quarantineinformationmanagement.model.QuarantineInformation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface QuarantineInformationRepository extends MongoRepository<QuarantineInformation, String> {

    @Query(sort = "{ _id : -1 }")
    Page<QuarantineInformation> findAllBy(Pageable pageable);
    @Query(sort = "{ _id : -1 }")
    Page<QuarantineInformation> findAllByOriginFrom(Pageable pageable, String originFrom);
    @Query(sort = "{ _id : -1 }")
    Page<QuarantineInformation> findAllByDestination(Pageable pageable, String destination);
    @Query(sort = "{ _id : -1 }")
    Page<QuarantineInformation> findAllByOriginFromAndDestination(Pageable pageable, String originFrom, String destination);


    Optional<QuarantineInformation> findByOriginFromAndDestinationAndStartAt(String originFrom
            , String destination, String startAt);
    QuarantineInformation findQuarantineInformationById(String id);


}
