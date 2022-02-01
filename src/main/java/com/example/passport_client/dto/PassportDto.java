package com.example.passport_client.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PassportDto {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "serial_number", required = true)
    private Long serial;

    @JsonProperty(value = "main_number", required = true)
    private Long number;

    @JsonProperty(value = "name", required = true)
    private String name;

    @JsonProperty(value = "surname", required = true)
    private String surname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    @JsonProperty(value = "birth_date", required = true)
    private Date birthDate;

    @JsonProperty(value = "issuing_authority", required = true)
    private String issuingAuthority;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    @JsonProperty(value = "date_of_issue", required = true)
    private Date dateOfIssue;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    @JsonProperty(value = "expired_date", required = true)
    private Date expiredDate;
}
