package cn.zko0.myRpc.protocal;

import cn.zko0.myRpc.entity.RpcRequest;
import cn.zko0.myRpc.enumeration.MessageType;
import cn.zko0.myRpc.serialize.Serializer;
import cn.zko0.myRpc.serialize.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author duanfuqiang
 * @date 2023/2/8 15:29
 * @description
 */
//消息转ByteBUf编码器
public class RpcEncoder extends MessageToByteEncoder {

    private static final int MAGIC_NUMBER = 0xAAAACCCC;

//    //序列化版本 0jdk 1json
//    private static int SERIALIZER_TYPE=1;

    private Serializer serializer;

    public RpcEncoder(Serializer serializer){
        this.serializer=serializer;
    }

    //4字节数据长度

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        //4字节魔数
        out.writeInt(MAGIC_NUMBER);
        //4字节,消息类型，区分request和response消息类型
        //request 为 0  response 为 1
        if (msg instanceof RpcRequest){
            out.writeInt(MessageType.REQUEST.getNum());
        }else {
            out.writeInt(MessageType.RESPONSE.getNum());
        }
        //4字节序列化版本
        Integer serializerNum = SerializerFactory.getSerializerNum(serializer);
        out.writeInt(serializerNum);
        //序列化
        byte[] msgBytes = serializer.serialize(msg);
        //4字节长度
        out.writeInt(msgBytes.length);
        //写入数据内容
        out.writeBytes(msgBytes);
    }
}
