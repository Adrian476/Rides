package domain;

public class Price {
	
	private float originalPrice;
	private float newPrice;
	
	public Price(float originalPrice, float percentage) {
		this.originalPrice = originalPrice;
		this.newPrice = originalPrice - originalPrice * (percentage / 100);
	}
	
	public float getOriginalPrice() {
		return originalPrice;
	}
	
	public float getNewPrice() {
		return newPrice;
	}

	public void updateDiscount(float percentage) {
		newPrice = originalPrice - (originalPrice * (percentage/100));
	}	
}
