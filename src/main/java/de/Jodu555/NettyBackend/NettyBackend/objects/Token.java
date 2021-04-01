package de.Jodu555.NettyBackend.NettyBackend.objects;

import java.util.concurrent.TimeUnit;

public class Token {

	private boolean valid;
	private String token;
	private String bindAddress;
	private String id;
	private int authorizationLevel;
	private long expiration;

	public Token(String token, String bindAddress, String id, int authorizationLevel) {
		this(token, bindAddress, id, authorizationLevel, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1));
	}
	
	public Token(String token, String bindAddress, String id, int authorizationLevel, long expiration) {
		super();
		this.valid = true;
		this.token = token;
		this.bindAddress = bindAddress;
		this.id = id;
		this.authorizationLevel = authorizationLevel;
		this.expiration = expiration;
	}

	@Override
	public String toString() {
		return "Token [valid=" + valid + ", token=" + token + ", bindAddress=" + bindAddress + ", authorizationLevel="
				+ authorizationLevel + ", expiration=" + expiration + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Token other = (Token) obj;
		if (authorizationLevel != other.authorizationLevel)
			return false;
		if (bindAddress == null) {
			if (other.bindAddress != null)
				return false;
		} else if (!bindAddress.equals(other.bindAddress))
			return false;
		if (expiration != other.expiration)
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		if (valid != other.valid)
			return false;
		return true;
	}

	

	public boolean isValid() {
		return valid;
	}
	
	public long getExpiration() {
		return expiration;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getBindAddress() {
		return bindAddress;
	}
	
	public String getId() {
		return id;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}
	
	public void setBindAddress(String bindAddress) {
		this.bindAddress = bindAddress;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public int getAuthorizationLevel() {
		return authorizationLevel;
	}

	public void setAuthorizationLevel(int authorizationLevel) {
		this.authorizationLevel = authorizationLevel;
	}

}
