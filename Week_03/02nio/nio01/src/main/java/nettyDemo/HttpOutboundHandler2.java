package nettyDemo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.FullHttpResponse;

import java.nio.charset.Charset;

/**
 * 用于打印被Handler处理过的msg
 */
public class HttpOutboundHandler2 extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        FullHttpResponse response = ((FullHttpResponse) msg);
        String content = ((ByteBuf) response.content()).toString(Charset.defaultCharset());
        System.out.println("HttpOutboundHandler1 response " + content);
        ctx.write(msg);
    }
}
