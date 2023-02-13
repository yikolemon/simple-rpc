package client;

import cn.zko0.myRpc.client.RpcClientProxy;
import cn.zko0.myRpc.api.HelloObject;
import cn.zko0.myRpc.api.HelloService;
import cn.zko0.myRpc.client.NettyRpcClient;
import cn.zko0.myRpc.lb.RoundRobinLoadBalancer;
import cn.zko0.myRpc.registry.NacosServiceDiscovery;
import cn.zko0.myRpc.registry.NacosServiceRegistry;
import cn.zko0.myRpc.serialize.KryoSerializer;

/**
 * @author duanfuqiang
 * @date 2023/2/8 17:45
 * @description
 */
public class NettyTestClient {
    public static void main(String[] args) {
        //NettyClient
        NettyRpcClient client = new NettyRpcClient(new KryoSerializer(),new NacosServiceDiscovery(new RoundRobinLoadBalancer()));
        client.init();
        //生成代理类
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        //获取代理服务
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject helloObject = new HelloObject(6, "this is test");
        String res = helloService.hello(helloObject);
        System.out.println(res);
    }
}
