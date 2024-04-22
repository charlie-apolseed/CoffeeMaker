package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIIngredientController extends APIController{
	
	/**
     * IngredientService object, to be autowired in by Spring to allow for
     * manipulating the ingredient model
     */
	@Autowired
	private IngredientService ingredientService;
	
	/**
     * InventoryService object, to be autowired in by Spring to allow for
     * manipulating the inventory model. Used for the POST/Ingredients endpoint. 
     */
	@Autowired
	private InventoryService inventoryService;
	
	/**
	 * REST API endpoint to provide get access to CoffeeMaker's ingredients. This 
	 * will return the ingredient object that matches the input name.
	 * @param name
	 * @return
	 */
	@GetMapping ( BASE_PATH + "ingredients/{name}" )
	public ResponseEntity getIngredient ( @PathVariable final String name ) {

	    final Ingredient ingr = ingredientService.findByName( name );

	    if ( null == ingr ) {
	        return new ResponseEntity( HttpStatus.NOT_FOUND );
	    }

	    return new ResponseEntity( ingr, HttpStatus.OK );
	}
	
	/**
     * REST API endpoint to provide update access to CoffeeMaker's singleton
     * Inventory. This will update the Inventory of the CoffeeMaker by adding
     * amounts from the Inventory provided to the CoffeeMaker's stored inventory
     *
     * @param inventory
     *            amounts to add to inventory
     * @return response to the request
     */
    @PostMapping ( BASE_PATH + "/ingredients" )
    public ResponseEntity addIngredient ( @RequestBody final Ingredient ingredient ) {
    	//Get name of the ingredient being added
    	String ingredientName = ingredient.getName();
    	//Check to see if an ingredient of the specified name already exists
    	if (ingredientService.findByName(ingredientName) != null) {
    		return new ResponseEntity( HttpStatus.CONFLICT); 
    	}
    	//Save the ingredient to the DB
    	ingredientService.save(ingredient);
    	
    	//Update the inventory
    	//TODO Add the functionality to update the inventory. This requires the inventory class 
    	//to be built out, which will not be done until next  (4/28). 
    	return null;
    }
}
