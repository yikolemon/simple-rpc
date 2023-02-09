package cn.zko0.myRpc.registry;

import java.net.InetSocketAddress;

/**
 * @author duanfuqiang
 * @date 2023/2/9 11:15
 * @description
 */
public interface ServiceRegistry {
    //服务注册
    void register(String serviceName, InetSocketAddress inetAddress);

    //通过服务名获取服务提供者的地址
    InetSocketAddress searchService(String serviceName);

}
