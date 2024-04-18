package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import edu.ncsu.csc.CoffeeMaker.models.enums.IngredientType;

@Entity
public class Ingredient extends DomainObject {
	@Id
	@GeneratedValue
	Long id;
	@Enumerated ( EnumType.STRING )
	IngredientType ingredient;
	Integer amount;

	public Ingredient(IngredientType ingredient, Integer amount) {
		super();
		this.ingredient = ingredient;
		this.amount = amount;
	}
	
	public Ingredient() {
		
	}

	@Override
	public String toString() {
		return "Ingredient [id=" + id + ", ingredient=" + ingredient + ", amount=" + amount + "]";
	}

	public IngredientType getIngredient() {
		return ingredient;
	}

	public void setIngredient(IngredientType ingredient) {
		this.ingredient = ingredient;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	@Override
	public Serializable getId() {
		// TODO Auto-generated method stub
		return null;
	}
	



}
