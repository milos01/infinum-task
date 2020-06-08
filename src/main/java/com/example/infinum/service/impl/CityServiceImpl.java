package com.example.infinum.service.impl;

import com.example.infinum.domain.City;
import com.example.infinum.dto.CityDto;
import com.example.infinum.exceptions.NotFoundException;
import com.example.infinum.repository.CityRepository;
import com.example.infinum.service.CityService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CityServiceImpl extends BaseServiceImpl<CityDto, City, Long> implements CityService {

    private CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository, ModelMapper mapper) {
        super(cityRepository, mapper);
        this.cityRepository = cityRepository;
    }

    /**
     * Updates (name, description, population) city entity
     * @param id
     * @param entityDto
     * @return CityDto
     */
    public CityDto update(Long id, CityDto entityDto) {
        City city = cityRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException(City.class, "id", id));

        city.setName(entityDto.getName());
        city.setDescription(entityDto.getDescription());
        city.setPopulation(entityDto.getPopulation());

        cityRepository.save(city);
        return this.mapper.map(city, CityDto.class);
    }

    /**
     * Get all cities sorted by number od users that marked them as favourites
     * @return List<Object[]>
     */
    @Override
    public List<Object[]> findAllByFavourites() {
        return this.cityRepository.findAllByFavourites();
    }
}
