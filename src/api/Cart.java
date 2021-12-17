package api;
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
		this.items = new ArrayList<Item>();
		this.discounts = new ArrayList<Discount>();
		JSONParser jsonParser = new JSONParser();
		try {
			JSONObject cart = (JSONObject) jsonParser.parse(JSONString);
			JSONArray items = (JSONArray) cart.get("items");
			Iterator<JSONObject> itemIterator = items.iterator();
			while(itemIterator.hasNext()) {
				//do all the items and put them in the arraylist
				Item currentItem = new Item(itemIterator.next().toJSONString());
				this.items.add(currentItem);
			}
			
			JSONArray discounts = (JSONArray) cart.get("discounts");
			Iterator<JSONObject> discountIterator = discounts.iterator();
			while(discountIterator.hasNext()) {
				//do all the discounts and put them in the arraylist
				Discount currentDiscount = new Discount(discountIterator.next().toJSONString());
				this.discounts.add(currentDiscount);
			}
			this.cartID = (String) cart.get("cartID");
			this.userID = (String) cart.get("userID");
			this.stateCode = (String) cart.get("stateCode");
			
		} catch (ParseException e) {
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
	public Item getItemFromCartByID(String itemID) {
		for(Item i : items) {
			if(i.itemID.equals(itemID)) {
				return i;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject cart = new JSONObject();

		cart.put("cartID", cartID);
		cart.put("userID", userID);
		cart.put("stateCode", stateCode);
		double subtotal = calculateSubtotal();
		double total = calculateTotal();
		double discountedAmount = calculateDiscountedAmount(subtotal);
		double estimatedTaxes = calculateEstimatedTaxes(subtotal - discountedAmount);
		cart.put("subtotal", calculateSubtotal());
		cart.put("total", total);
		cart.put("estimatedTaxes", estimatedTaxes);
		cart.put("amountSavedByDiscounts", discountedAmount);
		
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
		double total = calculateSubtotal();
		total = total - calculateDiscountedAmount(total);
		//apply tax after discounts
		total += calculateEstimatedTaxes(total);
		return (double) Math.round(total * 100) / 100;
	}
	
	public double calculateEstimatedTaxes(double total) {
		return TaxCalculator.calculateTaxAmount(stateCode, total);
	}
	
	public double calculateDiscountedAmount(double total) {
		double currentTotal = total;
		for(Discount d : discounts) {
			currentTotal = d.applyDiscountToTotal(currentTotal);
		}
		return total - currentTotal;
	}

}
