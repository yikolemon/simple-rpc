package cn.zko0.myRpc.lb;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author duanfuqiang
 * @date 2023/2/13 12:33
 * @description 轮训负载均衡 ==>这个负载均衡应该在服务端实现
 */
public class RoundRobinLoadBalancer implements LoadBalancer{

    private AtomicInteger nextServerCyclicCounter;

    @Override
    public Instance select(List<Instance> instances) {
        return instances.get(incrementAndGetModulo(instances.size()));
    }


    private int incrementAndGetModulo(int modulo) {
        // 死循环直到获取到一个索引下标
        for (;;) {
            // 获取当前AtomicInteger类型变量的原子值
            int current = nextServerCyclicCounter.get();
            // 当前原子值 + 1 然后对 服务实例个数取余
            int next = (current + 1) % modulo;
            // CAS修改AtomicInteger类型变量，CAS成功返回next，否则无限重试
            if (nextServerCyclicCounter.compareAndSet(current, next))
                return next;
        }
    }
}
