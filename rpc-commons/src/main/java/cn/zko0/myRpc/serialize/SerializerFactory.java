package cn.zko0.myRpc.serialize;

/**
 * @author duanfuqiang
 * @date 2023/2/8 16:52
 * @description
 */
public class SerializerFactory {
    public static Serializer getSerializerByNum(Integer num){
        if (num==0){
            return new JdkSerializer();
        }
        if (num==1){
            return new JsonSerializer();
        }
        if (num==2){
            return new KryoSerializer();
        }
        return null;
    }

    public static Serializer getSerializer(String serializerName){
        if ("java".equalsIgnoreCase(serializerName)){
            return new JdkSerializer();
        }
        if ("json".equalsIgnoreCase(serializerName)){
            return new JsonSerializer();
        }
        if ("kryo".equalsIgnoreCase(serializerName)) {
            return new KryoSerializer();
        }
        return null;
    }

    public static Integer getSerializerNum(Serializer serializer){
        if (serializer instanceof JdkSerializer){
            return 0;
        }
        if (serializer instanceof JsonSerializer){
            return 1;
        }
        if (serializer instanceof KryoSerializer){
            return 2;
        }
        return -1;
    }
}
