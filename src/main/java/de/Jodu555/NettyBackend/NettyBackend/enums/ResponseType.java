package de.Jodu555.NettyBackend.NettyBackend.enums;

public enum ResponseType {
	
	JSON("application/json"),
	HTML("text/html");
	
	
	private String contentType;
	private ResponseType(String contentType) {
		this.contentType = contentType;
	}
	
	public String getContentType() {
		return contentType;
	}
	
}
