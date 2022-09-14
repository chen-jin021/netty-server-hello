package com.imooc.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/*
 * @Description: 初始化器，channel注册后，会执行里面的相应初始化方法 
 * 我们的这个initializer是对channel去做的，所以extends ChannelInitializer
 */
public class HelloServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		//通过socketchannel来去获得对应的管道
		ChannelPipeline pipeline = channel.pipeline();
		
		//通过管道添加hander
		//httpservercodec是一个处理请求和响应的助手类，可以理解为拦截器
		//当请求到服务端，我们要解码，响应到客户端做编码
		pipeline.addLast("HttpServerCodec", new HttpServerCodec()); 
		
		//还要添加一个自定义的handler <- 为了返回hello netty
		pipeline.addLast("customerHandler", new CustomHandler());
	}

}
