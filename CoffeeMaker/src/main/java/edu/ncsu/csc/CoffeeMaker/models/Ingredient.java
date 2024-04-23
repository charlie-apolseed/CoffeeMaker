package edu.ncsu.csc.CoffeeMaker.models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;



@Entity
public class Ingredient extends DomainObject {
	
	
	@Id
	@GeneratedValue
	Long id;
	
	
	String name;
	
	
	Integer amount;

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
	 * @throws IAE if amount is negative
	 */
	public void setAmount(Integer amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("Amount cannot be negative");
		}
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
		
		if (name == null || "".equals(name)) {
			throw new IllegalArgumentException("Name cannot be null or empty");
		}
		
		this.name = name;
	}

	@Override
	public Long getId() {
		return id;
	}
	



}
