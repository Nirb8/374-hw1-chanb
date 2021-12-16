package api;

import java.util.UUID;

public class TestEnvironmentGenerator {
	public static Cart createGuestCart() {
		Cart testCart = new Cart(null, "IN");
		return testCart;
	}

	public static Cart createUserCart() {
		String uuid = "a6780619-c454-47ec-87b9-4a204f5072fc";
		Cart testCart = new Cart(uuid, "IN");
		return testCart;
	}

	public static Cart createRandomUserCart() {
		String uuid = UUID.randomUUID().toString();
		Cart randomUserCart = new Cart(uuid, "IL");
		return randomUserCart;
	}

	public static Item createItemOne() {
		String itemID = "3714a497-71e8-47f6-9667-c3df2d648656";
		String itemName = "Pocky Sticks";
		String itemDescription = "Delicious Creamy Chocolate Covered Biscuit Sticks";
		double itemPrice = 8.99;
		String itemPicture = "https://target.scene7.com/is/image/Target/GUEST_d0eb439c-3f20-4eb7-be83-471c10b67778?wid=488&hei=488&fmt=pjpeg";
		int itemQuantity = 5;
		Item testItem = new Item(itemID, itemName, itemDescription, itemPrice, itemPicture, itemQuantity);

		return testItem;
	}

	public static Item createItemTwo() {
		String itemID = "d67685b8-15de-4c91-b915-f3be1eb46ff6";
		String itemName = "Beef Jerky";
		String itemDescription = "Meat snack exquisitely seasoned to perfection";
		double itemPrice = 11.99;
		String itemPicture = "https://itsjerky.com/wp-content/uploads/2017/12/jerky-on-wooden-cutting-board.png";
		int itemQuantity = 5;
		Item testItem = new Item(itemID, itemName, itemDescription, itemPrice, itemPicture, itemQuantity);

		return testItem;
	}

	public static Discount createDiscountOne() {
		String discountCode = "5318008";
		double percentOff = 50;
		boolean isExpired = false;
		Discount testDiscount = new Discount(discountCode, percentOff, isExpired);
		return testDiscount;
	}

	public static ShoppingCartApi createApi() {
		ShoppingCartApi api = new ShoppingCartApi();
		api.carts.add(createUserCart());
		for (int i = 0; i < 8; i++) {
			api.carts.add(createRandomUserCart());
		}
		api.discounts.add(createDiscountOne());
		return api;
	}
}
