package api;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ShoppingCartApi {
	ArrayList<Cart> carts;
	ArrayList<Discount> discounts; //Initialize these in the test cases
	ArrayList<Item> items;
	public ShoppingCartApi() {
		carts = new ArrayList<Cart>();
		discounts = new ArrayList<Discount>();
		items = new ArrayList<Item>();
	}
	@SuppressWarnings("unchecked")
	public String writeError(String message) {
		JSONObject response = new JSONObject();
		response.put("error", message);
		return response.toJSONString();
	}
	@SuppressWarnings("unchecked")
	public String writeSuccess(String message) {
		JSONObject response = new JSONObject();
		response.put("success", message);
		return response.toJSONString();
	}
	public String handleRequest(String requestJSON) {
		JSONParser jsonParser = new JSONParser();
		try {
			JSONObject request = (JSONObject) jsonParser.parse(requestJSON);
			String requestType = (String) request.get("type");
			String cartID = (String) request.get("cartID");
			Cart requestedCart = getCartByID(cartID);
			if(requestedCart == null) {
				return writeError("Cart not found");
			}
			switch(requestType) {
				case "view-cart":
					return requestedCart.toJSONString();		
				case "add-item":
					String itemIDToAdd = (String) request.get("itemID");
					Item toAdd = getItemByID(itemIDToAdd);
					int quantityToAdd = Integer.parseInt(request.get("quantity").toString());
					if(toAdd == null) {
						return writeError("Item not found");
					}
					if(toAdd.quantity == 0) {
						return writeError("Item '"+ toAdd.name +"' is out of stock");
					}
					if(toAdd.quantity < quantityToAdd) {
						return writeError("Not enough stock to fulfill quantity requested");
					}
					requestedCart.addItemToCart(toAdd.takeFrom(quantityToAdd));
					return writeSuccess("OK");
				case "add-discount":
					break;
				case "modify-item-quantity":
					break;
				
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	public Cart getCartByID(String cartID) {
		for(Cart c : carts) {
			if(c.cartID.equals(cartID)) {
				return c;
			}
		}
		return null;
	}
	public Item getItemByID(String itemID) {
		for(Item i : items) {
			if(i.itemID.equals(itemID)) {
				return i;
			}
		}
		return null;
	}
	
}
