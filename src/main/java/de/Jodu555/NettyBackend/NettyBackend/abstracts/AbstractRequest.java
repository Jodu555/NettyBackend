package de.Jodu555.NettyBackend.NettyBackend.abstracts;

import java.util.HashMap;
import java.util.List;

import de.Jodu555.NettyBackend.NettyBackend.objects.Request;


public abstract class AbstractRequest {
	
	
	public abstract AbstractResponse onRequest(Request req, AbstractResponse response);
	
}
