package cn.zko0.myRpc.client;

import cn.zko0.myRpc.config.RpcConfig;
import cn.zko0.myRpc.entity.RpcRequest;
import cn.zko0.myRpc.entity.RpcResponse;
import cn.zko0.myRpc.enumeration.RpcError;
import cn.zko0.myRpc.exception.RpcException;
import cn.zko0.myRpc.handler.NettyClientHandler;
import cn.zko0.myRpc.protocal.ProcotolFrameDecoder;
import cn.zko0.myRpc.protocal.RpcDecoder;
import cn.zko0.myRpc.protocal.RpcEncoder;
import cn.zko0.myRpc.registry.ServiceDiscovery;
import cn.zko0.myRpc.registry.ServiceFactory;
import cn.zko0.myRpc.serialize.Serializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import cn.zko0.myRpc.serialize.SerializerFactory;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;


/**
 * @author duanfuqiang
 * @date 2023/2/8 13:54
 * @description
 */
@Slf4j
public class NettyRpcClient implements RpcClient{

    //序列化器
    private  Serializer serializer;

    private ServiceDiscovery discovery;

    //private String host;
    //private int port;

    //这里的netty是短连接设计，为了防止同
    private static final Bootstrap bootstrap=new Bootstrap();

    public NettyRpcClient() {
        serializer=SerializerFactory.getSerializer(RpcConfig.getSerializerType());
        discovery= ServiceFactory.getServiceDiscovery();
    }

    public void init(){
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        //在pipeline中添加编解器
                        pipeline.addLast(new ProcotolFrameDecoder());
                        pipeline.addLast(new RpcEncoder(serializer));
                        pipeline.addLast(new RpcDecoder());
                        pipeline.addLast(new NettyClientHandler());
                    }
                });
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if(serializer == null) {
            log.error("未设置序列化器");
            throw new RpcException(RpcError.NONE_SERIALIZER);
        }
        InetSocketAddress inetSocketAddress = discovery.searchService(rpcRequest.getInterfaceName());
        String host = inetSocketAddress.getHostName();
        int port = inetSocketAddress.getPort();
        ChannelFuture future = null;
        try {
            future = bootstrap.connect(host, port).sync();
        } catch (InterruptedException e) {
            //服务调用失败，清除本地缓存
            throw new RuntimeException(e);
        }
        log.info("客户端连接服务器,host==>{},port==>{}",host,port);
        Channel channel = future.channel();
        //消息发送
        if (channel.isActive()){
            channel.writeAndFlush(rpcRequest).addListener(future1 -> {
                if (future1.isSuccess()) {
                    log.info("客户端发送消息{},成功",rpcRequest.toString());
                }else {
                    log.error("发送消息失败,{}",future1.cause());
                }
            });
            //关闭channel连接，这里为短连接
            try {
                channel.closeFuture().sync();
            } catch (InterruptedException e) {
                log.info("发送消息错误,{}",e);
                throw new RuntimeException(e);
            }
            //这里通过 AttributeKey 的方式阻塞获得返回结果
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
            RpcResponse rpcResponse = channel.attr(key).get();
            return rpcResponse.getData();
        }
        //消息发送失败，清除本地服务缓存，下次请求从注册中心获取
        //NacosClientUtils.cleanLocalCache(rpcRequest.getInterfaceName());
        discovery.cleanLoaclCache(rpcRequest.getInterfaceName());
        throw new RpcException(RpcError.SEND_ERROR);
    }
}
