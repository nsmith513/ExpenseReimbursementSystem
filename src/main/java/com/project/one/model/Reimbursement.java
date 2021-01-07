package com.project.one.model;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.project.one.Money;

/**
 * Custom jackson serializer for {@code Reimbursement} objects.
 * 
 * @author Nicholas Smith
 */
class ReimbursementSerializer extends StdSerializer<Reimbursement> {

	private static final long serialVersionUID = 6560552861570124723L;

	public ReimbursementSerializer() { this(null); }
    public ReimbursementSerializer(Class<Reimbursement> t) { super(t); }

	public void serialize(Reimbursement value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		Money       amount            = value.getAmount();
		Timestamp   submitted         = value.getSubmitted();
		Timestamp   resolved          = value.getResolved();
		String      desc              = value.getDesc();
		byte[]      receipt           = value.getReceipt();
		Integer     resolver          = value.getResolver();
		ReimbStatus status            = value.getStatus();
		ReimbType   type              = value.getType();
		
		gen.writeStartObject();
		gen.writeNumberField("id",        value.getId());
		gen.writeStringField("amount",    (amount == null)    ? null : amount.toString());
		gen.writeStringField("submitted", (submitted == null) ? null : submitted.toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE));
		gen.writeStringField("resolved",  (resolved == null)  ? null : resolved.toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE));
		gen.writeStringField("desc",      (desc == null)      ? null : desc);
		gen.writeBinaryField("receipt",   (receipt == null)   ? new byte[] {} : receipt);
		gen.writeNumberField("author",    value.getAuthor());
		gen.writeNumberField("resolver",  (resolver == null)          ? null : resolver);
		gen.writeStringField("status",    (status == null)            ? null : status.toString());
		gen.writeStringField("type",      (type == null)              ? null : type.toString());
		gen.writeEndObject();
	}
}

/**
 * Model for records held in the reimbursement table.
 * 
 * @author Nicholas Smith
 */
@JsonSerialize(using = ReimbursementSerializer.class)
public class Reimbursement implements Comparable<Reimbursement> {
	private int id;
	private Money amount; // Money will always be represented in cents with a long
	private Timestamp submitted;
	private Timestamp resolved;
	private String desc;
	private byte[] receipt;
	private int author;
	private Integer resolver; // Using the wrapper class so it can be null
	private ReimbStatus status;
	private ReimbType type;
	
	public Reimbursement() {}
	
	public Reimbursement(int id, Money amount, Timestamp submitted, Timestamp resolved, String desc, byte[] receipt,
			int author, Integer resolver, ReimbStatus status, ReimbType type) {
		super();
		this.id = id;
		this.amount = amount;
		this.submitted = submitted;
		this.resolved = resolved;
		this.desc = desc;
		this.receipt = receipt;
		this.author = author;
		this.resolver = resolver;
		this.status = status;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Money getAmount() {
		return amount;
	}

	public void setAmount(Money amount) {
		this.amount = amount;
	}

	public Timestamp getSubmitted() {
		return submitted;
	}

	public void setSubmitted(Timestamp submitted) {
		this.submitted = submitted;
	}

	public Timestamp getResolved() {
		return resolved;
	}

	public void setResolved(Timestamp resolved) {
		this.resolved = resolved;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public byte[] getReceipt() {
		return receipt;
	}

	public void setReceipt(byte[] receipt) {
		this.receipt = receipt;
	}

	public int getAuthor() {
		return author;
	}

	public void setAuthor(int author) {
		this.author = author;
	}

	public Integer getResolver() {
		return resolver;
	}

	public void setResolver(Integer resolver) {
		this.resolver = resolver;
	}

	public ReimbStatus getStatus() {
		return status;
	}

	public void setStatus(ReimbStatus status) {
		this.status = status;
	}

	public ReimbType getType() {
		return type;
	}

	public void setType(ReimbType type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Reimbursement other = (Reimbursement) obj;
		if (id != other.id)
			return false;
		return true;
	}

    @Override
    public int compareTo(Reimbursement that){
        return this.id - that.id;
    }
    
    /**
     * {@code Comparator} for sorting by reimbursement submission date.
     */
	public static final Comparator<Reimbursement> sortBySubmitted =
			(Reimbursement o1, Reimbursement o2) -> o1.submitted.compareTo(o2.submitted);
	
	@Override
	public String toString() {
		return "Reimbursement [id=" + id + ", amount=" + amount + ", submitted=" + submitted + ", resolved=" + resolved
				+ ", desc=" + desc + ", receipt=" + Arrays.toString(receipt) + ", author=" + author + ", resolver="
				+ resolver + ", status=" + status + ", type=" + type + "]";
	}
	
}
