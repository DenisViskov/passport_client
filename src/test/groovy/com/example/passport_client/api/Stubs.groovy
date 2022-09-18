package com.example.passport_client.api

import com.example.passport_client.dto.PassportDto

import java.time.LocalDate
import java.time.ZoneId

class Stubs {

    final static PASSPORT_DTO = new PassportDto().with {
        id = 1
        serial = 4561L
        number = 123456L
        name = "Andrew"
        surname = "Alf"
        birthDate = toDate(LocalDate.of(1990, 03, 05))
        issuingAuthority = "Test"
        dateOfIssue = toDate(LocalDate.of(2010, 03, 05))
        expiredDate = toDate(LocalDate.of(2022, 03, 05))
        it
    }

    static Date toDate(final LocalDate localDate) {
        Date.from(
                localDate.atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
        )
    }
}
