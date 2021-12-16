package api;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

public class CartTests {
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
	void testCartConversionToJSONFormatAndBack() {
		Cart testCart = TestEnvironmentGenerator.createUserCart();
		Item testItem = TestEnvironmentGenerator.createItemOne();
		testCart.addItemToCart(testItem);
		Discount testDiscount = TestEnvironmentGenerator.createDiscountOne();
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
	void testAddItemToCart() {
		Cart testCart = TestEnvironmentGenerator.createGuestCart();
		Item testItem = TestEnvironmentGenerator.createItemOne();
		
		testCart.addItemToCart(testItem);
		
		assertFalse(testCart.items.isEmpty());
		assertTrue(testCart.items.contains(testItem));
	}
	@Test
	void testAddDiscountToCart() {
		Cart testCart = TestEnvironmentGenerator.createUserCart();
		Discount testDiscount = TestEnvironmentGenerator.createDiscountOne();
		testCart.addDiscountToCart(testDiscount);
		assertFalse(testCart.discounts.isEmpty());
		assertTrue(testCart.discounts.contains(testDiscount));
	}
	@Test
	void testCheckIfItemIsInCartByID() {
		Cart testCart = TestEnvironmentGenerator.createGuestCart();
		Item testItem = TestEnvironmentGenerator.createItemOne();
		
		testCart.addItemToCart(testItem);
		
		assertTrue(testCart.checkIfItemIsInCartById(testItem.itemID));
		String notInCart = "b397529e-bf9c-4f3e-aff6-0d01a3daeceb";
		assertFalse(testCart.checkIfItemIsInCartById(notInCart));
	}
	@Test
	void testCartSubtotalCalculation() {
		Cart testCart = TestEnvironmentGenerator.createGuestCart();
		Item testItem = TestEnvironmentGenerator.createItemOne();
		
		testCart.addItemToCart(testItem);
		
		assertTrue(testCart.calculateSubtotal() == 44.95);
	}
	@Test
	void testCartTotalCalculation() {
		Cart testCart = TestEnvironmentGenerator.createGuestCart();
		Item testItem = TestEnvironmentGenerator.createItemOne();
		
		testCart.addItemToCart(testItem);
		
		assertTrue(testCart.calculateTotal() == 48.55);
	}
	@Test
	void testCartTotalCalculationWithDiscounts() {
		Cart testCart = TestEnvironmentGenerator.createGuestCart();
		Item testItem = TestEnvironmentGenerator.createItemOne();
		Discount testDiscount = TestEnvironmentGenerator.createDiscountOne();
		
		testCart.addItemToCart(testItem);
		testCart.addDiscountToCart(testDiscount);
		
		assertTrue(testCart.calculateTotal() == 24.28);
	}
	@Test
	void testCartCalculateDiscountedAmount() {
		Cart testCart = TestEnvironmentGenerator.createGuestCart();
		Item testItem = TestEnvironmentGenerator.createItemOne();
		Discount testDiscount = TestEnvironmentGenerator.createDiscountOne();
		
		testCart.addItemToCart(testItem);
		testCart.addDiscountToCart(testDiscount);
		double subtotal = testCart.calculateSubtotal();
		assertTrue(testCart.calculateDiscountedAmount(subtotal) == 22.475);
	}
	@Test
	void testCartCalculateEstimatedTaxes() {
		Cart testCart = TestEnvironmentGenerator.createGuestCart();
		Item testItem = TestEnvironmentGenerator.createItemOne();
		
		testCart.addItemToCart(testItem);
		double totalBeforeTax = testCart.calculateSubtotal();
		assertTrue(testCart.calculateEstimatedTaxes(totalBeforeTax) == 3.6);
	}
}
