package de.Jodu555.NettyBackend.NettyBackend.objects;

import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;

import com.google.gson.JsonObject;

import de.Jodu555.NettyBackend.NettyBackend.enums.RequestMehtod;

public class Request {
	
	private String ip;
	private String uri;
	private String authId;
	private RequestMehtod requestMehtod;
	private HashMap<String, List<String>> parameters;
	private HashMap<String, String> variables;
	private HashMap<String, String> headers;
	private JSONObject body;
	
	public Request(RequestMehtod requestMehtod, String ip, String uri, HashMap<String, List<String>> parameters, HashMap<String, String> headers) {
		this(requestMehtod, ip, uri, null, parameters, headers);
	}
	
	public Request(RequestMehtod requestMehtod, String ip, String uri, String authId, HashMap<String, List<String>> parameters, HashMap<String, String> headers) {
		this.requestMehtod = requestMehtod;
		this.ip = ip;
		this.uri = uri;
		this.authId = authId;
		this.parameters = parameters;
		this.headers = headers;
		this.variables =  new HashMap<String, String>();
		this.body = null;
	}
	
	public RequestMehtod getRequestMehtod() {
		return requestMehtod;
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
	
	public HashMap<String, String> getVariables() {
		return variables;
	}
	
	public HashMap<String, String> getHeaders() {
		return headers;
	}
	
	public JSONObject getBody() {
		return body;
	}
	
	public void setBody(JSONObject body) {
		this.body = body;
	}
	
}
