package edu.ncsu.csc.CoffeeMaker;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )


public class TestDatabaseInteraction {
	@Autowired
	private RecipeService recipeService;
	
	
	@Test
	@Transactional
	public void testRecipes(){
		recipeService.deleteAll();
		/*New recipe*/
		Recipe mocha = new Recipe();
		mocha.setName("Mocha");
		mocha.setChocolate(5);
		mocha.setMilk(1);
		mocha.setCoffee(50);
		mocha.setPrice(350);
		recipeService.save(mocha );
		
		
		List<Recipe> dbRecipes = (List<Recipe>) recipeService.findAll();
		
		/*confirm that there is only one recipe in the system*/
		Assertions.assertEquals(1, dbRecipes.size());
		/*Get the mocha recipe from the database*/
		Recipe dbRecipe = dbRecipes.get(0);
		/*Confirm that fields are equal*/
		Assertions.assertEquals(mocha.getName(), dbRecipe.getName());
		Assertions.assertEquals(mocha.getChocolate(), dbRecipe.getChocolate());
		Assertions.assertEquals(mocha.getPrice(), dbRecipe.getPrice());
		Assertions.assertEquals(mocha.getMilk(), dbRecipe.getMilk());
		Assertions.assertEquals(mocha.getSugar(), dbRecipe.getSugar());
		Assertions.assertEquals(mocha.getCoffee(), dbRecipe.getCoffee());
		/*Test get recipe by name*/
		Assertions.assertEquals(mocha, recipeService.findByName("Mocha"));
		
		/*Test edit recipe */
		
		mocha.setName("Mocha1");
		mocha.setChocolate(6);
		mocha.setMilk(2);
		mocha.setCoffee(51);
		mocha.setPrice(351);
		recipeService.save(mocha);
		/*Confirm that the new fields are equal */
		Assertions.assertEquals(mocha.getName(), dbRecipe.getName());
		Assertions.assertEquals(mocha.getChocolate(), dbRecipe.getChocolate());
		Assertions.assertEquals(mocha.getPrice(), dbRecipe.getPrice());
		Assertions.assertEquals(mocha.getMilk(), dbRecipe.getMilk());
		Assertions.assertEquals(mocha.getSugar(), dbRecipe.getSugar());
		Assertions.assertEquals(mocha.getCoffee(), dbRecipe.getCoffee());
	}
}
