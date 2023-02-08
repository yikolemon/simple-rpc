package cn.zko0.myRpc.server;

import cn.zko0.myRpc.handler.NettyServerHandler;
import cn.zko0.myRpc.protocal.ProcotolFrameDecoder;
import cn.zko0.myRpc.protocal.RpcDecoder;
import cn.zko0.myRpc.protocal.RpcEncoder;
import cn.zko0.myRpc.serialize.JsonSerializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author duanfuqiang
 * @date 2023/2/8 13:32
 * @description
 */
@Slf4j
public class NettyRpcServer implements RpcServer{

    @Override
    public void start(int port)  {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.SO_BACKLOG,256)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .childOption(ChannelOption.TCP_NODELAY,true)
                .childHandler(
                        new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline pipeline = ch.pipeline();
                                //在这里添加编解码器
                                pipeline.addLast(new ProcotolFrameDecoder());
                                pipeline.addLast(new RpcEncoder(new JsonSerializer()));
                                pipeline.addLast(new RpcDecoder());
                                pipeline.addLast(new NettyServerHandler());
                            }
                        }
                );
        try {
            ChannelFuture future = serverBootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("Netty服务器启动失败");
            throw new RuntimeException(e);
        }finally {
            //资源关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
