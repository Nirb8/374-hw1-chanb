import java.util.ArrayList;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;


public class Cart {
	String cartID;
	ArrayList<Item> items;
	String userID;
	String stateCode;
	public Cart(String userID, String stateCode) {
		this.cartID = UUID.randomUUID().toString();
		this.items = new ArrayList<Item>();
		this.userID = userID;
		this.stateCode = stateCode;	
	}
	/**
	 * Adds a specified Item to the Cart   
	 */
	public void addItemToCart(Item item) {
		items.add(item);
	}
	@SuppressWarnings("unchecked")
	public String toJSONString() {
		JSONObject cart = new JSONObject();
		
		cart.put("cartID", cartID);
		cart.put("userID", userID);
		
		JSONArray itemsJSON = new JSONArray();
		for(Item i : this.items) {
			itemsJSON.add(i.toJSONObject());
		}
		cart.put("items", itemsJSON);
		
		return cart.toJSONString();
	}
	
}
