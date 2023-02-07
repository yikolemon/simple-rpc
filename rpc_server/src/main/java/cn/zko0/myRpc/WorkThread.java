package cn.zko0.myRpc;

import cn.zko0.myRpc.entity.RpcRequest;
import cn.zko0.myRpc.entity.RpcResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @author duanfuqiang
 * @date 2023/2/7 21:18
 * @description
 */
@Slf4j
@AllArgsConstructor
public class WorkThread implements Runnable{

    private Socket socket;

    private Object service;

    @Override
    public void run() {
        try(ObjectInputStream objectInputStream=new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
        ){
            //读取rpcRequest
            RpcRequest rpcRequest=(RpcRequest)objectInputStream.readObject();
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            Object resultObject = method.invoke(service, rpcRequest.getParameters());
            objectOutputStream.writeObject(RpcResponse.success(resultObject));
            objectOutputStream.flush();
        } catch (IOException|ClassNotFoundException|NoSuchMethodException|InvocationTargetException|IllegalAccessException e) {
            log.error("调用错误==>{}",e);
            throw new RuntimeException(e);
        }
    }
}
