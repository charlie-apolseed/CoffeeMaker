package edu.ncsu.csc.CoffeeMaker.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

    @Autowired
    private InventoryService inventoryService;

    @BeforeEach
    public void setup () {
        final Inventory ivt = inventoryService.getInventory();

        ivt.setChocolate( 500 );
        ivt.setCoffee( 500 );
        ivt.setMilk( 500 );
        ivt.setSugar( 500 );

        inventoryService.save( ivt );
    }
    
    @Test
    @Transactional
    public void testInventoryConstructor() {
    	int coffee = 50;
    	int milk = 51;
    	int sugar = 52;
    	int chocolate = 53;
    	
    	Inventory inventoryTest = new Inventory(coffee, milk, sugar, chocolate);
    	Assertions.assertEquals(coffee, inventoryTest.getCoffee());
    	Assertions.assertEquals(milk, inventoryTest.getMilk());
    	Assertions.assertEquals(sugar, inventoryTest.getSugar());
    	Assertions.assertEquals(chocolate, inventoryTest.getChocolate());
    	
    }
    
    @Test
    @Transactional
	public void testInventoryCheckChocolate() {
		final Inventory i = inventoryService.getInventory();
		Assertions.assertEquals(50, i.checkChocolate("50"));
		try {
			i.checkChocolate("hello");
			Assertions.fail();
		} catch (IllegalArgumentException e) {
			Assertions.assertEquals(e.getMessage(), "Units of chocolate must be a positive integer");
		}
		try {
			i.checkChocolate("-10");
			Assertions.fail();
		} catch (IllegalArgumentException e) {
			Assertions.assertEquals(e.getMessage(), "Units of chocolate must be a positive integer");
		}
	}
    
    @Test
    @Transactional
	public void testInventoryCheckCoffee() {
		final Inventory i = inventoryService.getInventory();
		Assertions.assertEquals(50, i.checkCoffee("50"));
		try {
			i.checkCoffee("hello");
			Assertions.fail();
		} catch (IllegalArgumentException e) {
			Assertions.assertEquals(e.getMessage(), "Units of coffee must be a positive integer");
		}
		try {
			i.checkCoffee("-10");
			Assertions.fail();
		} catch (IllegalArgumentException e) {
			Assertions.assertEquals(e.getMessage(), "Units of coffee must be a positive integer");
		}
	}
    
    @Test
    @Transactional
	public void testInventoryCheckSugar() {
		final Inventory i = inventoryService.getInventory();
		Assertions.assertEquals(50, i.checkSugar("50"));
		try {
			i.checkSugar("hello");
			Assertions.fail();
		} catch (IllegalArgumentException e) {
			Assertions.assertEquals(e.getMessage(), "Units of sugar must be a positive integer");
		}
		try {
			i.checkSugar("-10");
			Assertions.fail();
		} catch (IllegalArgumentException e) {
			Assertions.assertEquals(e.getMessage(), "Units of sugar must be a positive integer");
		}
	}
    
    
    @Test
    @Transactional
    public void testConsumeInventory () {
        final Inventory i = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.setChocolate( 10 );
        recipe.setMilk( 20 );
        recipe.setSugar( 5 );
        recipe.setCoffee( 1 );

        recipe.setPrice( 5 );

        i.useIngredients( recipe );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */

        Assertions.assertEquals( 490, (int) i.getChocolate() );
        Assertions.assertEquals( 480, (int) i.getMilk() );
        Assertions.assertEquals( 495, (int) i.getSugar() );
        Assertions.assertEquals( 499, (int) i.getCoffee() );
    }

    @Test
    @Transactional
    public void testAddInventory1 () {
        Inventory ivt = inventoryService.getInventory();

        ivt.addIngredients( 5, 3, 7, 2 );

        /* Save and retrieve again to update with DB */
        inventoryService.save( ivt );

        ivt = inventoryService.getInventory();

        Assertions.assertEquals( 505, (int) ivt.getCoffee(),
                "Adding to the inventory should result in correctly-updated values for coffee" );
        Assertions.assertEquals( 503, (int) ivt.getMilk(),
                "Adding to the inventory should result in correctly-updated values for milk" );
        Assertions.assertEquals( 507, (int) ivt.getSugar(),
                "Adding to the inventory should result in correctly-updated values sugar" );
        Assertions.assertEquals( 502, (int) ivt.getChocolate(),
                "Adding to the inventory should result in correctly-updated values chocolate" );

    }

    @Test
    @Transactional
    public void testAddInventory2 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredients( -5, 3, 7, 2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getCoffee(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee" );
            Assertions.assertEquals( 500, (int) ivt.getMilk(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk" );
            Assertions.assertEquals( 500, (int) ivt.getSugar(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar" );
            Assertions.assertEquals( 500, (int) ivt.getChocolate(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- chocolate" );
        }
    }

    @Test
    @Transactional
    public void testAddInventory3 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredients( 5, -3, 7, 2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getCoffee(),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- coffee" );
            Assertions.assertEquals( 500, (int) ivt.getMilk(),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- milk" );
            Assertions.assertEquals( 500, (int) ivt.getSugar(),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- sugar" );
            Assertions.assertEquals( 500, (int) ivt.getChocolate(),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- chocolate" );

        }

    }

    @Test
    @Transactional
    public void testAddInventory4 () { 
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredients( 5, 3, -7, 2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getCoffee(),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- coffee" );
            Assertions.assertEquals( 500, (int) ivt.getMilk(),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- milk" );
            Assertions.assertEquals( 500, (int) ivt.getSugar(),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- sugar" );
            Assertions.assertEquals( 500, (int) ivt.getChocolate(),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- chocolate" );

        }

    }

    @Test
    @Transactional
    public void testAddInventory5 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredients( 5, 3, 7, -2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getCoffee(),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- coffee" );
            Assertions.assertEquals( 500, (int) ivt.getMilk(),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- milk" );
            Assertions.assertEquals( 500, (int) ivt.getSugar(),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- sugar" );
            Assertions.assertEquals( 500, (int) ivt.getChocolate(),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- chocolate" );

        }

    }
    
    /**
     * This test is used to verify the checkChocolate method is working. 
     * Provides both legal and illegal arguments.
     * 
     * @author capolinsky
     */
    @Test
    @Transactional
    public void testCheckChocolate () {
        final Inventory inventory = inventoryService.getInventory();

        /* Legal arguments for checkChocolate */
        int amtChocolate = inventory.checkChocolate("50");
        Assertions.assertEquals(amtChocolate, 50, "Trying to checkChocolate with a legal argument of '50', should return 50 but did not.");
        amtChocolate = inventory.checkChocolate("0");
        Assertions.assertEquals(amtChocolate, 0, "Trying to checkChocolate with a legal argument of '0', should return 50 but did not.");
        amtChocolate = inventory.checkChocolate("1000");
        Assertions.assertEquals(amtChocolate, 1000, "Trying to checkChocolate with a legal argument of '0', should return 50 but did not.");
        
        /* Illegal arguments for checkChocolate */
        try {
            inventory.checkChocolate("-50");
            Assertions.fail("Trying to checkChocolate with an illegal argument of '-50', should have thrown an IAE but did not.");
        } catch (IllegalArgumentException iae) {
            // exception expected, carry on
        }
        try {
            inventory.checkChocolate("ooga-booga");
            Assertions.fail("Trying to checkChocolate with an illegal argument of 'ooga-booga', should have thrown an IAE but did not.");
        } catch (IllegalArgumentException iae) {
            // exception expected, carry on
        }
    }

    /**
     * This test is used to verify the checkCoffee method is working. 
     * Provides both legal and illegal arguments.
     * 
     * @author capolinsky
     */
    @Test
    @Transactional
    public void testCheckCoffee() {
        final Inventory inventory = inventoryService.getInventory();

        /* Legal arguments for checkCoffee */
        int amtCoffee = inventory.checkCoffee("50");
        Assertions.assertEquals(amtCoffee, 50, "Trying to checkCoffee with a legal argument of '50', should return 50 but did not.");
        amtCoffee = inventory.checkCoffee("0");
        Assertions.assertEquals(amtCoffee, 0, "Trying to checkCoffee with a legal argument of '0', should return 0 but did not.");
        amtCoffee = inventory.checkCoffee("1000");
        Assertions.assertEquals(amtCoffee, 1000, "Trying to checkCoffee with a legal argument of '1000', should return 1000 but did not.");

        /* Illegal arguments for checkCoffee */
        try {
            inventory.checkCoffee("-50");
            Assertions.fail("Trying to checkCoffee with an illegal argument of '-50', should have thrown an IAE but did not.");
        } catch (IllegalArgumentException iae) {
            // Exception expected, carry on
        }
        try {
            inventory.checkCoffee("ooga-booga");
            Assertions.fail("Trying to checkCoffee with an illegal argument of 'ooga-booga', should have thrown an IAE but did not.");
        } catch (IllegalArgumentException iae) {
            // Exception expected, carry on
        }
    }

    /**
     * This test is used to verify the checkMilk method is working. 
     * Provides both legal and illegal arguments.
     * 
     * @author capolinsky
     */
    @Test
    @Transactional
    public void testCheckMilk() {
        final Inventory inventory = inventoryService.getInventory();

        /* Legal arguments for checkMilk */
        int amtMilk = inventory.checkMilk("50");
        Assertions.assertEquals(amtMilk, 50, "Trying to checkMilk with a legal argument of '50', should return 50 but did not.");
        amtMilk = inventory.checkMilk("0");
        Assertions.assertEquals(amtMilk, 0, "Trying to checkMilk with a legal argument of '0', should return 0 but did not.");
        amtMilk = inventory.checkMilk("1000");
        Assertions.assertEquals(amtMilk, 1000, "Trying to checkMilk with a legal argument of '1000', should return 1000 but did not.");

        /* Illegal arguments for checkMilk */
        try {
            inventory.checkMilk("-50");
            Assertions.fail("Trying to checkMilk with an illegal argument of '-50', should have thrown an IAE but did not.");
        } catch (IllegalArgumentException iae) {
            // Exception expected, carry on
        }
        try {
            inventory.checkMilk("ooga-booga");
            Assertions.fail("Trying to checkMilk with an illegal argument of 'ooga-booga', should have thrown an IAE but did not.");
        } catch (IllegalArgumentException iae) {
            // Exception expected, carry on
        }
    }

    /**
     * This test is used to verify the checkSugar method is working. 
     * Provides both legal and illegal arguments.
     * 
     * @author capolinsky
     */
    @Test
    @Transactional
    public void testCheckSugar() {
        final Inventory inventory = inventoryService.getInventory();

        /* Legal arguments for checkSugar */
        int amtSugar = inventory.checkSugar("50");
        Assertions.assertEquals(amtSugar, 50, "Trying to checkSugar with a legal argument of '50', should return 50 but did not.");
        amtSugar = inventory.checkSugar("0");
        Assertions.assertEquals(amtSugar, 0, "Trying to checkSugar with a legal argument of '0', should return 0 but did not.");
        amtSugar = inventory.checkSugar("1000");
        Assertions.assertEquals(amtSugar, 1000, "Trying to checkSugar with a legal argument of '1000', should return 1000 but did not.");

        /* Illegal arguments for checkSugar */
        try {
            inventory.checkSugar("-50");
            Assertions.fail("Trying to checkSugar with an illegal argument of '-50', should have thrown an IAE but did not.");
        } catch (IllegalArgumentException iae) {
            // Exception expected, carry on
        }
        try {
            inventory.checkSugar("ooga-booga");
            Assertions.fail("Trying to checkSugar with an illegal argument of 'ooga-booga', should have thrown an IAE but did not.");
        } catch (IllegalArgumentException iae) {
            // Exception expected, carry on
        }
    }

    
    /**
     * Tests the toString method for the inventory. 
     * Makes sure the method returns the correct string representation for the current inventory contents.
     * 
     * @author capolinsky
     */
    @Test
    @Transactional
    public void testToString() {
    	final Inventory ivt = inventoryService.getInventory();
    	
    	String expectedString = "Coffee: 500\nMilk: 500\nSugar: 500\nChocolate: 500\n";
    	String resultString = ivt.toString();
    	
    	Assertions.assertEquals(expectedString, resultString, "Expected: " + expectedString + "\nBut got: " + resultString);	
    }
    
    /**
     * Tests the setId method. Assumes the getID method works. 
     * 
     * @author capolinsky
     */
    @Test
    @Transactional
    public void testSetId() {
    	Inventory ivt = inventoryService.getInventory();
    	Long newId = (long) 10;
    	
    	ivt.setId(newId);  
    	
    	Assertions.assertEquals(ivt.getId(), newId, "Expected the new id to be set to " + newId + "but was " + ivt.getId() + " instead.");
    	}
    
    /**
     * Tests to make sure the enoughIngredients method works. 
     * 
     * @author capolinsky
     */
    @Test
    @Transactional
    public void testEnoughIngredients() {
    	
    	Recipe r1 = new Recipe();
		r1.setName("Mocha");
		r1.setPrice(1);
		r1.setCoffee(1);
		r1.setMilk(1);
		r1.setSugar(1);
		r1.setChocolate(1);
		
		/*sufficient ingredients*/
		Inventory ivt1 = new Inventory(2, 2, 2, 2);
		Assertions.assertTrue(ivt1.enoughIngredients(r1));
		
		/*insufficient ingredients*/
		Inventory ivt2 = new Inventory(0, 2, 2, 2);
		Assertions.assertFalse(ivt2.enoughIngredients(r1),"Insufficient coffee in inventory, should return false but did not.");
   
		Inventory ivt3 = new Inventory(2, 0, 2, 2);
		Assertions.assertFalse(ivt3.enoughIngredients(r1),"Insufficient milk in inventory, should return false but did not.");
 
		Inventory ivt4 = new Inventory(2, 2, 0, 2);
		Assertions.assertFalse(ivt4.enoughIngredients(r1),"Insufficient sugar in inventory, should return false but did not.");
 
		Inventory ivt5 = new Inventory(2, 2, 2, 0);
		Assertions.assertFalse(ivt5.enoughIngredients(r1),"Insufficient chocolate in inventory, should return false but did not.");
    }
}