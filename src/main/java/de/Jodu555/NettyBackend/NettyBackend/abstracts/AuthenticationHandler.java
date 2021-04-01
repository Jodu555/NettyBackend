package de.Jodu555.NettyBackend.NettyBackend.abstracts;

import java.util.HashMap;
import java.util.List;

import de.Jodu555.NettyBackend.NettyBackend.objects.AuthenticationResponse;
import de.Jodu555.NettyBackend.NettyBackend.objects.Request;


public abstract class AuthenticationHandler {
	
	// req, res move ip uri params to req and 
	
	public abstract AuthenticationResponse onAuthentication(Request req);
	
}
