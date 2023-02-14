package cn.zko0.myRpc.registry;

import cn.zko0.myRpc.enumeration.RpcError;
import cn.zko0.myRpc.exception.RpcException;
import cn.zko0.myRpc.lb.LoadBalancer;
import cn.zko0.myRpc.util.NacosClientUtils;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author duanfuqiang
 * @date 2023/2/13 10:09
 * @description
 */
public class NacosServiceDiscovery implements ServiceDiscovery {

    private final LoadBalancer loadBalancer;

    public NacosServiceDiscovery(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    @Override
    public InetSocketAddress searchService(String serviceName) {
        return NacosClientUtils.searchService(serviceName);
    }
}
