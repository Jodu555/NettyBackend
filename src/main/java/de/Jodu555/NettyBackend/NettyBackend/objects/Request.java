package de.Jodu555.NettyBackend.NettyBackend.objects;

import java.util.HashMap;
import java.util.List;

public class Request {
	
	private String ip;
	private String uri;
	private String authId;
	private HashMap<String, List<String>> parameters;
	
	public Request(String ip, String uri, HashMap<String, List<String>> parameters) {
		this(ip, uri, null, parameters);
	}
	
	public Request(String ip, String uri, String authId, HashMap<String, List<String>> parameters) {
		this.ip = ip;
		this.uri = uri;
		this.authId = authId;
		this.parameters = parameters;
	}
	
	public String getIp() {
		return ip;
	}
	
	public String getUri() {
		return uri;
	}
	
	public String getAuthId() {
		return authId;
	}
	
	public void setAuthId(String authId) {
		this.authId = authId;
	}
	
	public HashMap<String, List<String>> getParameters() {
		return parameters;
	}
	
}
