package cn.zko0.myRpc.server;

import cn.zko0.myRpc.config.NacosConfig;
import cn.zko0.myRpc.enumeration.RpcError;
import cn.zko0.myRpc.exception.RpcException;
import cn.zko0.myRpc.handler.NettyServerHandler;
import cn.zko0.myRpc.protocal.ProcotolFrameDecoder;
import cn.zko0.myRpc.protocal.RpcDecoder;
import cn.zko0.myRpc.protocal.RpcEncoder;
import cn.zko0.myRpc.provider.ServiceProvider;
import cn.zko0.myRpc.registry.ServiceRegistry;
import cn.zko0.myRpc.serialize.Serializer;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author duanfuqiang
 * @date 2023/2/8 13:32
 * @description
 */
@Slf4j
@AllArgsConstructor
@Builder
public class NettyRpcServer implements RpcServer{

    //注册ip
    private String hostName;

    //本地端口号
    private Integer port;

    private Serializer serializer;

    private ServiceRegistry registry;

    private ServiceProvider provider;


    @Override
    public void start()  {
        if (serializer==null){
            log.error("序列化工具未注入");
            throw new RpcException(RpcError.NONE_SERIALIZER);
        }
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
//                                pipeline.addLast(new RpcEncoder(new JsonSerializer()));
                                pipeline.addLast(new RpcEncoder(serializer));
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

    //服务注册
    @Override
    public <T> void publishService(Object service, Class<T> serviceClass) {
//        if (serializer==null){
//            log.error("序列化工具未注入");
//            throw new RpcException(RpcError.NONE_SERIALIZER);
//        }
        //服务全限定名
        //本地提供服务
        provider.addProvider(service);
        //将服务注册进注册中心
        registry.register(serviceClass.getCanonicalName(),new InetSocketAddress(hostName,port));
    }
}
