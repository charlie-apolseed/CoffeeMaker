package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.repositories.IngredientRepository;

@Component
@Transactional
public class IngredientService extends Service<Ingredient,Long> {
	@Autowired
	private IngredientRepository repository;

	/**
	 * Retrieves the singleton repostorty instance from the database, creating it if
	 * it does not exist.
	 *
	 * @return the Inventory, either new or fetched
	 */
	public synchronized IngredientRepository getRepository() {
		return repository;
	}
	
	/**
     * Find an ingredient with the provided name
     * 
     * @param name
     *            Name of the ingredient to find
     * @return found ingredient, null if none
     */
    public Ingredient findByName ( final String name ) {
        return repository.findByName( name );
    }
}
