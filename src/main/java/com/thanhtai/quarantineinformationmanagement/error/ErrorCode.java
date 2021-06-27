package com.thanhtai.quarantineinformationmanagement.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class ErrorCode {
    @Getter
    @Setter
    private String timestamp;
    @Getter
    @Setter
    private Integer responseCode;
    @Getter
    @Setter
    private Integer errorCode;
    @Getter
    @Setter
    private String errorMessage;
}
