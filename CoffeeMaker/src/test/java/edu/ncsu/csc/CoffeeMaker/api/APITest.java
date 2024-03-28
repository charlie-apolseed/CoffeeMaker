package edu.ncsu.csc.CoffeeMaker.api;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;

@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APITest {
	/**
	 * MockMvc uses Spring's testing framework to handle requests to the REST
	 * API
	 */
	private MockMvc               mvc;

	@Autowired
	private WebApplicationContext context;

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
		    //test if mocha ingredients were succesfully removed from inventory.
		    Assertions.assertTrue(inventoryTest2.contains("\"milk\":46"));
		    Assertions.assertTrue(inventoryTest2.contains("\"coffee\":47"));
		    Assertions.assertTrue(inventoryTest2.contains("\"sugar\":42"));
		    Assertions.assertTrue(inventoryTest2.contains("\"chocolate\":45"));
		    
		    

		}
	}
}
