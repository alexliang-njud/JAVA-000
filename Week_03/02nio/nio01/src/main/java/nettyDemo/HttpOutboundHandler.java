package nettyDemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.FullHttpResponse;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * 接收从InboundHandler传来的msg，加工后，继续向后传递
 */
public class HttpOutboundHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise){
        FullHttpResponse outResponse = null;
        FullHttpResponse response = ((FullHttpResponse) msg);
        String content = ((ByteBuf) response.content()).toString(Charset.defaultCharset());
        System.out.println("HttpInboundHandler response " + content);
        String newMsg = content+" HttpOutboundHandler";
        try {
            outResponse =  response.replace(Unpooled.wrappedBuffer(newMsg.getBytes("UTF-8")));
//            outResponse = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(content.getBytes("UTF-8")));
//            outResponse.headers().set("Content-Type", "application/json");
            outResponse.headers().setInt("Content-Length", outResponse.content().readableBytes());
            ctx.writeAndFlush(outResponse);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }finally {
//            ReferenceCountUtil.release(msg);
        }
    }

}
