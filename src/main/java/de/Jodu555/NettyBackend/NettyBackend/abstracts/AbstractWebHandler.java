package de.Jodu555.NettyBackend.NettyBackend.abstracts;

import java.util.HashMap;
import java.util.regex.Pattern;

import de.Jodu555.NettyBackend.NettyBackend.objects.JsonResponse;
import de.Jodu555.NettyBackend.NettyBackend.objects.NettyBackend;
import de.Jodu555.NettyBackend.NettyBackend.objects.NettyEndpoint;
import de.Jodu555.NettyBackend.NettyBackend.objects.Request;

public abstract class AbstractWebHandler {

	private NettyBackend backend;

	public AbstractWebHandler(NettyBackend backend) {
		this.backend = backend;
	}

	public NettyBackend getBackend() {
		return backend;
	}

	public abstract String toResponseText(Request req, AbstractResponse response);

	public abstract AbstractResponse process(Request req, AbstractResponse response, NettyEndpoint endpoint);

	public abstract AbstractResponse onRequest(Request req, AbstractResponse response, NettyEndpoint endpoint);

}
