package com.project.one.model;


import java.io.IOException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.project.one.Money;

/**
 * Custom jackson serializer for {@code ReimbDetailed} objects.
 * 
 * @author Nicholas Smith
 */
class ReimbDetailedSerializer extends StdSerializer<ReimbDetailed> {

	private static final long serialVersionUID = 6560552861570124723L;

	public ReimbDetailedSerializer() { this(null); }
    public ReimbDetailedSerializer(Class<ReimbDetailed> t) { super(t); }

	public void serialize(ReimbDetailed value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		Money       amount            = value.getAmount();
		Timestamp   submitted         = value.getSubmitted();
		Timestamp   resolved          = value.getResolved();
		String      desc              = value.getDesc();
		byte[]      receipt           = value.getReceipt();
		String      authorFirstName   = value.getAuthorFirstName();
		String      authorLastName    = value.getAuthorLastName();
		Integer     resolver          = value.getResolver();
		String      resolverFirstName = value.getResolverFirstName();
		String      resolverLastName  = value.getResolverLastName();
		ReimbStatus status            = value.getStatus();
		ReimbType   type              = value.getType();
		
		gen.writeStartObject();
		gen.writeNumberField("id", value.getId());
		gen.writeStringField("amount",    (amount == null)    ? null : amount.toString());
		gen.writeStringField("submitted", (submitted == null) ? null : submitted.toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE));
		gen.writeStringField("resolved",  (resolved == null)  ? null : resolved.toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE));
		gen.writeStringField("desc",      (desc == null)      ? null : desc);
		gen.writeBinaryField("receipt",   (receipt == null)   ? new byte[] {} : receipt);
		gen.writeNumberField("author", value.getAuthor());
		gen.writeStringField("author_first_name",   (authorFirstName == null)   ? null : authorFirstName);
		gen.writeStringField("author_last_name",    (authorLastName == null)    ? null : authorLastName);
		gen.writeNumberField("resolver",            (resolver == null)          ? null : resolver);
		gen.writeStringField("resolver_first_name", (resolverFirstName == null) ? null : resolverFirstName);
		gen.writeStringField("resolver_last_name",  (resolverLastName == null)  ? null : resolverLastName);
		gen.writeStringField("status",              (status == null)            ? null : status.toString());
		gen.writeStringField("type",                (type == null)              ? null : type.toString());
		gen.writeEndObject();
	}
}

/**
 * Model for records held in the reimb_detailed view.
 * 
 * @author Nicholas Smith
 */
@JsonSerialize(using = ReimbDetailedSerializer.class)
public class ReimbDetailed extends Reimbursement {
	private String authorFirstName;
	private String authorLastName;
	private String resolverFirstName;
	private String resolverLastName;
	
	public ReimbDetailed() {
		super();
	}
	
	public ReimbDetailed(int id, Money amount, Timestamp submitted, Timestamp resolved, String desc, byte[] receipt,
			int author, String authorFirstName, String authorLastName, Integer resolver, String resolverFirstName,
			String resolverLastName, ReimbStatus status, ReimbType type) {
		super(id, amount, submitted, resolved, desc, receipt, author, resolver, status, type);
		this.authorFirstName = authorFirstName;
		this.authorLastName = authorLastName;
		this.resolverFirstName = resolverFirstName;
		this.resolverLastName = resolverLastName;
	}

	public String getAuthorFirstName() {
		return authorFirstName;
	}

	public void setAuthorFirstName(String authorFirstName) {
		this.authorFirstName = authorFirstName;
	}

	public String getAuthorLastName() {
		return authorLastName;
	}

	public void setAuthorLastName(String authorLastName) {
		this.authorLastName = authorLastName;
	}

	public String getResolverFirstName() {
		return resolverFirstName;
	}

	public void setResolverFirstName(String resolverFirstName) {
		this.resolverFirstName = resolverFirstName;
	}

	public String getResolverLastName() {
		return resolverLastName;
	}

	public void setResolverLastName(String resolverLastName) {
		this.resolverLastName = resolverLastName;
	}

	@Override
	public String toString() {
		return "ReimbDetailed [authorFirstName=" + authorFirstName + ", authorLastName=" + authorLastName
				+ ", resolverFirstName=" + resolverFirstName + ", resolverLastName=" + resolverLastName + ", getId()="
				+ getId() + ", getAmount()=" + getAmount() + ", getSubmitted()=" + getSubmitted() + ", getResolved()="
				+ getResolved() + ", getDesc()=" + getDesc() + ", getReceipt()=" + Arrays.toString(getReceipt())
				+ ", getAuthor()=" + getAuthor() + ", getResolver()=" + getResolver() + ", getStatus()=" + getStatus()
				+ ", getType()=" + getType() + "]";
	}
	
}
