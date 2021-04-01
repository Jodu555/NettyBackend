package de.Jodu555.NettyBackend.NettyBackend.utils;

import org.apache.commons.text.StringEscapeUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

public class JsonUtils {
	
	private JSONObject jsonObject = new JSONObject();
	
	private Gson gson = new Gson();
	
	public JsonUtils() {
	}
	
	public JsonUtils(String object) {
		try {
			jsonObject = (JSONObject) new JSONParser().parse(object);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public JsonUtils add(Object s1, Object s2) {
		jsonObject.put(s1, s2);
		return this;
	}
	
	public JsonUtils replace(Object key, Object value) {
		jsonObject.replace(key, value);
		return this;
	}
	
	public Object get(Object s1) {
		return jsonObject.get(s1);
	}
	
	public JSONObject getJsonObject() {
		return jsonObject;
	}
	
	@Override
	public String toString() {
		return jsonObject.toString();
	}
	
	public static String validate(String string) {
		return StringEscapeUtils.escapeJson(string);
	}
	
}
