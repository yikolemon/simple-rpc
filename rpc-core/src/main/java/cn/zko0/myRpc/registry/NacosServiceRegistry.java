package cn.zko0.myRpc.registry;

import cn.zko0.myRpc.util.NacosServerUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author duanfuqiang
 * @date 2023/2/9 11:16
 * @description
 */
@Slf4j
public class NacosServiceRegistry implements ServiceRegistry{

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        NacosServerUtils.register(serviceName,inetSocketAddress);
    }

    @Override
    public void cleanRegistry() {
        NacosServerUtils.cleanRegistry();
    }

}
