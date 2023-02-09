//import cn.zko0.myRpc.RpcClientProxy;
//import cn.zko0.myRpc.api.HelloObject;
//import cn.zko0.myRpc.api.HelloService;
//
///**
// * @author duanfuqiang
// * @date 2023/2/7 21:32
// * @description
// */
//public class TestClient_no1 {
//    public static void main(String[] args) {
//        RpcClientProxy rpcClientProxy = new RpcClientProxy("127.0.0.1", 9000);
//        //代理HelloService接口
//        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
//        HelloObject testMessage = new HelloObject(12, "Test message");
//        //代理类进行远程rpc，返回结果res
//        String res = helloService.hello(testMessage);
//        System.out.println(res);
//    }
//}
