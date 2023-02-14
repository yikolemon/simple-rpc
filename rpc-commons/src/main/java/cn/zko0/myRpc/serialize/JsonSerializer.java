package cn.zko0.myRpc.serialize;

import cn.zko0.myRpc.entity.RpcRequest;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @author duanfuqiang
 * @date 2023/2/8 16:46
 * @description
 */
public class JsonSerializer implements Serializer{
    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Class.class, new ClassCodec()).create();
        //Gson gson=new Gson();
        String json=new String(bytes, StandardCharsets.UTF_8);
        T t = gson.fromJson(json, clazz);
        if (t instanceof RpcRequest){
            handleRequest((RpcRequest)t);
        }
        return t;
    }

    @Override
    public <T> byte[] serialize(T object) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Class.class, new ClassCodec()).create();
        String json=gson.toJson(object);
        return json.getBytes(StandardCharsets.UTF_8);
    }

    //json返序列化，子类无法还原为原类型
    private RpcRequest handleRequest(RpcRequest t){
        Class<?>[] types = t.getParamTypes();
        Object[] parameters = t.getParameters();
        for (int i=0;i<types.length;i++) {
            //子对象类型和paramtype不匹配，需要转为paramtype
            if (!types[i].isAssignableFrom(t.getParameters()[i].getClass())){
                Gson gson = new Gson();
                String s = gson.toJson(parameters[i]);
                parameters[i] = gson.fromJson(s, types[i]);
            }
        }
        return t;
    }

    class ClassCodec implements com.google.gson.JsonSerializer<Class<?>>, JsonDeserializer<Class<?>> {

        @Override
        public Class<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                String str = json.getAsString();
                Class<?> aClass = Class.forName(str);
                return aClass;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public JsonElement serialize(Class<?> src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getName());
        }
    }
}
