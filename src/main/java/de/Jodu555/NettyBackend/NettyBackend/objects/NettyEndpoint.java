package de.Jodu555.NettyBackend.NettyBackend.objects;

import de.Jodu555.NettyBackend.NettyBackend.abstracts.AbstractRequest;

public class NettyEndpoint {
	
	private AbstractRequest request;
	private Integer authorizationLevel;
	
	public NettyEndpoint(AbstractRequest request, Integer authorizationLevel) {
		super();
		this.request = request;
		this.authorizationLevel = authorizationLevel;
	}
	
	public Integer getAuthorizationLevel() {
		return authorizationLevel;
	}
	
	public AbstractRequest getRequest() {
		return request;
	}
	
}
