package cn.zko0.myRpc.handler;

import cn.zko0.myRpc.entity.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author duanfuqiang
 * @date 2023/2/8 17:32
 * @description
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        try {
            log.info("客户端接收到消息,{}",msg);
            //这里通过 AttributeKey 的方式阻塞获得返回结果
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
            ctx.channel().attr(key).set(msg);
            ctx.channel().close();
        } finally {
            //引用计数-1，为0释放资源
            ReferenceCountUtil.release(msg);
        }
    }
}
