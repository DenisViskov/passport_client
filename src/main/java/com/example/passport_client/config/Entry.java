package com.example.passport_client.config;

import lombok.Data;

@Data
public class Entry {
    private String passport;

    private String save;

    private String update;

    private String delete;

    private String find;

    private String findBySerial;

    private String unavailable;

    private String findReplaceable;
}
