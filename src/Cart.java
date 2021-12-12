import java.util.ArrayList;
import java.util.UUID;

public class Cart {
	String cartID;
	ArrayList<Item> items;
	String userID;
	String stateCode;
	public Cart(String stateCode) {
		this.cartID = UUID.randomUUID().toString();
		this.items = new ArrayList<Item>();
		this.userID = null;
		this.stateCode = stateCode;		
	}
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
}
