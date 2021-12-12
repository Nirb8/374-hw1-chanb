import static org.junit.Assert.*;
import org.junit.jupiter.api.Test;

public class TestShoppingCartApi {
	@Test
	void testCreateCartWithStateCodeOnly() {
		Cart testCart = new Cart("IN");
		
		assertFalse(testCart == null);
		assertEquals(testCart.stateCode, "IN");
	}
	@Test
	void testCreateCartWithUserIDAndStateCode() {
		String uuid = "a6780619-c454-47ec-87b9-4a204f5072fc";
		Cart testCart = new Cart(uuid,"IN");
		
		assertFalse(testCart == null);
		assertEquals(testCart.stateCode, "IN");
	}
	@Test
	void testCreateItem() {
		String itemName = "Pocky Sticks";
		String itemDescription = "Delicious Creamy Chocolate Covered Biscuit Sticks";
		double itemPrice = 8.99;
		String itemPicture = "https://target.scene7.com/is/image/Target/GUEST_d0eb439c-3f20-4eb7-be83-471c10b67778?wid=488&hei=488&fmt=pjpeg";
		int itemQuantity = 5;
		Item testItem = new Item(itemName, itemDescription, itemPrice, itemPicture, itemQuantity);
		
		assertFalse(testItem == null);
		assertEquals(testItem.itemName, itemName);
		assertEquals(testItem.itemDescription, itemDescription);
		assertTrue(testItem.itemPrice - itemPrice == 0);
		assertEquals(testItem.itemPicture, itemPicture);
		assertEquals(testItem.quantity, itemQuantity);
	}
	@Test
	void testAddItemToCart() {
		String uuid = "a6780619-c454-47ec-87b9-4a204f5072fc";
		Cart testCart = new Cart(uuid,"IN");
		String itemName = "Pocky Sticks";
		String itemDescription = "Delicious Creamy Chocolate Covered Biscuit Sticks";
		double itemPrice = 8.99;
		String itemPicture = "https://target.scene7.com/is/image/Target/GUEST_d0eb439c-3f20-4eb7-be83-471c10b67778?wid=488&hei=488&fmt=pjpeg";
		int itemQuantity = 5;
		Item testItem = new Item(itemName, itemDescription, itemPrice, itemPicture, itemQuantity);
		
		testCart.addItemToCart(testItem);
		
		assertFalse(testCart.items.isEmpty());
		assertTrue(testCart.items.contains(testItem));
	}
}
