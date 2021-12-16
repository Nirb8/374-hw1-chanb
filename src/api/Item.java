package api;

import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Item extends JSONable {
	String itemID;
	String name;
	String desc;
	double price;
	String picture;
	int quantity;

	public Item(String itemID, String itemName, String itemDescription, double itemPrice, String itemPicture,
			int quantity) {
		this.itemID = itemID;
		this.name = itemName;
		this.desc = itemDescription;
		this.price = itemPrice;
		this.picture = itemPicture;
		this.quantity = quantity;
	}

	public Item(String itemName, String itemDescription, double itemPrice, String itemPicture, int quantity) {
		this.itemID = UUID.randomUUID().toString();
		this.name = itemName;
		this.desc = itemDescription;
		this.price = itemPrice;
		this.picture = itemPicture;
		this.quantity = quantity;
	}

	public Item(String JSONString) {
		JSONParser jsonParser = new JSONParser();
		try {
			JSONObject item = (JSONObject) jsonParser.parse(JSONString);
			this.itemID = (String) item.get("itemID");
			this.name = (String) item.get("itemName");
			this.desc = (String) item.get("itemDescription");
			this.price = Double.parseDouble((String) item.get("itemPrice"));
			this.picture = (String) item.get("itemPicture");
			this.quantity = Integer.parseInt((String) item.get("itemQuantity"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject item = new JSONObject();

		item.put("itemID", itemID);
		item.put("itemName", name);
		item.put("itemDescription", desc);
		item.put("itemPrice", String.valueOf(price));
		item.put("itemPicture", picture);
		item.put("itemQuantity", String.valueOf(quantity));

		return item;
	}

	public Item takeFrom(int quantity) {
		if (quantity > this.quantity) {
			return null;
		}
		Item newItem = new Item(this.itemID, this.name, this.desc, this.price, this.picture, quantity);
		return newItem;
	}

	public void mergeItem(Item toMerge) {

		if(this.itemID.equals(toMerge.itemID)) {
			return;
		}
		int quantityToMerge = toMerge.quantity;
		this.quantity += quantityToMerge;
		
	}
}
