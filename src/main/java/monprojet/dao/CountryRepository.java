package monprojet.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import monprojet.dto.PopulationPays;
import monprojet.entity.City;
import monprojet.entity.Country;

// This will be AUTO IMPLEMENTED by Spring 

public interface CountryRepository extends JpaRepository<Country, Integer> {

@Query(" SELECT SUM(population) " +
        " FROM City c "
    + " WHERE c.country.id = :pid")
    public int populationparID(Integer pid);

    @Query(value = "SELECT coun.name as nom, SUM(c.population) as pop "
    + "FROM City c "
    + "JOIN Country coun ON coun.id = c.country_id "
    + "GROUP BY country_id" ,
    nativeQuery = true)
public List<PopulationPays> listePopulationPays();


}
