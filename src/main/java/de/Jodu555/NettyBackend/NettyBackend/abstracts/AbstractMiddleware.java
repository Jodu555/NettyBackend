package de.Jodu555.NettyBackend.NettyBackend.abstracts;

import de.Jodu555.NettyBackend.NettyBackend.objects.Middleware;
import de.Jodu555.NettyBackend.NettyBackend.objects.Request;

public abstract class AbstractMiddleware {
	
	public abstract AbstractResponse onRequest(Middleware mIddleware);
	
}
