package cn.zko0.myRpc.util;

import cn.zko0.myRpc.config.RpcConfig;
import cn.zko0.myRpc.enumeration.RpcError;
import cn.zko0.myRpc.exception.RpcException;
import com.alibaba.nacos.common.utils.ConcurrentHashSet;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.util.Set;
import java.util.UUID;

/**
 * @author duanfuqiang
 * @date 2023/2/16 16:04
 * @description
 */
@Slf4j
public class ZookeeperServerUitls {

    private static CuratorFramework client = ZookeeperUtil.getZookeeperClient();

    private static final Set<String> instances=new ConcurrentHashSet<>();

    public static void register(String serviceName, InetSocketAddress inetSocketAddress){

        serviceName=ZookeeperUtil.serviceName2Path(serviceName);;
        String uuid = UUID.randomUUID().toString();
        serviceName=serviceName+"/"+uuid;
        String json = InetSocketAddressSerializerUtil.getJsonByInetSockerAddress(inetSocketAddress);
        try {
            if (RpcConfig.isZookeeperDestoryIsEphemeral()){
                //会话结束节点，创建消失
                client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .forPath(serviceName,json.getBytes());
            } else {
                client.create()
                        .creatingParentsIfNeeded()
                        .forPath(serviceName,json.getBytes());
            }
        }
            catch (Exception e) {
            log.error("服务注册失败");
            throw  new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
        //放入map
        instances.add(serviceName);
    }

    public static void cleanRegistry(){
        log.info("注销所有注册的服务");
        //如果自动销毁，不需要清除
        if (RpcConfig.isZookeeperDestoryIsEphemeral()) return;
        if (ZookeeperUtil.getServerHostname()!=null&&ZookeeperUtil.getServerPort()!=null&&!instances.isEmpty()){
            for (String path:instances) {
                try {
                    client.delete().forPath(path);
                } catch (Exception e) {
                    log.error("服务注销失败");
                    throw new RpcException(RpcError.DESTORY_REGISTER_FALL);
                }
            }
        }
    }
}
