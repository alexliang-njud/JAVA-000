package io.github.kimmking.gateway.outbound.netty4;

import io.github.kimmking.gateway.router.MyHttpEndpointRouter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

import java.net.URL;
import java.util.List;

public class NettyHttpClient {
    private List<String> backendUrlList;

    public NettyHttpClient(List<String> backendUrlList) {
        this.backendUrlList = backendUrlList;
    }

    public void connect(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx) throws Exception {
        MyHttpEndpointRouter myHttpEndpointRouter = new MyHttpEndpointRouter();
        String backendUrl = myHttpEndpointRouter.route(backendUrlList);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true)
             .option(ChannelOption.SO_RCVBUF, 32 * 1024);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                    ch.pipeline().addLast(new HttpResponseDecoder());
                     //客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
                    ch.pipeline().addLast(new HttpRequestEncoder());
                    ch.pipeline().addLast(new NettyHttpClientOutboundHandler(ctx, fullHttpRequest, backendUrl));
                }
            });

            // Start the client.
            URL url = new URL(backendUrl);
            int port = url.getPort() > 0 ? url.getPort() : 80;
            String host = url.getHost();

            ChannelFuture f = b.connect(host, port).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }

    }
}