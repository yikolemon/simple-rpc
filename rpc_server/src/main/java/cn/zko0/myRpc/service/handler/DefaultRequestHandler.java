package cn.zko0.myRpc.service.handler;

import cn.zko0.myRpc.entity.RpcRequest;
import cn.zko0.myRpc.entity.RpcResponse;
import cn.zko0.myRpc.enumeration.ResponseCode;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author duanfuqiang
 * @date 2023/2/8 11:05
 * @description
 */

@Slf4j
//对RpcRequest进行处理，通过反射进行服务调用
public class DefaultRequestHandler implements RequestHandler{
    public Object handle(RpcRequest rpcRequest,Object service){
        Object res=invokeTargetMethod(rpcRequest,service);
        log.info("服务方法调用成功:{}",rpcRequest.getInterfaceName());
        return res;
    }

    //反射调用
    public Object invokeTargetMethod(RpcRequest rpcRequest,Object service){
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            return method.invoke(service,rpcRequest.getParameters());
        } catch (NoSuchMethodException e) {
            return RpcResponse.fail(ResponseCode.NOT_FOUND_METHOD);
        } catch (InvocationTargetException|IllegalAccessException e) {
            //反射方法执行失败
            return RpcResponse.fail(ResponseCode.FALL);
        }
    }

}
