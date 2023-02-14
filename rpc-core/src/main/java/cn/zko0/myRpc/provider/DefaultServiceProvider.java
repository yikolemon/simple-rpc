package cn.zko0.myRpc.provider;

import cn.zko0.myRpc.enumeration.RpcError;
import cn.zko0.myRpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author duanfuqiang
 * @date 2023/2/8 10:16
 * @description
 */
@Slf4j
public class DefaultServiceProvider implements ServiceProvider {

    //保存接口名和类的对应关系
    private static final Map<String,Object> serviceMap=new ConcurrentHashMap<>();
    //保存哪些服务已经被注册了，如果被注册了可以通过map直接获取出
    private static final Set<String> serviceSet=ConcurrentHashMap.newKeySet();
    //加锁
    //这里的register，多个实例都是对同一个static的map和set进行修改
    public  synchronized <T> void addProvider(T service) {
        String serviceName = service.getClass().getCanonicalName();
        //set包含类的全限定名，已经注册，无需再注册
        if (serviceSet.contains(serviceName)) return;
        //未注册
        serviceSet.add(serviceName);
        Class<?>[] interfaces = service.getClass().getInterfaces();
        //获取泛型的接口.如果未实现接口，抛出服务注册失败异常
        if (interfaces.length==0){
            throw new RpcException(RpcError.REGSISRTY_FAILL);
        }
        //将接口，与类放入
        for (Class<?>i:interfaces) {
            serviceMap.put(i.getCanonicalName(),service);
        }
        log.info("提供服务:接口==>{},类:==>{}",interfaces,serviceName);
    }

    //通过接口名，获取服务实例
    @Override
    public synchronized Object getService(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (service==null){
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}
