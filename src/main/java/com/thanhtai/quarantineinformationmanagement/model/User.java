package com.thanhtai.quarantineinformationmanagement.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "users")
@Builder
public class User {
    @Id
    @Getter
    private String id;
    @Setter
    @Getter
    private String name;
    @Setter
    @Getter
    private String email;
    @Indexed
    @Setter
    @Getter
    private String username;
    @Setter
    @Getter
    private String password;
    @Getter
    @Setter
    private String role;
}
