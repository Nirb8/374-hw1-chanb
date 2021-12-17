package api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

public class DiscountTests {
	@Test
	void testDiscountConversionToJSONFormatAndBack() {
		Discount testDiscount = TestEnvironmentGenerator.createDiscountOne();
		String json = testDiscount.toJSONString();
		Discount convertedBackDiscount = new Discount(json);
		assertFalse(convertedBackDiscount == null);
		assertEquals(testDiscount.discountCode, convertedBackDiscount.discountCode);
		assertTrue(testDiscount.percentOff == convertedBackDiscount.percentOff);
	}
	@Test
	void testApplyDiscountToTotal() {
		Discount testDiscount = TestEnvironmentGenerator.createDiscountOne();
		double appliedTotal = testDiscount.applyDiscountToTotal(50.0);
		assertTrue(25.0 == appliedTotal);
	}
}
