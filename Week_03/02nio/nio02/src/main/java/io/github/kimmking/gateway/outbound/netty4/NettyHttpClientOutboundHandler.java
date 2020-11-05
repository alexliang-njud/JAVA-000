package io.github.kimmking.gateway.outbound.netty4;

import io.github.kimmking.gateway.util.ByteBufToBytes;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import java.net.URI;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class NettyHttpClientOutboundHandler  extends ChannelInboundHandlerAdapter {
    private ByteBufToBytes reader;
    private ChannelHandlerContext parentCtx;
    private int contentLength = 0;
    private FullHttpRequest fullHttpRequest = null;
    private String backendUrl;

    public NettyHttpClientOutboundHandler(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest, String backendUrl) {
        this.parentCtx = ctx;
        this.fullHttpRequest = fullHttpRequest;
        this.backendUrl = backendUrl;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception {
        String host = backendUrl.replaceAll("/", "").split(":")[1];
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(
                HTTP_1_1, fullHttpRequest.method(), new URI(backendUrl).toASCIIString());
        // 构建http请求
        request.headers().set(HttpHeaderNames.HOST, host);
        request.headers().set(HttpHeaderNames.CONNECTION,
                HttpHeaderNames.CONNECTION);
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH,
                request.content().readableBytes());
        ctx.writeAndFlush(request);
        
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        //System.out.println("channelRead");

        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;
            /*System.out.println("CONTENT_TYPE:"
                    + response.headers().get(HttpHeaderNames.CONTENT_TYPE));*/
            if (HttpUtil.isContentLengthSet(response)) {
                contentLength = (int) HttpUtil.getContentLength(response);
                reader = new ByteBufToBytes(contentLength);
            }
        }
        if (msg instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) msg;
            ByteBuf content = httpContent.content();
            reader.reading(content);
            content.release();
            byte[] bytes = reader.readFull();
            //System.out.println(new String(bytes));
            if (reader.isEnd()) {
                FullHttpResponse response = null;
                try {
                    response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(bytes));
                    response.headers().set("Content-Type", "text/html;charset=UTF-8");
                    response.headers().setInt("Content-Length", contentLength);
                } catch (Exception e) {
                    e.printStackTrace();
                    response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
                    exceptionCaught(parentCtx, e);
                } finally {
                    parentCtx.write(response);
                }
                parentCtx.flush();
                ctx.close();
            }
        }
    }
}