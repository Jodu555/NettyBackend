package de.Jodu555.NettyBackend.NettyBackend;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.text.StringEscapeUtils;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import de.Jodu555.NettyBackend.NettyBackend.abstracts.AbstractRequest;
import de.Jodu555.NettyBackend.NettyBackend.abstracts.AbstractResponse;
import de.Jodu555.NettyBackend.NettyBackend.abstracts.AbstractRouter;
import de.Jodu555.NettyBackend.NettyBackend.abstracts.AuthenticationHandler;
import de.Jodu555.NettyBackend.NettyBackend.enums.RequestMehtod;
import de.Jodu555.NettyBackend.NettyBackend.enums.ResponseType;
import de.Jodu555.NettyBackend.NettyBackend.netty.NettyServer;
import de.Jodu555.NettyBackend.NettyBackend.objects.AuthenticationResponse;
import de.Jodu555.NettyBackend.NettyBackend.objects.HTMLResponse;
import de.Jodu555.NettyBackend.NettyBackend.objects.JsonResponse;
import de.Jodu555.NettyBackend.NettyBackend.objects.NettyBackend;
import de.Jodu555.NettyBackend.NettyBackend.objects.Request;
import de.Jodu555.NettyBackend.NettyBackend.objects.Router;
import de.Jodu555.NettyBackend.NettyBackend.utils.JsonUtils;

public class App {

	public static App instance;
	private NettyBackend nettyBackend;

	// TODO: Add Validator Like: https://joi.dev/api/?v=17.4.0
	// TODO: Add Refresh Token to Aurh Part
	// TODO: Rate Limiting
	// TODO: Speed Limiter

	// Link port via Apache Hosts:
	// But install Commands before: sudo a2enmod proxy && sudo a2enmod proxy_http &&
	// sudo service apache2 restart
//	<VirtualHost *:80> 
//	  ProxyPreserveHost On
//	  ProxyRequests Off
//	  ServerName www.example.com
//	  ServerAlias example.com
//	  ProxyPass / http://localhost:8080/example/
//	  ProxyPassReverse / http://localhost:8080/example/
//	</VirtualHost> 

	public static void main(String[] args) throws Exception {
		new App();
	}

	public App() throws Exception {
		instance = this;
		this.nettyBackend = new NettyBackend(90, true, false, false);

		this.nettyBackend.setResponseType(ResponseType.JSON);

		nettyBackend.setDefaultEndpoint("/api/v1/cloud");

		nettyBackend.registerEndpoint("/proxy", new AbstractRequest() {

			@Override
			public AbstractResponse onRequest(Request req, AbstractResponse _response) {
				JsonResponse response = (JsonResponse) _response;
				String action = req.getParameters().get("action").get(0);

				System.out.println(action);
				
				return response;
			}
		});
		
		nettyBackend.registerEndpoint("/user/{:?name}/profile/settings", new AbstractRequest() {

			@Override
			public AbstractResponse onRequest(Request req, AbstractResponse _response) {
				JsonResponse response = (JsonResponse) _response;
				response.setSuccess(true);
				response.getJsonUtils().add("username", req.getVariables().get("name"));
				return response;
			}
		});

		nettyBackend.registerEndpoint("/index", new AbstractRequest() {

			@Override
			public AbstractResponse onRequest(Request req, AbstractResponse _response) {
				JsonResponse response = (JsonResponse) _response;
				response.setSuccess(true);
				response.getJsonUtils().add("username", "xD");
				return response;
			}
		}, ResponseType.JSON);

		nettyBackend.registerEndpoint(RequestMehtod.POST, "/index", new AbstractRequest() {

			@Override
			public AbstractResponse onRequest(Request req, AbstractResponse _response) {
				JsonResponse response = (JsonResponse) _response;
				response.setSuccess(true);
				response.getJsonUtils().add("username", "xD");
				response.getJsonUtils().add("body", req.getBody());
				return response;
			}
		}, ResponseType.JSON);

		nettyBackend.registerRouter("/auth", AuthRouter.class);

		this.nettyBackend.start();
	}

	public static class AuthRouter extends AbstractRouter {

		public AuthRouter() {
			System.out.println("AuthRouter called");
		}
		
		@Override
		public void registerEndpoint(Router router) {

			router.registerEndpoint("/", new AbstractRequest() {

				@Override
				public AbstractResponse onRequest(Request req, AbstractResponse _response) {
					JsonResponse response = (JsonResponse) _response;

					response.getJsonUtils().add("message", "/auth Router is Workng!");

					return response;
				}
			});

			router.registerEndpoint("/register", new AbstractRequest() {

				@Override
				public AbstractResponse onRequest(Request req, AbstractResponse _response) {
					JsonResponse response = (JsonResponse) _response;

					response.getJsonUtils().add("message", "/auth/register Rout is Workng too!");

					return response;
				}
			});
		}

	}

	public NettyBackend getNettyBackend() {
		return nettyBackend;
	}

	public static App getInstance() {
		return instance;
	}

}
