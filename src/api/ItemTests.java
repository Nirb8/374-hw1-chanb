package api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

public class ItemTests {
	@Test
	void testCreateItem() {
		String itemName = "Pocky Sticks";
		String itemDescription = "Delicious Creamy Chocolate Covered Biscuit Sticks";
		double itemPrice = 8.99;
		String itemPicture = "https://target.scene7.com/is/image/Target/GUEST_d0eb439c-3f20-4eb7-be83-471c10b67778?wid=488&hei=488&fmt=pjpeg";
		int itemQuantity = 5;
		Item testItem = new Item(itemName, itemDescription, itemPrice, itemPicture, itemQuantity);
		
		assertFalse(testItem == null);
		assertEquals(testItem.name, itemName);
		assertEquals(testItem.desc, itemDescription);
		assertTrue(testItem.price == itemPrice);
		assertEquals(testItem.picture, itemPicture);
		assertEquals(testItem.quantity, itemQuantity);
	}
	@Test
	void testItemConversionToJSONFormatAndBack() {
		Item testItem = TestEnvironmentGenerator.createItemOne();
		//System.out.println(testItem.toJSONString());
		String itemName = "Pocky Sticks";
		String itemDescription = "Delicious Creamy Chocolate Covered Biscuit Sticks";
		double itemPrice = 8.99;
		String itemPicture = "https://target.scene7.com/is/image/Target/GUEST_d0eb439c-3f20-4eb7-be83-471c10b67778?wid=488&hei=488&fmt=pjpeg";
		int itemQuantity = 5;
		
		String json = testItem.toJSONString();
		
		Item convertedBackItem = new Item(json);
		
		assertFalse(convertedBackItem == null);
		assertEquals(convertedBackItem.name, itemName);
		assertEquals(convertedBackItem.desc, itemDescription);
		assertTrue(convertedBackItem.price - itemPrice == 0);
		assertEquals(convertedBackItem.picture, itemPicture);
		assertEquals(convertedBackItem.quantity, itemQuantity);
	}
	@Test
	void testItemTakeFrom() {
		Item testItem = TestEnvironmentGenerator.createItemOne();
		assertEquals(testItem.quantity, 5);
		Item takenItem = testItem.takeFrom(3);
		
		assertEquals(testItem.itemID, takenItem.itemID); //verify that the taken item is the same as the source item

		assertEquals(takenItem.quantity, 3);
	}
	@Test
	void testItemTakeFromTooMany() {
		Item testItem = TestEnvironmentGenerator.createItemOne();
		
		Item attemptedTakenItem = testItem.takeFrom(6);
		
		assertEquals(attemptedTakenItem, null); //no item should be taken
		assertEquals(testItem.quantity, 5); // make sure that testItem did not have it's quantity reduced
	}
	@Test
	void testMergeItem() {
		Item testItem = TestEnvironmentGenerator.createItemOne();
		Item testItem2 = TestEnvironmentGenerator.createItemOne();

		assertEquals(testItem.quantity, 5);
		testItem.mergeItem(testItem2);
		assertEquals(testItem.quantity, 10);
	}
	@Test
	void testMergeItemNegative() {
		Item testItem = TestEnvironmentGenerator.createItemOne();
		Item testItem2 = testItem.takeFrom(-3);
		Item testItem3 = TestEnvironmentGenerator.createItemOne();
		
		assertEquals(testItem3.quantity, 5);
		testItem3.mergeItem(testItem2);
		assertEquals(testItem3.quantity, 2);
	}
}
