package com.example.infinum.repository;

import com.example.infinum.domain.City;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CityRepository extends BaseRepository<City, Long>{

    /**
     * I choose this way as most optimal. Reason is that is that db engine will fastest join data and do grouping operations than to do it programaticly.
     * For small amount of data programatic and db way will do with similar results. But differences comes will larger amount of data where this is fastest way.
     * Returning as list of Objects isnt the best this to do, but i choose native query because of time that i had to finish task.
     * Better solution would be to use HQL and return CityDto directly.
     */
    @Query(value = "select c.*, COUNT(uc.user_id) as num from cities c " +
            "left join user_city uc on c.id = uc.city_id group by c.id order by num desc", nativeQuery = true)
    List<Object[]> findAllByFavourites();
}
