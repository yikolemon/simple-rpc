package cn.zko0.myRpc.registry;

import cn.zko0.myRpc.config.RpcConfig;
import cn.zko0.myRpc.lb.LoadBalancerFactory;

/**
 * @author duanfuqiang
 * @date 2023/2/16 16:54
 * @description
 */
public class ServiceFactory {

    private static String center = RpcConfig.getRegisterCenterType();
    private static String lb= RpcConfig.getLoadBalanceType();

    private static  ServiceRegistry registry;

    private static  ServiceDiscovery discovery;

    private static Object registerLock=new Object();

    private static Object discoveryLock=new Object();

    public static ServiceDiscovery getServiceDiscovery(){
        if (discovery==null){
            synchronized (discoveryLock){
                if (discovery==null){
                    if ("nacos".equalsIgnoreCase(center)){
                        discovery= new NacosServiceDiscovery(LoadBalancerFactory.getLoadBalancer(lb));
                    }else if ("zookeeper".equalsIgnoreCase(center)){
                        discovery= new ZookeeperServiceDiscovery(LoadBalancerFactory.getLoadBalancer(lb));
                    }
                }
            }
        }
        return discovery;
    }

    public static ServiceRegistry getServiceRegistry(){
        if (registry==null){
            synchronized (registerLock){
                if (registry==null){
                    if ("nacos".equalsIgnoreCase(center)){
                        registry=  new NacosServiceRegistry();
                    }else if ("zookeeper".equalsIgnoreCase(center)){
                        registry= new ZookeeperServiceRegistry();
                    }
                }
            }
        }
        return registry;
    }

}
