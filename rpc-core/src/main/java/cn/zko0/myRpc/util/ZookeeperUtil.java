package cn.zko0.myRpc.util;

import cn.zko0.myRpc.config.RpcConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;


/**
 * @author zko0
 * @date 2023/2/16 15:37
 * @description
 */
@Slf4j
public class ZookeeperUtil {

    //内部化构造方法
    private ZookeeperUtil(){
    }

    private static final String SERVER_HOSTNAME= RpcConfig.getRegisterCenterHost();

    private static final Integer SERVER_PORT= RpcConfig.getRegisterCenterPort();

    private static CuratorFramework zookeeperClient;

    public static CuratorFramework getZookeeperClient(){
        if (zookeeperClient==null){
            synchronized (ZookeeperUtil.class){
                if (zookeeperClient==null){
                    RetryPolicy retryPolic=new ExponentialBackoffRetry(3000,10);
                    zookeeperClient = CuratorFrameworkFactory.builder()
                            .connectString(SERVER_HOSTNAME+":"+SERVER_PORT)
                            .retryPolicy(retryPolic)
                            //  zookeeper根目录为/serviceRegister，不为/
                            .namespace("serviceRegister")
                            .build();
                    zookeeperClient.start();
                }
            }
        }
        return zookeeperClient;
    }

    public static void close(){
        zookeeperClient.close();
    }

    public static String serviceName2Path(String serviceName){
        return "/"+serviceName.replaceAll("\\.","/");
    }

    public static String getServerHostname(){
        return SERVER_HOSTNAME;
    }

    public static Integer getServerPort(){
        return SERVER_PORT;
    }

}
