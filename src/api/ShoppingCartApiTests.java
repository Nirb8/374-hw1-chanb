package api;
import static org.junit.Assert.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

public class ShoppingCartApiTests {
	
	@Test
	void testGetCartByID() {
		ShoppingCartApi api = TestEnvironmentGenerator.createApi();
		Cart testCart = new Cart(null, "CA");
		api.carts.add(testCart);
		
		assertEquals(api.getCartByID(testCart.cartID), testCart);
	}
	@Test
	void testGetItemByID() {
		ShoppingCartApi api = TestEnvironmentGenerator.createApi();
		Item testItem = TestEnvironmentGenerator.createItemOne();
		
		api.items.add(testItem);
		
		assertEquals(api.getItemByID(testItem.itemID), testItem);
	}
	@Test
	void testGetDiscountByCode() {
		ShoppingCartApi api = TestEnvironmentGenerator.createApi();
		Discount testDiscount = TestEnvironmentGenerator.createDiscountOne();
		
		api.discounts.add(testDiscount);
		
		assertEquals(api.getDiscountByCode(testDiscount.discountCode).discountCode, testDiscount.discountCode);
		assertTrue(api.getDiscountByCode(testDiscount.discountCode).percentOff == testDiscount.percentOff);
	}
	@Test
	void testWriteError() {
		ShoppingCartApi api = TestEnvironmentGenerator.createApi();
		String errorResponse = api.writeError("error message");
		assertEquals(errorResponse, "{\"error\":\"error message\"}");
	}
	@Test
	void testWriteSuccess() {
		ShoppingCartApi api = TestEnvironmentGenerator.createApi();
		String successResponse = api.writeSuccess("success message");
		assertEquals(successResponse, "{\"success\":\"success message\"}");
	}
	@Test
	void testUserLockoutIncrement() {
		ShoppingCartApi api = TestEnvironmentGenerator.createApi();
		Cart testCart = TestEnvironmentGenerator.createUserCart();
		api.carts.add(testCart);
		
		api.doUserLockoutIncrement(testCart.userID);
		assertTrue(api.userDiscountLockout.containsKey(testCart.userID));
		int userLockout = api.userDiscountLockout.get(testCart.userID);
		assertEquals(1, userLockout);
	}
	@SuppressWarnings("unchecked")
	@Test
	void testHandleViewCartRequest() {
		ShoppingCartApi api = TestEnvironmentGenerator.createApi();
		Cart testCart = new Cart(null, "CA");
		String guid = "72040136-decd-497d-a7e0-8868c594a735";
		testCart.cartID = guid; //so that we can reference it in the test
		testCart.addItemToCart(TestEnvironmentGenerator.createItemOne());
		testCart.addDiscountToCart(TestEnvironmentGenerator.createDiscountOne());
		api.carts.add(testCart);
		
		//build the request JSON
		JSONObject request = new JSONObject();
		request.put("type", "view-cart");
		request.put("cartID", guid);
		String responseString = api.handleRequest(request.toJSONString());
		
		Cart responseCart = new Cart(responseString);
		assertEquals(responseCart.items.size(), testCart.items.size());
		assertEquals(responseCart.discounts.size(), testCart.discounts.size());
		assertEquals(responseCart.stateCode, testCart.stateCode);
		assertEquals(responseCart.cartID, testCart.cartID);
		assertEquals(responseCart.userID, testCart.userID);
		
		JSONParser jsonParser = new JSONParser();
		try {
			JSONObject response = (JSONObject) jsonParser.parse(responseString);
			assertEquals(response.get("total"), 23.6);
			assertEquals(response.get("subtotal"), 44.95);
			assertEquals(response.get("estimatedTaxes"), 1.12);
			assertEquals(response.get("amountSavedByDiscounts"), 22.475);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	@Test
	void testHandleAddItemRequest() {
		ShoppingCartApi api = TestEnvironmentGenerator.createApi();
		Cart testCart = TestEnvironmentGenerator.createGuestCart();
		Cart testCart2 = TestEnvironmentGenerator.createRandomUserCart();
		Item testItem = TestEnvironmentGenerator.createItemOne();
		api.carts.add(testCart);
		api.carts.add(testCart2);
		api.items.add(testItem);
		
		JSONObject request = new JSONObject();
		request.put("type", "add-item");
		request.put("cartID", testCart.cartID);
		request.put("itemID", "c74d92e7-cc56-46ae-8c71-08cff7309c7d");
		request.put("quantity", 50);
		String responseString = api.handleRequest(request.toJSONString());
		assertEquals(responseString, "{\"error\":\"Item not found\"}");
		
		request = new JSONObject();
		request.put("type", "add-item");
		request.put("cartID", testCart.cartID);
		request.put("itemID", testItem.itemID);
		request.put("quantity", 50);
		responseString = api.handleRequest(request.toJSONString());
		assertEquals(responseString, "{\"error\":\"Not enough stock to fulfill quantity requested\"}");
				
		request = new JSONObject();
		request.put("type", "add-item");
		request.put("cartID", testCart.cartID);
		request.put("itemID", testItem.itemID);
		request.put("quantity", 5);
		responseString = api.handleRequest(request.toJSONString());
		assertEquals(responseString, "{\"success\":\"OK\"}");
		
		request = new JSONObject();
		request.put("type", "add-item");
		request.put("cartID", testCart.cartID);
		request.put("itemID", testItem.itemID);
		request.put("quantity", 5);
		responseString = api.handleRequest(request.toJSONString());
		assertEquals(responseString, "{\"error\":\"Item already in cart\"}");
		
		testItem.quantity = 0; //manually set the item to out of stock
		
		assertTrue(testCart.getItemFromCartByID(testItem.itemID) != null);
		assertTrue(testCart.items.get(0).quantity == 5);
		
		
		request = new JSONObject();
		request.put("type", "add-item");
		request.put("cartID", testCart2.cartID);
		request.put("itemID", testItem.itemID);
		request.put("quantity", 5);
		responseString = api.handleRequest(request.toJSONString());
		assertEquals(responseString, "{\"error\":\"Item 'Pocky Sticks' is out of stock\"}");
	}
	@SuppressWarnings("unchecked")
	@Test
	void testHandleApplyDiscountRequest() {
		ShoppingCartApi api = TestEnvironmentGenerator.createApi();
		Cart testCart = TestEnvironmentGenerator.createGuestCart();
		Cart userCart = TestEnvironmentGenerator.createUserCart();
		Discount testDiscount = TestEnvironmentGenerator.createDiscountOne();
		Discount expiredDiscount = TestEnvironmentGenerator.createDiscountTwo();
		
		api.carts.add(testCart);
		api.carts.add(userCart);
		api.discounts.add(testDiscount);
		api.discounts.add(expiredDiscount);
		
		JSONObject request = new JSONObject();
		request.put("type", "apply-discount");
		request.put("cartID", testCart.cartID);
		request.put("discountCode", expiredDiscount.discountCode);
		
		String responseString = api.handleRequest(request.toJSONString());
		assertEquals(responseString, "{\"error\":\"That discount has expired\"}");
		
		request = new JSONObject();
		request.put("type", "apply-discount");
		request.put("cartID", testCart.cartID);
		request.put("discountCode", "mrgglglgllgg");
		
		responseString = api.handleRequest(request.toJSONString());
		
		assertEquals(responseString, "{\"error\":\"Invalid discount code\"}");
	
		request = new JSONObject();
		request.put("type", "apply-discount");
		request.put("cartID", testCart.cartID);
		request.put("discountCode", testDiscount.discountCode);
		
		responseString = api.handleRequest(request.toJSONString());
		
		assertEquals(responseString, "{\"success\":\"OK\"}");
		assertTrue(testCart.discounts.isEmpty() == false); //discount successfully added to cart
		
		//test carts that belong to users
		request = new JSONObject();
		request.put("type", "apply-discount");
		request.put("cartID", userCart.cartID);
		request.put("discountCode", "mrgglglgllgg");
		
		for(int i = 0; i<5;i++) {
			responseString = api.handleRequest(request.toJSONString());
		}
		
		request = new JSONObject();
		request.put("type", "apply-discount");
		request.put("cartID", userCart.cartID);
		request.put("discountCode", testDiscount.discountCode);
		
		responseString = api.handleRequest(request.toJSONString());
		
		assertEquals(responseString, "{\"error\":\"\"}");
	}
	@SuppressWarnings("unchecked")
	@Test
	void testHandleModifyItemQuantityRequest() {
		ShoppingCartApi api = TestEnvironmentGenerator.createApi();
		Cart testCart = TestEnvironmentGenerator.createGuestCart();
		Cart testCart2 = TestEnvironmentGenerator.createUserCart();
		Item testItem = TestEnvironmentGenerator.createItemOne();
		
		api.carts.add(testCart);
		api.carts.add(testCart2);
		api.items.add(testItem);
		
		testCart.addItemToCart(testItem.takeFrom(2));
		
		JSONObject request = new JSONObject();
		request.put("type", "modify-item-quantity");
		request.put("cartID", testCart.cartID);
		request.put("itemID", "c74d92e7-cc56-46ae-8c71-08cff7309c7d");
		request.put("quantity", 50);
		String responseString = api.handleRequest(request.toJSONString());
		assertEquals(responseString, "{\"error\":\"Item not found\"}");
		
		request = new JSONObject();
		request.put("type", "modify-item-quantity");
		request.put("cartID", testCart.cartID);
		request.put("itemID", testItem.itemID);
		request.put("quantity", 50);
		responseString = api.handleRequest(request.toJSONString());
		assertEquals(responseString, "{\"error\":\"Not enough stock to fulfill quantity requested\"}");
		
		request = new JSONObject();
		request.put("type", "modify-item-quantity");
		request.put("cartID", testCart.cartID);
		request.put("itemID", testItem.itemID);
		request.put("quantity", 5);
		responseString = api.handleRequest(request.toJSONString());
		Cart responseCart = new Cart(responseString);
		
		assertEquals(responseCart.items.size(), 1);
		assertEquals(5, responseCart.getItemFromCartByID(testItem.itemID).quantity);
		
		request = new JSONObject();
		request.put("type", "modify-item-quantity");
		request.put("cartID", testCart.cartID);
		request.put("itemID", testItem.itemID);
		request.put("quantity", 0);
		responseString = api.handleRequest(request.toJSONString());
		responseCart = new Cart(responseString);
		
		assertEquals(responseCart.items.size(), 0);
		
		request = new JSONObject();
		request.put("type", "modify-item-quantity");
		request.put("cartID", testCart2.cartID);
		request.put("itemID", testItem.itemID);
		request.put("quantity", 5);
		responseString = api.handleRequest(request.toJSONString());
		assertEquals(responseString, "{\"error\":\"Item not in cart\"}");
		
		testItem.quantity = 0; //manually set to out of stock
		
		request = new JSONObject();
		request.put("type", "modify-item-quantity");
		request.put("cartID", testCart2.cartID);
		request.put("itemID", testItem.itemID);
		request.put("quantity", 5);
		responseString = api.handleRequest(request.toJSONString());
		assertEquals(responseString, "{\"error\":\"Item 'Pocky Sticks' is out of stock\"}");
	}
}
