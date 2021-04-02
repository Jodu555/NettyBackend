package de.Jodu555.NettyBackend.NettyBackend.netty;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

import org.apache.commons.text.StringEscapeUtils;

import de.Jodu555.NettyBackend.NettyBackend.App;
import de.Jodu555.NettyBackend.NettyBackend.abstracts.AbstractRequest;
import de.Jodu555.NettyBackend.NettyBackend.abstracts.AbstractResponse;
import de.Jodu555.NettyBackend.NettyBackend.abstracts.AbstractWebHandler;
import de.Jodu555.NettyBackend.NettyBackend.enums.ResponseType;
import de.Jodu555.NettyBackend.NettyBackend.objects.HTMLWebHandler;
import de.Jodu555.NettyBackend.NettyBackend.objects.JsonResponse;
import de.Jodu555.NettyBackend.NettyBackend.objects.JsonWebHandler;
import de.Jodu555.NettyBackend.NettyBackend.objects.NettyBackend;
import de.Jodu555.NettyBackend.NettyBackend.objects.NettyEndpoint;
import de.Jodu555.NettyBackend.NettyBackend.objects.Request;
import de.Jodu555.NettyBackend.NettyBackend.utils.JsonUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
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

			if (backend.getResponseType() == ResponseType.JSON) {
				handler = new JsonWebHandler(backend);
			}
			if (backend.getResponseType() == ResponseType.HTML) {
				handler = new HTMLWebHandler(backend);
			}

			String responseText = "";

			if (handler != null) {
				response = handler.process(req, response);
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
			
			fullHttpResponse.headers().set(HttpHeaders.Names.CONTENT_TYPE, backend.getResponseType().getContentType());

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
		Request req = new Request(ipAddress, uri, params);
		return req;
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
