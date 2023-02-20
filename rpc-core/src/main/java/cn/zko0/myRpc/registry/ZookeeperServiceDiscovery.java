package cn.zko0.myRpc.registry;

import cn.zko0.myRpc.lb.LoadBalancer;
import cn.zko0.myRpc.util.ZookeeperClientCache;
import cn.zko0.myRpc.util.ZookeeperClientUtils;
import cn.zko0.myRpc.util.ZookeeperServerUitls;

import java.net.InetSocketAddress;

/**
 * @author duanfuqiang
 * @date 2023/2/16 14:59
 * @description
 */
public class ZookeeperServiceDiscovery implements ServiceDiscovery{

    private final LoadBalancer loadBalancer;

    public ZookeeperServiceDiscovery(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    @Override
    public InetSocketAddress searchService(String serviceName) {
        return ZookeeperClientUtils.searchService(serviceName,loadBalancer);
    }

    @Override
    public void cleanLoaclCache(String serviceName) {
        ZookeeperClientCache.cleanLocalCache(serviceName);
    }
}
