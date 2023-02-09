package cn.zko0.myRpc.serialize;

import cn.zko0.myRpc.entity.RpcRequest;
import cn.zko0.myRpc.entity.RpcResponse;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author duanfuqiang
 * @date 2023/2/9 10:22
 * @description
 */
@Slf4j
public class KryoSerializer implements Serializer{

    //kryo存在线程安全问题
    static private final ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            // Configure the Kryo instance.
            kryo.register(RpcResponse.class);
            kryo.register(RpcRequest.class);
            kryo.setReferences(true);
            //允许未注册的类序列化
            kryo.setRegistrationRequired(false);
            return kryo;
        };
    };

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input=new Input(byteArrayInputStream);
        ){
            Kryo kryo = kryos.get();
            T t = kryo.readObject(input, clazz);
            //释放ThreadLocal的kryo对象
            kryos.remove();
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> byte[] serialize(T object) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)) {
            Kryo kryo = kryos.get();
            kryo.writeObject(output,object);
            kryos.remove();
            return output.toBytes();
        } catch (IOException e) {
            log.error("序列化出现错误===>{}",e);
            throw new RuntimeException(e);
        }
    }
}
