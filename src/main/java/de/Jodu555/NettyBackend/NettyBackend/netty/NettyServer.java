package de.Jodu555.NettyBackend.NettyBackend.netty;

import de.Jodu555.NettyBackend.NettyBackend.objects.NettyBackend;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class NettyServer {

	public static EventLoopGroup EVENT_LOOP_GROUP;
	private final boolean EPOLL = Epoll.isAvailable();

	private EventLoopGroup masterGroup;
	private EventLoopGroup slaveGroup;

	public NettyServer(final NettyBackend backend, int port) {
		EVENT_LOOP_GROUP = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup();
		try {
			masterGroup = new NioEventLoopGroup(1);
			slaveGroup = new NioEventLoopGroup();

			new ServerBootstrap().group(masterGroup, slaveGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
//							SelfSignedCertificate cert = new SelfSignedCertificate();
//				            SslContext con = SslContextBuilder.forServer(cert.certificate(), cert.privateKey()).build();
				            
							socketChannel.pipeline().addLast("codec", new HttpServerCodec())
//									.addLast("ssl", new SslHandler(con.newEngine(socketChannel.alloc()), true))
									.addLast("aggregator", new HttpObjectAggregator(512 * 1024))
									.addLast(new CorsHandler(CorsConfig.withAnyOrigin().allowCredentials().build()))
									.addLast("request", new WebHandler(backend));
						}
					}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true).bind(port)
					.sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Netty Server started on port " + port + "!");
	}

}
