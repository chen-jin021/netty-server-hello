package com.imooc.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/*
 * @Description: netty server will send a request and return "hello"
 * 我们学习了netty的几种threadmodel，这边用的是主从线程组模型
 */
public class HelloServer {

	public static void main(String[] args) throws Exception{
		/* @Use: define a pair of threads
		 * Special EventExecutorGroup which allows registering Channels 
		 * that get processed for later selection during the event loop.
		 * 当我们有客户client要连接服务端的时候要通过这个线程组去注册，注册完之后会
		 * 获得相应的channels然后丢给后面的线程组去处理
		 */
		
		//主线程组：用于接受客户端连接，不做其他处理，像老板一样
		EventLoopGroup bossGroup = new NioEventLoopGroup();//nio模式
		//从：bossGroup把任务扔给他们
		EventLoopGroup workerGroup = new NioEventLoopGroup(); //从线程组：用的是nio的模式
		
		//try catch为了优雅的关闭我们的线程组 （里面的channel是通道的关闭）
		try {
			//server端的启动类 : start our serverChannel
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup) //对于我们server端的启动，我们把定义好的主从线程组绑定一下
				.channel(NioServerSocketChannel.class) //当我们客户端和server建立连接之后，我们会有相应通道的产生
				.childHandler(null); //从线程组里需要的助手类 <- 子处理器
			
			//启动server //同步的使用方式，netty会等待8088接口初始化完成
			ChannelFuture channelFuture = serverBootstrap.bind(8088).sync();
			
			//关闭监听channel 设置为同步
			channelFuture.channel().close().sync();
		} finally{
			bossGroup.shutdownGracefully(); //优雅关闭
			workerGroup.shutdownGracefully();
		}
	}

}
