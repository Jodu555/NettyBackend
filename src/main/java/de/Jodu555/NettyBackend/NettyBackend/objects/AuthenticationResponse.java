package de.Jodu555.NettyBackend.NettyBackend.objects;

public class AuthenticationResponse {

	private boolean auth;
	private int authLevel;
	private String id;

	public AuthenticationResponse(boolean auth, int authLevel, String id) {
		super();
		this.auth = auth;
		this.authLevel = authLevel;
		this.id = id;
	}
	
	public AuthenticationResponse(boolean auth) {
		this(false, 0, null);
	}
	
	
	public AuthenticationResponse() {
		this(false, 0, null);
	}
	
	public boolean isAuth() {
		return auth;
	}

	public void setAuth(boolean auth) {
		this.auth = auth;
	}

	public int getAuthLevel() {
		return authLevel;
	}

	public void setAuthLevel(int authLevel) {
		this.authLevel = authLevel;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

}
