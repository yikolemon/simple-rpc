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

    void cleanRegistry();
}
