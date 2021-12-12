import java.util.UUID;

public class Item {
	String itemID;
	String itemName;
	String itemDescription;
	double itemPrice;
	String itemPicture;
	int quantity;
	public Item(String itemName, String itemDescription, double itemPrice, String itemPicture, int quantity) {
		this.itemID = UUID.randomUUID().toString();
		this.itemName = itemName;
		this.itemDescription = itemDescription;
		this.itemPrice = itemPrice;
		this.itemPicture = itemPicture;
		this.quantity = quantity;
	}
}
