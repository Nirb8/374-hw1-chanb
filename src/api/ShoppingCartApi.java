package api;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ShoppingCartApi {
	ArrayList<Cart> carts;
	ArrayList<Discount> discounts; // Initialize these in the test cases
	ArrayList<Item> items;
	HashMap<String, Integer> userDiscountLockout;

	public ShoppingCartApi() {
		carts = new ArrayList<Cart>();
		discounts = new ArrayList<Discount>();
		items = new ArrayList<Item>();
		userDiscountLockout = new HashMap<String, Integer>();
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
			if (requestedCart == null) {
				return writeError("Cart not found");
			}
			switch (requestType) {
			case "view-cart":
				return requestedCart.toJSONString();
			case "add-item":
				String itemIDToAdd = (String) request.get("itemID");
				Item toAdd = getItemByID(itemIDToAdd);
				int quantityToAdd = Integer.parseInt(request.get("quantity").toString());
				if(requestedCart.getItemFromCartByID(itemIDToAdd) != null) {
					return writeError("Item already in cart");
				}
				if (toAdd == null) {
					return writeError("Item not found");
				}
				if (toAdd.quantity == 0) {
					return writeError("Item '" + toAdd.name + "' is out of stock");
				}
				if (toAdd.quantity < quantityToAdd) {
					return writeError("Not enough stock to fulfill quantity requested");
				}
				requestedCart.addItemToCart(toAdd.takeFrom(quantityToAdd));
				return writeSuccess("OK");
			case "apply-discount":
				String discountCodeToAdd = (String) request.get("discountCode");
				Discount toApply = this.getDiscountByCode(discountCodeToAdd);
				String requestedUserID = requestedCart.userID;
				if (requestedUserID != null && userDiscountLockout.containsKey(requestedUserID)
						&& userDiscountLockout.get(requestedUserID) >= 5) {
					return writeError("");
				}
				if (toApply != null && toApply.isExpired) {
					if (requestedUserID != null) {
						doUserLockoutIncrement(requestedUserID);
					}
					return writeError("That discount has expired");
				}
				if (toApply == null) {
					// discount code failed or user is locked out
					if (requestedUserID != null) {
						doUserLockoutIncrement(requestedUserID);
					}
					return writeError("Invalid discount code");
				} else {
					requestedCart.addDiscountToCart(toApply);
					return writeSuccess("OK");
				}
			case "modify-item-quantity":
				String itemIDToModify = (String) request.get("itemID");
				int quantityToModifyTo = Integer.parseInt(request.get("quantity").toString());
				Item toMod = getItemByID(itemIDToModify);
				if (toMod == null) {
					return writeError("Item not found");
				}
				if (toMod.quantity == 0) {
					return writeError("Item '" + toMod.name + "' is out of stock");
				}
				Item existingItem = requestedCart.getItemFromCartByID(itemIDToModify);
				if (existingItem == null) {
					return writeError("Item not in cart");
				}
				if(quantityToModifyTo == 0) {
					requestedCart.items.remove(existingItem);
					return requestedCart.toJSONString();
				}
				
				int amountToTake = quantityToModifyTo - existingItem.quantity;
				Item taken = toMod.takeFrom(amountToTake);
				if (taken == null) {
					return writeError("Not enough stock to fulfill quantity requested");
				}
				existingItem.mergeItem(taken);
				return requestedCart.toJSONString();

			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	public Cart getCartByID(String cartID) {
		for (Cart c : carts) {
			if (c.cartID.equals(cartID)) {
				return c;
			}
		}
		return null;
	}

	public Item getItemByID(String itemID) {
		for (Item i : items) {
			if (i.itemID.equals(itemID)) {
				return i;
			}
		}
		return null;
	}

	public Discount getDiscountByCode(String discountCode) {
		for (Discount d : discounts) {
			if (d.discountCode.equals(discountCode)) {
				return d;
			}
		}
		return null;
	};

	public void doUserLockoutIncrement(String userID) {
		if (userDiscountLockout.containsKey(userID)) {
			int current = userDiscountLockout.get(userID);
			current++;
			userDiscountLockout.put(userID, current);
		} else {
			userDiscountLockout.put(userID, 1);
		}
	}
}
