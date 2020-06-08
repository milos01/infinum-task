package com.example.infinum.controller;

import com.example.infinum.domain.City;
import com.example.infinum.dto.CityDto;
import com.example.infinum.service.CityService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/city")
public class CityController extends BaseController<CityDto, City, Long> {

    private CityService cityService;

    public CityController(CityService cityService) {
        super(cityService);
        this.cityService = cityService;
    }

    /**
     * Persisting new city
     * @param countryDto
     * @return CityDto
     */
    @PostMapping
    public CityDto post(@RequestBody @Valid CityDto countryDto) {
        //TODO This could me moved to BaseController and be generic,
        // but for the sake of this task i did not wanted to UserController
        // has access to post method (wanted to have that logic inside AuthController)
        return cityService.save(countryDto);
    }

    @PutMapping("{id}")
    public CityDto put(@PathVariable Long id, @RequestBody @Valid CityDto countryDto) {
        //TODO This could me moved to BaseController and be generic,
        // but for the sake of this task i did not wanted to UserController
        // has access to put method, because i run out of time to implement it :(
        return cityService.update(id, countryDto);
    }

    /**
     * Get all cities sorted by number od users that marked them as favourites
     * @return Object[]
     */
    @GetMapping("byFavourites")
    //TODO Because of leak of time, i did not have time to translate Object array to CityDto array. Hope you will be ok with this.
    public List<Object[]> allByFavourites() {
        return cityService.findAllByFavourites();
    }
}
