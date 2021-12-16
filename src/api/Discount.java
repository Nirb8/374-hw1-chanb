package api;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Discount extends JSONable{
	String discountCode;
	double percentOff;
	boolean isExpired;
	public Discount(String discountCode, double percentOff, boolean isExpired) {
		this.discountCode = discountCode;
		this.percentOff = percentOff;
		this.isExpired = false;
	}
	public Discount(String JSONString) {
		JSONParser jsonParser = new JSONParser();
		try {
			JSONObject discount = (JSONObject) jsonParser.parse(JSONString);
			this.discountCode = (String) discount.get("discountCode");
			this.percentOff = Double.parseDouble((String) discount.get("percentOff"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public double applyDiscountToTotal(double total) {
		double actualPercentOff = this.percentOff/100;
		return total - total*actualPercentOff;
	}
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject discount = new JSONObject();
		discount.put("discountCode", discountCode);
		discount.put("percentOff", String.valueOf(percentOff));
		return discount;
	}
}
