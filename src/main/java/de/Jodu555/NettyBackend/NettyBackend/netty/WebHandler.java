package de.Jodu555.NettyBackend.NettyBackend.netty;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.text.StringEscapeUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import de.Jodu555.NettyBackend.NettyBackend.abstracts.AbstractResponse;
import de.Jodu555.NettyBackend.NettyBackend.abstracts.AbstractWebHandler;
import de.Jodu555.NettyBackend.NettyBackend.enums.RequestMehtod;
import de.Jodu555.NettyBackend.NettyBackend.enums.ResponseType;
import de.Jodu555.NettyBackend.NettyBackend.objects.HTMLWebHandler;
import de.Jodu555.NettyBackend.NettyBackend.objects.JsonWebHandler;
import de.Jodu555.NettyBackend.NettyBackend.objects.NettyBackend;
import de.Jodu555.NettyBackend.NettyBackend.objects.NettyEndpoint;
import de.Jodu555.NettyBackend.NettyBackend.objects.Request;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

public class WebHandler extends ChannelInboundHandlerAdapter {

	NettyBackend backend;

	public WebHandler(NettyBackend backend) {
		this.backend = backend;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object object) throws Exception {
		if (object instanceof FullHttpRequest) {
			long income = System.currentTimeMillis();
			FullHttpRequest fullHttpRequest = (FullHttpRequest) object;

			Request req = generateRequest(ctx, fullHttpRequest);

			AbstractResponse response = null;
			AbstractWebHandler handler = null;

			NettyEndpoint endpoint = null;
			
			
			
			for (NettyEndpoint endpoints : backend.getEndpoints()) {
				
				if(matchEndPoint(req, endpoints))
					endpoint = endpoints;
				
			}

			
			if (endpoint != null) {
				if (endpoint.getResponseType() == ResponseType.JSON) {
					handler = new JsonWebHandler(backend);
				}
				if (endpoint.getResponseType() == ResponseType.HTML) {
					handler = new HTMLWebHandler(backend);
				}
			} else {
				handler = new JsonWebHandler(backend);
			}

			String responseText = "";

			if (handler != null) {
				response = handler.process(req, response, endpoint);
				responseText = handler.toResponseText(req, response);
			} else {
				responseText = "Error in processing Request the server maybe isn't setuped yet!";
			}

//			System.out.println(responseText);

			// HTTP_1_0

			FullHttpResponse fullHttpResponse = null;

			if (response.isRedirect()) {
				fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
				fullHttpResponse.headers().set(HttpHeaders.Names.LOCATION, response.getUrl());
			} else {
				fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
						Unpooled.copiedBuffer(responseText.getBytes()));
				fullHttpResponse.headers().set(HttpHeaders.Names.CONTENT_LENGTH, responseText.length());
			}

			if (HttpHeaders.isKeepAlive(fullHttpRequest)) {
				fullHttpResponse.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
			}

			if (endpoint != null && endpoint.getResponseType() != null) {
				fullHttpResponse.headers().set(HttpHeaders.Names.CONTENT_TYPE, endpoint.getResponseType());
			} else {
				fullHttpResponse.headers().set(HttpHeaders.Names.CONTENT_TYPE, backend.getResponseType());
			}

			// ------------------ Cors - Start ------------------ \\
			fullHttpRequest.headers().add(HttpHeaders.Names.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
			fullHttpRequest.headers().add(HttpHeaders.Names.ACCESS_CONTROL_ALLOW_METHODS,
					"GET,POST,PUT,DELETE,OPTIONS");
			fullHttpRequest.headers().add(HttpHeaders.Names.ACCESS_CONTROL_ALLOW_HEADERS,
					"X-Requested-With, Content-Type, Content-Length");
			// ------------------ Cors - End ------------------ \\

			ctx.writeAndFlush(fullHttpResponse);
			ctx.pipeline().close();
			long outcome = System.currentTimeMillis();

			if (backend.isLogRequests()) {
				boolean state = response == null ? false : response.isSuccess();
				System.out.println("Incoming Request on url " + req.getUri() + " with response " + state + " and IP "
						+ req.getIp() + " took " + (outcome - income) + "ms");
				if (response.isRedirect())
					System.out.println("  |-> But Redirected to " + response.getUrl());
			}

		} else {
			super.channelRead(ctx, object);
		}
		ReferenceCountUtil.release(object);

	}

	private Request generateRequest(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) {
		RequestMehtod method = null;
		if (fullHttpRequest.getMethod() == HttpMethod.GET) {
			method = RequestMehtod.GET;
		} else if (fullHttpRequest.getMethod() == HttpMethod.POST) {
			method = RequestMehtod.POST;
		}
		
		JSONObject body = null;
		if(method == RequestMehtod.POST) {
			ByteBuf jsonBuf = fullHttpRequest.content();
			String bodyString = jsonBuf.toString(CharsetUtil.UTF_8);
			bodyString = StringEscapeUtils.unescapeHtml4(bodyString);
			JSONParser parser = new JSONParser();
			try {
				body = (JSONObject) parser.parse(bodyString);
			} catch (Exception e) {
				System.out.println("Error in Parsing json Body: " + bodyString);
			}
		}

		QueryStringDecoder queryStringDecoder = new QueryStringDecoder(fullHttpRequest.getUri());
		// Generate Parameters
		Map<String, List<String>> prms = queryStringDecoder.parameters();
		HashMap<String, List<String>> params = new HashMap<String, List<String>>();
		if (!prms.isEmpty()) {
			for (Map.Entry<String, List<String>> entry : prms.entrySet()) {
				params.put(entry.getKey(), entry.getValue());
			}
		}
		InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
		InetAddress inetaddress = socketAddress.getAddress();
		String ipAddress = inetaddress.getHostAddress();
		String parseuri = fullHttpRequest.getUri();
		String uri = parseuri.split(Pattern.quote("?"))[0];
		Request req = new Request(method, ipAddress, uri, params);
		if (method == RequestMehtod.POST)
			req.setBody(body);
		return req;
	}

	public boolean matchEndPoint(Request req, NettyEndpoint endpoint) {
		
		if(req.getRequestMehtod() != endpoint.getRequestMehtod())
			return false;
		
		String match = endpoint.getPath();
		String path = req.getUri();
		String[] pattern = match.split("/");
		boolean output = false;
		int i = 0;
		try {
			for (String string : path.split("/")) {
				if (string.equals(pattern[i]) || pattern[i + 1].equals(string) || pattern[i].startsWith("{:")
						|| pattern[i].startsWith("{:?")) {
					if (pattern[i].startsWith("{:") && !pattern[i].startsWith("{:?")) {
						String var = pattern[i].replaceFirst(Pattern.quote("{:"), "").replaceAll(Pattern.quote("}"),
								"");
						req.getVariables().put(var, string);
						if (backend.isLogRequests())
							System.out.println("Var declared: " + var + " as " + string);
					} else if (pattern[i].startsWith("{:?")) {
						String var = pattern[i].replaceFirst(Pattern.quote("{:?"), "").replaceAll(Pattern.quote("}"),
								"");
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
		} catch (Exception e) {
			return false;
		}
		return output;
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR,
				Unpooled.copiedBuffer(cause.getMessage().getBytes())));
	}

}
