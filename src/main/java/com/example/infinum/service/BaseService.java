package com.example.infinum.service;

import java.util.List;

public interface BaseService<D, ID> {
    D findById(ID id);

    List<D> findAll();

    D save(D entityDto);

    void deleteById(ID id);
}
