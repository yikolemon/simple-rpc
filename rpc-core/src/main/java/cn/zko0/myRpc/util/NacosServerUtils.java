package cn.zko0.myRpc.util;

import cn.zko0.myRpc.enumeration.RpcError;
import cn.zko0.myRpc.exception.RpcException;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author duanfuqiang
 * @date 2023/2/13 10:04
 * @description
 */

@Slf4j
public class NacosServerUtils {

    private static final Map<String, InetSocketAddress> instances=new ConcurrentHashMap();

    //负责nacos注册信息的删除
    public static void cleanRegistry(){
        log.info("注销所有注册的服务");
        //存在未清除的服务
        if (NacosUtil.getServerHostname()!=null&&!instances.isEmpty()){
            for (Map.Entry<String,InetSocketAddress> instance:instances.entrySet()) {
                InetSocketAddress inetSocketAddress = instance.getValue();
                String host = inetSocketAddress.getHostName();
                int port = inetSocketAddress.getPort();
                try {
                    NacosUtil.getNamingService().deregisterInstance(instance.getKey(),host,port);
                    instances.remove(instance);
                } catch (NacosException e) {
                    log.error("服务注销失败====>{}",instance.getKey());
                    throw new RpcException(RpcError.DESTORY_REGISTER_FALL);
                }
            }
        }
    }

    //注册服务
    public static void register(String serviceName,InetSocketAddress inetSocketAddress){
        try {
            //注册服务
            String hostName = inetSocketAddress.getHostName();
            NacosUtil.getNamingService().registerInstance(serviceName,hostName,inetSocketAddress.getPort());
            //放入map
            instances.put(serviceName,inetSocketAddress);
        } catch (NacosException e) {
            log.error("服务注册失败===>{}",e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }

}
