import static org.junit.Assert.*;
import org.junit.jupiter.api.Test;

public class TestShoppingCartApi {
	Cart createGuestCart() {
		Cart testCart = new Cart(null, "IN");
		return testCart;
	}
	Cart createUserCart() {
		String uuid = "a6780619-c454-47ec-87b9-4a204f5072fc";
		Cart testCart = new Cart(uuid,"IN");
		return testCart;
	}
	Item createItemOne() {
		String itemName = "Pocky Sticks";
		String itemDescription = "Delicious Creamy Chocolate Covered Biscuit Sticks";
		double itemPrice = 8.99;
		String itemPicture = "https://target.scene7.com/is/image/Target/GUEST_d0eb439c-3f20-4eb7-be83-471c10b67778?wid=488&hei=488&fmt=pjpeg";
		int itemQuantity = 5;
		Item testItem = new Item(itemName, itemDescription, itemPrice, itemPicture, itemQuantity);
		
		return testItem;
	}
	@Test
	void testCreateCartWithStateCodeOnly() {
		Cart testCart = new Cart(null,"IN");
		
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
		assertEquals(testItem.name, itemName);
		assertEquals(testItem.desc, itemDescription);
		assertTrue(testItem.price - itemPrice == 0);
		assertEquals(testItem.picture, itemPicture);
		assertEquals(testItem.quantity, itemQuantity);
	}
	@Test
	void testAddItemToCart() {
		Cart testCart = createGuestCart();
		Item testItem = createItemOne();
		
		testCart.addItemToCart(testItem);
		
		assertFalse(testCart.items.isEmpty());
		assertTrue(testCart.items.contains(testItem));
	}
	@Test
	void testItemConversionToJSONFormatAndBack() {
		Item testItem = createItemOne();
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
	void testCartConversionToJSONFormatAndBack() {
		Cart testCart = createGuestCart();
		Item testITem = createItemOne();
		testCart.addItemToCart(testITem);
		
		System.out.println(testCart.toJSONString());
	}
}
