package de.Jodu555.NettyBackend.NettyBackend.objects;

import org.w3c.dom.views.AbstractView;

import de.Jodu555.NettyBackend.NettyBackend.abstracts.AbstractResponse;
import de.Jodu555.NettyBackend.NettyBackend.abstracts.AbstractWebHandler;

public class JsonWebHandler extends AbstractWebHandler {

	public JsonWebHandler(NettyBackend backend) {
		super(backend);
	}

	@Override
	public String toResponseText(Request req, AbstractResponse response) {
		return ((JsonResponse) response).getJsonUtils().toString();
	}

	public AbstractResponse process(Request req, AbstractResponse response, NettyEndpoint endpoint) {
		boolean cont = true;
		response = new JsonResponse();
		JsonResponse jsonresponse = (JsonResponse) response;
		if (getBackend().isBlockAddresses() && getBackend().getBlockedAddresses().contains(req.getIp())) {
			jsonresponse.setSuccess(false);
			jsonresponse.getJsonUtils().add("blockedIp", true);
			cont = false;
		}
		if (getBackend().isWhitelistAddresses() && !getBackend().getWhitelistedAddresses().contains(req.getIp())) {
			jsonresponse.setSuccess(false);
			jsonresponse.getJsonUtils().add("noWhitelistedAddress", true);
			cont = false;
		}

		if (cont) {
			jsonresponse.setSuccess(true);
			jsonresponse = (JsonResponse) onRequest(req, jsonresponse, endpoint);
		}
		return jsonresponse;
	}

	public AbstractResponse onRequest(Request req, AbstractResponse response, NettyEndpoint endpoint) {
		JsonResponse jsonResponse = (JsonResponse) response;
		if (endpoint != null) {

			// Check if Needs Auth
			if (endpoint.getAuthorizationLevel() > 0) {
				// Check if Token exists in params
				if (req.getParameters().containsKey("auth-token")) {
					String token = req.getParameters().get("auth-token").get(0);
					// Check if token is valid
					if (getBackend().getTokenManager().check(token, req.getIp(), endpoint.getAuthorizationLevel())) {
						req.setAuthId(getBackend().getTokenManager().getTokenByToken(token).getId());
						jsonResponse = (JsonResponse) endpoint.getRequest().onRequest(req, response);
					} else {
						jsonResponse.setSuccess(false);
						jsonResponse.getJsonUtils().add("message",
								"Authentication Token Invlaid or no Permissions on Token");
						return response;
					}
				} else {
					jsonResponse.setSuccess(false);
					jsonResponse.getJsonUtils().add("message", "No Authentication Token found");
					return response;
				}

			} else {
				jsonResponse = (JsonResponse) endpoint.getRequest().onRequest(req, response);

			}
		} else {
			jsonResponse.setSuccess(false);
			jsonResponse.getJsonUtils().add("message", "No Endpoint found at " + req.getUri() + " with Request Mehtod " + req.getRequestMehtod());
		}

		if (jsonResponse == null)
			jsonResponse = new JsonResponse();

		jsonResponse.getJsonUtils().add("success", jsonResponse.isSuccess());

		return jsonResponse;
	}

}
