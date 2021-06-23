package com.thanhtai.quarantineinformationmanagement.model;

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
    @Setter
    @CreatedDate
    private String createdAt;
    @Getter
    @Setter
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
