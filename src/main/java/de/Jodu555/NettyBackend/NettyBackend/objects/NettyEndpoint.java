package de.Jodu555.NettyBackend.NettyBackend.objects;

import de.Jodu555.NettyBackend.NettyBackend.abstracts.AbstractRequest;
import de.Jodu555.NettyBackend.NettyBackend.enums.RequestMehtod;
import de.Jodu555.NettyBackend.NettyBackend.enums.ResponseType;

public class NettyEndpoint {

	private String path;
	private RequestMehtod requestMehtod;
	private AbstractRequest request;
	private ResponseType responseType;
	private Integer authorizationLevel;

	public NettyEndpoint(String path, RequestMehtod requestMehtod, AbstractRequest request, ResponseType responseType,
			Integer authorizationLevel) {
		super();
		this.path = path;
		this.requestMehtod = requestMehtod;
		this.request = request;
		this.responseType = responseType;
		this.authorizationLevel = authorizationLevel;
	}

	@Override
	public String toString() {
		return "NettyEndpoint [path=" + path + ", requestMehtod=" + requestMehtod + ", responseType=" + responseType
				+ ", authorizationLevel=" + authorizationLevel + "]";
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public RequestMehtod getRequestMehtod() {
		return requestMehtod;
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
