package cn.zko0.myRpc.client;


import cn.zko0.myRpc.client.RpcClient;
import cn.zko0.myRpc.entity.RpcRequest;
import cn.zko0.myRpc.entity.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author duanfuqiang
 * @date 2023/2/7 16:37
 * @description
 */
public class RpcClientProxy implements InvocationHandler {

//    private String host;
//    private int port;

    public RpcClientProxy(RpcClient client) {
        this.client = client;
    }

    private RpcClient client;
//    public RpcClientProxy(String host, int port) {
//        this.host = host;
//        this.port = port;
//    }

    //clazz为代理的接口
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class<?>[]{clazz},this);
    }

    //所有被代理的对象都会将方法替换为该invoke方法,通过rpc请求发送接收，替换本地方法
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = RpcRequest.builder()
                //接口名
                .interfaceName(method.getDeclaringClass().getName())
                //方法名
                .methodName(method.getName())
                //参数
                .parameters(args)
                //参数类型
                .paramTypes(method.getParameterTypes())
                .build();
        //使用一个RpcClient将请求发送过去
        //SocketRpcClient socketRpcClient = new SocketRpcClient();
        Object res = client.sendRequest(rpcRequest);
        return res;
    }
}
