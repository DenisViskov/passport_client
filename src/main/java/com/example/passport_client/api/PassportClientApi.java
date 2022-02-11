package com.example.passport_client.api;

import com.example.passport_client.dto.PassportDto;
import com.example.passport_client.service.PassportService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/client")
public class PassportClientApi {

    private final PassportService<PassportDto> service;

    @PostMapping(value = "/save")
    public ResponseEntity<Long> save(@RequestBody final PassportDto passportDto) {
        return ResponseEntity.ok(service.save(passportDto));
    }

    @PutMapping(value = "/update")
    public ResponseEntity<Boolean> update(@RequestParam final Long id, @RequestBody final PassportDto passportDto) {
        final boolean result = service.update(passportDto, id);
        return result ? ResponseEntity.ok(true) : ResponseEntity.badRequest().body(false);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<Boolean> delete(@RequestParam final Long id) {
        final boolean result = service.delete(id);
        return result ? ResponseEntity.ok(true) : ResponseEntity.badRequest().body(false);
    }

    @GetMapping(value = "/find")
    public ResponseEntity<List<PassportDto>> find() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping(value = "/find", params = "serial")
    public ResponseEntity<List<PassportDto>> findBySerial(@RequestParam final Long serial) {
        return ResponseEntity.ok(service.findBySerial(serial));
    }

    @GetMapping(value = "/unavailable")
    public ResponseEntity<List<PassportDto>> unavailable() {
        return ResponseEntity.ok(service.findUnavailable());
    }

    @GetMapping(value = "/find-replaceable")
    public ResponseEntity<List<PassportDto>> findReplaceable() {
        return ResponseEntity.ok(service.findReplaceable());
    }

}
