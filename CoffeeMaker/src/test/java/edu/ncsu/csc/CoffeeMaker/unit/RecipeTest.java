package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class RecipeTest {

    @Autowired
    private RecipeService service;

    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    @Test
    @Transactional
    public void testAddRecipe () {

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.setCoffee( 1 );
        r1.setMilk( 0 );
        r1.setSugar( 0 );
        r1.setChocolate( 0 );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.setCoffee( 1 );
        r2.setMilk( 1 );
        r2.setSugar( 1 );
        r2.setChocolate( 1 );
        service.save( r2 );

        final List<Recipe> recipes = service.findAll();
        Assertions.assertEquals( 2, recipes.size(),
                "Creating two recipes should result in two recipes in the database" );

        Assertions.assertEquals( r1, recipes.get( 0 ), "The retrieved recipe should match the created one" );
    }

    @Test
    @Transactional
    public void testNoRecipes () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Tasty Drink" );
        r1.setPrice( 12 );
        r1.setCoffee( -12 );
        r1.setMilk( 0 );
        r1.setSugar( 0 );
        r1.setChocolate( 0 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.setCoffee( 1 );
        r2.setMilk( 1 );
        r2.setSugar( 1 );
        r2.setChocolate( 1 );

        final List<Recipe> recipes = List.of( r1, r2 );

        try {
            service.saveAll( recipes );
            Assertions.assertEquals( 0, service.count(),
                    "Trying to save a collection of elements where one is invalid should result in neither getting saved" );
        }
        catch ( final Exception e ) {
            Assertions.assertTrue( e instanceof ConstraintViolationException );
        }

    }

    @Test
    @Transactional
    public void testAddRecipe1 () {

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, 1, 0 );

        service.save( r1 );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
        Assertions.assertNotNull( service.findByName( name ) );

    }

    /* Test2 is done via the API for different validation */

    @Test
    @Transactional
    public void testAddRecipe3 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, -50, 3, 1, 1, 0 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative price" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe4 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, -3, 1, 1, 2 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of coffee" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe5 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, -1, 1, 2 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of milk" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe6 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, -1, 2 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of sugar" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe7 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, 1, -2 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of chocolate" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe13 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );

        Assertions.assertEquals( 2, service.count(),
                "Creating two recipes should result in two recipes in the database" );

    }

    @Test
    @Transactional
    public void testAddRecipe14 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

    }

    @Test
    @Transactional
    public void testDeleteRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );

        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        service.delete( r1 );
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void testDeleteRecipe2 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(), "There should be three recipes in the database" );

        service.deleteAll();

        Assertions.assertEquals( 0, service.count(), "`service.deleteAll()` should remove everything" );

    }

    @Test
    @Transactional
    public void testEditRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );

        r1.setPrice( 70 );

        service.save( r1 );

        final Recipe retrieved = service.findByName( "Coffee" );

        Assertions.assertEquals( 70, (int) retrieved.getPrice() );
        Assertions.assertEquals( 3, (int) retrieved.getCoffee() );
        Assertions.assertEquals( 1, (int) retrieved.getMilk() );
        Assertions.assertEquals( 1, (int) retrieved.getSugar() );
        Assertions.assertEquals( 0, (int) retrieved.getChocolate() );

        Assertions.assertEquals( 1, service.count(), "Editing a recipe shouldn't duplicate it" );

    }
    
    /**
     * Tests the checkRecipe method
     */
    @Test
    public void testCheckRecipe() {
    	
    	final Recipe recipe = createRecipe("Coffee", 10, 0, 0, 0, 0);
    	
    	//Check recipe should return true when all ingredients are zero
    	Assertions.assertEquals(true, recipe.checkRecipe());
    	
    	
    	
    	recipe.setChocolate(10);
    	
    	Assertions.assertEquals(false, recipe.checkRecipe());
    	
    	recipe.setSugar(10);
    	
    	Assertions.assertEquals(false, recipe.checkRecipe());
    	
    	recipe.setMilk(10);
    	
    	Assertions.assertEquals(false, recipe.checkRecipe());
    	
    	recipe.setCoffee(10);
    	
    	Assertions.assertEquals(false, recipe.checkRecipe());
    	
    	
    }
    
    /**
     * Tests the updateRecipe method
     */
    @Test
    public void testUpdateRecipe() {
    	
    	Recipe recipe = createRecipe("Coffee", 50, 1, 2, 3, 4);
    	
    	Assertions.assertEquals("Coffee", recipe.getName());
    	Assertions.assertEquals(50, recipe.getPrice());
    	Assertions.assertEquals(1, recipe.getCoffee());
    	Assertions.assertEquals(2, recipe.getMilk());
    	Assertions.assertEquals(3, recipe.getSugar());
    	Assertions.assertEquals(4, recipe.getChocolate());
    	
    	//updating price
    	Recipe updateRecipe = createRecipe("Coffee", 60, 1, 2, 3, 4);
    	recipe.updateRecipe(updateRecipe);
    	
    	Assertions.assertEquals("Coffee", recipe.getName());
    	Assertions.assertEquals(60, recipe.getPrice());
    	Assertions.assertEquals(1, recipe.getCoffee());
    	Assertions.assertEquals(2, recipe.getMilk());
    	Assertions.assertEquals(3, recipe.getSugar());
    	Assertions.assertEquals(4, recipe.getChocolate());
    	
    	//updating coffee
    	updateRecipe = createRecipe("Coffee", 60, 5, 2, 3, 4);
    	recipe.updateRecipe(updateRecipe);
    	
    	Assertions.assertEquals("Coffee", recipe.getName());
    	Assertions.assertEquals(60, recipe.getPrice());
    	Assertions.assertEquals(5, recipe.getCoffee());
    	Assertions.assertEquals(2, recipe.getMilk());
    	Assertions.assertEquals(3, recipe.getSugar());
    	Assertions.assertEquals(4, recipe.getChocolate());
    	
    	//updating milk
    	updateRecipe = createRecipe("Coffee", 60, 5, 6, 3, 4);
    	recipe.updateRecipe(updateRecipe);
    	
    	Assertions.assertEquals("Coffee", recipe.getName());
    	Assertions.assertEquals(60, recipe.getPrice());
    	Assertions.assertEquals(5, recipe.getCoffee());
    	Assertions.assertEquals(6, recipe.getMilk());
    	Assertions.assertEquals(3, recipe.getSugar());
    	Assertions.assertEquals(4, recipe.getChocolate());
    	
    	//updating sugar
    	updateRecipe = createRecipe("Coffee", 60, 5, 6, 7, 4);
    	recipe.updateRecipe(updateRecipe);
    	
    	Assertions.assertEquals("Coffee", recipe.getName());
    	Assertions.assertEquals(60, recipe.getPrice());
    	Assertions.assertEquals(5, recipe.getCoffee());
    	Assertions.assertEquals(6, recipe.getMilk());
    	Assertions.assertEquals(7, recipe.getSugar());
    	Assertions.assertEquals(4, recipe.getChocolate());
    	
    	//updating chocolate
    	updateRecipe = createRecipe("Coffee", 60, 5, 6, 7, 8);
    	recipe.updateRecipe(updateRecipe);
    	
    	Assertions.assertEquals("Coffee", recipe.getName());
    	Assertions.assertEquals(60, recipe.getPrice());
    	Assertions.assertEquals(5, recipe.getCoffee());
    	Assertions.assertEquals(6, recipe.getMilk());
    	Assertions.assertEquals(7, recipe.getSugar());
    	Assertions.assertEquals(8, recipe.getChocolate());
    	
    	
    }
    
    /**
     * Tests equals method
     */
    @Test
    public void testEquals() {
    	
    	Recipe recipe = createRecipe(null, 50, 1, 2, 3, 4);
    	
    	//equal to itself
    	Assertions.assertTrue(recipe.equals(recipe));
    	//not equal to objects of other type
    	Assertions.assertFalse(recipe.equals("Coffee"));
    	//not equal to null object
    	Assertions.assertFalse(recipe.equals(null));
    	
    	Recipe other = createRecipe("Coffee", 1, 2, 3, 4, 5);
    	
    	//not equal when different name
    	Assertions.assertFalse(recipe.equals(other));
    	
    	recipe.setName("Mocha");
    	
    	//not equal when different name
    	Assertions.assertFalse(recipe.equals(other));
    	
    	recipe.setName("Coffee");
    	
    	//equal when same name
    	Assertions.assertTrue(recipe.equals(other));
    	
    	
    }
    

    
    
    private Recipe createRecipe ( final String name, final Integer price, final Integer coffee, final Integer milk,
            final Integer sugar, final Integer chocolate ) {
        final Recipe recipe = new Recipe();
        recipe.setName( name );
        recipe.setPrice( price );
        recipe.setCoffee( coffee );
        recipe.setMilk( milk );
        recipe.setSugar( sugar );
        recipe.setChocolate( chocolate );

        return recipe;
    }

}
