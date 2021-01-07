package com.project.one;

import java.util.regex.Pattern;

///**
// * Jackson serializer for {@code Money} objects.
// * 
// * @author Nicholas Smith
// */
//class MoneySerializer extends StdSerializer<Money> {
//
//	private static final long serialVersionUID = 3281707965008494396L;
//
//	public MoneySerializer() { this(null); }
//    public MoneySerializer(Class<Money> t) { super(t); }
//
//	@Override
//	public void serialize(Money value, JsonGenerator gen, SerializerProvider provider) throws IOException {
//		gen.writeStartObject();
//		gen.writeNumberField("cents", value.getAmount()); // This whole thing is so the json field will be named "cents" and not "amount"
//		gen.writeEndObject();
//	}
//}

/**
 * Class to manage a USD amount, stored in cents.
 * 
 * @author Nicholas Smith
 */
//@JsonSerialize(using = MoneySerializer.class)
public class Money {
	
	private static Pattern moneyPattern = null;
	
	private long cents;
	
	/**
	 * Instantiate a {@code Money} object having 0 cents.
	 */
	public Money() { this(0L); }
	/**
	 * Instantiate a {@code Money} object having {@code amount} cents.
	 * 
	 * @param amount - {@code long} representing the number of cents this {@code Money} object holds.
	 */
	public Money(long amount) { cents = amount; }
	/**
	 * Instantiate a {@code Money} object having {@code amount} dollars/cents.
	 * 
	 * @param amount - String representing a dollar amount this {@code Money} object holds.
	 */
	public Money(String amount) { setAmount(amount); }
	
	/**
	 * @return {@code long} representing the number of cents this {@code Money} object holds.
	 */
	public long getAmount() { return cents; }
	
	/**
	 * Sets the number of cents this {@code Money} object holds.
	 * 
	 * @param amount - {@code long} representing the number of cents this object will hold.
	 */
	public void setAmount(long amount) { cents = amount; }
	/**
	 * Sets the dollars/cents this {@code Money} object holds.
	 * 
	 * @param amount - String representing a dollar amount this object will hold.
	 */
	public void setAmount(String amount) {
		if (moneyPattern == null)
			moneyPattern = Pattern.compile("^([1-9]\\d*(\\.\\d\\d)?|0?\\.\\d\\d)$");
		if (!moneyPattern.matcher(amount).find())
			throw new IllegalArgumentException("Cannot parse \"" + amount + "\" as money.");
		cents = Long.parseLong((amount.indexOf('.') == -1) ? 
				amount + "00" :
				amount.replaceFirst("\\.", ""));
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (cents ^ (cents >>> 32));
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Money other = (Money) obj;
		if (cents != other.cents)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(String.format("%02d", cents));
		return sb.insert(sb.length() - 2, (sb.length() == 2) ? "0." : '.').toString();
	}
	
}
