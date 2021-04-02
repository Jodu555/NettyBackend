package de.Jodu555.NettyBackend.NettyBackend.abstracts;

import java.util.HashMap;
import java.util.regex.Pattern;

import de.Jodu555.NettyBackend.NettyBackend.objects.JsonResponse;
import de.Jodu555.NettyBackend.NettyBackend.objects.NettyBackend;
import de.Jodu555.NettyBackend.NettyBackend.objects.Request;

public abstract class AbstractWebHandler {

	private NettyBackend backend;

	public AbstractWebHandler(NettyBackend backend) {
		this.backend = backend;
	}

	public NettyBackend getBackend() {
		return backend;
	}

	public boolean matchEndPoint(String path, String match, Request req) {
		String[] pattern = match.split("/");

		boolean output = false;

		int i = 0;
		for (String string : path.split("/")) {
			if (string.equals(pattern[i]) || pattern[i + 1].equals(string) || pattern[i].startsWith("{:")
					|| pattern[i].startsWith("{:?")) {
				if (pattern[i].startsWith("{:") && !pattern[i].startsWith("{:?")) {
					String var = pattern[i].replaceFirst(Pattern.quote("{:"), "").replaceAll(Pattern.quote("}"), "");
					req.getVariables().put(var, string);
					if (backend.isLogRequests())
						System.out.println("Var declared: " + var + " as " + string);
				} else if (pattern[i].startsWith("{:?")) {
					String var = pattern[i].replaceFirst(Pattern.quote("{:?"), "").replaceAll(Pattern.quote("}"), "");
					if (pattern[i + 1].equals(string)) {
						if (backend.isLogRequests())
							System.out.println("Optional var could not be declared");
					} else {
						req.getVariables().put(var, string);
						if (backend.isLogRequests())
							System.out.println("Optional var declared: " + var + " as " + string);
					}
				}
				output = true;
			} else {
				output = false;
				break;
			}
			i++;
		}

		return output;
	}

	public abstract String toResponseText(Request req, AbstractResponse response);

	public abstract AbstractResponse process(Request req, AbstractResponse response);

	public abstract AbstractResponse onRequest(Request req, AbstractResponse response);

}
