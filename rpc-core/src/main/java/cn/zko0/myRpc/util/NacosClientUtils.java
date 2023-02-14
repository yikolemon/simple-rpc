package cn.zko0.myRpc.util;

import cn.zko0.myRpc.enumeration.RpcError;
import cn.zko0.myRpc.exception.RpcException;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author duanfuqiang
 * @date 2023/2/13 10:50
 * @description
 */
@Slf4j
public class NacosClientUtils {

    //获取Nacos的Server地址

    private static final Map<String,InetSocketAddress> instances=new ConcurrentHashMap<>();


    public static InetSocketAddress searchService(String serviceName) {
        InetSocketAddress address;
        //本地缓存查询
        if ((address=instances.get(serviceName))!=null){
            return address;
        }
        else {
            try {
                List<Instance> instances = NacosUtil.getNamingService().getAllInstances(serviceName);
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

    public static void cleanLocalCache(String serviceName){
        log.info("服务调用失败，清除本地缓存，重新获取实例===>{}",serviceName);
        instances.remove(serviceName);
    }

}
