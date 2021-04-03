package de.Jodu555.NettyBackend.NettyBackend.objects;

import de.Jodu555.NettyBackend.NettyBackend.abstracts.AbstractRequest;
import de.Jodu555.NettyBackend.NettyBackend.enums.ResponseType;

public class NettyEndpoint {
	
	private AbstractRequest request;
	private ResponseType responseType;
	private Integer authorizationLevel;
	
	public NettyEndpoint(AbstractRequest request, ResponseType responseType, Integer authorizationLevel) {
		super();
		this.request = request;
		this.responseType = responseType;
		this.authorizationLevel = authorizationLevel;
	}
	
	public ResponseType getResponseType() {
		return responseType;
	}
	
	public Integer getAuthorizationLevel() {
		return authorizationLevel;
	}
	
	public AbstractRequest getRequest() {
		return request;
	}
	
}
