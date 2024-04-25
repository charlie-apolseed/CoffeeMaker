package edu.ncsu.csc.CoffeeMaker.models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


/**
 * The ingredient class represents an ingredient that can be added to a recipe or the inventory.
 * Each ingredient stores its name and its amount (needed for the recipe or in the inventory).
 */
@Entity
public class Ingredient extends DomainObject {
	
	
	@Id
	@GeneratedValue
	private Long id;
	
	/**
	 * name of ingredient
	 */
	@NotBlank
	private String name;
	
	/**
	 * current amount of ingredient
	 */
	@Min(0)
	private Integer amount;

	/**
	 * Constructor sets fields
	 * 
	 * @param name to set
	 * @param amount to set 
	 */
	public Ingredient(String name, Integer amount) {
		super();
		setName(name);
		setAmount(amount);
	}
	
	/**
	 * Empty constructor for Hibernate
	 */
	public Ingredient() {
		
	}

	@Override
	public String toString() {
		return "Ingredient [id=" + id + ", ingredient=" + name + ", amount=" + amount + "]";
	}



	/**
	 * 
	 * @return amount
	 */
	public Integer getAmount() {
		return amount;
	}

	/**
	 * Setter for amount
	 * 
	 * @param amount to set
	 */
	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets name 
	 * 
	 * @param name to set
	 * @throws IAE if the name is empty or null
	 */
	public void setName(String name) {
		
		this.name = name;
	}

	@Override
	public Long getId() {
		return id;
	}
	



}
