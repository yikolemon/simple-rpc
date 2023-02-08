package cn.zko0.myRpc.serialize;

import com.google.gson.*;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @author duanfuqiang
 * @date 2023/2/8 15:58
 * @description
 */
public interface Serializer {
    <T> T deserialize(Class<T> clazz,byte[] bytes);

    //序列化
    <T> byte[] serialize(T object);

}
