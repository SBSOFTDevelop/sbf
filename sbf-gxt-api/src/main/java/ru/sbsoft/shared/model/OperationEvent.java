package ru.sbsoft.shared.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author panarin
 */
public class OperationEvent implements Serializable {

	private BigDecimal eventId;
	private String message;
	private OperationEventType type;
	private Date createDate;
	private String trace;

	public OperationEvent() {
		this("");
	}

	public OperationEvent(final String message) {
		this.message = message;
		this.eventId = BigDecimal.ZERO;
		this.type = OperationEventType.INFO;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public OperationEventType getType() {
		return type;
	}

	public void setType(final OperationEventType type) {
		this.type = type;
	}

	public BigDecimal getEventId() {
		return eventId;
	}

	public void setEventId(final BigDecimal eventId) {
		this.eventId = eventId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getTrace() {
		return trace;
	}

	public void setTrace(String trace) {
		this.trace = trace;
	}

    @Override
    public String toString() {
        return "'" + message + "' {eventId=" + eventId + ", type=" + type + ", createDate=" + createDate + ", trace=" + trace + '}';
    }
    
    
}
