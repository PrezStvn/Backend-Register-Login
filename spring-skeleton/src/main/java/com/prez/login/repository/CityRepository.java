package com.prez.login.repository;

import com.prez.login.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByCityName(String cityName);

    @Query("SELECT c FROM City c WHERE c.latitude BETWEEN :latitude - :range AND :latitude + :range AND c.longitude BETWEEN :longitude - :range AND :longitude + :range")
    List<City> findNearbyCities(@Param("latitude") Double latitude, @Param("longitude") Double longitude, @Param("range") Double range);
}
