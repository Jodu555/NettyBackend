package de.Jodu555.NettyBackend.NettyBackend.objects;

import java.util.ArrayList;

import de.Jodu555.NettyBackend.NettyBackend.abstracts.AbstractRequest;
import de.Jodu555.NettyBackend.NettyBackend.enums.RequestMehtod;
import de.Jodu555.NettyBackend.NettyBackend.enums.ResponseType;

public class Router {

	private NettyBackend backend;
	private String defaultEnpoint = "";
	private ArrayList<NettyEndpoint> endpoints;
	private ResponseType responseType;

	public Router(NettyBackend backend, String defaultEnpoint) {
		this(backend, defaultEnpoint, ResponseType.JSON);
	}
	
	public Router(NettyBackend backend, String defaultEnpoint, ResponseType responseType) {
		super();
		this.backend = backend;
		this.defaultEnpoint = defaultEnpoint;
		this.responseType = responseType;
		this.endpoints = new ArrayList<NettyEndpoint>();
	}
	
	public void registerEndpoint(String endpoint, AbstractRequest request) {
		abstractRegisterEndpoint(endpoint, new NettyEndpoint(endpoint, RequestMehtod.GET, request, getResponseType(), 0));
	}

	public void registerEndpoint(RequestMehtod requestMehtod, String endpoint, AbstractRequest request) {
		abstractRegisterEndpoint(endpoint, new NettyEndpoint(endpoint, requestMehtod, request, getResponseType(), 0));
	}
	
	public void registerEndpoint(RequestMehtod requestMehtod, String endpoint, AbstractRequest request, ResponseType responseType) {
		abstractRegisterEndpoint(endpoint, new NettyEndpoint(endpoint, requestMehtod, request, responseType, 0));
	}
	
	public void registerEndpoint(String endpoint, AbstractRequest request, ResponseType responseType) {
		abstractRegisterEndpoint(endpoint, new NettyEndpoint(endpoint, RequestMehtod.GET, request, responseType, 0));
	}
	
	public void registerAuthEndpoint(RequestMehtod requestMehtod, String endpoint, AbstractRequest request, int authorizationLevel) {
		abstractRegisterEndpoint(endpoint, new NettyEndpoint(endpoint, requestMehtod, request, getResponseType(), authorizationLevel));
	}
	
	public void registerAuthEndpoint(RequestMehtod requestMehtod, String endpoint, AbstractRequest request, ResponseType responseType, int authorizationLevel) {
		abstractRegisterEndpoint(endpoint, new NettyEndpoint(endpoint, requestMehtod, request, responseType, authorizationLevel));
	}

	
	private void abstractRegisterEndpoint(String endpoint, NettyEndpoint nettyEndpoint) {
		nettyEndpoint.setPath(getDefaultEnpoint() + nettyEndpoint.getPath());
		this.endpoints.add(nettyEndpoint);
		System.out.println("Registered new Endpoint: " + nettyEndpoint.getPath());
	}
	
	public ArrayList<NettyEndpoint> getEndpoints() {
		return endpoints;
	}
	
	public NettyBackend getBackend() {
		return backend;
	}

	public String getDefaultEnpoint() {
		return defaultEnpoint;
	}
	
	public ResponseType getResponseType() {
		return responseType;
	}
	
	public void setResponseType(ResponseType responseType) {
		this.responseType = responseType;
	}

}
