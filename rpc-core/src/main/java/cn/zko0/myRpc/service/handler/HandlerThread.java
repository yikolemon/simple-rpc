//package cn.zko0.myRpc.service.handler;
//
//import cn.zko0.myRpc.entity.RpcRequest;
//import cn.zko0.myRpc.entity.RpcResponse;
//import cn.zko0.myRpc.provider.ServiceProvider;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.Socket;
//
///**
// * @author duanfuqiang
// * @date 2023/2/7 21:18
// * @description
// */
//@Slf4j
//@AllArgsConstructor
//public class HandlerThread implements Runnable{
//
//    private Socket socket;
//
//    private ServiceProvider serviceProvider;
//
//    private RequestHandler requestHandler;
//
//    @Override
//    public void run() {
//        try(
//            ObjectInputStream objectInputStream=new ObjectInputStream(socket.getInputStream());
//            ObjectOutputStream objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
//        ){
//            //读取rpcRequest,通过服务注册器获取服务实例
//            RpcRequest rpcRequest=(RpcRequest)objectInputStream.readObject();
//            String interfaceName = rpcRequest.getInterfaceName();
//            Object service = serviceProvider.getService(interfaceName);
//            //通过Handler，对服务进行调用
//            Object res = requestHandler.handle(rpcRequest, service);
//            //将方法反射调用，使用Handler进行处理
//            /*Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
//            Object resultObject = method.invoke(service, rpcRequest.getParameters());*/
//            objectOutputStream.writeObject(RpcResponse.success(res));
//            objectOutputStream.flush();
//        } catch (IOException|ClassNotFoundException e) {
//            log.error("调用异常==>{}",e);
//            throw new RuntimeException(e);
//        }
//    }
//}
