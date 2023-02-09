package cn.zko0.myRpc.registry;

import cn.zko0.myRpc.config.NacosConfig;
import cn.zko0.myRpc.enumeration.RpcError;
import cn.zko0.myRpc.exception.RpcException;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author duanfuqiang
 * @date 2023/2/9 11:16
 * @description
 */
@Slf4j
public class NacosServiceRegistry implements ServiceRegistry{

    //获取Nacos的Server地址
    private static final String SERVER_HOSTNAME= NacosConfig.getHostName();

    private static final NamingService namingService;

    static {
        try {
            namingService = NamingFactory.createNamingService(SERVER_HOSTNAME);
        } catch (NacosException e) {
            log.error("连接到Nacos时有错误发生: ", e);
            throw new RpcException(RpcError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            //注册服务
            namingService.registerInstance(serviceName,inetSocketAddress.getHostName(),inetSocketAddress.getPort());
        } catch (NacosException e) {
            log.error("服务注册失败===>{}",e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }

    @Override
    public InetSocketAddress searchService(String serviceName) {
        try {
            List<Instance> instances = namingService.getAllInstances(serviceName);
            Instance instance = instances.get(0);
            //将instance包装为intentSocketAddress
            InetSocketAddress inetSocketAddress = new InetSocketAddress(instance.getIp(), instance.getPort());
            return inetSocketAddress;
        } catch (NacosException e) {
            log.error("服务获取失败====>{}",e);
            throw new RpcException(RpcError.SERVICE_NONE_INSTANCE);
        }
    }
}
