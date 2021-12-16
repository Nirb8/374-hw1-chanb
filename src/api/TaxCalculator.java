package api;

public class TaxCalculator {
	//this is tested by the test for calculateEstimatedTaxes(), the logic is just here in case the tax algorithm changes
	public static double calculateTaxAmount(String stateCode, double total) {
		if(stateCode.length() != 2) {
			return 0.0; //not a valid state
		}
		int first = stateCode.charAt(0);
		int second = stateCode.charAt(1);
		double tax = (double) Math.abs(first-second) + 3;
		double taxPercent = tax/100;
		
		return (double) Math.round((total*taxPercent) * 100) / 100;
	}
}
