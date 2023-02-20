package cn.zko0.myRpc.util;

import cn.zko0.myRpc.enumeration.RpcError;
import cn.zko0.myRpc.exception.RpcException;
import cn.zko0.myRpc.lb.LoadBalancer;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author duanfuqiang
 * @date 2023/2/16 17:18
 * @description
 */
@Slf4j
public class ZookeeperClientUtils {

    private static CuratorFramework client = ZookeeperUtil.getZookeeperClient();

    public static InetSocketAddress searchService(String serviceName, LoadBalancer loadBalancer) {
        InetSocketAddress address;
        //本地缓存查询
        if (ZookeeperClientCache.containsKey(serviceName)){
            List<InetSocketAddress> addressList = ZookeeperClientCache.getOrDefault(serviceName);
            if (!addressList.isEmpty()){
                //使用lb进行负载均衡
                return loadBalancer.select(addressList);
            }
        }
        try {
            String path = ZookeeperUtil.serviceName2Path(serviceName);
            //获取路径下所有的实现
            List<String> instancePaths = client.getChildren().forPath(path);
            List<InetSocketAddress> addressList = new ArrayList<>();
            for (String instancePath : instancePaths) {
                byte[] bytes = client.getData().forPath(path+"/"+instancePath);
                String json = new String(bytes);
                InetSocketAddress instance = InetSocketAddressSerializerUtil.getInetSocketAddressByJson(json);
                addressList.add(instance);
            }
            ZookeeperClientCache.addLocalCache(serviceName,addressList);
            return loadBalancer.select(addressList);
        } catch (Exception e) {
            log.error("服务获取失败====>{}",e);
            throw new RpcException(RpcError.SERVICE_NONE_INSTANCE);
        }
    }
}
