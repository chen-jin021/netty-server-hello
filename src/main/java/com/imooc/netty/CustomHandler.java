package com.imooc.netty;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/*
 * @Description:创建自定义助手类
 * 
 * SimpleChannelInboundHandler：对于请求来讲，相当于是一个[入站，入境]的概念
 */
public class CustomHandler extends SimpleChannelInboundHandler<HttpObject>{

	// ChannelHandlerContext是一个上下文的对象
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) 
			throws Exception {
		//获取channel
		Channel channel = ctx.channel();
		
		//显示客户端的远程地址
		System.out.println(channel.remoteAddress());
		
		//通过缓冲区来发送消息 //unpooled是用来深拷贝buffer的 (我们在这边定义了要发送的消息）
		ByteBuf content = Unpooled.copiedBuffer("Hello Netty", CharsetUtil.UTF_8); //这个时候数据在content buff里了
			
		//构建一个http response
		FullHttpResponse response =
				new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
						HttpResponseStatus.OK,
						content); //content就是response的内容
		//为响应http response增加数据类型和长度
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());
		
		//把response写了然后刷 (writeAndFlush)到客户端client 
		ctx.writeAndFlush(response);
	}	
		
}
