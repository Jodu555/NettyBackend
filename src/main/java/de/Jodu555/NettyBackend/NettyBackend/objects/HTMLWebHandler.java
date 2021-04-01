package de.Jodu555.NettyBackend.NettyBackend.objects;

import java.io.File;
import java.io.IOException;
import java.nio.charset.CodingErrorAction;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import de.Jodu555.NettyBackend.NettyBackend.abstracts.AbstractResponse;
import de.Jodu555.NettyBackend.NettyBackend.abstracts.AbstractWebHandler;
import de.Jodu555.NettyBackend.NettyBackend.objects.HTMLResponse.HTMLResponseType;

public class HTMLWebHandler extends AbstractWebHandler {

	public HTMLWebHandler(NettyBackend backend) {
		super(backend);
	}

	@Override
	public String toResponseText(Request req, AbstractResponse response) {
		HTMLResponse htmlResponse = (HTMLResponse) response;

		if (htmlResponse.getHtmlResponseType() == HTMLResponseType.TEXT) {
			return htmlResponse.getResponseString().toString();
		} else if (htmlResponse.getHtmlResponseType() == HTMLResponseType.FILE) {
			try {
				List<String> lines = Files.readAllLines(Paths.get(htmlResponse.getResponseFile().toURI()));
				if (!htmlResponse.isCodeInterpreter()) {
					StringBuilder builder = new StringBuilder();
					for (String line : lines) {
						builder.append(line);
					}
					return builder.toString();
				} else {
					CodeInterpreter interpreter = new CodeInterpreter(htmlResponse.getVariables());
					interpreter.interpret((ArrayList<String>) lines);
					return interpreter.output();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public AbstractResponse process(Request req, AbstractResponse response) {
		boolean cont = true;
		response = new HTMLResponse();
		HTMLResponse htmlresponse = (HTMLResponse) response;
		if (getBackend().isBlockAddresses() && getBackend().getBlockedAddresses().contains(req.getIp())) {
			htmlresponse.setSuccess(false);
			htmlresponse.getResponseString().append("blockedIp: true");
			cont = false;
		}
		if (getBackend().isWhitelistAddresses() && !getBackend().getWhitelistedAddresses().contains(req.getIp())) {
			htmlresponse.setSuccess(false);
			htmlresponse.getResponseString().append("noWhitelistedAddress: true");
			cont = false;
		}

		if (cont) {
			htmlresponse.setSuccess(true);
			htmlresponse = (HTMLResponse) onRequest(req, htmlresponse);
		}
		return htmlresponse;
	}

	public AbstractResponse onRequest(Request req, AbstractResponse response) {
		NettyEndpoint endpoint = getBackend().getEndpoints().get(req.getUri());
		HTMLResponse htmlResponse = (HTMLResponse) response;
		if (endpoint != null) {

			// Check if Needs Auth
			if (endpoint.getAuthorizationLevel() > 0) {
				// Check if Token exists in params
				if (req.getParameters().containsKey("auth-token")) {
					String token = req.getParameters().get("auth-token").get(0);
					// Check if token is valid
					if (getBackend().getTokenManager().check(token, req.getIp(), endpoint.getAuthorizationLevel())) {
						req.setAuthId(getBackend().getTokenManager().getTokenByToken(token).getId());
						htmlResponse = (HTMLResponse) endpoint.getRequest().onRequest(req, response);
					} else {
						htmlResponse.setSuccess(false);
						htmlResponse.getResponseString()
								.append("Authentication Token Invlaid or no Permissions on Token");
						return response;
					}
				} else {
					htmlResponse.setSuccess(false);
					htmlResponse.getResponseString().append("No Authentication Token found");
					return response;
				}

			} else {
				htmlResponse = (HTMLResponse) endpoint.getRequest().onRequest(req, response);

			}
		} else {
			htmlResponse.setSuccess(false);
			htmlResponse.getResponseString().append("No Endpoint found at " + req.getUri());
		}

		if (htmlResponse == null)
			htmlResponse = new HTMLResponse();

		return htmlResponse;
	}

}
