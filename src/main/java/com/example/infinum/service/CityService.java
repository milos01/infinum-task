package com.example.infinum.service;

import com.example.infinum.dto.CityDto;

import javax.validation.Valid;
import java.util.List;

public interface CityService extends BaseService<CityDto, Long> {
    List<Object[]> findAllByFavourites();

    CityDto update(Long id, @Valid CityDto countryDto);
}
