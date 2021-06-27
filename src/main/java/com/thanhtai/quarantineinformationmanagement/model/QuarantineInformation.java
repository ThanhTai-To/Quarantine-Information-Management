package com.thanhtai.quarantineinformationmanagement.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "quarantine_informations")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuarantineInformation {
    @Id
    @Getter
    private String id;
    @Getter
    @Setter
    private String originFrom;
    @Getter
    @Setter
    private String destination;
    @Getter
    @Setter
    private String startAt;
    @Getter
    @Setter
    private String endAt;
    @Getter
    @CreatedDate
    private String createdAt;
    @Getter
    @LastModifiedDate
    private String updatedAt;
    @Getter
    @Setter
    @Builder.Default
    private String status = QuarantineInformationStatus.NEW.toString();
    @Getter
    @Setter
    private String reasonUpdated;
}
