package org.nepu.httpserver;

import io.netty.bootstrap.ServerBootstrap;  
import io.netty.channel.Channel;  
import io.netty.channel.EventLoopGroup;  
import io.netty.channel.nio.NioEventLoopGroup;  
import io.netty.channel.socket.nio.NioServerSocketChannel;  
  
public class HttpDemoServer {  
    private final int port;  
    public static boolean isSSL;  
  
    public HttpDemoServer(int port) {  
        this.port = port;  
    }  
  
    public void run() throws Exception {  
    	// 创建事件循环群组
        EventLoopGroup parentGroup = new NioEventLoopGroup(1);  
        EventLoopGroup workerGroup = new NioEventLoopGroup();  
        try {  
        	// 创建服务端引导程序
            ServerBootstrap b = new ServerBootstrap();  
        	b.group(parentGroup, workerGroup)
            	.channel(NioServerSocketChannel.class)  // 指定channel类型,实例化一个用于生成此channel类型对象的工厂对象
                .childHandler(new HttpDemoServerInitializer());  // 指定处理Channel注册到EventLoop后，对这个Channel执行一些初始化操作的工具
  
            Channel ch = b.bind(port).sync().channel();  // 打开一个port端口上的通道（同步）
            System.out.println("HTTP Upload Server at port " + port + '.');  
            System.out.println("Open your browser and navigate to http://localhost:" + port + '/');  
  
            ch.closeFuture().sync();  // 通道的关闭凭证（许可）(同步)
        } finally {  // 优雅退出Netty
            parentGroup.shutdownGracefully();  
            workerGroup.shutdownGracefully();  
        }  
    }  
  
    public static void main(String[] args) throws Exception {  
        int port;  
        if (args.length > 0) {  
            port = Integer.parseInt(args[0]);  
        } else {  
            port = 8080;  
        }  
        if (args.length > 1) {  
            isSSL = true;  
        }  
        new HttpDemoServer(port).run();  
    }  
}  