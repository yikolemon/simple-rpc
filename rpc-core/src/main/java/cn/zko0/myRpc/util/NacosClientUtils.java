package cn.zko0.myRpc.util;

import cn.zko0.myRpc.enumeration.RpcError;
import cn.zko0.myRpc.exception.RpcException;
import cn.zko0.myRpc.lb.LoadBalancer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.common.utils.ConcurrentHashSet;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author duanfuqiang
 * @date 2023/2/13 10:50
 * @description
 */
@Slf4j
public class NacosClientUtils {


    public static InetSocketAddress searchService(String serviceName, LoadBalancer loadBalancer) {
        InetSocketAddress address;
        //本地缓存查询
        try {
            List<Instance> instances = NacosUtil.getNamingService().getAllInstances(serviceName);
            List<InetSocketAddress> list=new ArrayList<>();
            for (Instance i : instances) {
                list.add(new InetSocketAddress(i.getIp(),i.getPort()));
            }
            InetSocketAddress res = loadBalancer.select(list);
            return res;
        } catch (NacosException e) {
            log.error("服务获取失败====>{}",e);
            throw new RpcException(RpcError.SERVICE_NONE_INSTANCE);
        }
    }


}
