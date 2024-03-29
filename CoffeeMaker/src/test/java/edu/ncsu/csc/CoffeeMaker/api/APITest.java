package edu.ncsu.csc.CoffeeMaker.api;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APITest {
	
	
	@Autowired
	private RecipeService recipeService;
	/**
	 * MockMvc uses Spring's testing framework to handle requests to the REST
	 * API
	 */
	private MockMvc mvc;

	@Autowired
	private WebApplicationContext context;
	
	@Autowired
    private RecipeService recipeService;

	@Autowired
    private InventoryService inventoryService;
	
	/**
	 * Sets up the tests.
	 */
	@BeforeEach
	public void setup () {
	    mvc = MockMvcBuilders.webAppContextSetup( context ).build();
	}
	
	private Recipe makeMocha() {
		final Recipe r = new Recipe();
		 r.setChocolate( 5 );
		    r.setCoffee( 3 );
		    r.setMilk( 4 );
		    r.setSugar( 8 );
		    r.setPrice( 10 );
		    r.setName( "Mocha" );
		    return r;
	}
	
	@Test 
	@Transactional
	public void apiTesting() throws UnsupportedEncodingException, Exception {
		recipeService.deleteAll();
		inventoryService.deleteAll();
		
		String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
		        .andReturn().getResponse().getContentAsString();

		/* Figure out if the recipe we want is present */
		if (!recipe.contains( "Mocha" ) ) {
		    final Recipe r = makeMocha();
		    
		    mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
		            .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );
		    
		    String recipeTest = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
			        .andReturn().getResponse().getContentAsString();
		    //test getting recipes
		    Assertions.assertTrue(recipeTest.contains("\"Mocha\""));
		    
		    final Inventory i = new Inventory(50,50,50,50);
		    mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
		            .content( TestUtils.asJsonString( i) ) ).andExpect( status().isOk() );
		    String inventoryTest = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() ).andExpect( status().isOk() )
			        .andReturn().getResponse().getContentAsString();
		    //test if the inventory was added
		    Assertions.assertTrue(inventoryTest.contains("\"milk\":50"));
		    Assertions.assertTrue(inventoryTest.contains("\"coffee\":50"));
		    Assertions.assertTrue(inventoryTest.contains("\"sugar\":50"));
		    Assertions.assertTrue(inventoryTest.contains("\"chocolate\":50"));
		    
		    mvc.perform( post( "/api/v1/makecoffee/Mocha" ).contentType( MediaType.APPLICATION_JSON )
		            .content( TestUtils.asJsonString(350) ) ).andExpect( status().isOk() );
		    
		    String inventoryTest2 = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() ).andExpect( status().isOk() )
			        .andReturn().getResponse().getContentAsString();
		    //test if mocha ingredients were successfully removed from inventory.
		    Assertions.assertTrue(inventoryTest2.contains("\"milk\":46"));
		    Assertions.assertTrue(inventoryTest2.contains("\"coffee\":47"));
		    Assertions.assertTrue(inventoryTest2.contains("\"sugar\":42"));
		    Assertions.assertTrue(inventoryTest2.contains("\"chocolate\":45"));
		    
		    

		}
	}
	@Test 
	@Transactional
	public void validDeleteRecipe() throws UnsupportedEncodingException, Exception {
	    recipeService.deleteAll();
	
	    Recipe mocha = new Recipe();
	    mocha.setName("Mocha");
	    mocha.setChocolate(5);
	    mocha.setMilk(1);
	    mocha.setCoffee(50);
	    mocha.setPrice(350);
	    recipeService.save(mocha);

	    Recipe latte = new Recipe();
	    latte.setName("Latte");
	    latte.setChocolate(1);
	    latte.setMilk(5);
	    latte.setCoffee(50);
	    latte.setPrice(450);
	    recipeService.save(latte);
	    
	    // ensure that both are saved
	    List<Recipe> dbRecipes = (List<Recipe>) recipeService.findAll();
	    Assertions.assertEquals(2, dbRecipes.size());

	    // delete operation on latte
	    mvc.perform(delete("/api/v1/recipes/Latte")
	            .contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk());

	    // get the updated list of recipes after the delete operation
	    dbRecipes = (List<Recipe>) recipeService.findAll();
	    Assertions.assertEquals(1, dbRecipes.size());

	    // remaining recipe should be mocha
	    Recipe remainingRecipe = dbRecipes.get(0);
	    Assertions.assertEquals("Mocha", remainingRecipe.getName());
	}
}
	

