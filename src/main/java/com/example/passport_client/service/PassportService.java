package com.example.passport_client.service;

import java.util.List;

public interface PassportService<T> {
    Long save(T passport);

    boolean update(T passport);

    boolean delete(final Long id);

    List<T> findAll();

    List<T> findBySerial(final Long serial);

    List<T> findUnavailable();

    List<T> findReplaceable();
}
