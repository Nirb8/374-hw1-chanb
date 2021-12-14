import org.json.simple.JSONObject;

public class Discount extends JSONable{
	String discountCode;
	double percentOff;
	public Discount(String discountCode, double percentOff) {
		this.discountCode = discountCode;
		this.percentOff = percentOff;
	}
	public double applyDiscountToTotal(double total) {
		double actualPercentOff = this.percentOff/100;
		return total*actualPercentOff;
	}
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject discount = new JSONObject();
		discount.put("discountCode", discountCode);
		discount.put("percentOff", String.valueOf(percentOff));
		return discount;
	}
}
