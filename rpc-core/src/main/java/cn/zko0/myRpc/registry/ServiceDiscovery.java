package cn.zko0.myRpc.registry;

import cn.zko0.myRpc.lb.LoadBalancer;

import java.net.InetSocketAddress;

/**
 * @author duanfuqiang
 * @date 2023/2/13 10:08
 * @description 服务发现
 */
public interface ServiceDiscovery {
    InetSocketAddress searchService(String serviceName);
}
