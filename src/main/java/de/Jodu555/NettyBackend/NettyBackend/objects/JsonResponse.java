package de.Jodu555.NettyBackend.NettyBackend.objects;

import de.Jodu555.NettyBackend.NettyBackend.abstracts.AbstractResponse;
import de.Jodu555.NettyBackend.NettyBackend.utils.JsonUtils;

public class JsonResponse extends AbstractResponse {
	
	private JsonUtils jsonUtils = new JsonUtils();
	
	public JsonUtils getJsonUtils() {
		return jsonUtils;
	}
	
	public void setJsonUtils(JsonUtils jsonUtils) {
		this.jsonUtils = jsonUtils;
	}
	
}
