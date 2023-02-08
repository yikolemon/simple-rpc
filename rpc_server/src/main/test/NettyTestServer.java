import cn.zko0.myRpc.registry.DefaultServiceRegistry;
import cn.zko0.myRpc.server.NettyRpcServer;
import cn.zko0.myRpc.service.HelloServiceImpl;

/**
 * @author duanfuqiang
 * @date 2023/2/8 17:45
 * @description
 */
public class NettyTestServer {
    public static void main(String[] args) {
        HelloServiceImpl helloService = new HelloServiceImpl();
        DefaultServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(helloService);
        NettyRpcServer nettyRpcServer = new NettyRpcServer();
    }
}
