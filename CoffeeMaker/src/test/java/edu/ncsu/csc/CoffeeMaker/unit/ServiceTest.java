package edu.ncsu.csc.CoffeeMaker.unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;


import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class ServiceTest {

    @Autowired
    private RecipeService service;

    private Recipe r1;
    private Recipe r2;

    @Transactional
    @BeforeEach
    public void setUp() { 
    	service.deleteAll();
    	r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.setCoffee( 1 );
        r1.setMilk( 0 );
        r1.setSugar( 0 );
        r1.setChocolate( 0 );
        service.save(r1);

        r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.setCoffee( 1 );
        r2.setMilk( 1 );
        r2.setSugar( 1 );
        r2.setChocolate( 1 );
        service.save(r2);      
    }

    @Test
    @Transactional
    public void testExistsById() {
        assertTrue(service.existsById(r1.getId()));
        assertTrue(service.existsById(r2.getId()));
    }

    @Test
    @Transactional
    public void testFindById() {
        Recipe foundRecipe = service.findById(r1.getId());
        assertNotNull(foundRecipe);
        assertEquals("Black Coffee", foundRecipe.getName());
        
        Recipe secondRecipe = service.findById(r2.getId());
        assertNotNull(secondRecipe);
        assertEquals("Mocha", secondRecipe.getName());
    }

    @Test
    @Transactional
    public void testSaveAll() {
        List<Recipe> objects = Arrays.asList(r1, r2);
        service.saveAll(objects);
        assertEquals(2, service.count());
    }
}
