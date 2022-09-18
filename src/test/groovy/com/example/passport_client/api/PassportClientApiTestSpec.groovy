package com.example.passport_client.api

import com.example.passport_client.config.PassportServiceApiInfoHolder
import com.example.passport_client.dto.PassportDto
import com.example.passport_client.service.PassportService
import com.fasterxml.jackson.databind.ObjectMapper
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class PassportClientApiTestSpec extends Specification {

    private static final ROOT_PATH = "/client"

    @Autowired
    private MockMvc mockMvc

    @SpringBean
    private PassportService<PassportDto> service = Mock()

    @Autowired
    private PassportServiceApiInfoHolder apiHolder

    final def mapper = new ObjectMapper()

    def "when perform save call should return id"() {
        given: 'prepare request'
        final def request = Stubs.PASSPORT_DTO

        when: 'mock service and make a call'
        1 * service.save(_ as PassportDto) >> request.id
        final def result = mockMvc.perform(
                post("$ROOT_PATH/${apiHolder.entry.save}")
                        .content(mapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON)
        )

        then: 'should return id from request'
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString() == request.id.toString()
    }

    def "when perform update call should return true"() {
        given: 'prepare request'
        final def request = Stubs.PASSPORT_DTO

        when: 'mock service and make a call'
        1 * service.update(_ as PassportDto, _ as Long) >> true
        final def result = mockMvc.perform(
                put("$ROOT_PATH/${apiHolder.entry.update}")
                        .queryParam("id", request.id.toString())
                        .content(mapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON)
        )

        then: 'should return true in response'
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString() == true.toString()
    }

    def "when perform delete call should return true"() {
        given: 'prepare request'
        final def request = Stubs.PASSPORT_DTO

        when: 'mock service and make a call'
        1 * service.delete(_ as Long) >> true
        final def result = mockMvc.perform(
                delete("$ROOT_PATH/${apiHolder.entry.delete}")
                        .queryParam("id", request.id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
        )

        then: 'should return true in response'
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString() == true.toString()
    }

    def "when perform find call should return list"() {
        when: 'mock service'
        1 * service.findAll() >> [Stubs.PASSPORT_DTO]

        then: 'list of passports'
        mockMvc.perform(
                get("$ROOT_PATH/${apiHolder.entry.find}")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath('$[0].id').value(Stubs.PASSPORT_DTO.id))
                .andExpect(jsonPath('$[0].name').value(Stubs.PASSPORT_DTO.name))
                .andExpect(jsonPath('$[0].surname').value(Stubs.PASSPORT_DTO.surname))
                .andExpect(jsonPath('$[0].serial_number').value(Stubs.PASSPORT_DTO.serial))
                .andExpect(jsonPath('$[0].main_number').value(Stubs.PASSPORT_DTO.number))
    }

    def "when perform findBySerial call should return list with given serial"() {
        given: 'prepare result'
        final def result = Stubs.PASSPORT_DTO.with {
            serial = 1234L
            it
        }

        when: 'mock service'
        1 * service.findBySerial(result.serial) >> [result]

        then: 'list of passports with given serial'
        mockMvc.perform(
                get("$ROOT_PATH/${apiHolder.entry.findBySerial}")
                        .queryParam("serial", result.serial.toString())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath('$[0].id').value(Stubs.PASSPORT_DTO.id))
                .andExpect(jsonPath('$[0].name').value(Stubs.PASSPORT_DTO.name))
                .andExpect(jsonPath('$[0].surname').value(Stubs.PASSPORT_DTO.surname))
                .andExpect(jsonPath('$[0].serial_number').value(Stubs.PASSPORT_DTO.serial))
                .andExpect(jsonPath('$[0].main_number').value(Stubs.PASSPORT_DTO.number))
    }
}
