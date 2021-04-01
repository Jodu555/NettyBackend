package de.Jodu555.NettyBackend.NettyBackend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import javax.crypto.spec.PSource;

import org.json.simple.JSONObject;

import com.google.gson.Gson;

import de.Jodu555.NettyBackend.NettyBackend.abstracts.AbstractRequest;
import de.Jodu555.NettyBackend.NettyBackend.utils.JsonUtils;

public class Testing {

	public Testing() {
		long start = System.currentTimeMillis();
		
		final String API_URL = "http://127.0.0.1:9090/";
		
		try {
			System.out.println(" ");
			System.out.println(" ");
			System.out.println("Started testing");
			
			String authReq = get(API_URL + "auth?username=Jodu555");
			String token = (String) new JsonUtils(authReq).get("token");
			
			String url = API_URL + "api/tweet?auth-token=" + token + "&tweet=" + URLEncoder.encode("Hallo ich bin ein witziger tweet");
			System.out.println(get(url));
			

		} catch (Exception e) {
			// TODO: handle exception
		}

		System.out.println("Test End in " + (System.currentTimeMillis() - start) + "ms");
		System.out.println(" ");
		System.out.println(" ");

	}

	private String get(String restUrl, String parameters) {
		byte[] postData = parameters.getBytes(StandardCharsets.UTF_8);
		try {
			URL url = new URL(restUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Content-Length", Integer.toString(postData.length));
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = null;
			StringWriter out = new StringWriter(
					connection.getContentLength() > 0 ? connection.getContentLength() : 2048);
			while ((line = reader.readLine()) != null) {
				out.append(line);
			}
			String response = out.toString();
			return response;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	private String get(String restUrl) {
		try {
			URL url = new URL(restUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = null;
			StringWriter out = new StringWriter(
					connection.getContentLength() > 0 ? connection.getContentLength() : 2048);
			while ((line = reader.readLine()) != null) {
				out.append(line);
			}
			String response = out.toString();
			return response;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

}
