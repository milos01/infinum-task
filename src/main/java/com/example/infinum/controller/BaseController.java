package com.example.infinum.controller;

import com.example.infinum.service.BaseService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public abstract class BaseController<D, T, ID> {

    private final BaseService<D, ID> service;

    public BaseController(BaseService<D, ID> service) {
        this.service = service;
    }

    /**
     * Generic route for updating entities based on entity id and entity dto
     * @param id
     * @param countryDto
     */
//    @PutMapping("{id}")
//    public D put(@PathVariable ID id, @RequestBody @Valid D countryDto) {
//        return this.service.update(id, countryDto);
//    }

    /**
     * Generic route for deleting(soft) entities base on entity id
     * @param id
     */
    @DeleteMapping("{id}")
    public void delete(@PathVariable ID id) {
        this.service.deleteById(id);
    }

    /**
     * Generic route for getting single entity based on entity id
     * @param id
     */
    @GetMapping("{id}")
    public D get(@PathVariable ID id) {
        return this.service.findById(id);
    }

    /**
     * Generic route for getting all entities
     */
    @GetMapping("all")
    public List<D> get() {
        return this.service.findAll();
    }
}
