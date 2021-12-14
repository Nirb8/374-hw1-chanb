import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;

public class Cart extends JSONable{
	String cartID;
	ArrayList<Item> items;
	ArrayList<Discount> discounts;
	String userID;
	String stateCode;

	public Cart(String userID, String stateCode) {
		this.cartID = UUID.randomUUID().toString();
		this.items = new ArrayList<Item>();
		this.discounts = new ArrayList<Discount>();
		this.userID = userID;
		this.stateCode = stateCode;
	}
	public Cart(String JSONString) {
		JSONParser jsonParser = new JSONParser();
		try {
			JSONObject cart = (JSONObject) jsonParser.parse(JSONString);
			JSONArray items = (JSONArray) cart.get("items");
			Iterator<JSONObject> itemIterator = items.iterator();
			while(itemIterator.hasNext()) {
				//do all the items and chonk them in an arraylist
			}
			
			JSONArray discounts = (JSONArray) cart.get("discounts");
			Iterator<JSONObject> discountIterator = discounts.iterator();
			while(discountIterator.hasNext()) {
				//do all the discounts and chonk them in an arraylist
			}
			
			//TODO: handle the rest of the cart specific fields
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Adds a specified Item to the Cart
	 */
	public void addItemToCart(Item item) {
		items.add(item);
	}
	public void addDiscountToCart(Discount discount) {
		discounts.add(discount);
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject cart = new JSONObject();

		cart.put("cartID", cartID);
		cart.put("userID", userID);
		cart.put("stateCode", stateCode);
		cart.put("subtotal", calculateSubtotal());
		cart.put("total", calculateTotal());

		JSONArray itemsJSON = new JSONArray();
		for (Item i : this.items) {
			itemsJSON.add(i.toJSONObject());
		}
		cart.put("items", itemsJSON);
		JSONArray discountsJSON = new JSONArray();
		for (Discount d : this.discounts) {
			discountsJSON.add(d.toJSONObject());
		}
		cart.put("discounts", discountsJSON);

		return cart;
	}

	public double calculateSubtotal() {
		double total = 0.0;
		for (Item i : this.items) {
			total += i.price * (double) i.quantity;
		}
		return total;
	}

	public double calculateTotal() {
		double currentTotal = calculateSubtotal();
		for(Discount d : discounts) {
			currentTotal = d.applyDiscountToTotal(currentTotal);
		}
		return currentTotal;
	}

}
