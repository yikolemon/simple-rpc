package cn.zko0.myRpc.server;

import cn.zko0.myRpc.util.NacosServerUtils;
import cn.zko0.myRpc.util.ThreadPoolFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

/**
 * @author duanfuqiang
 * @date 2023/2/13 11:06
 * @description Netty连接断开的钩子函数，用于销毁服务注册实例
 */

@Slf4j
public class ShutdownHook {

    private final ExecutorService threadPool=ThreadPoolFactory.createDefaultThreadPool("shutdown-hook");

    private static ShutdownHook shutdownHook;

    //内部构造方法，防止外部实例化
    private ShutdownHook(){};

    //单例模式
    public static ShutdownHook getShutdownHook(){
        if (shutdownHook==null){
            synchronized (ShutdownHook.class){
                if (shutdownHook==null){
                    shutdownHook=new ShutdownHook();
                }
            }
        }
        return shutdownHook;
    }

    public void addClearAllHook(){
        log.info("关闭后注销所有服务");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            NacosServerUtils.cleanRegistry();
            threadPool.shutdown();
        }));
    }

}
