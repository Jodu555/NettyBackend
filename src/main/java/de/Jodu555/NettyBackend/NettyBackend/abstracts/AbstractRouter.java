package de.Jodu555.NettyBackend.NettyBackend.abstracts;

import de.Jodu555.NettyBackend.NettyBackend.objects.Request;
import de.Jodu555.NettyBackend.NettyBackend.objects.Router;

public abstract class AbstractRouter {
	
	public abstract void registerEndpoint(Router router);

}
