package cn.zko0.myRpc.registry;

/**
 * @author duanfuqiang
 * @date 2023/2/8 10:13
 * @description
 */
public interface ServiceRegistry {
    //通过接口返回接口实现
    <T> void register(T service);

    //通过服务名获取服务实例
    Object getService(String serviceName);

}
