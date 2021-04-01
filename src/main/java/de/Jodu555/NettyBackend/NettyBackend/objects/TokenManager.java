package de.Jodu555.NettyBackend.NettyBackend.objects;

import java.util.ArrayList;
import java.util.UUID;

public class TokenManager {

	ArrayList<Token> tokens;

	public TokenManager() {
		this.tokens = new ArrayList<Token>();
	}

	private Token getTokenByRequest(Request req, int level) {
		cleanUp();
		for (Token token : tokens) {
			if(token.getBindAddress().equals(req.getIp()) && token.getAuthorizationLevel() == level && token.isValid()) {
				return token;
			}
		}
		return null;
	}
	
	public Token getTokenByToken(String tokentoken) {
		cleanUp();
		for (Token token : tokens) {
			if(token.getToken().equals(tokentoken))
				return token;
		}
		return null;
	}
	
	public String createToken(Request req, String id, int level) {
		if(getTokenByRequest(req, level) != null) {
			return getTokenByRequest(req, level).getToken();
		}
		String token = "";

		for (int i = 0; i < 3; i++) {
			token += UUID.randomUUID().toString().replaceAll("-", "");
		}
		tokens.add(new Token(token, req.getIp(), id, level));

		return token;
	}

	public boolean check(String tokenCode, String ip, int level) {
		cleanUp();
		for (Token token : tokens) {
			if ((token.getAuthorizationLevel() >= level) && token.getToken().equals(tokenCode) && token.getBindAddress().equalsIgnoreCase(ip) && token.isValid()) {
				return true;
			}
		}
		return false;
	}
	
	private void cleanUp() {
		validateTime();
		removeOutRunnedTokens();
	}
	
	private void validateTime() {
		for (Token token : tokens) {
			if(token.getExpiration() < System.currentTimeMillis()) {
				token.setValid(false);
			}
		}
	}
	
	private void removeOutRunnedTokens() {
		ArrayList<Token> remove = new ArrayList<>();
		
		for (Token token : tokens) {
			if(!token.isValid())
				remove.add(token);
		}
		
		if(remove.size() > 0) {
			System.out.println("Removed " + remove.size() + " tokens! Cause: Expired!");
		}
		
		
		for (Token token : remove) {
			tokens.remove(token);
		}
	}
	
	public ArrayList<Token> getTokens() {
		return tokens;
	}

}
