package cn.zko0.myRpc.server;

import cn.zko0.myRpc.enumeration.RpcError;
import cn.zko0.myRpc.exception.RpcException;
import cn.zko0.myRpc.service.HelloServiceImpl;
import cn.zko0.myRpc.util.ReflectUtil;
import cn.zko0.myRpc.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * @author duanfuqiang
 * @date 2023/2/13 13:57
 * @description
 */
@Slf4j
public abstract class AbstractRpcServer implements RpcServer{

    public void scanServices(){
        //获取启动类类名
        String mainClassName = ReflectUtil.getStackTrace();
        Class<?> startClass;
        try {
            startClass = Class.forName(mainClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (!startClass.isAnnotationPresent(ServiceScan.class)){
           log.error("启动类缺少@ServiceScan注解");
           throw new RpcException(RpcError.SERVICE_SCAN_BASE_ANNOTATION_NOT_FOUND);
        }
        //获取包扫描范围
        String basePackage = startClass.getAnnotation(ServiceScan.class).value();
        //默认值
        if ("".equals(basePackage)) {
            //启动类所在包
            basePackage=mainClassName.substring(0,mainClassName.lastIndexOf("."));
        }
        Set<Class<?>> classes = ReflectUtil.getClasses(basePackage);
        for (Class<?> clazz :classes ) {
            //当前类存在Service注解
            if (clazz.isAnnotationPresent(Service.class)){
                String serviceName = clazz.getAnnotation(Service.class).name();
                Object obj;
                try {
                    obj=clazz.newInstance();
                } catch (InstantiationException|IllegalAccessException e) {
                    log.error("自动创建类失败===>{}",clazz);
                    continue;
                }
                //服务为默认名称，这里存在问题，会将多余的接口也注册进
                if ("".equals(serviceName)){
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> oneInterface : interfaces) {
                        publishService(obj,oneInterface.getCanonicalName());
                    }
                }else {
                    publishService(obj,serviceName);
                }
            }
        }
    }

}
