import cn.zko0.myRpc.server.SocketRpcServer;
import cn.zko0.myRpc.registry.DefaultServiceRegistry;
import cn.zko0.myRpc.service.HelloServiceImpl;
import cn.zko0.myRpc.service.handler.DefaultRequestHandler;

/**
 * @author duanfuqiang
 * @date 2023/2/7 21:31
 * @description
 */
public class TestServer_no1 {
    public static void main(String[] args) {
        //服务注册器，注册服务
        DefaultServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(new HelloServiceImpl());
        //RpcServer,需要一个服务注册器，和请求处理器
        SocketRpcServer socketRpcServer = new SocketRpcServer(serviceRegistry,new DefaultRequestHandler());
        //regist启动一个socketServer，当accept连接，启动一个workThread
        //workThread通过obejct输入输出流，通过反射调用service的方法
        //注意！此设计socket只能绑定一个service
        socketRpcServer.start(9000);
    }
}
