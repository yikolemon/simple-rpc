package cn.zko0.myRpc.protocal;

import cn.zko0.myRpc.entity.RpcRequest;
import cn.zko0.myRpc.entity.RpcResponse;
import cn.zko0.myRpc.enumeration.RpcError;
import cn.zko0.myRpc.exception.RpcException;
import cn.zko0.myRpc.serialize.Serializer;
import cn.zko0.myRpc.serialize.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author duanfuqiang
 * @date 2023/2/8 15:37
 * @description
 */
@Slf4j
public class RpcDecoder extends ByteToMessageDecoder {

    private static final int MAGIC_NUMBER = 0xAAAACCCC;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //读取协议魔数
        int magic = in.readInt();
        if (magic!=MAGIC_NUMBER){
            log.error("不识别的协议,{}",magic);
            throw new RpcException(RpcError.UNKNOWN_PORTOCOL);
        }
        //读取版本
        Class<?> clazz;
        int msgType = in.readInt();
        if (msgType==0){
            clazz= RpcRequest.class;
        } else if (msgType==1) {
            clazz= RpcResponse.class;
        }else {
            log.error("不支持的消息类型,{}",msgType);
            throw new RpcException(RpcError.UNSUPPORT_MSG_TYPE);
        }
        //读取序列化方式
        int serializeNum = in.readInt();
        //读取数据长度
        int length = in.readInt();
        byte[] msgBytes=new byte[length];
        in.readBytes(msgBytes);
        //反序列化
        Serializer serializer = SerializerFactory.getSerializerByNum(serializeNum);
        Object obj = serializer.deserialize(clazz, msgBytes);
        out.add(obj);
    }
}
