package cn.zko0.myRpc.service;



import cn.zko0.myRpc.api.HelloObject;
import cn.zko0.myRpc.api.HelloService;
import lombok.extern.slf4j.Slf4j;


/**
 * @author duanfuqiang
 * @date 2023/2/7 10:09
 * @description
 */
@Slf4j
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(HelloObject helloObject) {
        log.info("接收到：{}", helloObject.getMessage());
        return "这是掉用的返回值，id=" + helloObject.getId();
    }
}
