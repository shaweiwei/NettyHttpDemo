package org.nepu.httpserver;

import io.netty.channel.ChannelInitializer;  
import io.netty.channel.ChannelPipeline;  
import io.netty.channel.socket.SocketChannel;  
import io.netty.example.securechat.SecureChatSslContextFactory;  
import io.netty.handler.codec.http.HttpContentCompressor;  
import io.netty.handler.codec.http.HttpRequestDecoder;  
import io.netty.handler.codec.http.HttpResponseEncoder;  
import io.netty.handler.ssl.SslHandler;  
import javax.net.ssl.SSLEngine;  
  
// 对注册的Channel执行一些初始化操作
public class HttpDemoServerInitializer extends ChannelInitializer<SocketChannel> {  
	
    @Override  
    public void initChannel(SocketChannel ch) throws Exception {  
        // 给channel分配一个ChannelPipeline
//    	ChannelPipeline可以理解为ChannelHandler实例链
    	// 当创建一个新的Channel时，都会分配了一个新的ChannelPipeline,在 Netty 中每个 Channel 都有且仅有一个 ChannelPipeline 与之对应
//    	，该关联是永久的，该通道既不能附加另一个ChannelPipeline也不能分离当前的ChannelPipeline。
        ChannelPipeline pipeline = ch.pipeline();  
  
        if (HttpDemoServer.isSSL) {  
        	// Netty通过JDK的SSLEngine，以SslHandler的方式提供对SSL/TLS安全传输的支持
            SSLEngine engine = SecureChatSslContextFactory.getServerContext().createSSLEngine();  
            engine.setUseClientMode(false);  // 不使用客户端模型
            pipeline.addLast("ssl", new SslHandler(engine));  // 添加ssl处理器，把pipeline理解成一个队列，addLast是添加到队列的尾部
        }  
  
        // 添加netty封装的http服务器端的request解码器 
        pipeline.addLast("decoder", new HttpRequestDecoder());  
        
        // 添加netty封装的http服务器端的response编码器 
        pipeline.addLast("encoder", new HttpResponseEncoder());  
  
        // 添加压缩处理 
        pipeline.addLast("deflater", new HttpContentCompressor());  
  
        // 添加自定义的服务端处理器
        pipeline.addLast("handler", new HttpDemoServerHandler());  
    }  
}  