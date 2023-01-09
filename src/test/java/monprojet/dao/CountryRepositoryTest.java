package monprojet.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import monprojet.dto.PopulationPays;
import monprojet.entity.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

@Log4j2 // Génère le 'logger' pour afficher les messages de trace
@DataJpaTest
public class CountryRepositoryTest {

    @Autowired
    private CountryRepository countryDAO;
    @Autowired
    private CityRepository cityDAO;
    @Test
    void lesNomsDePaysSontTousDifferents() {
        log.info("On vérifie que les noms de pays sont tous différents ('unique') dans la table 'Country'");
        
        Country paysQuiExisteDeja = new Country("XX", "France");
        try {
            countryDAO.save(paysQuiExisteDeja); // On essaye d'enregistrer un pays dont le nom existe   

            fail("On doit avoir une violation de contrainte d'intégrité (unicité)");
        } catch (DataIntegrityViolationException e) {
            // Si on arrive ici c'est normal, l'exception attendue s'est produite
        }
    }

    @Test
    @Sql("test-data.sql") // On peut charger des donnnées spécifiques pour un test
    void onSaitCompterLesEnregistrements() {
        log.info("On compte les enregistrements de la table 'Country'");
        int combienDePaysDansLeJeuDeTest = 3 + 1; // 3 dans data.sql, 1 dans test-data.sql
        long nombre = countryDAO.count();
        assertEquals(combienDePaysDansLeJeuDeTest, nombre, "On doit trouver 4 pays" );
    }
@Test
@Sql("test-data.sql")
void TestComptagePopulation(){
        Country Italie = countryDAO.findById(4).orElseThrow();
        City florence = new City("florence",Italie);
        florence.setPopulation(12);
        countryDAO.save(Italie);
        cityDAO.save(florence);
        assertEquals(12,
               countryDAO.populationparID(Italie.getId()),
                "pas bon nombre de population");
}



@Test
@Sql("test-data.sql")
void testPopPourTousLesPays(){
    PopulationPays testFr = new PopulationPays() {
        @Override
        public String getNom() {
            return "France";
        }

        @Override
        public Integer getPopulation() {
            return 12;
        }
    };
    PopulationPays testUk = new PopulationPays() {
        @Override
        public String getNom() {
            return "United Kingdom";
        }

        @Override
        public Integer getPopulation() {
            return 18;
        }
    };
    PopulationPays testUs = new PopulationPays() {
        @Override
        public String getNom() {
            return "United States of America";
        }

        @Override
        public Integer getPopulation() {
            return 27;
        }
    };
    assertEquals(testFr.getNom(), countryDAO.listePopulationPays().get(0).getNom());
    assertEquals(testFr.getPopulation(), countryDAO.listePopulationPays().get(0).getPopulation());

    assertEquals(testUk.getNom(), countryDAO.listePopulationPays().get(1).getNom());
    assertEquals(testUk.getPopulation(), countryDAO.listePopulationPays().get(1).getPopulation());

    assertEquals(testUs.getNom(), countryDAO.listePopulationPays().get(2).getNom());
    assertEquals(testUs.getPopulation(), countryDAO.listePopulationPays().get(2).getPopulation());
}
}


