package api;
import static org.junit.Assert.*;

import java.util.UUID;

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
			// TODO Auto-generated catch block
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
		
		assertEquals(testItem.quantity, 0);
		assertTrue(testCart.checkIfItemIsInCartById(testItem.itemID));
		assertTrue(testCart.items.get(0).quantity == 5);
		
		
		request = new JSONObject();
		request.put("type", "add-item");
		request.put("cartID", testCart2.cartID);
		request.put("itemID", testItem.itemID);
		request.put("quantity", 5);
		responseString = api.handleRequest(request.toJSONString());
		assertEquals(responseString, "{\"error\":\"Item 'Pocky Sticks' is out of stock\"}");
	}
	
}
