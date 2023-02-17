package cn.zko0.myRpc.util;

import com.google.gson.Gson;

import java.net.InetSocketAddress;
import java.util.HashMap;

/**
 * @author duanfuqiang
 * @date 2023/2/17 14:58
 * @description
 */
public class InetSocketAddressSerializerUtil {
    public static String getJsonByInetSockerAddress(InetSocketAddress address){
        HashMap<String, String> map = new HashMap<>();
        map.put("host",address.getHostName());
        map.put("port",address.getPort()+"");
        return new Gson().toJson(map);
    }

    public static InetSocketAddress getInetSocketAddressByJson(String json){
        HashMap<String,String> hashMap = new Gson().fromJson(json, HashMap.class);
        String host = hashMap.get("host");
        Integer port=Integer.parseInt(hashMap.get("port"));
        return new InetSocketAddress(host,port);
    }

}
