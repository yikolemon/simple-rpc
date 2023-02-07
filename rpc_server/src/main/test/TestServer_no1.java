import cn.zko0.myRpc.RpcServer;
import cn.zko0.myRpc.service.HelloServiceImpl;

/**
 * @author duanfuqiang
 * @date 2023/2/7 21:31
 * @description
 */
public class TestServer_no1 {
    public static void main(String[] args) {
        HelloServiceImpl helloService = new HelloServiceImpl();
        RpcServer rpcServer = new RpcServer();
        //regist启动一个socketServer，当accept连接，启动一个workThread
        //workThread通过obejct输入输出流，通过反射调用service的方法
        //注意！此设计socket只能绑定一个service
        rpcServer.register(helloService,9000);
    }
}
