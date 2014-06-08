package com.v5;

public class Message implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -414609116706129196L;
	private MessageType type;
	private Object content;
	
	public Message(MessageType type, Object content) {
		super();
		this.type = type;
		this.content = content;
	}
	public MessageType getType() {
		return type;
	}
	public void setType(MessageType type) {
		this.type = type;
	}
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	
	
}
