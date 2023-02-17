package cn.zko0.myRpc.lb;

/**
 * @author duanfuqiang
 * @date 2023/2/16 16:56
 * @description
 */
public class LoadBalancerFactory {
    public static LoadBalancer getLoadBalancer(String name){
        if ("random".equals(name)){
            return new RandomLoadBalancer();
        }else if ("roundRobin".equals(name)){
            return new RoundRobinLoadBalancer();
        }else {
            return null;
        }
    }
}
