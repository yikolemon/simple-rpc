package cn.zko0.myRpc.registry;

import cn.zko0.myRpc.util.NacosServerUtils;
import cn.zko0.myRpc.util.ZookeeperServerUitls;

import java.net.InetSocketAddress;

/**
 * @author duanfuqiang
 * @date 2023/2/16 14:59
 * @description
 */
public class ZookeeperServiceRegistry implements ServiceRegistry{
    @Override
    public void register(String serviceName, InetSocketAddress inetAddress) {
        ZookeeperServerUitls.register(serviceName,inetAddress);
    }

    @Override
    public void cleanRegistry() {
        ZookeeperServerUitls.cleanRegistry();
    }
}
