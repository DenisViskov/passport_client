package com.example.passport_client;

import com.example.passport_client.dto.PassportDto;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.function.Consumer;

public class StubBuilder {

    private Long id;

    private Long serial = 4561L;

    private Long number = 123456L;

    private String name = "Andrew";

    private String surname = "Alf";

    private Date birthDate = toDate(LocalDate.of(1990, 03, 05));

    private String issuingAuthority = "Test";

    private Date dateOfIssue = toDate(LocalDate.of(2010, 03, 05));

    private Date expiredDate = toDate(LocalDate.of(2022, 03, 05));

    public static StubBuilder builder() {
        return new StubBuilder();
    }

    public StubBuilder id(final Long id) {
        setValue(id, i -> this.id = i);
        return this;
    }

    public StubBuilder serial(final Long serial) {
        setValue(serial, ser -> this.serial = ser);
        return this;
    }

    public StubBuilder number(final Long number) {
        setValue(number, num -> this.number = num);
        return this;
    }

    public StubBuilder name(final String name) {
        setValue(name, n -> this.name = n);
        return this;
    }

    public StubBuilder surname(final String surname) {
        setValue(surname, sur -> this.surname = sur);
        return this;
    }

    public StubBuilder birthDate(final LocalDate birthDate) {
        setValue(toDate(birthDate), birth -> this.birthDate = birth);
        return this;
    }

    public StubBuilder issuingAuthority(final String issuingAuthority) {
        setValue(issuingAuthority, issAuth -> this.issuingAuthority = issAuth);
        return this;
    }

    public StubBuilder dateOfIssue(final LocalDate dateOfIssue) {
        setValue(toDate(dateOfIssue), issue -> this.dateOfIssue = issue);
        return this;
    }

    public StubBuilder expiredDate(final LocalDate expiredDate) {
        setValue(toDate(expiredDate), exp -> this.expiredDate = exp);
        return this;
    }

    public PassportDto build() {
        return PassportDto.builder()
            .id(id)
            .serial(serial)
            .number(number)
            .name(name)
            .surname(surname)
            .birthDate(birthDate)
            .issuingAuthority(issuingAuthority)
            .dateOfIssue(dateOfIssue)
            .expiredDate(expiredDate)
            .build();
    }

    private <T> void setValue(final T value, final Consumer<T> action) {
        Optional.ofNullable(value)
            .ifPresent(action);
    }

    private static Date toDate(final LocalDate localDate) {
        return Date.from(localDate.atStartOfDay()
            .atZone(ZoneId.systemDefault())
            .toInstant());
    }
}
