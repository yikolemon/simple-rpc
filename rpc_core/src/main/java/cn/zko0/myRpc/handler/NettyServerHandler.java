package cn.zko0.myRpc.handler;

import cn.zko0.myRpc.entity.RpcRequest;
import cn.zko0.myRpc.entity.RpcResponse;
import cn.zko0.myRpc.provider.DefaultServiceProvider;
import cn.zko0.myRpc.provider.ServiceProvider;
import cn.zko0.myRpc.service.handler.DefaultRequestHandler;
import cn.zko0.myRpc.service.handler.RequestHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author duanfuqiang
 * @date 2023/2/8 17:06
 * @description
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final ServiceProvider registry;

    private static final RequestHandler requestHandler;

    static {
        registry=new DefaultServiceProvider();
        requestHandler=new DefaultRequestHandler();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        try {
            log.info("服务器接收到请求====>{}",msg);
            String interfaceName = msg.getInterfaceName();
            Object service = registry.getService(interfaceName);
            Object res = requestHandler.handle(msg, service);
            ChannelFuture future = ctx.writeAndFlush(RpcResponse.success(res));
            future.addListener(ChannelFutureListener.CLOSE);
        } finally {
            //引用计数-1,为0释放ByteBuf资源
            ReferenceCountUtil.release(msg);
        }
    }
}
