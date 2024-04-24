package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * Recipe for the coffee maker. Recipe is tied to the database using Hibernate
 * libraries. See RecipeRepository and RecipeService for the other two pieces
 * used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Recipe extends DomainObject {

	/** Recipe id */
	@Id
	@GeneratedValue
	private Long id;

	/** Recipe name */
	@NotBlank
	private String name;

	/** Recipe price */
	@Min(0)
	private Integer price;

	/** Ingredient list */
	@NotEmpty
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Ingredient> ingredients;

	/**
	 * Creates a default recipe for the coffee maker.
	 */
	public Recipe() {
		this.name = "";
		ingredients = new ArrayList<Ingredient>();
	}

	/**
	 * Adds a single ingredient to a recipe
	 * 
	 * @param ingredient the ingredient to add
	 */
	public void addIngredient(Ingredient ingredient) {
		ingredients.add(ingredient);

	}

	/***
	 * Gets the list of ingredients in recipe
	 * 
	 * @return list of ingredients in recipe
	 */
	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	/**
	 * Get the ID of the Recipe
	 *
	 * @return the ID
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * Set the ID of the Recipe (Used by Hibernate)
	 *
	 * @param id the ID
	 */
	@SuppressWarnings("unused")
	private void setId(final Long id) {
		this.id = id;
	}

	/**
	 * Returns name of the recipe.
	 *
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the recipe name.
	 *
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Returns the price of the recipe.
	 *
	 * @return Returns the price.
	 */
	public Integer getPrice() {
		return price;
	}

	/**
	 * Sets the recipe price.
	 *
	 * @param price The price to set.
	 */
	public void setPrice(final Integer price) {
		this.price = price;
	}

	/**
	 * Updates recipe ingredients and price to match the given recipe
	 * 
	 * @param r given recipe
	 */
	public void updateRecipe(Recipe r) {
		this.ingredients = r.getIngredients();
		setPrice(r.price);

	}

	@Override
	public String toString() {
		return "Recipe [id=" + id + ", name=" + name + ", price=" + price + ", ingredients=" + ingredients + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		Integer result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Recipe other = (Recipe) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

}
