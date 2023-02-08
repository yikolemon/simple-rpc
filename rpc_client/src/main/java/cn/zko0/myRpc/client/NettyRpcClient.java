package cn.zko0.myRpc.client;

import cn.zko0.myRpc.entity.RpcRequest;
import cn.zko0.myRpc.entity.RpcResponse;
import cn.zko0.myRpc.handler.NettyClientHandler;
import cn.zko0.myRpc.protocal.ProcotolFrameDecoder;
import cn.zko0.myRpc.protocal.RpcDecoder;
import cn.zko0.myRpc.protocal.RpcEncoder;
import cn.zko0.myRpc.serialize.JsonSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author duanfuqiang
 * @date 2023/2/8 13:54
 * @description
 */
@Slf4j
@AllArgsConstructor
public class NettyRpcClient implements RpcClient{

    private String host;
    private int port;

    //这里的netty是短连接设计，为了防止同
    private static final Bootstrap bootstrap;

    static {
        bootstrap=new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        //在pipeline中添加编解器
                        pipeline.addLast(new ProcotolFrameDecoder());
                        pipeline.addLast(new RpcEncoder(new JsonSerializer()));
                        pipeline.addLast(new RpcDecoder());
                        pipeline.addLast(new NettyClientHandler());
                    }
                });
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        ChannelFuture future = null;
        try {
            future = bootstrap.connect(host, port).sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("客户端连接服务器,host==>{},port==>{}",host,port);
        Channel channel = future.channel();
        //消息发送
        if (channel!=null){
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
        return null;
    }
}
