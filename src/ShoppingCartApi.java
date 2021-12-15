import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ShoppingCartApi {
//	public static void main(String args[]) {
//		
//	}
	ArrayList<Cart> carts;
	ArrayList<Discount> discounts; //Initialize these in the test cases
	public ShoppingCartApi() {
		carts = new ArrayList<Cart>();
	}
	public String handleRequest(String requestJSON) {
		JSONParser jsonParser = new JSONParser();
		try {
			JSONObject request = (JSONObject) jsonParser.parse(requestJSON);
			String requestType = (String) request.get("type");
			switch(requestType) {
				case "view":
					
					break;
				
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
}
