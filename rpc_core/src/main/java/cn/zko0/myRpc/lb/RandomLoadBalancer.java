package cn.zko0.myRpc.lb;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.Random;

/**
 * @author duanfuqiang
 * @date 2023/2/13 12:32
 * @description
 */
public class RandomLoadBalancer implements LoadBalancer{
    @Override
    public Instance select(List<Instance> instances) {
        return instances.get(new Random().nextInt(instances.size()));
    }
}
