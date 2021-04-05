package de.Jodu555.NettyBackend.NettyBackend.objects;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DebugGraphics;

import de.Jodu555.NettyBackend.NettyBackend.abstracts.AbstractRequest;
import de.Jodu555.NettyBackend.NettyBackend.abstracts.AbstractResponse;
import de.Jodu555.NettyBackend.NettyBackend.abstracts.AbstractRouter;
import de.Jodu555.NettyBackend.NettyBackend.abstracts.AuthenticationHandler;
import de.Jodu555.NettyBackend.NettyBackend.enums.RequestMehtod;
import de.Jodu555.NettyBackend.NettyBackend.enums.ResponseType;
import de.Jodu555.NettyBackend.NettyBackend.netty.NettyServer;
import de.Jodu555.NettyBackend.NettyBackend.utils.JsonUtils;

public class NettyBackend {

	// TODO: Request Limiting Only n requests per x unit

	private NettyServer server;

	private ArrayList<String> blockedAddresses = new ArrayList<String>();
	private ArrayList<String> whitelistedAddresses = new ArrayList<String>();

//	private HashMap<String, NettyEndpoint> endpoints = new HashMap<String, NettyEndpoint>();
	private ArrayList<NettyEndpoint> endpoints;
	private ArrayList<Router> routers;
	private String defaultEndpoint = "";
	private AuthenticationHandler authenticationHandler;
	private TokenManager tokenManager;

	private int port;
	private boolean blockAddresses;
	private boolean whitelistAddresses;
	private boolean logRequests;

	private ResponseType responseType = ResponseType.JSON;

	public NettyBackend(int port, boolean logRequests, boolean blockAddresses, boolean whitelistAddresses) {
		System.out.println("Initialized new NettyBackend on Port: " + port);
		this.port = port;
		this.logRequests = logRequests;
		this.blockAddresses = blockAddresses;
		this.whitelistAddresses = whitelistAddresses;
		routers = new ArrayList<Router>();
		endpoints = new ArrayList<NettyEndpoint>();

		this.tokenManager = new TokenManager();
	}

	public void start() {
		server = new NettyServer(this, this.port);
	}

	public void setAuthenticationHandler(AuthenticationHandler authenticationHandler) {
		if (getResponseType() != ResponseType.JSON) {
			System.out.println("Cant add Auth Handler without JSON response");
			return;
		}
		this.authenticationHandler = authenticationHandler;
		registerEndpoint(RequestMehtod.GET, "/auth", new AbstractRequest() {

			@Override
			public AbstractResponse onRequest(Request req, AbstractResponse response) {
				AuthenticationResponse authenticationResponse;
				try {
					authenticationResponse = getAuthenticationHandler().onAuthentication(req);
				} catch (Exception e) {
					e.printStackTrace();
					authenticationResponse = new AuthenticationResponse();
				}

				JsonResponse jsonResponse = (JsonResponse) response;

				if (authenticationResponse != null && authenticationResponse.isAuth()) {
					String token = getTokenManager().createToken(req, authenticationResponse.getId(),
							authenticationResponse.getAuthLevel());
					response.setSuccess(true);
					jsonResponse.getJsonUtils().add("message", "Successfully Authorized");
					jsonResponse.getJsonUtils().add("token", token);
				} else {
					response.setSuccess(false);
					jsonResponse.getJsonUtils().add("message", "Authentication Faild");
				}
				return response;
			}
		});

	}

	public void setDefaultEndpoint(String defaultEndpoint) {
		this.defaultEndpoint = defaultEndpoint;
	}

	public void registerRouter(String startEndpoint, Class<? extends AbstractRouter> cls) {
		registerRouter(startEndpoint, cls, getResponseType());
	}

	public void registerRouter(String startEndpoint, Class<? extends AbstractRouter> cls, ResponseType responseType) {
		try {
			AbstractRouter abstractRouter = (AbstractRouter) cls.newInstance();
			Router router = new Router(this, startEndpoint);
			System.out.println("Registered new Router: " + startEndpoint + " on class " + cls.getSimpleName());
			abstractRouter.registerEndpoint(router);
			this.routers.add(router);
			router.getEndpoints().forEach(endpoint -> {
				endpoints.add(endpoint);
			});
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Cannot cast Router");
		}
	}

	public void registerEndpoint(String endpoint, AbstractRequest request) {
		abstractRegisterEndpoint(endpoint,
				new NettyEndpoint(endpoint, RequestMehtod.GET, request, getResponseType(), 0));
	}

	public void registerEndpoint(RequestMehtod requestMehtod, String endpoint, AbstractRequest request) {
		abstractRegisterEndpoint(endpoint, new NettyEndpoint(endpoint, requestMehtod, request, getResponseType(), 0));
	}

	public void registerEndpoint(RequestMehtod requestMehtod, String endpoint, AbstractRequest request,
			ResponseType responseType) {
		abstractRegisterEndpoint(endpoint, new NettyEndpoint(endpoint, requestMehtod, request, responseType, 0));
	}

	public void registerEndpoint(String endpoint, AbstractRequest request, ResponseType responseType) {
		abstractRegisterEndpoint(endpoint, new NettyEndpoint(endpoint, RequestMehtod.GET, request, responseType, 0));
	}

	public void registerAuthEndpoint(RequestMehtod requestMehtod, String endpoint, AbstractRequest request,
			int authorizationLevel) {
		abstractRegisterEndpoint(endpoint,
				new NettyEndpoint(endpoint, requestMehtod, request, getResponseType(), authorizationLevel));
	}

	public void registerAuthEndpoint(RequestMehtod requestMehtod, String endpoint, AbstractRequest request,
			ResponseType responseType, int authorizationLevel) {
		abstractRegisterEndpoint(endpoint,
				new NettyEndpoint(endpoint, requestMehtod, request, responseType, authorizationLevel));
	}

	private void abstractRegisterEndpoint(String path, NettyEndpoint endpoint) {
		System.out.println("Registered new Endpoint: " + path);
		endpoints.add(endpoint);
	}

	public void setResponseType(ResponseType responseType) {
		this.responseType = responseType;
	}

	public ResponseType getResponseType() {
		return responseType;
	}

	public TokenManager getTokenManager() {
		return tokenManager;
	}

	public AuthenticationHandler getAuthenticationHandler() {
		return authenticationHandler;
	}

	public boolean isBlockAddresses() {
		return blockAddresses;
	}

	public boolean isWhitelistAddresses() {
		return whitelistAddresses;
	}

	public boolean isLogRequests() {
		return logRequests;
	}

	public ArrayList<String> getBlockedAddresses() {
		return blockedAddresses;
	}

	public ArrayList<String> getWhitelistedAddresses() {
		return whitelistedAddresses;
	}

	public ArrayList<NettyEndpoint> getEndpoints() {
		return endpoints;
	}

}
