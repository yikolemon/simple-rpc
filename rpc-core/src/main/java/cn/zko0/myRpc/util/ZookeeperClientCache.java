package cn.zko0.myRpc.util;

import cn.zko0.myRpc.enumeration.RpcError;
import cn.zko0.myRpc.exception.RpcException;
import com.alibaba.nacos.common.utils.ConcurrentHashSet;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCache;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author duanfuqiang
 * @date 2023/2/17 15:45
 * @description
 */
@Slf4j
public class ZookeeperClientCache {

    private static final Map<String, List<InetSocketAddress>> instances=new ConcurrentHashMap<>();

    private static final Set<String> watchPathSet=new ConcurrentHashSet<>();

    private static CuratorFramework zookeeperClient;

    private static boolean isListening=false;


    //将服务加入监听set中
    public static void addListenService(String service){
        //开启服务监听
        openListen();
        //path路径放入
        watchPathSet.add(ZookeeperUtil.serviceName2Path(service));
    }

    //添加本地缓存，同时开启监听服务
    public static void addLocalCache(String serviceName,List<InetSocketAddress> addressList){
        //直接替换原本的缓存
        instances.put(serviceName,addressList);
        //将服务加入监听set
        addListenService(serviceName);
    }

    public static void cleanLocalCache(String serviceName){
        log.info("服务调用失败，清除本地缓存，重新获取实例===>{}",serviceName);
        instances.remove(serviceName);
    }


    public static boolean containsKey(String serviceName){
        return instances.containsKey(serviceName);
    }

    public static List<InetSocketAddress> getOrDefault(String serviceName){
        return instances.getOrDefault(serviceName,null);
    }

    public static List<InetSocketAddress> getInstances(String serviceName){
        try {
            String path = ZookeeperUtil.serviceName2Path(serviceName);
            //获取路径下所有的实现
            List<String> instancePaths = zookeeperClient.getChildren().forPath(path);
            List<InetSocketAddress> addressList = new ArrayList<>();
            for (String instancePath : instancePaths) {
                byte[] bytes = zookeeperClient.getData().forPath(path+"/"+instancePath);
                String json = new String(bytes);
                InetSocketAddress instance = InetSocketAddressSerializerUtil.getInetSocketAddressByJson(json);
                addressList.add(instance);
            }
            return addressList;
        } catch (Exception e) {
            log.error("服务获取失败====>{}",e);
            throw new RpcException(RpcError.SERVICE_NONE_INSTANCE);
        }
    }

    private static synchronized void openListen(){
        //已初始化过
        if (isListening){
            return;
        }
        //注入client
        if (zookeeperClient==null) {
            zookeeperClient=ZookeeperUtil.getZookeeperClient();
        }
        TreeCache cache = TreeCache.newBuilder(zookeeperClient, "/cn/zko0/myRpc/api").setCacheData(true).build();
        cache.getListenable().addListener((c, event) -> {
            if ( event.getData() != null )
            {
                System.out.println("type=" + event.getType() + " path=" + event.getData().getPath());
                //可以通过event.type来进行节点的处理，我这里直接多节点每次行为做reload
                if (event.getData().getPath().contains("Service/")){
                    //是服务节点，做更新
                    String path = event.getData().getPath();
                    //去除尾部实例段
                    path=path.substring(0,path.lastIndexOf("/"));
                    String serviceName = ZookeeperUtil.path2ServiceName(path);
                    if (watchPathSet.contains(path)) {
                        log.info("更新本地缓存");
                        List<InetSocketAddress> addressList = getInstances(serviceName);
                        addLocalCache(serviceName,addressList);
                    }
                }
            }
            else
            {
                System.out.println("type=" + event.getType());
            }
        });
        try {
            cache.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        isListening=true;
    }
}
