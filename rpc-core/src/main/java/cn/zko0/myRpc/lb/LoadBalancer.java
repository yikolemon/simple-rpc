package cn.zko0.myRpc.lb;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author duanfuqiang
 * @date 2023/2/13 12:28
 * @description
 */
public interface LoadBalancer {
    InetSocketAddress select(List<InetSocketAddress> addresses);
}
