package nettyDemo;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 定义channel中各个Handler的执行顺序，inbound 顺序执行，outbound逆序执行
 *
 * HttpServerCodec-》HttpObjectAggregator-》HttpInboundHandler-》HttpOutboundHandler-》HttpOutboundHandler2
 */
public class HttpInitializer extends ChannelInitializer<SocketChannel> {
	
	@Override
	public void initChannel(SocketChannel ch) {
		ChannelPipeline p = ch.pipeline();
		p.addLast(new HttpServerCodec());
		//p.addLast(new HttpServerExpectContinueHandler());
		p.addLast(new HttpObjectAggregator(1024 * 1024));
		p.addLast(new HttpOutboundHandler2());
		p.addLast(new HttpOutboundHandler());
		p.addLast(new HttpInboundHandler());
	}
}
