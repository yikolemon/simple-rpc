package cn.zko0.myRpc.util;

import cn.zko0.myRpc.config.RpcConfig;
import cn.zko0.myRpc.enumeration.RpcError;
import cn.zko0.myRpc.exception.RpcException;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author duanfuqiang
 * @date 2023/2/13 10:54
 * @description
 */
//使用单例模式，创建一个NamingService
@Slf4j
public class NacosUtil {

    //私有构造方法，无法外部new
    private NacosUtil(){};

    private static final String SERVER_HOSTNAME= RpcConfig.getRegisterCenterHost();

    private static NamingService namingService;

    public static NamingService getNamingService(){
        if (namingService==null){
            synchronized (NacosUtil.class){
                if (namingService==null){
                    try {
                        namingService = NamingFactory.createNamingService(SERVER_HOSTNAME);
                    } catch (NacosException e) {
                        log.error("连接到Nacos时有错误发生: ", e);
                        throw new RpcException(RpcError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
                    }
                }
            }
        }
        return namingService;
    }

    public static String getServerHostname(){
        return SERVER_HOSTNAME;
    }
}
