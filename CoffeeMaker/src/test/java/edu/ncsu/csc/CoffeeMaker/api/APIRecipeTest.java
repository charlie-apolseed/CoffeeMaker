package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIRecipeTest {

    @Autowired
    private RecipeService service;

    @Autowired
    private MockMvc       mvc;
    private Ingredient coffee;
    private Ingredient milk;
    private Ingredient sugar;
    private Ingredient chocolate;

    @Test
    @Transactional
    public void ensureRecipe () throws Exception {
        service.deleteAll();

        final Recipe r = new Recipe();
        coffee = new Ingredient("coffee", 3);
        milk = new Ingredient("milk", 4);
        sugar = new Ingredient("sugar", 8);
        chocolate = new Ingredient("chocolate", 5);
        r.setPrice( 10 );
        r.setName( "Mocha" );
        r.addIngredient(coffee);
        r.addIngredient(milk);
        r.addIngredient(sugar);
        r.addIngredient(chocolate);
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

    }
    
    
    @Test
    @Transactional
    public void testRecipeAPI () throws Exception {

        service.deleteAll();
        
        service.deleteAll();

        final Recipe recipe = new Recipe();
        coffee = new Ingredient("coffee", 1);
        milk = new Ingredient("milk", 20);
        sugar = new Ingredient("sugar", 5);
        chocolate = new Ingredient("chocolate", 10);
        recipe.setPrice( 50 );
        recipe.setName( "Mocha" );
        recipe.addIngredient(coffee);
        recipe.addIngredient(milk);
        recipe.addIngredient(sugar);
        recipe.addIngredient(chocolate);



        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) );

        Assertions.assertEquals( 1, (int) service.count() );

    }


    @Test
    @Transactional
    public void testAddRecipe2 () throws Exception {
        service.deleteAll();

        /* Tests a recipe with a duplicate name to make sure it's rejected */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        
        final Recipe r1 = new Recipe();
        coffee = new Ingredient("coffee", 3);
        milk = new Ingredient("milk", 1);
        sugar = new Ingredient("sugar", 1);
        chocolate = new Ingredient("chocolate", 2);
        r1.setPrice( 50 );
        r1.setName( "Mocha" );
        r1.addIngredient(coffee);
        r1.addIngredient(milk);
        r1.addIngredient(sugar);
        r1.addIngredient(chocolate);
        
        final Recipe r2 = new Recipe();
        coffee = new Ingredient("coffee", 3);
        milk = new Ingredient("milk", 1);
        sugar = new Ingredient("sugar", 1);
        chocolate = new Ingredient("chocolate", 2);
        r2.setPrice( 50 );
        r2.setName( "Mocha" );
        r2.addIngredient(coffee);
        r2.addIngredient(milk);
        r2.addIngredient(sugar);
        r2.addIngredient(chocolate);

        service.save( r1 );
        


        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().is4xxClientError() );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
    }



}