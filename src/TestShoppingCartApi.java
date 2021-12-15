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
	Discount createDiscountOne() {
		String discountCode = "5318008";
		double percentOff = 50;
		Discount testDiscount = new Discount(discountCode, percentOff);
		return testDiscount;
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
		assertTrue(testItem.price == itemPrice);
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
		Cart testCart = createUserCart();
		Item testItem = createItemOne();
		testCart.addItemToCart(testItem);
		Discount testDiscount = createDiscountOne();
		testCart.addDiscountToCart(testDiscount);
		
		String json = testCart.toJSONString();
		
		Cart convertedBackCart = new Cart(json);
		assertEquals(convertedBackCart.items.size(), testCart.items.size());
		assertEquals(convertedBackCart.discounts.size(), testCart.discounts.size());
		assertEquals(convertedBackCart.stateCode, testCart.stateCode);
		assertEquals(convertedBackCart.cartID, testCart.cartID);
		assertEquals(convertedBackCart.userID, testCart.userID);
	}
	@Test
	void testCartSubtotalCalculation() {
		Cart testCart = createGuestCart();
		Item testItem = createItemOne();
		
		testCart.addItemToCart(testItem);
		
		assertTrue(testCart.calculateSubtotal() == 44.95);
	}
	@Test
	void testCartTotalCalculation() {
		Cart testCart = createGuestCart();
		Item testItem = createItemOne();
		
		testCart.addItemToCart(testItem);
		
		assertTrue(testCart.calculateTotal() == 48.55);
	}
	@Test
	void testAddDiscountToCart() {
		Cart testCart = createUserCart();
		Discount testDiscount = createDiscountOne();
		testCart.addDiscountToCart(testDiscount);
		assertFalse(testCart.discounts.isEmpty());
		assertTrue(testCart.discounts.contains(testDiscount));
	}
	@Test
	void testCartTotalCalculationWithDiscounts() {
		Cart testCart = createGuestCart();
		Item testItem = createItemOne();
		Discount testDiscount = createDiscountOne();
		
		testCart.addItemToCart(testItem);
		testCart.addDiscountToCart(testDiscount);
		
		assertTrue(testCart.calculateTotal() == 24.27);
	}
}
