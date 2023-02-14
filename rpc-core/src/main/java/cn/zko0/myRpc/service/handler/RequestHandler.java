package cn.zko0.myRpc.service.handler;

import cn.zko0.myRpc.entity.RpcRequest;

/**
 * @author duanfuqiang
 * @date 2023/2/8 11:32
 * @description
 */
//请求处理器
public interface RequestHandler {

    public Object handle(RpcRequest rpcRequest, Object service);
}
